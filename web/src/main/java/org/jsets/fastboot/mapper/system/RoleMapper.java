package org.jsets.fastboot.mapper.system;

import org.apache.ibatis.annotations.Mapper;
import org.jsets.fastboot.model.entity.system.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 
 * 角色 Mapper
 * 
 * @author wangjie
 *
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}