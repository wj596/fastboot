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

import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.model.IAccount;
import org.jsets.fastboot.security.session.Session;
import org.jsets.fastboot.security.session.SessionManager;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 安全工具类
 *
 * @Author wangjie
 * @date 2021.07.05 22:04
 */
public class SecurityUtils {

    
    private static SecurityManager securityManager;

    /**
     * 当前安全上下文中是否存在会话
     */
    public static boolean hasSession() {
        String sessionId = SecurityContext.get();
        if (Objects.isNull(sessionId)) {
            return false;
        }
        
        Optional<Session> opt = getSessionManager().get(sessionId);
        if (!opt.isPresent()) {
        	return false;
        }
        
        return true;
    }

    /**
     * 获取当前上下文中的会话
     */
    public static Optional<Session> getSession() {
        String sessionId = SecurityContext.get();
        if (Objects.isNull(sessionId)) {
            return Optional.empty();
        }
        return getSessionManager().get(sessionId);
    }

    /**
     * 获取当前认证的用户
     */
    public static <T extends IAccount> T getAccount() throws UnauthorizedException {
    	Optional<Session> opt = getSession();
    	if(!opt.isPresent()) {
    		throw new UnauthorizedException(getSecurityManager().getProperties().getUnauthorizedTips());
    	}
    	
    	IAccount account = getAccountProvider().loadAccount(opt.get().getUsername());
    	return (T) account;
    }

    /**
     * 登陆
     */
    public static AuthenticationInfo login(AuthRequest authRequest) throws UnauthorizedException {
       return getSecurityManager().login(authRequest);
    }
    
    /**
     * 登出
     */
    public static AuthenticationInfo logout(String token) throws UnauthorizedException {
        return getSecurityManager().logout(token);
    }
    
    /**
     * 认证
     */
    public static void authenticate(String token) throws UnauthorizedException {
        getSecurityManager().authenticate(token);
    }
    
    public static boolean hasRole(String roleIdentifier) {
    	return getSecurityManager().hasRole(roleIdentifier);
    }

    public static boolean hasAnyRole(Set<String> roleIdentifiers) {
    	return getSecurityManager().hasAnyRole(roleIdentifiers);
    }
 
    public static boolean hasAllRoles(Set<String> roleIdentifiers) {
    	return getSecurityManager().hasAnyRole(roleIdentifiers);
    }
    
    /**
     * 密码加密
     */
    public static String encryptPassword(String password) {
        return getSecurityManager().getEncryptProvider().encrypt(password);
    }

    protected static SecurityManager getSecurityManager() {
        return SecurityUtils.securityManager;
    }

    protected static IAccountProvider getAccountProvider() {
        return getSecurityManager().getAccountProvider();
    }

    protected static SessionManager getSessionManager() {
        return getSecurityManager().getSessionManager();
    }

    protected static void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.securityManager = securityManager;
    }
}