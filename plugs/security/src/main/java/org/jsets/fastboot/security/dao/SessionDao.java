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

import java.util.Optional;
import java.util.Set;
import org.jsets.fastboot.security.cache.InnerCache;
import org.jsets.fastboot.security.session.Session;
import com.google.common.collect.Sets;

public class SessionDao {

	private final InnerCache cache;

	public SessionDao(InnerCache cache) {
		this.cache = cache;
	}

	public void insert(Session session) {
		this.cache.put(session.getId(), session);
	}

	public boolean isExists(String sessionId) {
		return this.cache.hasKey(sessionId);
	}
	
	public Optional<Session> get(String sessionId) {
		Optional<Session> opt = this.cache.get(sessionId, Session.class);
		if (opt.isPresent()) {
			Session session = opt.get();
			session.touch();
			this.cache.put(session.getId(), session);
		}
		return opt;
	}

	public Set<Session> getSessions() {
		Set<Session> tokens = Sets.newHashSet();
		for (String sessionId : this.cache.getKeys()) {
			Optional<Session> token = this.cache.get(sessionId, Session.class);
			if (token.isPresent()) {
				tokens.add(token.get());
			}
		}
		return tokens;
	}

	public void delete(String sessionId) {
		this.cache.delete(sessionId);
	}
}