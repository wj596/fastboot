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
import org.springframework.http.HttpStatus;
import java.io.Serializable;

/**
 * 认证响应
 *
 * @author wangjie (https://github.com/wj596)
 * @Date 2021.07.06 22:26
 * @since 0.1
 */
@Data
public class AuthResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer status; //业务状态
    private String message;//消息
    private String accessToken;//访问令牌
    private String refreshToken;//刷新令牌

    public static AuthResponse succeed(){
        AuthResponse ar = new AuthResponse();
        ar.status = HttpStatus.OK.value();
        return ar;
    }

    public static AuthResponse succeed(String message){
        AuthResponse ar = new AuthResponse();
        ar.status = HttpStatus.OK.value();
        ar.setMessage(message);
        return ar;
    }

    public static AuthResponse succeed(String message, String accessToken){
        AuthResponse ar = new AuthResponse();
        ar.status = HttpStatus.OK.value();
        ar.setMessage(message);
        ar.setAccessToken(accessToken);
        return ar;
    }

    public static AuthResponse failed(Integer status, String message){
        AuthResponse ar = new AuthResponse();
        ar.status = status;
        ar.setMessage(message);
        return ar;
    }



}