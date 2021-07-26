package org.jsets.fastboot.frame.model;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应基类
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
@Data
@ApiModel(description = "响应")
public abstract class BaseResp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 操作成功
	public static final boolean SUCCEED = true;
	// 操作失败
	public static final boolean FAILED = false;

	// 身份验证成功
	public static final String MSG_AUTH_SUCCEED = "auth:succeed";
	// 身份验证失败
	public static final String MSG_AUTH_LOGIN_ERROR = "auth:failed";
	// 需要身份验证
	public static final String MSG_AUTH_UNAUTHORIZED = "auth:unauthorized";
	// 无权限访问
	public static final String MSG_AUTH_FORBIDDEN = "auth:forbidden";
	// 未知错误
	public static final String MSG_INTERNAL_UNKNOWN_ERROR = "internal:unknown_error";
	
	@ApiModelProperty(value="是否成功")
	private boolean succeed;
	@ApiModelProperty(value="业务状态")
	private Integer status;
	@ApiModelProperty(value="消息")
	private String message;
	
	public void setStatus(HttpStatus status) {
		this.status = status.value();
	}
	
}
