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

import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.common.util.JsonUtils;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.SecurityManager;
import org.jsets.fastboot.security.auth.AuthResponse;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.util.WebUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 内部过滤器的抽象父类
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.09 16:20
 * @since 0.1
 */
@Slf4j
public abstract class AbstractInnerFilter implements InnerFilter {

    protected static final String HEADER_KEY_AUTHORIZATION = "Authorization";
    private SecurityManager securityManager;
    private String loginPath;
    
    protected boolean isLoginRequest(HttpServletRequest request) {
        if(!WebUtils.isPostMethod(request)){
            return false;
        }
        if(StringUtils.isBlank(this.getLoginPath())) {
            return false;
        }
        return WebUtils.pathMatch(this.getLoginPath(), request.getServletPath());
    }

    protected String checkAndGetAuthorization(HttpServletRequest request) throws UnauthorizedException {
        String authorization = request.getHeader(HEADER_KEY_AUTHORIZATION);
        if(StringUtils.isBlank(authorization)) {
        	log.warn(this.securityManager.getProperties().getTokenBlankTips());
            throw new UnauthorizedException(this.getSecurityManager().getProperties().getTokenBlankTips());
        }
        return authorization;
    }

    protected void writeAuthResponse(HttpServletResponse httpResponse, int httpStatus, AuthResponse authResponse) {
    	WebUtils.writeResponse(httpResponse, httpStatus, JsonUtils.toJsonString(authResponse));
    }

    protected SecurityManager getSecurityManager() {
        return securityManager;
    }

    protected void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    protected String getLoginPath() {
        return loginPath;
    }

    protected void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }
}
