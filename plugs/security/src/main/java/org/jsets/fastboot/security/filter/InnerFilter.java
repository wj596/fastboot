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
package org.jsets.fastboot.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 内部访问控制过滤器接口
 *
 * @Author wangjie
 * @date 2021.07.04 23:26
 */
public interface InnerFilter {

	/**
	  * 
	  * 是否允许访问
	  * 当返回false时触发 onAccessDenied 方法
	  * 
	  */
	boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Set<String> props) throws Exception;

	/**
	 * 
	 * 当isAccessAllowed返回false时触发此方法
	 * <br>
	 * 当返回false,SecurityFilter过滤器不执行doFilter，停止<br>
	 * 当返回true,SecurityFilter过滤器执行doFilter，向下继续<br>
	 * <br>
	 * 当需要response输出内容时，则返回false<br>
	 * 当需要web过滤器继续向下则返回ture<br>
	 * 
	 */
	boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception;
}