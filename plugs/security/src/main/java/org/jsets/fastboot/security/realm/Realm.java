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

import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.auth.AuthorizationInfo;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.session.Session;

/**
 * 
 * 认证器
 * @author wj596
 *
 */
public interface Realm {
	/**
	 * 认证
	 * @param request 认证请求
	 * @return AuthenticationInfo
	 * @throws UnauthorizedException
	 */
	AuthenticationInfo doAuthenticate(AuthRequest request) throws UnauthorizedException;

	/**
	 * 获取授权信息
	 * @param username 账号
	 * @return 授权信息
	 */
	AuthorizationInfo getAuthorizationInfo(String username);

	/**
	 * 支持的认证请求类型
	 * @return Class<? extends AuthcRequest>
	 */
	Class<? extends AuthRequest> getSupportRequestClass();

	/**
	 * 是否支持此认证请求
	 * @param request 认证请求
	 * @return boolean
	 */
	boolean isSupport(AuthRequest request);
}
