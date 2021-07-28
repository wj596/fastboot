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
package org.jsets.fastboot.security.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsets.fastboot.security.IAccountProvider;
import org.jsets.fastboot.security.IAuthRuleProvider;
import org.jsets.fastboot.security.IAuthenticator;
import org.jsets.fastboot.security.IAuthorizer;
import org.jsets.fastboot.security.ICaptchaProvider;
import org.jsets.fastboot.security.IEncryptProvider;
import org.jsets.fastboot.security.filter.InnerFilter;
import org.jsets.fastboot.security.listener.AuthcListener;
import org.jsets.fastboot.security.listener.AuthzListener;
import org.jsets.fastboot.security.listener.PasswordRetryListener;
import org.jsets.fastboot.security.realm.Realm;

import java.util.List;
import java.util.Map;

public class SecurityCustomizer {

	private IAccountProvider accountProvider;
	private IEncryptProvider encryptProvider;
	private ICaptchaProvider captchaProvider;
	private IAuthRuleProvider authRuleProvider;
	private IAuthenticator authenticator;
	private IAuthorizer authorizer;

	private Map<String, InnerFilter> filters = Maps.newLinkedHashMap();
	private List<Realm> realms = Lists.newLinkedList();
	private List<AuthcListener> authcListeners = Lists.newLinkedList();
	private List<AuthzListener> authzListeners = Lists.newLinkedList();
	private PasswordRetryListener passwordRetryListener;

	public IAccountProvider getAccountProvider() {
		return accountProvider;
	}

	public void setAccountProvider(IAccountProvider accountProvider) {
		this.accountProvider = accountProvider;
	}
	
	public IEncryptProvider getEncryptProvider() {
		return encryptProvider;
	}
	public void setEncryptProvider(IEncryptProvider encryptProvider) {
		this.encryptProvider = encryptProvider;
	}
	public ICaptchaProvider getCaptchaProvider() {
		return captchaProvider;
	}
	public void setCaptchaProvider(ICaptchaProvider captchaProvider) {
		this.captchaProvider = captchaProvider;
	}
	public IAuthRuleProvider getAuthRuleProvider() {
		return authRuleProvider;
	}
	public void setAuthRuleProvider(IAuthRuleProvider authRuleProvider) {
		this.authRuleProvider = authRuleProvider;
	}

	public Map<String, InnerFilter> getFilters() {
		return filters;
	}

	public void addFilter(String name, InnerFilter filter) {
		this.filters.put(name, filter);
	}

	public List<Realm> getRealms() {
		return realms;
	}

	public void addRealm(Realm realm) {
		this.realms.add(realm);
	}

	public List<AuthcListener> getAuthcListeners() {
		return authcListeners;
	}

	public void setAuthcListeners(List<AuthcListener> authcListeners) {
		this.authcListeners = authcListeners;
	}

	public void setFilters(Map<String, InnerFilter> filters) {
		this.filters = filters;
	}

	public List<AuthzListener> getAuthzListeners() {
		return authzListeners;
	}

	public void setAuthzListeners(List<AuthzListener> authzListeners) {
		this.authzListeners = authzListeners;
	}

	public PasswordRetryListener getPasswordRetryListener() {
		return passwordRetryListener;
	}

	public void setPasswordRetryListener(PasswordRetryListener passwordRetryListener) {
		this.passwordRetryListener = passwordRetryListener;
	}

	public IAuthenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(IAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	public IAuthorizer getAuthorizer() {
		return authorizer;
	}

	public void setAuthorizer(IAuthorizer authorizer) {
		this.authorizer = authorizer;
	}
}