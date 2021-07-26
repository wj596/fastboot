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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsets.fastboot.common.util.IoUtils;
import org.jsets.fastboot.common.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebUtils {
	
    protected static final String HTTP_METHOD_POST = "post";
    protected static final String HEADER_KEY_AUTHORIZATION = "Authorization";
    protected static final String HEADER_KEY_USER_AGENT = "User-Agent";
    protected static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static void writeResponse(HttpServletResponse httpResponse, int httpStatus, String content) {
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(httpStatus);
        PrintWriter out = null;
        try {
            out = httpResponse.getWriter();
            out.write(content);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtils.closeQuietly(out);
        }
    }
    
    public static boolean isJsonRequest(HttpServletRequest httpRequest) {
        String contentType = httpRequest.getHeader(HttpHeaders.CONTENT_TYPE);
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType)) {
            return true;
        }
        return false;
    }

    public static String readJsonBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException e) {
            throw new RuntimeException("读取json body失败", e);
        } finally {
            IoUtils.closeQuietly(bufferedReader);
        }
        return stringBuilder.toString();
    }
    
    public static boolean pathMatch(String pattern, String path) {
        if(StringUtils.isBlank(pattern)){
            return false;
        }
        if(StringUtils.isBlank(path)){
            return false;
        }
        return PATH_MATCHER.match(pattern, path);
    }

    public static boolean isPostMethod(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HTTP_METHOD_POST);
    }
    
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HEADER_KEY_USER_AGENT);
    }

}