package org.jsets.fastboot.security.config;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CaffeineTest {

	public static void main(String[] args) {
		Cache<Object, Object> cache = Caffeine.newBuilder()
				// 设置最后一次写入或访问后经过固定时间过期
				.expireAfterAccess(5, TimeUnit.SECONDS)
				// 初始的缓存空间大小
				.initialCapacity(1000)
				// 缓存的最大条数
				.maximumSize(Integer.MAX_VALUE).recordStats().build();

		cache.put("user", "wangjie");
		Object user = cache.getIfPresent("user");
		System.out.println("user:" + user);

		try {
			Thread.sleep(4 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		user = cache.getIfPresent("user");
		System.out.println("user:" + user);

		for(;;) {
			try {
				Thread.sleep(4*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			user = cache.getIfPresent("user");
			System.out.println("user:"+ user);
		}

	}

}
