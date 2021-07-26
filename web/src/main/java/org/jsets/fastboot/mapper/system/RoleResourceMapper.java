package org.jsets.fastboot.mapper.system;

import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.jsets.fastboot.model.entity.system.RoleResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource>{
	
	Set<String> selectFuncCodeListByUsername(String username);

}
