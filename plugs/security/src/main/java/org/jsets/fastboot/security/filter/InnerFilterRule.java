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
package org.jsets.fastboot.security.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import org.jsets.fastboot.common.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 
 * 过滤规则
 * 
 * @author wangjie
 *
 */
@Data
public class InnerFilterRule {
	
	private String accessPath;
	private String httpMethod;
	private boolean ignore;
	private List<FilterProps> filters = Lists.newLinkedList();

	@Data
	protected static class FilterProps {
		private String name;// 过滤器名称
		private Set<String> props = Sets.newHashSet();//属性

		@Override
		public String toString() {
			return this.getName() + "[" + StringUtils.join(this.getProps()) + "]";
		}
	}
}