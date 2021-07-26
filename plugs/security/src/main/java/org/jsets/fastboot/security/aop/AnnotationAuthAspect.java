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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jsets.fastboot.security.SecurityManager;

@Aspect
public class AnnotationAuthAspect {
	
    private static final String POINTCUT_EXPRESSION = // 切入点表达式
             "execution(@org.jsets.fastboot.security.annotation.Authenticated * *(..)) || " +
             "execution(@org.jsets.fastboot.security.annotation.HasRole * *(..)) || " +
             "execution(@org.jsets.fastboot.security.annotation.HasPermission * *(..)) || " +
             "execution(@org.jsets.fastboot.security.annotation.HasAnyRoles * *(..)) || " +
             "execution(@org.jsets.fastboot.security.annotation.HasAnyPermissions * *(..))";
	
    private final AnnotationAuthInterceptor interceptor;

    public AnnotationAuthAspect(SecurityManager securityManager) {
		this.interceptor = new AnnotationAuthInterceptor(securityManager);
	}

	@Pointcut(POINTCUT_EXPRESSION)
    public void anyAnnotatedMethod(){}
    
    @Before("anyAnnotatedMethod()")
    public void executeAnnotatedMethod(JoinPoint thisJoinPoint) throws Throwable {
    	interceptor.interception(thisJoinPoint);
    }
    
}
