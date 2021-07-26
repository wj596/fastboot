package org.jsets.fastboot.test;

import org.jsets.fastboot.common.util.JsonUtils;
import org.jsets.fastboot.model.entity.system.User;
import org.jsets.fastboot.model.enums.system.Sex;
import org.jsets.fastboot.model.enums.system.UserStatus;


public class UserTest {

	public static void main(String[] args) {
		User user = new User();
		user.setName("test");
		user.setSex(Sex.MALE);
		user.setStatus(UserStatus.OK);
		String json = JsonUtils.toJsonString(user);
		

		
		System.out.println(json);
		
		User user2 = JsonUtils.parse(json, User.class);
		System.out.println(user2.getSex().getTitle());
		System.out.println(user2.getStatus().getTitle());
	}

}
