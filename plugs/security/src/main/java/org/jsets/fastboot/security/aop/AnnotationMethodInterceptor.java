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
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.jsets.fastboot.security.SecurityUtils;
import org.jsets.fastboot.security.exception.RuntimeForbiddenException;
import org.jsets.fastboot.security.exception.RuntimeUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.reflect.AdviceSignature;

@Slf4j
public abstract class AnnotationMethodInterceptor {
	
	protected final Class<? extends Annotation> annotationClass;
	
	public AnnotationMethodInterceptor(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}
	
	public Class<? extends Annotation> getAnnotationClass() {
        return this.annotationClass;
    }

	protected Method getInvokeMethod(JoinPoint joinPoint) {
		if (joinPoint.getSignature() instanceof MethodSignature) {
            return ((MethodSignature) joinPoint.getSignature()).getMethod();
        } else if (joinPoint.getSignature() instanceof AdviceSignature) {
            return ((AdviceSignature) joinPoint.getSignature()).getAdvice();
        } else {
            throw new IllegalArgumentException("The joint point signature is invalid: expected a MethodSignature or an AdviceSignature but was " + joinPoint.getSignature());
        }
    }
	
	protected Object[] getInvokeMethodArgs(JoinPoint joinPoint) {
		return joinPoint.getArgs();
    }
	
	protected Object getInvokeObject(JoinPoint joinPoint) {
		return joinPoint.getThis();
    }
	
    public Annotation getAnnotation(JoinPoint joinPoint) {
        if (joinPoint == null) {
            throw new IllegalArgumentException("method argument cannot be null");
        }
        Method m = getInvokeMethod(joinPoint);
        if (m == null) {
            throw new IllegalArgumentException("invoke method cannot be null");
        }
        
        Class<? extends Annotation> annotationClass = this.getAnnotationClass();
        Annotation annotation = m.getAnnotation(annotationClass);
        if (annotation == null ) {
            Object miThis = this.getInvokeObject(joinPoint);
            annotation = miThis != null ? miThis.getClass().getAnnotation(annotationClass) : null;
        }

        return annotation;
    }
   
    public boolean supports(JoinPoint joinPoint) {
        return getAnnotation(joinPoint) != null;
    }
    
    protected void assertAuthenticated(JoinPoint joinPoint)   {
    	if(!SecurityUtils.isAuthenticated()) {
    		if(log.isInfoEnabled()) {
    			log.info("未认证, 拒绝访问["+ this.getInvokeMethod(joinPoint) +"]");
    		}
    		throw new RuntimeUnauthorizedException(SecurityUtils.getProperties().getUnauthorizedTips());
    	}
		if(log.isInfoEnabled()) {
			log.info("已认证, 允许访问["+ this.getInvokeMethod(joinPoint) +"]");
		}
    }
    
    protected void assertAllowed(boolean allowed)   {
    	if(!allowed) {
    		throw new RuntimeForbiddenException(SecurityUtils.getProperties().getForbiddenTips());
    	}
    }
    
	abstract void doAuthenticate(JoinPoint joinPoint) throws Throwable;

}