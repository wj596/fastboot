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
package org.jsets.fastboot.security.util;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 密码学 相关工具
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.08 22:59
 * @since 0.1
 */
public class CryptoUtils {

	public static String issueJWT(String id, String subject, String secretKey) {

		return BaseEncoding.base64().encode(
				Jwts.builder()
				.setId(id)
				.setSubject(subject)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact()
				.getBytes()
				);
	}

	public static Claims parseJWT(String jwt, String secretKey) {
		String content = new String(BaseEncoding.base64().decode(jwt));
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(content).getBody();
	}
	
	public static String hmacSha256(String plan, String secretKey) {
		return Hashing.hmacSha256(secretKey.getBytes()).hashBytes(plan.getBytes()).toString();
	}
	
	public static void main(String[] args) {
		Long curr = System.currentTimeMillis();
		System.out.println(curr);
		String hmac =  hmacSha256(curr+"fastboot", "abcdee78985eve*fdafec");
		System.out.println(hmac);
	}

}