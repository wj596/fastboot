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
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.common.util.JsonUtils;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.auth.AuthResponse;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.auth.UsernamePasswordRequest;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.util.WebUtils;
import org.springframework.http.HttpStatus;
import java.util.Set;

/**
 *   登陆过滤器
 * 
 * @date 2016年6月31日
 */
@Slf4j
public class LoginFilter extends AbstractInnerFilter {
	@Override
	public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Set<String> props) throws Exception {
		return false;
	}

	@Override
	public boolean onAccessDenied(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
		if(!WebUtils.isPostMethod(servletRequest)){// 只处理POST
			return false;
		}

		UsernamePasswordRequest authRequest = new UsernamePasswordRequest();
		if(WebUtils.isJsonRequest(servletRequest)){
			String body = WebUtils.readJsonBody(servletRequest);
			authRequest = JsonUtils.parse(body, UsernamePasswordRequest.class);
		}else{
			boolean rememberMe = StringUtils.toBoolean(servletRequest.getParameter(AuthRequest.PARAM_REMEMBER_ME));
			authRequest.setUsername(servletRequest.getParameter(AuthRequest.PARAM_USERNAME));
			authRequest.setPassword(servletRequest.getParameter(AuthRequest.PARAM_PASSWORD));
			authRequest.setCaptchaKey(servletRequest.getParameter(AuthRequest.PARAM_CAPTCHA_KEY));
			authRequest.setCaptcha(servletRequest.getParameter(AuthRequest.PARAM_CAPTCHA));
			authRequest.setRememberMe(rememberMe);
		}
		authRequest.setUserHost(WebUtils.getClientIp(servletRequest));
		authRequest.setUserAgent(WebUtils.getUserAgent(servletRequest));

		if(StringUtils.isBlank(authRequest.getUsername())) {
			this.getListenerManager().onLoginFailure(authRequest, this.getProperties().getUsernameBlankTips());
			throw new IllegalArgumentException(this.getProperties().getUsernameBlankTips());
		}

		if(StringUtils.isBlank(authRequest.getPassword())) {
			this.getListenerManager().onLoginFailure(authRequest, this.getProperties().getPasswordBlankTips());
			throw new IllegalArgumentException(this.getProperties().getPasswordBlankTips());
		}

		if(this.getProperties().isCaptchaEnabled()){
			if(StringUtils.isBlank(authRequest.getCaptchaKey())) {
				this.getListenerManager().onLoginFailure(authRequest, this.getProperties().getCaptchaKeyBlankTips());
				throw new IllegalArgumentException(this.getProperties().getCaptchaKeyBlankTips());
			}
			if(StringUtils.isBlank(authRequest.getCaptcha())) {
				this.getListenerManager().onLoginFailure(authRequest, this.getProperties().getCaptchaBlankTips());
				throw new IllegalArgumentException(this.getProperties().getCaptchaBlankTips());
			}
			if(!this.getCaptchaProvider().validateCaptcha(authRequest.getCaptchaKey(), authRequest.getCaptcha())) {
				this.getListenerManager().onLoginFailure(authRequest, this.getProperties().getCaptchaErrorTips());
				throw new UnauthorizedException(this.getProperties().getCaptchaErrorTips());
			}
		}

		AuthenticationInfo info = this.getAuthenticator().login(authRequest);
		AuthResponse authResponse = AuthResponse.succeed();
		authResponse.setMessage(this.getProperties().getLoginSucceedTips());
		authResponse.setAccessToken(info.getToken().getAccessToken());
		this.getListenerManager().onLoginSuccess(authRequest);
		writeAuthResponse(servletResponse, HttpStatus.OK.value(), authResponse);
		return false;
	}

}