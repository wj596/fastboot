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
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsets.fastboot.security.SecurityUtils;
import org.jsets.fastboot.common.util.JsonUtils;
import org.jsets.fastboot.security.auth.AuthResponse;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.util.WebUtils;
import org.jsets.fastboot.security.exception.ForbiddenException;
import org.jsets.fastboot.security.exception.RuntimeForbiddenException;
import org.jsets.fastboot.security.exception.RuntimeUnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 访问控制拦截器
 * 
 * @author wnagjie
 * 
 */
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		boolean doContinue = false;
		try {
			doContinue = SecurityUtils.doInnerFilterChain(request, response);
		} catch (Exception e) {
			this.handleException(response, e);
		}

		if (doContinue) {
			filterChain.doFilter(request, response);
		}
	}

	private void handleException(HttpServletResponse response, Throwable ex) {
		int httpStatus = 0;
		AuthResponse authResponse = null;
		log.error(ex.getMessage(), ex);
		if (ex instanceof IllegalArgumentException) {
			httpStatus = HttpStatus.BAD_REQUEST.value();
			authResponse = AuthResponse.failed(httpStatus, ex.getMessage());
		} else if (ex instanceof RuntimeUnauthorizedException) {
			httpStatus = HttpStatus.UNAUTHORIZED.value();
			authResponse = AuthResponse.failed(httpStatus, ex.getMessage());
		} else if (ex instanceof RuntimeForbiddenException) {
			httpStatus = HttpStatus.FORBIDDEN.value();
			authResponse = AuthResponse.failed(httpStatus, ex.getMessage());
		} else if (ex instanceof UnauthorizedException) {
			UnauthorizedException exception = (UnauthorizedException) ex;
			httpStatus = HttpStatus.UNAUTHORIZED.value();
			int bizStatus = httpStatus;
			if (Objects.nonNull(exception.getBizStatus())) {
				bizStatus = exception.getBizStatus();
			}
			authResponse = AuthResponse.failed(bizStatus, exception.getMessage());
		} else if (ex instanceof ForbiddenException) {
			ForbiddenException exception = (ForbiddenException) ex;
			httpStatus = HttpStatus.FORBIDDEN.value();
			int bizStatus = httpStatus;
			if (Objects.nonNull(exception.getBizStatus())) {
				bizStatus = exception.getBizStatus();
			}
			authResponse = AuthResponse.failed(bizStatus, exception.getMessage());
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
			authResponse = AuthResponse.failed(httpStatus, ex.getMessage());
		}

		WebUtils.writeResponse(response, httpStatus, JsonUtils.toJsonString(authResponse));
	}

}