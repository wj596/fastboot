package org.jsets.fastboot.frame.config;

import org.jsets.fastboot.frame.handler.BaseEntityMetaObjectHandler;
import org.jsets.fastboot.frame.util.SpringContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 基础配置
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.12 21:49
 * @since 0.1
 */
@Configuration
public class BaseConfiguration implements ApplicationContextAware,EnvironmentAware{

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.setApplicationContext(applicationContext);
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		SpringContextHolder.setEnvironment(environment);
	}
	
	@Bean
    public BaseEntityMetaObjectHandler baseEntityMetaObjectHandler() {
		return new BaseEntityMetaObjectHandler();
	}

}