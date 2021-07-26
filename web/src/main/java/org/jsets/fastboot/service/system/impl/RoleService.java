package org.jsets.fastboot.service.system.impl;

import org.jsets.fastboot.model.entity.system.Role;
import org.jsets.fastboot.frame.service.impl.BaseService;
import org.jsets.fastboot.mapper.system.RoleMapper;
import org.jsets.fastboot.service.system.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, RoleMapper> implements IRoleService {

}