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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import com.google.common.collect.Sets;
import org.jsets.fastboot.security.Manager;
import org.jsets.fastboot.security.SecurityManager;
import org.jsets.fastboot.security.config.AuthRuleConfig;
import org.jsets.fastboot.security.model.AuthRule;
import org.jsets.fastboot.security.util.WebUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsets.fastboot.common.util.CollectionUtils;
import org.jsets.fastboot.common.util.StringUtils;
import org.jsets.fastboot.security.IAuthRuleProvider;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.filter.InnerFilterRule.FilterProps;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 内部访问控制过滤器管理器
 *
 * @Author wangjie
 * @date 2021.07.04 23:26
 */
@Slf4j
public class InnerFilterManager implements Manager {
	
	private static final String FILTER_ANON = "anon";
	private static final String FILTER_LOGIN = "login";
	private static final String FILTER_LOGOUT = "logout";
	private static final String FILTER_TOKEN = "token";
	private static final String FILTER_ROLES = "roles";
	private static final String FILTER_PERMS = "perms";
	
	private final List<InnerFilterRule> RULES = Lists.newLinkedList();
	private final Map<String, InnerFilter> FILTERS = Maps.newHashMap();
	private final Set<String> filterPlaceholders = Sets.newHashSet();

	private String loginPath;// 登录地址
	private SecurityManager securityManager;
	private SecurityProperties properties;

	@Override
	public void initialize(SecurityManager securityManager) {
		this.securityManager = securityManager;
		this.properties = this.securityManager.getProperties();
		this.buildFilterRules();
		this.buildFilter();
	}

	private void buildFilterRules() {
		
		for (String accessPath : SecurityProperties.IGNORED_AUTH_PATHS) {
			InnerFilterRule filterRule = new InnerFilterRule();
			filterRule.setAccessPath(accessPath);
			filterRule.setIgnore(true);
			InnerFilterRule.FilterProps fp = new InnerFilterRule.FilterProps();
			fp.setName(FILTER_ANON);
			filterRule.getFilters().add(fp);
			this.RULES.add(filterRule);
		}
		
		IAuthRuleProvider authRuleProvider = this.securityManager.getAuthRuleProvider();
		if (Objects.nonNull(authRuleProvider)) {
			List<AuthRule> list = authRuleProvider.loadAuthRuleList();
			for (AuthRule rule : list) {
				if (StringUtils.isEmpty(rule.getPath())) {
					throw new IllegalArgumentException("authRuleProvider提供的鉴权规则[" + rule + "]格式错误，path不能为空");
				}
				if (SecurityProperties.URL_PREFIX.equals(rule.getPath())) {
					throw new IllegalArgumentException("authRuleProvider提供的鉴权规则[" + rule + "]格式错误，path不能为: '/'");
				}
				if (!rule.getPath().startsWith(SecurityProperties.URL_PREFIX)) {
					throw new IllegalArgumentException("authRuleProvider提供的鉴权规则[" + rule + "]格式错误，path以'/'开头");
				}
				if (StringUtils.isBlank(rule.getRole()) && StringUtils.isBlank(rule.getPermission())) {
					throw new IllegalArgumentException("authRuleProvider提供的鉴权规则[" + rule + "]格式错误，role和permission不能同时为空");
				}
				
				InnerFilterRule exist = null;
				for(InnerFilterRule filterRule : this.RULES) {
					if(filterRule.getAccessPath().equals(rule.getPath())
							&&filterRule.getHttpMethod().equalsIgnoreCase(rule.getMethod())) {
						exist = filterRule;
					}
				}
				
				if(Objects.isNull(exist)) {
					exist = this.buildInnerFilterRule(rule);
					this.RULES.add(exist);
				}else {
					this.updateInnerFilterRule(rule, exist);
				}
			}
		}

		if (CollectionUtils.notEmpty(this.properties.getAuthRules())) {
			List<String> lines = this.properties.getAuthRules();
			for (String line : lines) {
				AuthRuleConfig config = new AuthRuleConfig();
				config.parseFromLine(line);
				if(CollectionUtils.notEmpty(config.getHttpMethods())) {
					config.getHttpMethods().forEach(t->{
						InnerFilterRule rule = buildInnerFilterRule(config, t);
						this.RULES.add(rule);
					});
				}else {
					InnerFilterRule rule = buildInnerFilterRule(config, null);
					this.RULES.add(rule);
				}
			}
		}
		
		if (log.isInfoEnabled()) {
			log.info("初始化鉴权规则");
			this.RULES.forEach(v -> {
				log.info("{} {}-->{}", v.getHttpMethod()==null?"":v.getHttpMethod(), v.getAccessPath(), v.getFilters());
			});
		}
	}
	
	private InnerFilterRule buildInnerFilterRule(AuthRule authRule) {
		InnerFilterRule filterRule = new InnerFilterRule();
		filterRule.setIgnore(false);
		filterRule.setAccessPath(authRule.getPath());
		filterRule.setHttpMethod(authRule.getMethod());
		
		if (StringUtils.notEmpty(authRule.getRole())) {
			InnerFilterRule.FilterProps fp = new InnerFilterRule.FilterProps();
			fp.setName(FILTER_ROLES);
			fp.getProps().add(authRule.getRole());
			filterRule.getFilters().add(fp);
			this.filterPlaceholders.add(FILTER_ROLES);
		}
		if (StringUtils.notEmpty(authRule.getPermission())) {
			InnerFilterRule.FilterProps fp = new InnerFilterRule.FilterProps();
			fp.setName(FILTER_PERMS);
			fp.getProps().add(authRule.getPermission());
			filterRule.getFilters().add(fp);
			this.filterPlaceholders.add(FILTER_PERMS);
		}
		return filterRule;
	}
	
	private void updateInnerFilterRule(AuthRule authRule, InnerFilterRule filterRule) {
		if (StringUtils.notEmpty(authRule.getRole())) {
			for(FilterProps filterProp : filterRule.getFilters()) {
				if(FILTER_ROLES.equals(filterProp.getName())) {
					filterProp.getProps().add(authRule.getRole());
				}
			}
		}
		if (StringUtils.notEmpty(authRule.getPermission())) {
			for(FilterProps filterProp : filterRule.getFilters()) {
				if(FILTER_PERMS.equals(filterProp.getName())) {
					filterProp.getProps().add(authRule.getPermission());
				}
			}
		}
	}
	
	private InnerFilterRule buildInnerFilterRule(AuthRuleConfig config, String httpMethod) {
		InnerFilterRule filterRule = new InnerFilterRule();
		if(!StringUtils.isBlank(httpMethod)) {
			filterRule.setHttpMethod(httpMethod);
		}
		filterRule.setAccessPath(config.getAccessPath());
		config.getFilters().forEach(t -> {
			InnerFilterRule.FilterProps fp = new InnerFilterRule.FilterProps();
			fp.setName(t.getName());
			if(CollectionUtils.notEmpty(t.getProps())) {
				fp.getProps().addAll(t.getProps());
			}
			filterRule.getFilters().add(fp);
			if (FILTER_ANON.equals(t.getName())) {
				filterRule.setIgnore(true);
			} else {
				this.filterPlaceholders.add(t.getName());
			}
			if(FILTER_LOGIN.equals(t.getName())){
				this.loginPath = config.getAccessPath();
			}
		});
		return filterRule;
	}

	private void buildFilter() {
		this.filterPlaceholders.forEach(name -> {
			switch (name) {
				case FILTER_ROLES:
					RoleAuthzFilter roleAuthzFilter = new RoleAuthzFilter();
					FILTERS.put(FILTER_ROLES, roleAuthzFilter);
					break;
				case FILTER_PERMS:
					PermAuthzFilter permAuthzFilter = new PermAuthzFilter();
					FILTERS.put(FILTER_PERMS, permAuthzFilter);
					break;
				case FILTER_LOGIN:
					LoginFilter loginFilter = new LoginFilter();
					FILTERS.put(FILTER_LOGIN, loginFilter);
					break;
				case FILTER_LOGOUT:
					LogoutFilter logoutFilter = new LogoutFilter();
					FILTERS.put(FILTER_LOGOUT, logoutFilter);
					break;
				case FILTER_TOKEN:
					TokenFilter tokenFilter = new TokenFilter();
					FILTERS.put(FILTER_TOKEN, tokenFilter);
					break;
			}
		});

		if (CollectionUtils.notEmpty(this.securityManager.getCustomFilters())) {
			this.securityManager.getCustomFilters().forEach((name, filter) -> {
				FILTERS.put(name, filter);
			});
		}

		FILTERS.forEach((name, filter)->{
			if(AbstractInnerFilter.class.isAssignableFrom(filter.getClass()) ){
				AbstractInnerFilter abstractInnerFilter = ((AbstractInnerFilter)filter);
				abstractInnerFilter.setSecurityManager(this.securityManager);
				abstractInnerFilter.setLoginPath(this.loginPath);
			}
			log.info("初始化过滤器，名称：{}， 类：{}", name, filter.getClass().getName());
		});
	}

	public boolean doInnerFilterChain(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		String servletPath = httpRequest.getServletPath();
		String httpMethod = httpRequest.getMethod();
		InnerFilterRule matched = null;
		InnerFilterRule temp = null;
		for (Iterator<InnerFilterRule> iterator = RULES.iterator(); iterator.hasNext();){
			temp = iterator.next();
			if(StringUtils.notEmpty(temp.getHttpMethod()) && !temp.getHttpMethod().equalsIgnoreCase(httpMethod)) {
				continue;
			}
			if(WebUtils.pathMatch(temp.getAccessPath(), servletPath)){
				 matched = temp;
				 break;
			}
		}
		
		if (Objects.isNull(matched)){
			return true;
		}

		if (matched.isIgnore()) {
			if(log.isInfoEnabled()) {
				log.info("method:{} ,uri: {} ,filter :{}", matched.getHttpMethod()==null?"*":matched.getHttpMethod(), servletPath, FILTER_ANON);
			}
			return true;
		}

		for (InnerFilterRule.FilterProps filterProps : matched.getFilters()) {
			if(log.isInfoEnabled()) {
				log.info("method:{} ,uri: {} ,filter :{}", matched.getHttpMethod()==null?"*":matched.getHttpMethod(), servletPath, filterProps.getName());
			}
			InnerFilter filter = FILTERS.get(filterProps.getName());
			if(Objects.isNull(filter)){
				throw new IllegalArgumentException("没有名称为["+ filterProps.getName() +"]的Filter");
			}
			
			boolean accessAllowed = filter.isAccessAllowed(httpRequest, httpResponse, filterProps.getProps());
			if (accessAllowed) {
				return true;
			}

			accessAllowed = filter.onAccessDenied(httpRequest, httpResponse);
			if (accessAllowed) {
				return true;
			}
		}

		return false;
	}


}