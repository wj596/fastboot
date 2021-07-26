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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisCache implements InnerCache {

	private final String name;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ValueOperations<String, Object> ops;
	private final long expire;
	
	public RedisCache(String name, RedisTemplate<String, Object> redisTemplate, long expire) {
		this.name = name;
		this.redisTemplate = redisTemplate;
		this.ops = this.redisTemplate.opsForValue();
		this.expire = expire;
	}

	@Override
	public boolean put(String key, Object value) {
		if(expire>0) {
			this.ops.set(this.wrap(key), value, this.expire, TimeUnit.SECONDS);
		}else {
			this.ops.set(this.wrap(key), value);
		}
		return true;
	}
	
	@Override
	public boolean hasKey(String key) {
		return this.redisTemplate.hasKey(this.wrap(key));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Optional<T> get(String key, Class<T> type) {
		Object value = this.redisTemplate.opsForValue().get(this.wrap(key));
		if (Objects.isNull(value)) {
			return Optional.empty();
		}
		
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		
		if(this.expire > 0) {
			this.ops.set(this.wrap(key), value, this.expire, TimeUnit.SECONDS);
		}
		
		return Optional.of((T)value);
	}

	@Override
	public boolean delete(String key) {
		this.redisTemplate.delete(this.wrap(key));
		return true;
	}

	@Override
	public Set<String> getKeys() {
		Set<String> keys = this.redisTemplate.keys(this.name+":*");
		return keys;
	}
	
	@Override
	public void cleanUp() {
		this.redisTemplate.delete(getKeys());
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public long getExpire() {
		return this.expire;
	}
	
	private String wrap(String key) {
		return this.name + "::" + key;
	}

}