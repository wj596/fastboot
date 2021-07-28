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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;
import org.jsets.fastboot.common.util.JsonUtils;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.SecurityUtils;
import org.jsets.fastboot.security.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {
	
	private static final int ACCESS_CONTROL_MAX_AGE = 20*24*60*60;
	private static final String CAPTCHA_KEY = "captchaKey";
	private static final String CAPTCHA_IMAGE_BASE64 = "captchaImageBase64";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("CaptchaFilter:{}", request.getServletPath());
		String captchaKey = StringUtils.getUUID();
		BufferedImage image = SecurityUtils.generateCaptcha(captchaKey);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", stream);
		String captchaImageBase64 = "data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(stream.toByteArray());
		stream.close();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Map<String, String> map = Maps.newHashMap();
		map.put(CAPTCHA_KEY, captchaKey);
		map.put(CAPTCHA_IMAGE_BASE64, captchaImageBase64);
		
        // 添加参数，允许任意domain访问
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Headers", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
		httpResponse.setHeader("Access-Control-Max-Age", ACCESS_CONTROL_MAX_AGE+"");
		WebUtils.writeResponse(httpResponse, HttpStatus.OK.value(), JsonUtils.toJsonString(map));
	}
	
}