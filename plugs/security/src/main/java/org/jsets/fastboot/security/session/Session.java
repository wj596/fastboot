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
package org.jsets.fastboot.security.session;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.util.Date;

/**
 * 会话
 * @Author wangjie
 * @date 2021.06.08 22:24
 */
@Slf4j
@Data
public class Session implements Serializable {

	private String id;//主键
	private String username;//用户名
	private String authRequestType;//请求类型
	private Date startTimestamp;//开始时间
	private Long lastAccessTime;//最后访问时间

	public void touch() {
		log.info("session[{}] touched", this.getId());
		this.lastAccessTime = System.currentTimeMillis();
	}

}