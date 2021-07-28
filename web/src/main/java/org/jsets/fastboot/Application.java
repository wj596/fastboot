package org.jsets.fastboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

/**
 * 
 * 启动程序
 *
 */
@EnableCaching
@SpringBootApplication
public class Application {  
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {  
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Application.class).logStartupInfo(true).run(args);
        Environment env = ctx.getEnvironment();
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        stopWatch.stop();
        log.info("{}启动完成，监听端口：{}，耗时：{}s", applicationName, serverPort, stopWatch.getTotalTimeSeconds());
	}
}