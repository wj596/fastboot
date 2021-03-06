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
package org.jsets.fastboot.security.model;

import java.io.Serializable;

/**
 * 账号的抽象，应用中的用户实体要实现这个接口
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月31日
 */ 
public interface IAccount extends Serializable{
	
	/**
	 * 获取用户名
	 */
	public String getAccount();
	
	/**
	 * 获取登陆口令
	 */
	public String getPassword();
}