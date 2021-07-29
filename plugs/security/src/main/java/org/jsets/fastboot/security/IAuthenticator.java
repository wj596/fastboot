package org.jsets.fastboot.security;

import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.model.IAccount;

public interface IAuthenticator {

	/**
	 * 登陆
	 * 
	 * @param authRequest 认证请求
	 * @return AuthenticationInfo
	 * @throws UnauthorizedException 未认证异常
	 */
	AuthenticationInfo login(AuthRequest authRequest) throws UnauthorizedException;

	/**
	 * 登出
	 * 
	 * @param token 令牌
	 * @return AuthenticationInfo
	 * @throws UnauthorizedException 未认证异常
	 */
	AuthenticationInfo logout(String token) throws UnauthorizedException;

	/**
	 * 认证
	 * 
	 * @param token 令牌
	 * @throws UnauthorizedException 未认证异常
	 */
	void authenticate(String token) throws UnauthorizedException;
	
	/**
	 * 是否认证
	 * 
	 * @return boolean
	 */
	boolean isAuthenticated();
	
	/**
	 * 获取当前用户
	 * @param <T>
	 * @return
	 * @throws UnauthorizedException
	 */
	<T extends IAccount> T getAccount() throws UnauthorizedException;
}