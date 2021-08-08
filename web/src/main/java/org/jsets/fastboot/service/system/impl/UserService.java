package org.jsets.fastboot.service.system.impl;

import org.jsets.fastboot.model.entity.system.User;
import org.jsets.fastboot.model.enums.system.UserStatus;
import org.jsets.fastboot.security.SecurityUtils;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.model.IAccount;
import java.util.Optional;
import java.util.Set;
import org.jsets.fastboot.config.Constants;
import org.jsets.fastboot.frame.service.impl.BaseService;
import org.jsets.fastboot.mapper.system.RoleResourceMapper;
import org.jsets.fastboot.mapper.system.UserMapper;
import org.jsets.fastboot.mapper.system.UserRoleMapper;
import org.jsets.fastboot.service.system.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, UserMapper> implements IUserService {

	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	private RoleResourceMapper roleResourceMapper;
	
    @Value("${fastboot.init-password:"+Constants.INITIAL_PASSWORD+"}")
    protected String initialPassword;// 用户初始密码
	
	@Override
	public boolean insert(User entity) {
		//设置初始密码
		entity.setPassword(SecurityUtils.encryptPassword(initialPassword));
		return super.insert(entity);
	}
	
	@Override
	public IAccount loadAccount(String username) throws UnauthorizedException {
		Optional<User> opt = this.findOne(this.getLambdaWrapper().eq(User::getAccount, username));
		if(!opt.isPresent()) {
			throw new UnauthorizedException("账号或密码错误");
		}
		if(opt.get().getStatus()==UserStatus.LOCKED) {
			throw new UnauthorizedException("账号被锁定，请联系管理员");
		}
		return opt.get();
	}

	@Override
	public Set<String> loadRoles(String username) {
		return this.userRoleMapper.selectRoleCodeListByUsername(username);
	}

	@Override
	public Set<String> loadPermissions(String username) {
		return this.roleResourceMapper.selectFuncCodeListByUsername(username);
	}

	@Override
	public Optional<User> findByAccount(String username) {
		return this.findOne(this.getLambdaWrapper().eq(User::getAccount, username));
	}

}