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
package org.jsets.fastboot.security.listener;

import org.jsets.fastboot.security.auth.AuthRequest;

public interface AuthcListener {

	/**
	 * 登录成功
	 * @param authRequest 认证请求
	 */
	void onLoginSuccess(AuthRequest authRequest);

	/**
	 * 登录失败
	 * @param authRequest 认证请求
	 * @param reason  登录失败原因原因
	 */
	void onLoginFailure(AuthRequest authRequest, String reason);
	
	/**
	 * 登出
	 * @param username 用户名
	 */
	void onLogout(String username);
}
