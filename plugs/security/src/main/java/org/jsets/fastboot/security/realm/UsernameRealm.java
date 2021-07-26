package org.jsets.fastboot.security.realm;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.IAccountProvider;
import org.jsets.fastboot.security.auth.*;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.listener.ListenerManager;
import org.jsets.fastboot.security.model.IAccount;

/**
 * 基于用户的控制域
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月31日
 */
@Slf4j
public class UsernameRealm extends AbstractRealm {

	@Override
	public AuthenticationInfo doAuthenticate(AuthRequest authRequest) throws UnauthorizedException {
		// 只认证UsernamePasswordToken
		if (!(authRequest instanceof UsernameRequest)) {
			return null;
		}

		ListenerManager listener = this.getSecurityManager().getListenerManager();
		SecurityProperties properties = this.getSecurityManager().getProperties();
		IAccountProvider accountProvider = this.getSecurityManager().getAccountProvider();
		UsernameRequest request = (UsernameRequest)authRequest;
		if(StringUtils.isEmpty(request.getUsername())){
			log.warn("认证失败，用户名为空");
			listener.onLoginFailure(request, properties.getUsernameBlankTips());
			throw new UnauthorizedException(properties.getUsernameBlankTips());
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

		log.warn("账号[{}]认证成功", account);
		AuthenticationInfo info = new AuthenticationInfo();
		info.setUsername(request.getUsername());
		return info;
	}

	@Override
	public Class<? extends AuthRequest> getSupportRequestClass() {
		return UsernameRequest.class;
	}

}