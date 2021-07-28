package org.jsets.fastboot.frame.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 
 * 列表响应
 * <br>
 * 使用泛型，对swagger友好
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ListResp<T> extends BaseResp{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="列表")
	private List<T> result;
	@ApiModelProperty(value="列表条数")
	private Long total;//总数据条数
	
	public static <T> ListResp<T> ok() {
		ListResp<T> baseReturn = new ListResp<T>();
		baseReturn.setCode(RespCode.SUCCEED);
		return baseReturn;
	}
	
	public static <T> ListResp<T> fail() {
		ListResp<T> baseReturn = new ListResp<T>();
		baseReturn.setCode(RespCode.FAILED);
		return baseReturn;
	}
	
	public static <T> ListResp<T> fail(String message) {
		ListResp<T> baseReturn = new ListResp<T>();
		baseReturn.setCode(RespCode.FAILED);
		baseReturn.setMessage(message);
		return baseReturn;
	}
	
}