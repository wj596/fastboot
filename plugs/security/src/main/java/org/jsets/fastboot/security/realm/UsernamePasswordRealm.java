/*
 * Copyright 2021-2022 the original author(https://github.com/wj596)
 * 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package org.jsets.fastboot.security.realm;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.IAccountProvider;
import org.jsets.fastboot.security.IEncryptProvider;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.auth.UsernamePasswordRequest;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.listener.ListenerManager;
import org.jsets.fastboot.security.model.IAccount;

/**
 * 基于用户、名密码的认证器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月31日
 */
@Slf4j
public class UsernamePasswordRealm extends AbstractRealm {

	@Override
	public AuthenticationInfo doAuthenticate(AuthRequest authRequest) throws UnauthorizedException {
		// 只认证UsernamePasswordToken
		if (!(authRequest instanceof UsernamePasswordRequest)) {
			log.warn("不支持的认证请求类型[{}]", authRequest.getClass().getName());
			return null;
		}

		ListenerManager listener = this.getSecurityManager().getListenerManager();
		SecurityProperties properties = this.getSecurityManager().getProperties();
		IAccountProvider accountProvider = this.getSecurityManager().getAccountProvider();
		IEncryptProvider encryptProvider = this.getSecurityManager().getEncryptProvider();
		UsernamePasswordRequest request = (UsernamePasswordRequest)authRequest;
		if(StringUtils.isEmpty(request.getUsername())){
			log.warn("认证失败，用户名为空");
			listener.onLoginFailure(authRequest, properties.getUsernameBlankTips());
			throw new UnauthorizedException(properties.getUsernameBlankTips());
		}
		if(StringUtils.isEmpty(request.getPassword())){
			log.warn("认证失败，密码为空");
			listener.onLoginFailure(authRequest, properties.getPasswordBlankTips());
			throw new UnauthorizedException(properties.getPasswordBlankTips());
		}

		IAccount account = null;
		try{
			account = accountProvider.loadAccount(request.getUsername());
		}catch (UnauthorizedException ex){
			log.error("认证失败：{}",ex.getMessage(),ex);
			listener.onLoginFailure(authRequest, ex.getMessage());
			throw ex;
		}
		if (Objects.isNull(account)) {
			log.warn("认证失败，账号为空");
			listener.onLoginFailure(authRequest, properties.getUsernameOrPasswordErrorTips());
			throw new UnauthorizedException(properties.getUsernameOrPasswordErrorTips());
		}
		
		String encrypted = encryptProvider.encrypt(request.getPassword());
		if (!Objects.equals(encrypted, account.getPassword())) {
			Integer passwordRetryMaximum = properties.getPasswordRetryMaximum();
			if(Objects.nonNull(passwordRetryMaximum)) {
				int retries = this.getPasswdRetryRecordDao().increaseAndGet(request.getUsername());
				log.warn("密码最大重试次数{}，当前第{}次", passwordRetryMaximum, retries);
				listener.onPasswordRetry(request.getUsername(), passwordRetryMaximum, retries);
			}
			log.warn("认证失败，密码错误");
			listener.onLoginFailure(authRequest, properties.getUsernameOrPasswordErrorTips());
			throw new UnauthorizedException(properties.getUsernameOrPasswordErrorTips());
		}

		log.info("账号[{}]认证成功", account.getAccount());
		AuthenticationInfo info = new AuthenticationInfo();
		info.setUsername(request.getUsername());
		return info;
	}

	@Override
	public Class<? extends AuthRequest> getSupportRequestClass() {
		return UsernamePasswordRequest.class;
	}
}