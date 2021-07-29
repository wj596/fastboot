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
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.cache.InnerCacheManager;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.filter.InnerFilterManager;
import org.jsets.fastboot.security.model.ContextItem;
import org.jsets.fastboot.security.model.IAccount;
import org.jsets.fastboot.security.session.Session;
import org.jsets.fastboot.security.session.SessionManager;
import org.jsets.fastboot.security.util.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全工具类
 *
 * @Author wangjie
 * @date 2021.07.05 22:04
 */
@Slf4j
public class SecurityUtils {

	private static SecurityProperties properties;
	private static IAuthenticator authenticator;
	private static IAuthorizer authorizer;
	private static InnerCacheManager cacheManager;
	private static InnerFilterManager filterManager;
	private static SessionManager sessionManager;
	private static IEncryptProvider encryptProvider;
	private static IAccountProvider accountProvider;
	private static ICaptchaProvider captchaProvider;

	/**
	 * 获取当前上下文
	 */
	public static ContextItem getContext() {
		return SecurityContext.get();
	}
	
	/**
	 * 获取当前上下文中的SessionId
	 */
	public static String getSessionId() {
		return SecurityContext.getSessionId();
	}
	
	/**
	 * 清空当前上下文
	 */
	public static void setContext(ContextItem item) {
		SecurityContext.set(item);
	}
	
	/**
	 * 获取当前上下文中的Username
	 */
	public static String getUsername() {
		return SecurityContext.getUsername();
	}
	
	/**
	 * 获取当前上下文中的Token
	 */
	public static String getToken() {
		return SecurityContext.getToken();
	}
	
	/**
	 * 清空当前上下文
	 */
	public static void cleanupContext() {
		SecurityContext.cleanup();
	}
	
	/**
	 * 当前安全上下文中是否存在会话
	 */
	public static boolean hasSession() {
		String sessionId = SecurityContext.getSessionId();
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
		String sessionId = SecurityContext.getSessionId();
		if (Objects.isNull(sessionId)) {
			return Optional.empty();
		}
		return getSessionManager().get(sessionId);
	}

	public static String encryptPassword(String plain) {
		return getEncryptProvider().encrypt(plain);
	}

	/**
	 * 执行内部过滤器链
	 * 
	 * @param httpRequest  请求
	 * @param httpResponse 响应
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean doInnerFilterChain(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		return SecurityUtils.filterManager.doInnerFilterChain(httpRequest, httpResponse);
	}

	/**
	 * 生成验证码
	 * 
	 * @param captchaKey 验证码KEY
	 * @return BufferedImage
	 */
	public static BufferedImage generateCaptcha(String captchaKey) {
		return getCaptchaProvider().generateCaptcha(captchaKey);
	}

	/**
	 * HMAC签名
	 * @param timestamp 时间戳
	 * @return  String
	 */
	public static String hmacSignature(Long timestamp) {
		String context = timestamp.toString();
		if (StringUtils.notEmpty(getProperties().getHMACSignSalt())) {
			context = context + getProperties().getHMACSignSalt();
		}
		return CryptoUtils.hmacSha256(context, getProperties().getHMACSignKey());
	}

	/**
	 * HMAC签名验证
	 * @param timestamp 时间戳
	 * @param sign 签名
	 * @return
	 */
	public static boolean hmacSignatureValidate(Long timestamp, String sign) {
		String context = timestamp.toString();
		if (StringUtils.notEmpty(getProperties().getHMACSignSalt())) {
			context = context + getProperties().getHMACSignSalt();
		}

		String curr = CryptoUtils.hmacSha256(context, getProperties().getHMACSignKey());
		if (!curr.equals(sign)) {
			return false;
		}

		long ss = System.currentTimeMillis() - timestamp;
		if (ss > (getProperties().getHMACSignTimeout() * 1000)) {
			log.warn("令牌过期");
			return false;
		}
		return true;
	}

	/**
	 * 登陆
	 */
	public static AuthenticationInfo login(AuthRequest authRequest) throws UnauthorizedException {
		return getAuthenticator().login(authRequest);
	}

	/**
	 * 登出
	 */
	public static AuthenticationInfo logout(String token) throws UnauthorizedException {
		return getAuthenticator().logout(token);
	}

	/**
	 * 认证
	 */
	public static void authenticate(String token) throws UnauthorizedException {
		getAuthenticator().authenticate(token);
	}

	/**
	 * 是否认证
	 */
	public static boolean isAuthenticated() {
		return getAuthenticator().isAuthenticated();
	}

	/**
	 * 获取当前认证的用户
	 */
	public static <T extends IAccount> T getAccount() throws UnauthorizedException {
		return getAuthenticator().getAccount();
	}
	
	public static boolean hasRole(String roleIdentifier) {
		return getAuthorizer().hasRole(roleIdentifier);
	}

	public static boolean hasAnyRole(Set<String> roleIdentifiers) {
		return getAuthorizer().hasAnyRole(roleIdentifiers);
	}

	public static boolean hasAllRoles(Set<String> roleIdentifiers) {
		return getAuthorizer().hasAnyRole(roleIdentifiers);
	}

	public static boolean isPermitted(String permission) {
		return getAuthorizer().isPermitted(permission);
	}

	public static boolean isPermittedAny(Set<String> permissions) {
		return getAuthorizer().isPermittedAny(permissions);
	}

	public static boolean isPermittedAll(Set<String> permissions) {
		return getAuthorizer().isPermittedAll(permissions);
	}

	public static SecurityProperties getProperties() {
		return properties;
	}

	protected static void setProperties(SecurityProperties properties) {
		SecurityUtils.properties = properties;
	}

	protected static IAuthenticator getAuthenticator() {
		return authenticator;
	}

	protected static void setAuthenticator(IAuthenticator authenticator) {
		SecurityUtils.authenticator = authenticator;
	}

	protected static IAuthorizer getAuthorizer() {
		return authorizer;
	}

	protected static void setAuthorizer(IAuthorizer authorizer) {
		SecurityUtils.authorizer = authorizer;
	}

	protected static InnerFilterManager getFilterManager() {
		return filterManager;
	}

	protected static void setFilterManager(InnerFilterManager filterManager) {
		SecurityUtils.filterManager = filterManager;
	}

	protected static SessionManager getSessionManager() {
		return sessionManager;
	}

	protected static void setSessionManager(SessionManager sessionManager) {
		SecurityUtils.sessionManager = sessionManager;
	}

	protected static IEncryptProvider getEncryptProvider() {
		return encryptProvider;
	}

	protected static void setEncryptProvider(IEncryptProvider encryptProvider) {
		SecurityUtils.encryptProvider = encryptProvider;
	}

	protected static IAccountProvider getAccountProvider() {
		return accountProvider;
	}

	protected static void setAccountProvider(IAccountProvider accountProvider) {
		SecurityUtils.accountProvider = accountProvider;
	}

	protected static ICaptchaProvider getCaptchaProvider() {
		return captchaProvider;
	}

	protected static void setCaptchaProvider(ICaptchaProvider captchaProvider) {
		SecurityUtils.captchaProvider = captchaProvider;
	}

	public static InnerCacheManager getCacheManager() {
		return cacheManager;
	}

	protected static void setCacheManager(InnerCacheManager cacheManager) {
		SecurityUtils.cacheManager = cacheManager;
	}
}