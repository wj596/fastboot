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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

public class RedisCacheManager implements InnerCacheManager {

	private static final ConcurrentMap<String, InnerCache> CACHES = new ConcurrentHashMap<String, InnerCache>();

	private RedisTemplate<String, Object> redisTemplate;
	
	public RedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
		this.redisTemplate = new RedisTemplate<String, Object>();
		this.redisTemplate.setConnectionFactory(redisConnectionFactory);
		this.redisTemplate.setKeySerializer(jsonSerializer);
		//this.redisTemplate.setValueSerializer(jsonSerializer);
		this.redisTemplate.setBeanClassLoader(RedisCache.class.getClassLoader());
		this.redisTemplate.afterPropertiesSet();
	}
	
	public InnerCache getCache(String cacheName, long expire) {
		InnerCache cache = CACHES.get(cacheName);
		if (Objects.nonNull(cache)) {
			return cache;
		} else {
			synchronized (CACHES) {
				cache = CACHES.get(cacheName);
				if (Objects.isNull(cache)) {
					cache = new RedisCache(cacheName, this.redisTemplate, expire);
					CACHES.put(cacheName, cache);
				}
				return cache;
			}
		}
	}
	
}
