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
package org.jsets.fastboot.security.aop;

import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.jsets.fastboot.security.SecurityManager;
import org.jsets.fastboot.security.annotation.HasAnyPermissions;
import org.jsets.fastboot.security.annotation.HasAnyRoles;
import org.jsets.fastboot.security.annotation.HasPermission;
import org.jsets.fastboot.security.annotation.HasRole;
import org.jsets.fastboot.security.annotation.Authenticated;
import com.google.common.collect.Lists;

public class AnnotationAuthInterceptor {
	
	protected List<AnnotationMethodInterceptor> methodInterceptors = Lists.newArrayList();

	protected AnnotationAuthInterceptor(SecurityManager securityManager) {
		methodInterceptors.add(new AuthenticatedMethodInterceptor(securityManager, Authenticated.class));
		methodInterceptors.add(new HasRoleMethodInterceptor(securityManager, HasRole.class));
		methodInterceptors.add(new HasAnyRolesMethodInterceptor(securityManager, HasAnyRoles.class));
		methodInterceptors.add(new HasPermissionMethodInterceptor(securityManager, HasPermission.class));
		methodInterceptors.add(new HasAnyPermissionsMethodInterceptor(securityManager, HasAnyPermissions.class));
	}

	protected void interception(JoinPoint joinPoint) throws Throwable {

		for(AnnotationMethodInterceptor methodInterceptor : methodInterceptors) {
			if(methodInterceptor.supports(joinPoint)) {
				methodInterceptor.doAuthenticate(joinPoint);
			}
		}
		
	}

}
