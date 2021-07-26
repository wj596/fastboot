package org.jsets.fastboot.mapper.system;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.jsets.fastboot.model.entity.system.UserRole;
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
public interface UserRoleMapper extends BaseMapper<UserRole> {
	List<Role> selectRoleListByUserId(Long userId);
	Set<String> selectRoleCodeListByUsername(String username);
}