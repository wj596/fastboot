package org.jsets.fastboot.frame.model;

import org.springframework.http.HttpStatus;

/**
 * 基本响应
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
public class Respond extends BaseResp {

	private static final long serialVersionUID = 1L;

	public static Respond ok() {
		Respond baseReturn = new Respond();
		baseReturn.setCode(RespCode.SUCCEED);
		return baseReturn;
	}

	public static Respond ok(String message) {
		Respond baseReturn = new Respond();
		baseReturn.setCode(RespCode.SUCCEED);
		baseReturn.setMessage(message);
		return baseReturn;
	}

	public static Respond ok(HttpStatus status, String message) {
		Respond baseReturn = new Respond();
		baseReturn.setCode(RespCode.SUCCEED);
		baseReturn.setMessage(message);
		return baseReturn;
	}

	public static Respond fail() {
		Respond baseReturn = new Respond();
		baseReturn.setCode(RespCode.FAILED);
		return baseReturn;
	}

	public static Respond fail(String message) {
		Respond baseReturn = new Respond();
		baseReturn.setCode(RespCode.FAILED);
		baseReturn.setMessage(message);
		return baseReturn;
	}

	public static Respond fail(HttpStatus status, String message) {
		Respond baseReturn = new Respond();
		baseReturn.setCode(RespCode.FAILED);
		baseReturn.setMessage(message);
		return baseReturn;
	}
}