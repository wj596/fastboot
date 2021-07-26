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
package org.jsets.fastboot.security.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

/**
 * 属性
 *
 * @Author wangjie
 * @date 2021.07.04 23:26
 */
@Data
@ConfigurationProperties(prefix = "fastboot.security", ignoreUnknownFields = true, ignoreInvalidFields = true)
public class SecurityProperties {
	
	public static final char COMMA_SEPARATOR = ','; //逗号分隔符
	public static final String URL_FILTER_SEPARATOR = "-->"; //URL过滤器分隔符
	public static final String METHOD_PATH_SEPARATOR = ":"; //HTTP方法和路径分割符
	public static final char SECTION_PREFIX = '[';//过滤器参数开始标识
	public static final char SECTION_SUFFIX = ']';//过滤器参数结束标识
	public static final String URL_PREFIX = "/";//URL开始标识
	
	// 永不过期
	public static final Integer EXPIRE_NEVER = -1;
	// 默认会话有效期：30分钟=1800秒(s)
	public static final Long SESSION_TIMEOUT = 1800L;
	// 会话缓存名称
	public static final String SESSION_CACHE_NAME = "fastboot-session";
	// 验证码缓存名称
	public static final String CAPTCHA_CACHE_NAME = "fastboot-captcha";
	// 密码重试记录缓存名称
	public static final String PASSWD_RETRY_RECORD_CACHE_NAME = "fastboot-passwd-retry";
	// 密码重试记录缓存时间，默认1天 86400 秒
	public static final long PASSWD_RETRY_RECORD_CACHE_TIMEOUT = 86400l;
	// 验证码缓存时间，默认60 秒
	public static final long CAPTCHA_CACHE_TIMEOUT = 60l;

	// 提示信息
	public static final String TIPS_USERNAME_NOT_BLANK = "账号不能为空";
	public static final String TIPS_PASSWORD_NOT_BLANK = "密码不能为空";
	public static final String TIPS_CAPTCHA_KEY_NOT_BLANK = "验证码Key不能为空";
	public static final String TIPS_CAPTCHA_NOT_BLANK = "验证码不能为空";
	public static final String TIPS_USERNAME_OR_PASSWORD_ERROR = "账号或密码错误";
	public static final String TIPS_TOKEN_NOT_BLANK = "令牌不能为空";
	public static final String TIPS_TOKEN_SIGNATURE_ERROR = "令牌验签失败";
	public static final String TIPS_TOKEN_EXPIRED = "令牌过期";
	public static final String TIPS_TOKEN_INVALID = "令牌无效";
	public static final String TIPS_CAPTCHA_ERROR = "验证码错误";
	public static final String TIPS_LOGIN_SUCCEED = "登陆成功";
	public static final String TIPS_LOGIN_ERROR = "登陆失败";
	public static final String TIPS_LOGOUT_SUCCEED = "登出成功";
	public static final String TIPS_FORBIDDEN = "权限不足";
	public static final String TIPS_UNAUTHORIZED = "未认证";

	// 无需认证和授权即可访问的路径
	public static final List<String> IGNORED_AUTH_PATHS = Arrays.asList(
					"/**/favicon.ico"
					,"/css/**"
					,"/js/**"
					,"/images/**"
					,"/webjars/**"
					,"/swagger-ui/**"
					,"/swagger-resources/**"
					,"/v3/api-docs/**");

	private String usernameBlankTips = TIPS_USERNAME_NOT_BLANK;// 账号为空提示
	private String passwordBlankTips = TIPS_PASSWORD_NOT_BLANK;// 密码为空提示
	private String captchaKeyBlankTips = TIPS_CAPTCHA_KEY_NOT_BLANK;// 验证码KEY为空提示
	private String captchaBlankTips = TIPS_CAPTCHA_NOT_BLANK;// 验证码为空提示
	private String usernameOrPasswordErrorTips = TIPS_USERNAME_OR_PASSWORD_ERROR;// 账号或密码错误提示
	private String tokenBlankTips = TIPS_TOKEN_NOT_BLANK;// 令牌为空提示
	private String tokenSignatureErrorTips = TIPS_TOKEN_SIGNATURE_ERROR;// 令牌验签失败提示
	private String tokenExpiredTips = TIPS_TOKEN_EXPIRED;// 令牌失效提示
	private String tokenInvalidTips = TIPS_TOKEN_INVALID;// 令牌无效提示
	private String captchaErrorTips = TIPS_TOKEN_INVALID;// 验证码错误提示
	private String loginSucceedTips = TIPS_LOGIN_SUCCEED;// 登陆成功提示
	private String loginErrorTips = TIPS_LOGIN_ERROR;// 登陆失败提示
	private String logoutSucceedTips = TIPS_LOGOUT_SUCCEED;// 登出成功提示
	private String forbiddenTips = TIPS_FORBIDDEN;// 权限不足提示
	private String unauthorizedTips = TIPS_UNAUTHORIZED;// 未认证提示
	

	
	private boolean corsEnabled;// 是否允许跨域
	private boolean captchaEnabled;// 是否启用验证码
	private Long sessionTimeout = SESSION_TIMEOUT;// 会话有效期,单位秒
	private String tokenSignKey;// 令牌签名秘钥

	private String sessionCacheName = SESSION_CACHE_NAME;// 会话缓存名称
	private String passwdRetryRecordCacheName = PASSWD_RETRY_RECORD_CACHE_NAME;// 密码重试记录缓存名称
	private Long passwdRetryRecordCacheTimeout = PASSWD_RETRY_RECORD_CACHE_TIMEOUT;// 密码重试记录缓存超时间,单位秒

	private String captchaCacheName = CAPTCHA_CACHE_NAME;// 验证码缓存名称
	private Long captchaCacheTimeout = CAPTCHA_CACHE_TIMEOUT;// 验证码缓存名称

	private Integer passwordRetryMaximum;// 密码最大重试次数
	private List<String> authRules = new LinkedList<String>();// 过滤规则

}