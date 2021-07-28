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
package org.jsets.fastboot.security.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.security.SecurityManager;
import org.jsets.fastboot.security.aop.AnnotationAuthAspect;
import org.jsets.fastboot.security.filter.CaptchaFilter;
import org.jsets.fastboot.security.filter.CorsFilter;
import org.jsets.fastboot.security.filter.SecurityFilter;

/**
 * Security配置
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.12 21:49
 * @since 0.1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration {
	
	@Bean
	public SecurityManager securityManager(ApplicationContext applicationContext, SecurityProperties properties,ObjectProvider<SecurityCustomizer> customizerPvd) {
		SecurityManager manager = new SecurityManager(applicationContext, properties, customizerPvd.getIfAvailable());
		manager.initialize();
		return manager;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "fastboot.security", name = "cors-enabled", havingValue = "true")
	public FilterRegistrationBean<CorsFilter> corsFilterRegister() {
		log.info("实例化CorsFilter");
		FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<CorsFilter>();
		CorsFilter corsFilter = new CorsFilter();
		registration.setFilter(corsFilter);
		registration.addUrlPatterns("/*");
		registration.setName("corsFilter");
		registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
		return registration;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "fastboot.security", name = "captcha-enabled", havingValue = "true")
	public FilterRegistrationBean<CaptchaFilter> captchaFilterRegister() {
		log.info("实例化CaptchaFilter");
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<CaptchaFilter>();
		CaptchaFilter captchaFilter = new CaptchaFilter();
		registration.setFilter(captchaFilter);
		registration.addUrlPatterns("/captcha");
		registration.setName("captchaFilter");
		registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
		return registration;
	}
	
	@Bean
	public FilterRegistrationBean<SecurityFilter> securityFilterRegister() {
		FilterRegistrationBean<SecurityFilter> registration = new FilterRegistrationBean<SecurityFilter>();
		SecurityFilter securityFilter = new SecurityFilter();
		registration.setFilter(securityFilter);
		registration.addUrlPatterns("/*");
		registration.setName("securityFilter");
		registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
		return registration;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "fastboot.security", name = "annotation-enabled", havingValue = "true")
	public AnnotationAuthAspect annotationAuthAspect() {
		return new AnnotationAuthAspect();
	}

}