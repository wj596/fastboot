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
package org.jsets.fastboot.security.auth;

import lombok.Data;

/**
 * 抽象认证请求
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.06 22:26
 * @since 0.1
 */
@Data
public abstract class AbstractAuthRequest implements AuthRequest{

    private String username;//用户名
    private String userHost;//登陆地址
    private String userAgent;//浏览器
    private String source;//来源，如:PC、APP、微信等
    private boolean rememberMe;//记住我
    
}