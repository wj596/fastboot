package org.jsets.fastboot.multipleds;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Method;

/**
 * 
 * 动态可切换数据源AOP切面
 * 
 * @author wj596
 *
 */
@Slf4j
@Aspect
public class MultipleDataSourceAspect {

	@Pointcut("@annotation(org.jsets.fastboot.multipleds.SwitchDataSource)")
	public void switchDS() {
	}

	@Before("switchDS()")
	public void beforeSwitch(JoinPoint point) {
		Class<?> clazz = point.getTarget().getClass();
		String methodName = point.getSignature().getName();
		String dataSourceName = "";
		try {
			Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
			Method method = clazz.getMethod(methodName, parameterTypes);
			if (method.isAnnotationPresent(SwitchDataSource.class)) {
				SwitchDataSource annotation = method.getAnnotation(SwitchDataSource.class);
				dataSourceName = annotation.value();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if(""!=dataSourceName&&null!=dataSourceName) {
			log.info("切换数据源,名称：{}", dataSourceName);
			MultipleDataSourceContext.setDataSourceName(dataSourceName);
		}
	}

	@After("switchDS()")
	public void afterSwitch(JoinPoint point) {
		MultipleDataSourceContext.clear();
	}

}