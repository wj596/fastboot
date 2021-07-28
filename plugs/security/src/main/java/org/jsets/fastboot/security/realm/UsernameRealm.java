package org.jsets.fastboot.security.realm;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.auth.UsernameRequest;
import org.jsets.fastboot.security.exception.UnauthorizedException;
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

		UsernameRequest request = (UsernameRequest)authRequest;
		if(StringUtils.isEmpty(request.getUsername())){
			log.warn("认证失败，用户名为空");
			this.getListenerManager().onLoginFailure(request, this.getProperties().getUsernameBlankTips());
			throw new UnauthorizedException(this.getProperties().getUsernameBlankTips());
		}

		IAccount account = null;
		try{
			account = this.getAccountProvider().loadAccount(request.getUsername());
		}catch (UnauthorizedException ex){
			log.error("认证失败：{}",ex.getMessage(),ex);
			this.getListenerManager().onLoginFailure(authRequest, ex.getMessage());
			throw ex;
		}
		if (Objects.isNull(account)) {
			log.warn("认证失败，账号为空");
			this.getListenerManager().onLoginFailure(authRequest, this.getProperties().getUsernameOrPasswordErrorTips());
			throw new UnauthorizedException(this.getProperties().getUsernameOrPasswordErrorTips());
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