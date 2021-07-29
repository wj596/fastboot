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

import org.jsets.fastboot.security.auth.CaptchaProviderImpl;
import org.jsets.fastboot.security.auth.EncryptProviderImpl;
import org.jsets.fastboot.security.cache.CaffeineCacheManager;
import org.jsets.fastboot.security.cache.InnerCacheManager;
import org.jsets.fastboot.security.cache.RedisCacheManager;
import org.jsets.fastboot.security.config.SecurityCustomizer;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.filter.InnerFilterManager;
import org.jsets.fastboot.security.listener.ListenerManager;
import org.jsets.fastboot.security.realm.RealmManager;
import org.jsets.fastboot.security.session.InnerSessionManager;
import org.jsets.fastboot.security.token.InnerTokenManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
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

	private IAuthenticator authenticator;
	private IAuthorizer authorizer;
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
			
			this.authenticator = this.customizer.getAuthenticator();
			if (Objects.isNull(this.authenticator)) {
				this.authenticator = new AuthenticatorImpl();
			}
			if(AbstractAuthenticator.class.isAssignableFrom(this.authenticator.getClass()) ){
				AbstractAuthenticator abstractAuthenticator = (AbstractAuthenticator)this.authenticator;
				abstractAuthenticator.setProperties(this.properties);
				abstractAuthenticator.setAccountProvider(this.accountProvider);
				abstractAuthenticator.setRealmManager(this.realmManager);
				abstractAuthenticator.setSessionManager(this.sessionManager);
				abstractAuthenticator.setTokenManager(this.tokenManager);
			}
			
			this.authorizer = this.customizer.getAuthorizer();
			if (Objects.isNull(this.authorizer)) {
				this.authorizer = new AuthorizerImpl();
			}
			if(AbstractAuthorizer.class.isAssignableFrom(this.authorizer.getClass()) ){
				AbstractAuthorizer abstractAuthorizer = (AbstractAuthorizer)this.authorizer;
				abstractAuthorizer.setListenerManager(this.listenerManager);
				abstractAuthorizer.setRealmManager(this.realmManager);
				abstractAuthorizer.setSessionManager(this.sessionManager);
			}
			
			this.buildFilterManager();
			if (Objects.isNull(this.captchaProvider)) {
				this.captchaProvider = new CaptchaProviderImpl(this.properties, this.cacheManager);
			}
			
			this.initialized.set(true);
			SecurityUtils.setProperties(this.properties);
			SecurityUtils.setAuthenticator(this.authenticator);
			SecurityUtils.setAuthorizer(this.authorizer);
			SecurityUtils.setCacheManager(this.cacheManager);
			SecurityUtils.setFilterManager(this.filterManager);
			SecurityUtils.setSessionManager(this.sessionManager);
			SecurityUtils.setEncryptProvider(this.encryptProvider);
			SecurityUtils.setAccountProvider(this.accountProvider);
		}
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
		this.sessionManager.initialize(this.properties, this.cacheManager);
	}

	private void buildTokenManager() {
		this.tokenManager = new InnerTokenManager();
		this.tokenManager.initialize(this.properties);
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
		this.realmManager.initialize(this.properties
									,this.cacheManager
						    		,this.listenerManager
						    		,this.accountProvider
						    		,this.encryptProvider
						    		,this.customizer.getRealms());
	}

	private void buildFilterManager() {
		this.filterManager = new InnerFilterManager();
		this.filterManager.initialize(this.properties
									,this.authenticator
									,this.authorizer
									,this.authRuleProvider
									,this.captchaProvider
									,this.listenerManager
									,this.customizer.getFilters());
	}

	private <T> T tryBean(Class<T> requiredType) {
		try {
			return this.ctx.getBean(requiredType);
		} catch (Exception e) {
			// 忽略
		}
		return null;
	}
}
