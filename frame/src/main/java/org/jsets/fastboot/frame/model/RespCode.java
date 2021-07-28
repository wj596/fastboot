package org.jsets.fastboot.frame.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RespCode {
	
	SUCCEED(0), FAILED(1), TIMEOUT(9);
	
	@JsonValue
	private final Integer value;

	RespCode(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}

}
