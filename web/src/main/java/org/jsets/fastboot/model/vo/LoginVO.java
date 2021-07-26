package org.jsets.fastboot.model.vo;

import lombok.Data;

@Data
public class LoginVO {
	private String username;// 用户名
	private String password;// 密码
	private String captchaKey;
	private String captcha;
}