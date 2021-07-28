package org.jsets.fastboot.security;

import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.realm.RealmManager;
import org.jsets.fastboot.security.session.SessionManager;
import org.jsets.fastboot.security.token.TokenManager;

public abstract class AbstractAuthenticator implements IAuthenticator {
	
	private SecurityProperties properties;
	private RealmManager realmManager;
	private SessionManager sessionManager;
	private TokenManager tokenManager;
	
	protected SecurityProperties getProperties() {
		return properties;
	}
	protected void setProperties(SecurityProperties properties) {
		this.properties = properties;
	}
	protected RealmManager getRealmManager() {
		return realmManager;
	}
	protected void setRealmManager(RealmManager realmManager) {
		this.realmManager = realmManager;
	}
	protected SessionManager getSessionManager() {
		return sessionManager;
	}
	protected void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	protected TokenManager getTokenManager() {
		return tokenManager;
	}
	protected void setTokenManager(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}
}
