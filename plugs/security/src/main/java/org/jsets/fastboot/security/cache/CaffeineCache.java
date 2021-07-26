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
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.collect.Sets;

public class CaffeineCache implements InnerCache{

	private final String name;
	private final Cache<Object, Object> actual;
	private final long expire;
	
	public CaffeineCache(String name, Cache<Object, Object> actual, long expire) {
		this.name = name;
		this.actual = actual;
		this.expire = expire;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public long getExpire() {
		return this.expire;
	}

	@Override
	public boolean put(String key, Object value) {
		this.actual.put(key, value);
		return true;
	}

	@Override
	public boolean hasKey(String key) {
		return this.actual.asMap().containsKey(key);
	}

	@Override
	public <T> Optional<T> get(String key, Class<T> type) {
		Object value = this.actual.getIfPresent(key);
		if (Objects.isNull(value)) {
			return Optional.empty();
		}
		
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		
		return Optional.of((T)value);
	}

	@Override
	public boolean delete(String key) {
		this.actual.invalidate(key);
		return true;
	}

	@Override
	public Set<String> getKeys() {
		Set<String> keys = Sets.newLinkedHashSet();
		for(Object o : this.actual.asMap().keySet()) {
			keys.add(o.toString());
		}
		return keys;
	}
	
	@Override
	public void cleanUp() {
		this.actual.cleanUp();
	}
}
