package org.jsets.fastboot.frame.model;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 数据响应
 * <br>
 * 使用泛型，对swagger友好
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DataResp<T> extends BaseResp{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="数据ss")
	private T data;
	
	public static <T> DataResp<T> ok() {
		DataResp<T> baseReturn = new DataResp<T>();
		baseReturn.setSucceed(SUCCEED);
		baseReturn.setStatus(HttpStatus.OK);
		return baseReturn;
	}
	
	public static <T> DataResp<T> fail() {
		DataResp<T> baseReturn = new DataResp<T>();
		baseReturn.setSucceed(FAILED);
		baseReturn.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return baseReturn;
	}
	
	public static <T> DataResp<T> fail(String message) {
		DataResp<T> baseReturn = new DataResp<T>();
		baseReturn.setSucceed(FAILED);
		baseReturn.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		baseReturn.setMessage(message);
		return baseReturn;
	}
	
}