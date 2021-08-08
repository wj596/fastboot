package org.jsets.fastboot.frame.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 查询条件
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
public class QueryCondition {

	private Integer pageNum; // 当前页码
	private Integer pageSize; // 每页条数
	private Map<String, Object> ps = new HashMap<String, Object>(); // 查询参数

	/**
	 * 构建查询条件
	 * 
	 * @return QueryCondition
	 */
	public static QueryCondition build() {
		return new QueryCondition();
	}

	private QueryCondition oneself() {
		return this;
	}

	/**
	 * 获取当前页码
	 * 
	 * @return Integer
	 */
	public Integer getPageNum() {
		return pageNum;
	}

	/**
	 * 设置当前页码
	 * 
	 * @return QueryCondition
	 */
	public QueryCondition setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		return oneself();
	}

	/**
	 * 获取每页条数
	 * 
	 * @return Integer
	 */
	public Integer getPageSize() {
		return pageSize;

	}

	/**
	 * 设置每页条数
	 * 
	 * @return QueryCondition
	 */
	public QueryCondition setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return oneself();
	}

	/**
	 * 添加参数
	 * 
	 * @return QueryCondition
	 */
	public QueryCondition add(String key, Object value) {
		this.ps.put(key, value);
		return oneself();
	}

	/**
	 * 批量添加参数
	 * 
	 * @return QueryCondition
	 */
	public QueryCondition addAll(Map<String, ?> map) {
		this.ps.putAll(map);
		return oneself();
	}

	/**
	 * 获取参数
	 * 
	 * @return Object
	 */
	public Object getObject(String key) {
		return this.ps.get(key);
	}

	/**
	 * 是否分页
	 * 
	 * @return boolean
	 */
	public boolean pageable() {
		if (Objects.nonNull(pageNum) && Objects.nonNull(pageSize)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取参数
	 * 
	 * @return <T> T
	 */
	public <T> T get(String key) {
		Object v = this.ps.get(key);
		if (Objects.isNull(v)) {
			return null;
		}
		return (T) v;
	}

	/**
	 * 获取String类型参数
	 * 
	 * @return String
	 */
	public String getString(String key) {
		Object v = this.ps.get(key);
		if (Objects.isNull(v)) {
			return null;
		}

		if (v instanceof String) {
			return (String) v;
		}

		return String.valueOf(v);
	}

	/**
	 * 获取Integer类型参数
	 * 
	 * @return Integer
	 */
	public Integer getInteger(String key) {
		Object v = this.ps.get(key);
		if (Objects.isNull(v)) {
			return null;
		}

		if (v instanceof Integer) {
			return (Integer) v;
		}

		return Integer.valueOf(String.valueOf(v));
	}

	/**
	 * 获取Long类型参数
	 * 
	 * @return Long
	 */
	public Long getLong(String key) {
		Object v = this.ps.get(key);
		if (Objects.isNull(v)) {
			return null;
		}

		if (v instanceof Long) {
			return (Long) v;
		}

		return Long.valueOf(String.valueOf(v));
	}

}