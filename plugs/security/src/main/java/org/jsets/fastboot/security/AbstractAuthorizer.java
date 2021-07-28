package org.jsets.fastboot.security;

import org.jsets.fastboot.security.listener.ListenerManager;
import org.jsets.fastboot.security.realm.RealmManager;
import org.jsets.fastboot.security.session.SessionManager;

public abstract class AbstractAuthorizer implements IAuthorizer{
	
	private RealmManager realmManager;
	private SessionManager sessionManager;
	private ListenerManager listenerManager;

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
	protected ListenerManager getListenerManager() {
		return listenerManager;
	}
	protected void setListenerManager(ListenerManager listenerManager) {
		this.listenerManager = listenerManager;
	}

}
