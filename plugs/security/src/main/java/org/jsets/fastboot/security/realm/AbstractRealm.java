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

import java.util.Set;
import org.jsets.fastboot.common.util.CollectionUtils;
import org.jsets.fastboot.security.IAccountProvider;
import org.jsets.fastboot.security.SecurityManager;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthorizationInfo;
import org.jsets.fastboot.security.dao.PasswdRetryRecordDao;

/**
 * 
 * 抽象认证器
 * @author wj596
 *
 */
public abstract class AbstractRealm implements Realm {

	private SecurityManager securityManager;
	private PasswdRetryRecordDao passwdRetryRecordDao;

	@Override
	public boolean isSupport(AuthRequest request) {
		return request != null && getSupportRequestClass().isAssignableFrom(request.getClass());
	}

	public AuthorizationInfo getAuthorizationInfo(String username) {
		IAccountProvider accountProvider = this.getSecurityManager().getAccountProvider();
		AuthorizationInfo info =  new AuthorizationInfo();
		Set<String> roles = accountProvider.loadRoles(username);
		if(CollectionUtils.notEmpty(roles)) { 
			info.setRoles(roles);
		}
		Set<String> permissions = accountProvider.loadPermissions(username);
		if(CollectionUtils.notEmpty(permissions)) { 
			info.setPermissions(permissions);
		}
		return info;
	}

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	protected void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public PasswdRetryRecordDao getPasswdRetryRecordDao() {
		return passwdRetryRecordDao;
	}

	protected void setPasswdRetryRecordDao(PasswdRetryRecordDao passwdRetryRecordDao) {
		this.passwdRetryRecordDao = passwdRetryRecordDao;
	}
}