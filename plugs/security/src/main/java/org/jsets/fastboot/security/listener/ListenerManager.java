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
package org.jsets.fastboot.security.listener;

import java.util.List;
import java.util.Objects;
import com.google.common.collect.Lists;
import org.jsets.fastboot.security.auth.AuthRequest;

public class ListenerManager implements AuthcListener,AuthzListener,PasswordRetryListener{

	private List<AuthcListener> authcListeners = Lists.newLinkedList();
	private List<AuthzListener> authzListeners = Lists.newLinkedList();
	private PasswordRetryListener passwordRetryListener;

	@Override
	public void onLoginSuccess(AuthRequest request) {
		authcListeners.forEach(t->{
			t.onLoginSuccess(request);
		});
	}

	@Override
	public void onLoginFailure(AuthRequest request, String reason) {
		authcListeners.forEach(t->{
			t.onLoginFailure(request ,reason);
		});
	}
	
	@Override
	public void onLogout(String username) {
		authcListeners.forEach(t->{
			t.onLogout(username);
		});
	}
	
	@Override
	public void onAuthzAssert(String account, String roles, boolean allowed) {
		authzListeners.forEach(t->{
			t.onAuthzAssert(account, roles, allowed);
		});
	}
	
	@Override
	public void onPasswordRetry(String username, int maximum, int currentTimes) {
		if(Objects.nonNull(this.getPasswordRetryListener())) {
			passwordRetryListener.onPasswordRetry(username, maximum, currentTimes);
		}
	}

	public List<AuthcListener> getAuthcListeners() {
		return authcListeners;
	}

	public List<AuthzListener> getAuthzListeners() {
		return authzListeners;
	}

	public PasswordRetryListener getPasswordRetryListener() {
		return passwordRetryListener;
	}

	public void setPasswordRetryListener(PasswordRetryListener passwordRetryListener) {
		this.passwordRetryListener = passwordRetryListener;
	}

	public void setAuthcListeners(List<AuthcListener> authcListeners) {
		this.authcListeners = authcListeners;
	}

	public void setAuthzListeners(List<AuthzListener> authzListeners) {
		this.authzListeners = authzListeners;
	}
}