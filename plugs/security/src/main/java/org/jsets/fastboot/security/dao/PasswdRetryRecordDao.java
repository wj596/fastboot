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
package org.jsets.fastboot.security.dao;

import org.jsets.fastboot.security.cache.InnerCache;
import java.util.Optional;

/**
 * 
 * 密码重试记录持久类
 * @author wj596
 *
 */
public class PasswdRetryRecordDao {

	private final InnerCache cache;
	private final Object cacheMonitor = new Object();

	public PasswdRetryRecordDao(InnerCache cache) {
		this.cache = cache;
	}
	
	/**
	 * 获取并增加密码重试次数
	 */
	public int increaseAndGet(String account) {
		Integer count = null;
		synchronized (cacheMonitor) {
			Optional<Integer> opt = cache.get(account, Integer.class);
			if (!opt.isPresent()) {
				count = new Integer(0);
			}
			cache.put(account, ++count);
			return count;
		}
	}

	/**
	 * 清扫密码重试次数
	 */
	public void cleanup(String account) {
		this.cache.delete(account);
	}
	
}