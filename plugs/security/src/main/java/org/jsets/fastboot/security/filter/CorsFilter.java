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

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * 跨域过滤器
 * 
 * @author wj596
 *
 */
@Slf4j
public class CorsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("CorsFilter:{}", request.getServletPath());
		String origin = request.getHeader("Origin");
		if (origin == null) {
			origin = request.getHeader("Referer");
		}
		
		response.setHeader("Access-Control-Allow-Origin", origin); // 允许指定域访问跨域资源
		response.setHeader("Access-Control-Allow-Credentials", "true"); // 允许客户端携带跨域cookie，此时origin值不能为“*”，只能为指定单一域名
		if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
			String allowMethod = request.getHeader("Access-Control-Request-Method");
			String allowHeaders = request.getHeader("Access-Control-Request-Headers");
			response.setHeader("Access-Control-Max-Age", "18000"); // 浏览器缓存预检请求结果时间,单位:秒
			response.setHeader("Access-Control-Allow-Methods", allowMethod); // 允许浏览器在预检请求成功之后发送的实际请求方法名
			response.setHeader("Access-Control-Allow-Headers", allowHeaders); // 允许浏览器发送的请求消息头
			return;
		}
		filterChain.doFilter(request, response);	
	}

}