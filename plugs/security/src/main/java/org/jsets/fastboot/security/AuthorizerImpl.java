package org.jsets.fastboot.security;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.session.Session;

public class AuthorizerImpl extends AbstractAuthorizer {

	/**
	 * 获取当前上下文中的Session
	 * 
	 * @return Session
	 */
	private Session getCurrentSession() {
		String sessionId = SecurityContext.getSessionId();
		if (StringUtils.isBlank(sessionId)) {
			return null;
		}
		Optional<Session> opt = this.getSessionManager().get(sessionId);
		if (!opt.isPresent()) {
			return null;
		}
		return opt.get();
	}
	
	@Override
	public boolean hasRole(String roleIdentifier) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.getRealmManager().hasRole(session, roleIdentifier);
		this.getListenerManager().onAuthzAssert(session.getUsername(), roleIdentifier, allowed);
		return allowed;
	}

	@Override
	public boolean hasAnyRole(Set<String> roleIdentifiers) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed = this.getRealmManager().hasAnyRoles(session, roleIdentifiers);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(roleIdentifiers), allowed);
		return allowed;
	}

	@Override
	public boolean hasAllRoles(Set<String> roleIdentifiers) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.getRealmManager().hasAllRoles(session, roleIdentifiers);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(roleIdentifiers), allowed);
		return allowed;
	}

	@Override
	public boolean isPermitted(String permission) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.getRealmManager().isPermitted(session, permission);
		this.getListenerManager().onAuthzAssert(session.getUsername(), permission, allowed);
		return allowed;
	}

	@Override
	public boolean isPermittedAny(Set<String> permissions) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.getRealmManager().isPermittedAny(session, permissions);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(permissions), allowed);
		return allowed;
	}

	@Override
	public boolean isPermittedAll(Set<String> permissions) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.getRealmManager().isPermittedAll(session, permissions);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(permissions), allowed);
		return allowed;
	}
}