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
import org.jsets.fastboot.security.exception.ForbiddenException;
import org.jsets.fastboot.security.exception.UnauthorizedException;

public class AuthenticatedMethodInterceptor extends AnnotationMethodInterceptor {

	public AuthenticatedMethodInterceptor(Class<? extends Annotation> annotationClass) {
		super(annotationClass);
	}

	@Override
	void doAuthenticate(JoinPoint joinPoint) throws UnauthorizedException, ForbiddenException {
		this.assertAuthenticated(joinPoint);
	}

}