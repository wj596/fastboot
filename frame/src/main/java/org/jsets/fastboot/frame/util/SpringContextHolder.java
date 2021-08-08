package org.jsets.fastboot.frame.util;

import java.util.Map;
import java.util.Objects;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * spring context 静态持有类
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
public class SpringContextHolder {

	private static ApplicationContext applicationContext;
	private static Environment environment;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		if (Objects.isNull(SpringContextHolder.applicationContext)) {
			SpringContextHolder.applicationContext = applicationContext;
		}
	}

	public static void setEnvironment(Environment environment) {
		if (Objects.isNull(SpringContextHolder.environment)) {
			SpringContextHolder.environment = environment;
		}
	}

	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	public static Environment getEnvironment() {
		checkEnvironment();
		return environment;
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext未注入");
		}
	}

	private static void checkEnvironment() {
		if (environment == null) {
			throw new IllegalStateException("environment未注入");
		}
	}

	public static <T> T getBean(Class<T> requiredType) {
		checkApplicationContext();
		return applicationContext.getBean(requiredType);
	}

	public static <T> T tryBean(Class<T> clazz) {
		checkApplicationContext();
		try {
			return applicationContext.getBean(clazz);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

	public static <T> Map<String, T> tryBeans(Class<T> clazz) {
		checkApplicationContext();
		try {
			return applicationContext.getBeansOfType(clazz);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
}