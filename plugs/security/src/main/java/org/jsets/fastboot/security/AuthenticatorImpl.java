package org.jsets.fastboot.security;

import java.util.Objects;
import java.util.Optional;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.model.ContextItem;
import org.jsets.fastboot.security.model.IAccount;
import org.jsets.fastboot.security.session.Session;
import org.jsets.fastboot.security.token.InnerToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticatorImpl extends AbstractAuthenticator{

	/**
	 * 根据Id获取Session
	 * 
	 * @param sessionId 主键
	 * @return Session
	 * @throws UnauthorizedException 未认证异常
	 */
	public Optional<Session> getSession(String sessionId) {
		return this.getSessionManager().get(sessionId);
	}
	
	@Override
	public AuthenticationInfo login(AuthRequest authRequest) throws UnauthorizedException {
		AuthenticationInfo info = this.getRealmManager().authenticate(authRequest);
		Session session = this.getSessionManager().create(authRequest);
		InnerToken token = this.getTokenManager().issue(session);
		info.setToken(token);
		info.setSessionId(session.getId());
		
		SecurityContext.set(ContextItem.build()
				.setToken(token.getAccessToken())
				.setSessionId(session.getId())
				.setUsername(authRequest.getUsername()));
		
		return info;
	}

	@Override
	public AuthenticationInfo logout(String token) throws UnauthorizedException {
		AuthenticationInfo info = this.getTokenManager().validate(token);
		this.getSessionManager().delete(info.getSessionId());
		SecurityContext.cleanup();
		return info;
	}

	@Override
	public void authenticate(String token) throws UnauthorizedException {
		AuthenticationInfo info = this.getTokenManager().validate(token);
		boolean exists = this.getSessionManager().isExists(info.getSessionId());
		if (!exists) {
			log.warn(this.getProperties().getTokenInvalidTips());
			throw new UnauthorizedException(this.getProperties().getTokenInvalidTips());
		}
		
		SecurityContext.set(ContextItem.build()
				.setToken(token)
				.setSessionId(info.getSessionId())
				.setUsername(info.getUsername()));
	}

	@Override
	public boolean isAuthenticated() {
		String sessionId = SecurityContext.getSessionId();
		if (StringUtils.isBlank(sessionId)) {
			return false;
		}
		return this.getSessionManager().isExists(sessionId);
	}
	
	/**
	 * 获取当前认证的用户
	 */
	public <T extends IAccount> T getAccount() throws UnauthorizedException {
		String sessionId = SecurityContext.getSessionId();
		if (Objects.isNull(sessionId)) {
			throw new UnauthorizedException(getProperties().getUnauthorizedTips());
		}
		
		Optional<Session> opt = getSessionManager().get(sessionId);
		if (!opt.isPresent()) {
			throw new UnauthorizedException(getProperties().getUnauthorizedTips());
		}
		
		IAccount account = getAccountProvider().loadAccount(opt.get().getUsername());
		return (T) account;
	}

	
}