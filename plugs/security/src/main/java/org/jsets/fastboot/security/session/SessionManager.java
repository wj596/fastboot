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
package org.jsets.fastboot.security.session;

import org.jsets.fastboot.security.auth.AuthRequest;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import java.util.Optional;
import java.util.Set;

/**
 * Session管理器
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.09 14:23
 * @since 0.1
 */
public interface SessionManager {

    Session create(AuthRequest authRequest);

    boolean isExists(String id);
    
    Optional<Session> get(String id);
    
    Set<Session> getSessions();

    void delete(String sessionId);
}
