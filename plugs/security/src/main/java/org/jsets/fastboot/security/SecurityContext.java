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

import org.jsets.fastboot.security.model.ContextItem;

/**
 * 
 * 安全上下文
 * @author wj596
 *
 */
public class SecurityContext {

	private static final ThreadLocal<ContextItem> CTX = new ThreadLocal<ContextItem>();
	
    /**
     * 设置当前安全上下文中的回话
     */
    protected static void set(ContextItem item) {
    	SecurityContext.CTX.set(item);
    }
	
    /**
     * 获取当前上下文中的会话
     */
    public static ContextItem get() {
        return SecurityContext.CTX.get();
    }
    
    /**
     * 获取当前上下文中的SessionId
     */
    public static String getSessionId() {
    	ContextItem item = SecurityContext.CTX.get();
    	if(null!=item) {
    		return item.getSessionId();
    	}
    	return null;
    }
    
    /**
     * 获取当前上下文中的Token
     */
    public static String getToken() {
    	ContextItem item = SecurityContext.CTX.get();
    	if(null!=item) {
    		return item.getToken();
    	}
    	return null;
    }
    
    /**
     * 获取当前上下文中的Username
     */
    public static String getUsername() {
    	ContextItem item = SecurityContext.CTX.get();
    	if(null!=item) {
    		return item.getUsername();
    	}
    	return null;
    }
    
    /**
     * 删除当前安全上下文中的会话
     */
    protected static void cleanup() {
    	SecurityContext.CTX.remove();
    }
}
