package org.jsets.fastboot.frame.service;

import java.io.Serializable;
import java.util.Optional;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * 基础Service接口
 * 
 * @param <T> 实体
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
public interface IBaseService<T> extends IService<T>{
	
	/**
	 * 插入实体
	 * @param entity 实体
	 * @return boolean
	 */
    default boolean insert(T entity) {
        return SqlHelper.retBool(getBaseMapper().insert(entity));
    }
	
	
	/**
	 * 更新实体
	 * @param entity 实体
	 * @return boolean
	 */
    default boolean update(T entity) {
        return SqlHelper.retBool(getBaseMapper().updateById(entity));
    }
	
	/**
	 * 根据主键查找对象
	 * @param id 主键
	 * @return Optional<T>
	 */
	Optional<T> findById(Serializable id);
	
	
	/**
	 * 根据Wrapper查找对象
	 * @param queryWrapper 条件包装
	 * @return Optional<T>
	 */
	Optional<T> findOne(Wrapper<T> queryWrapper);
	
	/**
	 * 获取LambdaQueryWrappe
	 * @return LambdaQueryWrapper<T>
	 */
	LambdaQueryWrapper<T> getLambdaWrapper();
	
	/**
	 * 获取UpdateWrapper
	 * @return UpdateWrapper<T>
	 */
	UpdateWrapper<T> getUpdateWrapper();
	
	/**
	 * 获取QueryWrapper
	 * @return QueryWrapper<T>
	 */
	QueryWrapper<T> getQueryWrapper();
	
}