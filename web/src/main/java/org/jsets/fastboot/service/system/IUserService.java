package org.jsets.fastboot.service.system;

import org.jsets.fastboot.model.entity.system.User;
import org.jsets.fastboot.security.IAccountProvider;

import java.util.Optional;

import org.jsets.fastboot.frame.service.IBaseService;

public interface IUserService extends IBaseService<User>,IAccountProvider {
	Optional<User> findByAccount(String username);
}
