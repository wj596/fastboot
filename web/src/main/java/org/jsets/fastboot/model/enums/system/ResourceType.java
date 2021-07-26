package org.jsets.fastboot.model.enums.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceType {

	CATALOG(1,"目录"), MENU(2,"菜单"), FUNC(3,"功能");
	
    @EnumValue
	private final Integer value;
    @JsonValue
    private final String title;

    ResourceType(Integer value,String title) {
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
