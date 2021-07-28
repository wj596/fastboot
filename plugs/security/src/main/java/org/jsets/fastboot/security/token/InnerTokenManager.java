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
package org.jsets.fastboot.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.security.auth.AuthenticationInfo;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.session.Session;
import org.jsets.fastboot.security.util.CryptoUtils;
import java.util.Objects;

/**
 * 令牌管理器
 *
 * @Author wangjie
 * @date 2021.06.08 23:07
 */
@Slf4j
public class InnerTokenManager implements TokenManager {

	private SecurityProperties properties;

    public void initialize(SecurityProperties properties){
		this.properties = properties;
    }

	@Override
	public InnerToken issue(Session session) {
    	String sessionId = session.getId();
		String accessToken = CryptoUtils.issueJWT(sessionId, sessionId, this.properties.getTokenSignKey());
		InnerToken token = new InnerToken();
		token.setAccessToken(accessToken);
		return token;
	}

	@Override
	public AuthenticationInfo validate(String accessToken) throws UnauthorizedException {
		Claims claims = null;
		try{
			claims = CryptoUtils.parseJWT(accessToken, this.properties.getTokenSignKey());
		} catch(SignatureException e){
			log.error(e.getMessage(), e);
			throw new UnauthorizedException(this.properties.getTokenInvalidTips());
		} catch(ExpiredJwtException e){
			log.error(e.getMessage(), e);
			throw new UnauthorizedException(this.properties.getTokenExpiredTips());
		} catch(Exception e){
			log.error(e.getMessage(), e);
			throw new UnauthorizedException(this.properties.getTokenInvalidTips());
		}

		if(Objects.isNull(claims)){
			log.warn(this.properties.getTokenInvalidTips());
			throw new UnauthorizedException(this.properties.getTokenInvalidTips());
		}

		AuthenticationInfo info = new AuthenticationInfo();
		info.setSessionId(claims.getId());
		info.setUsername(claims.getSubject());
		return info;
	}

}