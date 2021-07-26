package org.jsets.fastboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CommonStatus {
	
	OK(0,"正常"), DISABLE(1,"停用"), DELETED(9,"删除");
	
    @EnumValue
	private final Integer value;
    @JsonValue
    private final String title;

    CommonStatus(Integer value,String title) {
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
