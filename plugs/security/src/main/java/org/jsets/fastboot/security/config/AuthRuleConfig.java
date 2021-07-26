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
package org.jsets.fastboot.security.config;

import java.util.Arrays;
import java.util.List;
import lombok.ToString;
import org.jsets.fastboot.common.util.CollectionUtils;
import org.jsets.fastboot.common.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.Data;

/**
 * 鉴权规则配置
 *
 * @Author wangjie
 * @date 2021.07.04 23:26
 */
@Data
@ToString
public class AuthRuleConfig {

	private static final List<String> SUPPORTED_HTTPMETHODS = Arrays.asList("get", "post","put","delete");
	
	private String accessPath;
	private List<String> httpMethods;
	private final List<AuthRuleFilterConfig> filters = Lists.newLinkedList();// 属性

	/**
	 * 解析一行配置配置
	 * @param line 配置
	 */
	public void parseFromLine(String line) {
		if (StringUtils.isEmpty(line)) {
			throw new IllegalStateException("鉴权规则不能为空");
		}
		String[] temp = line.split(SecurityProperties.URL_FILTER_SEPARATOR);
		if (temp.length != 2) {
			throw new IllegalStateException("鉴权规则[" + line + "]格式错误，正确格式：url-->filter");
		}
		String url = temp[0];
		String filterConfig = temp[1];
		if (StringUtils.isBlank(url) || StringUtils.isBlank(filterConfig)) {
			throw new IllegalStateException("鉴权规则[" + line + "]格式错误，url和filter均不能为空");
		}
		url = url.trim();
		filterConfig = filterConfig.trim();
		this.parseUrl(line, url);
		List<String> filterConfigs = this.parseFilters(filterConfig);
		for (String item : filterConfigs) {
			item = item.trim();
			AuthRuleFilterConfig arfc = new AuthRuleFilterConfig();
			if (item.contains(String.valueOf(SecurityProperties.SECTION_PREFIX)) && item.endsWith(String.valueOf(SecurityProperties.SECTION_SUFFIX))) {
				int sectionStart = item.indexOf(String.valueOf(SecurityProperties.SECTION_PREFIX));
				String name = item.substring(0, sectionStart);
				if (StringUtils.isEmpty(name)) {
					throw new IllegalStateException("鉴权规则[" + line + "]格式错误，过滤器名称不能为空");
				}
				boolean repeated = this.filters.stream().map(t->t.getName()).anyMatch(t->t.equalsIgnoreCase(name));
				if (repeated) {
					throw new IllegalStateException("鉴权规则[" + line + "]格式错误，存在名称为：" +name+ "的过滤器");
				}
				String propsConfig = item.substring(sectionStart+1, item.length()-1);
				List<String> props = StringUtils.split(propsConfig, String.valueOf(SecurityProperties.COMMA_SEPARATOR));
				if (CollectionUtils.isEmpty(props)) {
					throw new IllegalStateException("鉴权规则[" + line + "]格式错误，filter参数不能为空");
				}
				arfc.setName(name);
				arfc.setProps(props);
			} else {
				arfc.setName(item);
			}
			this.filters.add(arfc);
		}
	}

	private void parseUrl(String line, String url) {
		if(url.contains(SecurityProperties.METHOD_PATH_SEPARATOR)) {
			String[] temp = url.split(SecurityProperties.METHOD_PATH_SEPARATOR);
			String methodConfg = temp[0];
			String path = temp[1];
			if (StringUtils.isBlank(methodConfg)) {
				throw new IllegalStateException("鉴权规则[" + line + "]格式错误，http method参数不能为空");
			}
			if (StringUtils.isBlank(path)) {
				throw new IllegalStateException("鉴权规则[" + line + "]格式错误，路径参数不能为空");
			}
			List<String> methods = StringUtils.split(methodConfg.toLowerCase(), String.valueOf(SecurityProperties.COMMA_SEPARATOR));
			methods.forEach(t->{
				if(!SUPPORTED_HTTPMETHODS.contains(t)) {
					throw new IllegalStateException("鉴权规则[" + line + "]格式错误，不支持的HTTP方法["+ t +"]");
				}
			});
			this.setHttpMethods(methods);
			this.setAccessPath(path);
		}else {
			this.setAccessPath(url);
		}
	}
	
	
	/**
	 * 解析过滤器
	 */
	private List<String> parseFilters(String filterConfig) {
		List<String> ls = Lists.newArrayList();
		boolean occlusion = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = filterConfig.length(); i < n; i++) {
			char c = filterConfig.charAt(i);
			if (occlusion && c == SecurityProperties.COMMA_SEPARATOR) {
				ls.add(sb.toString());
				sb = new StringBuilder();
			} else {
				if (c == SecurityProperties.SECTION_PREFIX) {
					occlusion = false;
				} else if (c == SecurityProperties.SECTION_SUFFIX) {
					occlusion = true;
				}
				sb.append(c);
			}
			if (sb.length() > 0 && i == (filterConfig.length() - 1)) {
				ls.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		return ls;
	}
}
