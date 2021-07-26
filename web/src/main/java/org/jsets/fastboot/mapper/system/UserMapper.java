package org.jsets.fastboot.mapper.system;

import org.apache.ibatis.annotations.Mapper;
import org.jsets.fastboot.model.entity.system.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 
 * 用户 Mapper
 * 
 * @author wangjie
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}