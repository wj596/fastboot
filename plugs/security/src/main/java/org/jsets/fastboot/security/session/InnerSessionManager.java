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
package org.jsets.fastboot.security.session;

import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.cache.InnerCache;
import org.jsets.fastboot.security.cache.InnerCacheManager;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.dao.SessionDao;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Session管理器
 *
 * @Author wangjie
 * @date 2021.06.08 23:07
 */
@Slf4j
public class InnerSessionManager implements SessionManager {

	private SecurityProperties properties;
	private SessionDao sessionDao;
	
    public void initialize(SecurityProperties properties,InnerCacheManager cacheManager){
		this.properties = properties;
    	InnerCache cache = cacheManager.getCache(this.properties.getSessionCacheName(), this.properties.getSessionTimeout());
    	sessionDao = new SessionDao(cache);
    }

	public Session create(AuthRequest authRequest){
		Session session = new Session();
		String sessionId = StringUtils.getUUID();
		session.setAuthRequestType(authRequest.getTypeName());
		session.setStartTimestamp(new Date());
		session.setId(sessionId);
		session.setUsername(authRequest.getUsername());
		this.sessionDao.insert(session);
		return session;
	}
	
	@Override
	public boolean isExists(String id) {
		return this.sessionDao.isExists(id);
	}

	public Optional<Session> get(String id) {
    	return this.sessionDao.get(id);
	}

    public void delete(String sessionId) {
    	this.sessionDao.delete(sessionId);
    }

	@Override
	public Set<Session> getSessions() {
		return this.sessionDao.getSessions();
	}
}