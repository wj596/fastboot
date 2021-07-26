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

/**
 * 
 * 安全上下文
 * @author wj596
 *
 */
public class SecurityContext {

	private static final ThreadLocal<String> ctx = new ThreadLocal<String>();
	
    /**
     * 设置当前安全上下文中的回话
     */
    protected static void set(String sessionId) {
    	SecurityContext.ctx.set(sessionId);
    }
	
    /**
     * 获取当前上下文中的会话
     */
    public static String get() {
        return SecurityContext.ctx.get();
    }
    
    /**
     * 删除当前安全上下文中的会话
     */
    protected static void cleanup() {
    	SecurityContext.ctx.remove();
    }
}
