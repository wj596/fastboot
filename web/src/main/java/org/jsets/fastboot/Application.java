package org.jsets.fastboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
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
        new SpringApplicationBuilder(Application.class).logStartupInfo(true).run(args);
        stopWatch.stop();
        log.info("fastboot服务启动完成，耗时:{}s", stopWatch.getTotalTimeSeconds());
	}
}