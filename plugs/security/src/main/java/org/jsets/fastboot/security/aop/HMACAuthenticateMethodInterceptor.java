package org.jsets.fastboot.security.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.jsets.fastboot.security.SecurityUtils;
import org.jsets.fastboot.security.annotation.HMACAuthenticate;
import org.jsets.fastboot.security.exception.RuntimeForbiddenException;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HMACAuthenticateMethodInterceptor extends AnnotationMethodInterceptor {

	private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	public HMACAuthenticateMethodInterceptor(Class<? extends Annotation> annotationClass) {
		super(annotationClass);
	}

	@Override
	void doAuthenticate(JoinPoint joinPoint) throws Throwable {
		
		Annotation ann = this.getAnnotation(joinPoint);
		if (!(ann instanceof HMACAuthenticate)) {
			throw new IllegalStateException("annotation type mismatching");
		}
		
		Method method = this.getInvokeMethod(joinPoint);
		String[] paraNames = parameterNameDiscoverer.getParameterNames(method);
		if(null==paraNames||paraNames.length==0) {
			throw new IllegalArgumentException("方法必须包含timestamp和signature两个参数");
		}
		Object timestamp = null;
		Object signature = null;
		Object[] args = this.getInvokeMethodArgs(joinPoint);
		for(int i=0;i<paraNames.length;i++) {
			if("timestamp".equals(paraNames[i])) {
				timestamp = args[i];
			}
			if("signature".equals(paraNames[i])) {
				signature = args[i];
			}
		}
		if(null==timestamp||null==signature) {
			throw new IllegalArgumentException("参数timestamp和signature的值不能为空");
		}
		
		if(!(timestamp instanceof Long)) {
			throw new IllegalArgumentException("参数timestamp必须为Long类型");
		}
		if(!(signature instanceof String)) {
			throw new IllegalArgumentException("参数signature必须为String类型");
		}
		
		log.info("HMAC签名验证，timestamp：{}，signature：{}", timestamp, signature);
		boolean allowed = SecurityUtils.hmacSignatureValidate((Long)timestamp, (String)signature);
    	if(!allowed) {
    		throw new RuntimeForbiddenException(SecurityUtils.getProperties().getHmacValidateInvalidTips());
    	}
	}
}
