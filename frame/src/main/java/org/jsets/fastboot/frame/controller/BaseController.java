package org.jsets.fastboot.frame.controller;

import java.util.List;
import java.util.Objects;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.frame.model.DataResp;
import org.jsets.fastboot.frame.model.ListResp;
import org.jsets.fastboot.frame.model.Respond;
import org.springframework.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Controller基类
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
public abstract class BaseController {

	protected static final String RESP_INSERT_SUCCEED = "新增成功";
	protected static final String RESP_UPDATE_SUCCEED = "修改成功";
	protected static final String RESP_DELETE_SUCCEED = "删除成功";

	/**
	 * 操作成功
	 */
	protected Respond succeed() {
		return Respond.ok();
	}

	/**
	 * 操作成功
	 * 
	 * @param message 消息
	 */
	protected Respond succeed(String message) {
		return Respond.ok(message);
	}

	/**
	 * 操作成功
	 * 
	 * @param status  状态
	 * @param message 消息
	 */
	protected Respond succeed(HttpStatus status, String message) {
		return Respond.ok(status, message);
	}

	/**
	 * 操作失败
	 */
	protected <T> Respond failed() {
		return Respond.fail();
	}

	/**
	 * 操作失败
	 * 
	 * @param message 响应消息
	 */
	protected <T> Respond failed(String message) {
		return Respond.fail(message);
	}
	
	/**
	 * 操作失败
	 * 
	 * @param status  状态
	 * @param message 消息
	 */
	protected <T> Respond failed(HttpStatus status, String message) {
		return Respond.fail(status, message);
	}

	/**
	 * 返回单条数据
	 */
	protected <T> DataResp<T> dataResp(T t) {
		DataResp<T> respond = DataResp.ok();
		respond.setData(t);
		return respond;
	}
	
	/**
	 * 单条数据响应失败
	 */
	protected <T> DataResp<T> dataRespFail() {
		return DataResp.fail();
	}
	
	/**
	 * 单条数据响应失败
	 */
	protected <T> DataResp<T> dataRespFail(String message) {
		return DataResp.fail(message);
	}
	
	
	/**
	 * 返回数据列表
	 */
	protected <T> ListResp<T> listResp(List<T> ls) {
		return ListResp.ok();
	}
	
	
	/**
	 * 数据列表响应失败
	 */
	protected <T> ListResp<T> listRespFail() {
		return ListResp.fail();
	}
	
	/**
	 * 数据列表响应失败
	 */
	protected <T> ListResp<T> listRespFail(String message) {
		return ListResp.fail(message);
	}
	
	
	/**
	 * 返回分页数据
	 */
	protected <T> ListResp<T> pageResp(IPage<T> result) {
		ListResp<T> respond = ListResp.ok();
		respond.setData(result.getRecords());
		respond.setTotal(result.getTotal());
		return respond;
	}
	
	/**
	 * 分页数据响应失败
	 */
	protected <T> ListResp<T> pageResp() {
		return this.listRespFail();
	}
	
	/**
	 * 分页数据响应失败
	 */
	protected <T> ListResp<T> pageResp(String message) {
		return this.listRespFail(message);
	}
	
	/**
	 * 参数断言
	 * @param expression 表达式
	 * @param message 异常消息
	 */
	protected void argThat(final boolean expression, final String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 参数断言
	 * @param expression 表达式
	 * @param formatter 消息格式
	 * @param args 消息参数
	 */
	protected void argumentThat(final boolean expression, final String formatter, final Object... args) {
		if (!expression) {
			throw new IllegalArgumentException(String.format(formatter, args));
		}
	}

	/**
	 * 断言参数不为空
	 * @param argument 参数
	 * @param argumentName 参数名称
	 */
	protected void argNotNull(final Object argument, final String argumentName) {
		if (Objects.isNull(argument)) {
			throw new IllegalArgumentException("参数["+argumentName+"],不能为空");
		}
	}

	/**
	 * 断言参数不为空字符串
	 * @param argument 参数
	 * @param argumentName 参数名称
	 */
	protected void stringArgNotEmpty(final String argument, final String argumentName) {
		if (StringUtils.isEmpty(argument)) {
			throw new IllegalArgumentException("参数["+argumentName+"],不能为空");
		}
	}

	/**
	 * 断言参数不为空白字符串
	 * @param argument 参数
	 * @param argumentName 参数名称
	 */
	protected void stringArgNotBlank(final String argument, final String argumentName) {
		if (StringUtils.isBlank(argument)) {
			throw new IllegalArgumentException("参数["+argumentName+"],不能为空或空白字符串");
		}
	}

}