package org.jsets.fastboot.model.enums.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * 性别
 * 
 * @author wj596
 *
 */
public enum Sex {

	MALE(0,"男"), FEMALE(1,"女"), UNKOWN(2,"未知");
	
    @EnumValue
	private final Integer value;
    @JsonValue
    private final String title;

	Sex(Integer value,String title) {
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