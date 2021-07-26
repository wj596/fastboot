package org.jsets.fastboot.model.enums.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
	
	OK(0,"正常"), DISABLE(1,"停用"), LOCKED(2,"锁定"), DELETED(9,"删除");
	
    @EnumValue
	private final Integer value;
    @JsonValue
    private final String title;

    UserStatus(Integer value,String title) {
		this.value = value;
		this.title = title;
	}

	public Integer getValue() {
		return value;
	}

	public String getTitle() {
		return title;
	}
}
