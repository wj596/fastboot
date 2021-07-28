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

import org.jsets.fastboot.security.auth.AuthResponse;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class LogoutFilter extends AbstractInnerFilter {

    @Override
    public boolean isAccessAllowed(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Set<String> props) throws Exception {
        return false;
    }

    @Override
    public boolean onAccessDenied(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        String token = this.checkAndGetAuthorization(servletRequest);
        this.getAuthenticator().logout(token);
        AuthResponse authResponse = AuthResponse.succeed(this.getProperties().getLogoutSucceedTips());
        writeAuthResponse(servletResponse, HttpStatus.OK.value(), authResponse);
        return false;
    }
}