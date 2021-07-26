/*
 * Copyright 2021-2022 the original author(https://github.com/wj596)
 * 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package org.jsets.fastboot.security.exception;


/**
 * 
 * 鉴权异常
 * @author wj596
 *
 */
public class ForbiddenException extends Exception{

	private static final long serialVersionUID = 1L;

	private Integer bizStatus;// 业务状态

	public ForbiddenException(String message) {
		super(message);
	}
	
	public ForbiddenException(String message,Integer bizStatus) {
		super(message);
		this.bizStatus = bizStatus;
	}

	public Integer getBizStatus() {
		return bizStatus;
	}

	public void setBizStatus(Integer bizStatus) {
		this.bizStatus = bizStatus;
	}
	
}