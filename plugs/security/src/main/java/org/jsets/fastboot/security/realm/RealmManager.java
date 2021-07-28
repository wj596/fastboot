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
package org.jsets.fastboot.security.realm;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.security.IAccountProvider;
import org.jsets.fastboot.security.IEncryptProvider;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.auth.AuthorizationInfo;
import org.jsets.fastboot.security.cache.InnerCache;
import org.jsets.fastboot.security.cache.InnerCacheManager;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.dao.PasswdRetryRecordDao;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.listener.ListenerManager;
import org.jsets.fastboot.security.session.Session;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 认证管理器
 *
 * @Author wangjie
 * @date 2021.07.05 21:58
 */
@Slf4j
public class RealmManager {

    private final Map<String, Realm> REALMS = Maps.newHashMap();
	private SecurityProperties properties;
	private InnerCacheManager cacheManager;
	private ListenerManager listenerManager;
	private IAccountProvider accountProvider;
	private IEncryptProvider encryptProvider;
	private PasswdRetryRecordDao passwdRetryRecordDao;
    

    public void initialize(SecurityProperties properties
    					,InnerCacheManager cacheManager
			    		,ListenerManager listenerManager
			    		,IAccountProvider accountProvider
			    		,IEncryptProvider encryptProvider
			    		,List<Realm> customRealms) {
    	
    	this.properties = properties;
    	this.cacheManager = cacheManager;
    	this.listenerManager = listenerManager;
    	this.accountProvider = accountProvider;
    	this.encryptProvider = encryptProvider;

        String cacheName = properties.getPasswdRetryRecordCacheName();
        Long timeout = properties.getPasswdRetryRecordCacheTimeout();
        InnerCache cache = cacheManager.getCache(cacheName, timeout);
        this.passwdRetryRecordDao = new PasswdRetryRecordDao(cache);

        UsernamePasswordRealm usernamePasswordRealm =  new UsernamePasswordRealm();
        UsernameRealm usernameRealm =  new UsernameRealm();
        REALMS.put(usernamePasswordRealm.getSupportRequestClass().getName(), usernamePasswordRealm);
        REALMS.put(usernameRealm.getSupportRequestClass().getName(), usernameRealm);

        customRealms.forEach(custom -> {
            String support = custom.getSupportRequestClass().getName();
            REALMS.put(support, custom);
        });

        REALMS.forEach((name, custom)->{
            if(AbstractRealm.class.isAssignableFrom(custom.getClass()) ){
            	AbstractRealm abstractRealm = ((AbstractRealm)custom);
            	abstractRealm.setListenerManager(this.listenerManager);
            	abstractRealm.setProperties(this.properties);
            	abstractRealm.setAccountProvider(this.accountProvider);
            	abstractRealm.setEncryptProvider(this.encryptProvider);
            	abstractRealm.setPasswdRetryRecordDao(this.passwdRetryRecordDao);
            }
            log.info("初始化Realm，名称：{}， 类：{}", name, custom.getClass().getName());
        });
    }

    public AuthenticationInfo authenticate(AuthRequest request) throws UnauthorizedException {
        Realm realm = REALMS.get(request.getClass().getName());
        if (Objects.isNull(realm)) {
            throw new IllegalStateException("配置错误，没有支持" + request.getClass()+" 类型请求的Realm");
        }

        AuthenticationInfo authenticationInfo  = realm.doAuthenticate(request);
        if(Objects.isNull(authenticationInfo)){
            throw new UnauthorizedException(this.properties.getLoginErrorTips());
        }

        return authenticationInfo;
    }

    public boolean hasRole(Session session, String roleIdentifier) {
        Realm realm = REALMS.get(session.getAuthRequestType());
        if (Objects.isNull(realm)) {
            return false;
        }
        AuthorizationInfo authorizationInfo = realm.getAuthorizationInfo(session.getUsername());
        return authorizationInfo.getRoles().contains(roleIdentifier);
    }
    
    public boolean hasAnyRoles(Session session, Set<String> roleIdentifiers) {
        Realm realm = REALMS.get(session.getAuthRequestType());
        if (Objects.isNull(realm)) {
            return false;
        }
        
        AuthorizationInfo authorizationInfo = realm.getAuthorizationInfo(session.getUsername());
		for (Iterator<String> iterator = roleIdentifiers.iterator(); iterator.hasNext();){
			String roleIdentifier = iterator.next();
			if(authorizationInfo.getRoles().contains(roleIdentifier)) {
				return true;
			}
		}
        return false;
    }
    
    public boolean hasAllRoles(Session session, Set<String> roleIdentifiers) {
        Realm realm = REALMS.get(session.getAuthRequestType());
        if (Objects.isNull(realm)) {
            return false;
        }
        
        AuthorizationInfo authorizationInfo = realm.getAuthorizationInfo(session.getUsername());
		for (Iterator<String> iterator = roleIdentifiers.iterator(); iterator.hasNext();){
			String roleIdentifier = iterator.next();
			if(!authorizationInfo.getRoles().contains(roleIdentifier)) {
				return false;
			}
		}
        return true;
    }
    
    public boolean isPermitted(Session session, String permission) {
        Realm realm = REALMS.get(session.getAuthRequestType());
        if (Objects.isNull(realm)) {
            return false;
        }
        AuthorizationInfo authorizationInfo = realm.getAuthorizationInfo(session.getUsername());
        return authorizationInfo.getPermissions().contains(permission);
    }
    
    public boolean isPermittedAny(Session session, Set<String> permissions) {
        Realm realm = REALMS.get(session.getAuthRequestType());
        if (Objects.isNull(realm)) {
            return false;
        }
        
        AuthorizationInfo authorizationInfo = realm.getAuthorizationInfo(session.getUsername());
		for (Iterator<String> iterator = permissions.iterator(); iterator.hasNext();){
			String permission = iterator.next();
			if(authorizationInfo.getPermissions().contains(permission)) {
				return true;
			}
		}
        
        return false;
    }
    
    public boolean isPermittedAll(Session session, Set<String> permissions) {
        Realm realm = REALMS.get(session.getAuthRequestType());
        if (Objects.isNull(realm)) {
            return false;
        }
        
        AuthorizationInfo authorizationInfo = realm.getAuthorizationInfo(session.getUsername());
		for (Iterator<String> iterator = permissions.iterator(); iterator.hasNext();){
			String permission = iterator.next();
			if(!authorizationInfo.getPermissions().contains(permission)) {
				return false;
			}
		}
		
        return true;
    }

}