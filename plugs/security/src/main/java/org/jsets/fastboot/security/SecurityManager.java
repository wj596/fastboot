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
package org.jsets.fastboot.security;

import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.auth.*;
import org.jsets.fastboot.security.cache.CaffeineCacheManager;
import org.jsets.fastboot.security.cache.InnerCacheManager;
import org.jsets.fastboot.security.cache.RedisCacheManager;
import org.jsets.fastboot.security.config.SecurityCustomizer;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.filter.InnerFilter;
import org.jsets.fastboot.security.filter.InnerFilterManager;
import org.jsets.fastboot.security.listener.ListenerManager;
import org.jsets.fastboot.security.realm.Realm;
import org.jsets.fastboot.security.realm.RealmManager;
import org.jsets.fastboot.security.session.InnerSessionManager;
import org.jsets.fastboot.security.session.Session;
import org.jsets.fastboot.security.session.SessionManager;
import org.jsets.fastboot.security.token.InnerTokenManager;
import org.jsets.fastboot.security.token.TokenManager;
import org.jsets.fastboot.security.token.InnerToken;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 安全管理器
 *
 * @Author wangjie
 * @date 2021.07.05 23:37
 */
@Slf4j
public class SecurityManager {

	private static final String REDIS_CACHE_MANAGER_CLASS_NAME = "RedisCacheManager";

	private final AtomicBoolean initialized = new AtomicBoolean(false);
	private final ApplicationContext ctx;
	private final SecurityProperties properties;
	private final SecurityCustomizer customizer;

	private InnerCacheManager cacheManager;
	private InnerSessionManager sessionManager;
	private InnerTokenManager tokenManager;
	private ListenerManager listenerManager;
	private InnerFilterManager filterManager;
	private RealmManager realmManager;

	private IAccountProvider accountProvider;
	private IEncryptProvider encryptProvider;
	private ICaptchaProvider captchaProvider;
	private IAuthRuleProvider authRuleProvider;

	public SecurityManager(ApplicationContext ctx, SecurityProperties properties, SecurityCustomizer customizer) {
		this.ctx = ctx;
		this.properties = properties;
		this.customizer = customizer;
	}

	public void initialize() {
		if (!this.initialized.get()) {
			this.accountProvider = this.customizer.getAccountProvider();
			if (Objects.isNull(this.accountProvider)) {
				throw new RuntimeException("请设置AccountProvider");
			}
			this.encryptProvider = this.customizer.getEncryptProvider();
			if (Objects.isNull(this.encryptProvider)) {
				this.encryptProvider = new EncryptProviderImpl();
			}
			this.captchaProvider = this.customizer.getCaptchaProvider();
			this.authRuleProvider = this.customizer.getAuthRuleProvider();

			this.buildCacheManager();
			this.buildSessionManager();
			this.buildTokenManager();
			this.buildRealmManager();
			this.buildListenerManager();
			this.buildFilterManager();
			this.initialized.set(true);

			if (Objects.isNull(this.captchaProvider)) {
				this.captchaProvider = new CaptchaProviderImpl(this.properties, this.cacheManager);
			}

			SecurityUtils.setSecurityManager(this.oneself());
		}
	}

	/**
	 * 登陆
	 * 
	 * @param authRequest 认证请求
	 * @return AuthenticationInfo
	 * @throws UnauthorizedException 未认证异常
	 */
	public AuthenticationInfo login(AuthRequest authRequest) throws UnauthorizedException {
		AuthenticationInfo info = this.realmManager.authenticate(authRequest);
		Session session = this.getSessionManager().create(authRequest);
		InnerToken token = this.getTokenManager().issue(session);
		info.setToken(token);
		info.setSessionId(session.getId());
		SecurityContext.set(info.getSessionId());
		return info;
	}

	/**
	 * 登出
	 * 
	 * @param token 令牌
	 * @return AuthenticationInfo
	 * @throws UnauthorizedException 未认证异常
	 */
	public AuthenticationInfo logout(String token) throws UnauthorizedException {
		AuthenticationInfo info = this.tokenManager.validate(token);
		
		this.sessionManager.delete(info.getSessionId());
		SecurityContext.cleanup();
		return info;
	}

	/**
	 * 认证
	 * 
	 * @param token 令牌
	 * @throws UnauthorizedException 未认证异常
	 */
	public void authenticate(String token) throws UnauthorizedException {
		AuthenticationInfo info = this.tokenManager.validate(token);
		System.out.println("SessionId: "+info.getSessionId());
		boolean exists = this.sessionManager.isExists(info.getSessionId());
		if (!exists) {
			log.warn(this.properties.getTokenInvalidTips());
			throw new UnauthorizedException(this.properties.getTokenInvalidTips());
		}
		SecurityContext.set(info.getSessionId());
	}
	
	/**
	 * 是否认证
	 * 
	 * @return boolean
	 */
	public boolean isAuthenticated() {
		String sessionId = SecurityContext.get();
		if (StringUtils.isBlank(sessionId)) {
			return false;
		}
		return this.sessionManager.isExists(sessionId);
	}

	/**
	 * 根据Id获取Session
	 * 
	 * @param sessionId 主键
	 * @return Session
	 * @throws UnauthorizedException 未认证异常
	 */
	public Optional<Session> getSession(String sessionId) {
		return this.sessionManager.get(sessionId);
	}

	/**
	 * 获取当前上下文中的Session
	 * 
	 * @return Session
	 */
	private Session getCurrentSession() {
		String sessionId = SecurityContext.get();
		if (StringUtils.isBlank(sessionId)) {
			return null;
		}
		Optional<Session> opt = this.sessionManager.get(sessionId);
		if (!opt.isPresent()) {
			return null;
		}
		return opt.get();
	}

	public boolean hasRole(String roleIdentifier) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.realmManager.hasRole(session, roleIdentifier);
		this.getListenerManager().onAuthzAssert(session.getUsername(), roleIdentifier, allowed);
		return allowed;
	}

	public boolean hasAnyRole(Set<String> roleIdentifiers) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed = this.getRealmManager().hasAnyRoles(session, roleIdentifiers);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(roleIdentifiers), allowed);
		return allowed;
	}

	public boolean hasAllRoles(Set<String> roleIdentifiers) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.realmManager.hasAllRoles(session, roleIdentifiers);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(roleIdentifiers), allowed);
		return allowed;
	}
	
	public boolean isPermitted(String permission) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.realmManager.isPermitted(session, permission);
		this.getListenerManager().onAuthzAssert(session.getUsername(), permission, allowed);
		return allowed;
	}
	
	public boolean isPermittedAny(Set<String> permissions) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.realmManager.isPermittedAny(session, permissions);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(permissions), allowed);
		return allowed;
	}
	
	public boolean isPermittedAll(Set<String> permissions) {
		Session session = getCurrentSession();
		if (Objects.isNull(session)) {
			return false;
		}
		boolean allowed =  this.realmManager.isPermittedAll(session, permissions);
		this.getListenerManager().onAuthzAssert(session.getUsername(), StringUtils.join(permissions), allowed);
		return allowed;
	}

	private SecurityManager oneself() {
		return this;
	}

	private void buildCacheManager() {
		CacheManager cm = this.tryBean(CacheManager.class);
		if (Objects.nonNull(cm) && REDIS_CACHE_MANAGER_CLASS_NAME.equals(cm.getClass().getSimpleName())) {
			log.info("缓存类型： Redis");
			RedisConnectionFactory rcf = this.tryBean(RedisConnectionFactory.class);
			if (Objects.isNull(rcf)) {
				throw new RuntimeException("CacheManager初始化失败，redisConnectionFactory为空");
			}
			this.cacheManager = new RedisCacheManager(rcf);
		} else {
			log.info("缓存类型： Caffeine");
			this.cacheManager = new CaffeineCacheManager();
		}
	}

	private void buildSessionManager() {

		this.sessionManager = new InnerSessionManager();
		this.sessionManager.initialize(this.oneself());
	}

	private void buildTokenManager() {
		this.tokenManager = new InnerTokenManager();
		this.tokenManager.initialize(this.oneself());
	}

	private void buildListenerManager() {
		this.listenerManager = new ListenerManager();
		if (Objects.nonNull(this.customizer.getPasswordRetryListener())) {
			this.listenerManager.setPasswordRetryListener(this.customizer.getPasswordRetryListener());
		}
		if (Objects.nonNull(this.customizer.getAuthcListeners())) {
			this.listenerManager.setAuthcListeners(this.customizer.getAuthcListeners());
		}
		if (Objects.nonNull(this.customizer.getAuthzListeners())) {
			this.listenerManager.setAuthzListeners(this.customizer.getAuthzListeners());
		}
	}

	private void buildRealmManager() {
		this.realmManager = new RealmManager();
		this.realmManager.initialize(this.oneself());
	}

	private void buildFilterManager() {
		this.filterManager = new InnerFilterManager();
		this.filterManager.initialize(this.oneself());
	}

	private <T> T tryBean(Class<T> requiredType) {
		try {
			return this.ctx.getBean(requiredType);
		} catch (Exception e) {
			// 忽略
		}
		return null;
	}

	public SecurityProperties getProperties() {
		return properties;
	}

	public IAccountProvider getAccountProvider() {
		return accountProvider;
	}

	public IEncryptProvider getEncryptProvider() {
		return encryptProvider;
	}

	public ICaptchaProvider getCaptchaProvider() {
		return captchaProvider;
	}

	public IAuthRuleProvider getAuthRuleProvider() {
		return authRuleProvider;
	}

	public ApplicationContext getCtx() {
		return ctx;
	}

	public InnerCacheManager getCacheManager() {
		return cacheManager;
	}

	public SessionManager getSessionManager() {
		return this.sessionManager;
	}

	public TokenManager getTokenManager() {
		return this.tokenManager;
	}

	public ListenerManager getListenerManager() {
		return listenerManager;
	}

	public InnerFilterManager getFilterManager() {
		return filterManager;
	}

	public RealmManager getRealmManager() {
		return realmManager;
	}

	public List<Realm> getCustomRealms() {
		return this.customizer.getRealms();
	}

	public Map<String, InnerFilter> getCustomFilters() {
		return this.customizer.getFilters();
	}
}
