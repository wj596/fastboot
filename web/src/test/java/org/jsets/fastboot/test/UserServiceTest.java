package org.jsets.fastboot.test;

import java.util.Date;
import org.jsets.fastboot.model.entity.system.User;
import org.jsets.fastboot.model.enums.system.Sex;
import org.jsets.fastboot.model.enums.system.UserStatus;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.jsets.fastboot.security.model.IAccount;
import org.jsets.fastboot.service.system.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private IUserService userService;

	@Test
	public void case1() throws UnauthorizedException {
//		User u = new User();
//		u.setAccount("admin");
//		u.setName("管理员");
//		u.setSex(Sex.MALE); 
//		u.setOrgId("001");
//		u.setStatus(UserStatus.DISABLE);
//		this.userService.insert(u);
	}
	
	//@Test
	public void case2() throws UnauthorizedException {
		
		//System.out.println("java版本号：" + System.getProperty("java.version")); 
		
		//IAccount u = this.userService.loadAccount("admin");
		//System.out.println(u.getAccount());
		//System.out.println(u.getPassword());
	}
}
