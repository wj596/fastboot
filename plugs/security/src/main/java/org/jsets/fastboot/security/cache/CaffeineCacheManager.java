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
package org.jsets.fastboot.security.cache;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CaffeineCacheManager implements InnerCacheManager {

	private static final ConcurrentMap<String, InnerCache> CACHES = new ConcurrentHashMap<String, InnerCache>();

	public InnerCache getCache(String cacheName, long expire) {
		InnerCache cache = CACHES.get(cacheName);
		if (Objects.nonNull(cache)) {
			return cache;
		} else {
			synchronized (CACHES) {
				cache = CACHES.get(cacheName);
				if (Objects.isNull(cache)) {
					Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
							.initialCapacity(1000)
							.maximumSize(Integer.MAX_VALUE)
							.recordStats();
					if (expire > 0) {
						caffeine.expireAfterAccess(expire, TimeUnit.SECONDS);
					}
					cache = new CaffeineCache(cacheName, caffeine.build(), expire);
					CACHES.put(cacheName, cache);
				}
				return cache;
			}
		}
	}

}