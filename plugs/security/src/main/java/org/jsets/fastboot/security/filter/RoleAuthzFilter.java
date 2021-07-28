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

import org.jsets.fastboot.security.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * TODO
 *
 * @Author wangjie
 * @date 2021.07.05 14:10
 */
@Slf4j
public class RoleAuthzFilter extends AbstractInnerFilter {

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Set<String> props) throws Exception {
		if (this.isLoginRequest(request)) {
			return true;
		}
		
		String token = this.checkAndGetAuthorization(request);
		this.getAuthenticator().authenticate(token);
        if(this.getAuthorizer().hasAnyRole(props)) {
			log.info("访问路径：{}，需要角色：{}，允许访问", request.getServletPath(), props);
			return true;
		}
		
		log.info("访问路径：{}，需要角色：{}，拒绝访问", request.getServletPath(), props);
        throw new ForbiddenException(this.getProperties().getForbiddenTips());
    }

    @Override
    public boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return true;
    }
}
