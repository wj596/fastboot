package org.jsets.fastboot.config;

import org.jsets.fastboot.security.config.SecurityCustomizer;
import org.jsets.fastboot.service.system.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 系统配置
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.12 21:49
 * @since 0.1
 */
@Configuration
public class AppConfiguration{

	@Autowired
	private IUserService userService;
	
    @Bean
    public SecurityCustomizer securityCustomizer() {
    	SecurityCustomizer customizer = new SecurityCustomizer();
    	customizer.setAccountProvider(this.userService);
        return customizer;
    }
	
}