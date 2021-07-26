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

import java.lang.annotation.Annotation;
import org.aspectj.lang.JoinPoint;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.SecurityManager;
import org.jsets.fastboot.security.annotation.HasAnyPermissions;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HasAnyPermissionsMethodInterceptor extends AnnotationMethodInterceptor {

	public HasAnyPermissionsMethodInterceptor(SecurityManager securityManager, Class<? extends Annotation> annotationClass) {
		super(securityManager, annotationClass);
	}

	@Override
	void doAuthenticate(JoinPoint joinPoint) throws Throwable {
		
		Annotation ann = this.getAnnotation(joinPoint);
		if (!(ann instanceof HasAnyPermissions)) {
			throw new IllegalStateException("annotation type mismatching");
		}
		
		this.assertAuthenticated(joinPoint);
		
		HasAnyPermissions hap = (HasAnyPermissions)ann;
		String[] permissions = hap.value();
		boolean allowed = this.getSecurityManager().isPermittedAny(Sets.newHashSet(permissions));
		if(log.isInfoEnabled()) {
			log.info("访问方法["+ this.getInvokeMethod(joinPoint) +"]需要权限["+ StringUtils.join(permissions) +"]，鉴权结果["+ allowed +"]");
		}
		this.assertAllowed(allowed);
		
	}

}
