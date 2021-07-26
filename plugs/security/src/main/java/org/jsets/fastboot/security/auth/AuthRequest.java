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

import java.io.Serializable;

/**
 * 认证请求接口
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.06 22:26
 * @since 0.1
 */
public interface AuthRequest extends Serializable {
	
    //username参数的名称
    public static final String PARAM_USERNAME = "username";
    //password参数的名称
    public static final String PARAM_PASSWORD = "password";
    //captchaKey参数的名称
    public static final String PARAM_CAPTCHA_KEY = "captchaKey";
    //captcha参数的名称
    public static final String PARAM_CAPTCHA = "captcha";
    //rememberMe参数的名称
    public static final String PARAM_REMEMBER_ME = "rememberMe";

    /*
     * 获取用户名
     * @return String
     **/
    String getUsername();

    /*
     * 获取客户地址
     * @return String
     **/
    String getUserHost();

    /*
     * 获取客户端信息
     * @return String
     **/
    String getUserAgent();

    /*
     * 获取客户来源
     * @return String
     **/
    String getSource();

    /*
     * 是否记住我
     * @return String
     **/
    boolean isRememberMe();

    /*
     * 获取请求类型名称
     * @return String
     **/
    String getTypeName();
}