package org.jsets.fastboot.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author wangjie
 * 项目启动后 会执行这个方法，类似spring中的监听器
 * 这是个示例，你要不需要项目启动后进行额外的初始化，这个类可以删掉
 */
@Component
public class AppCommandLineRunner implements CommandLineRunner {

	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	}

}