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
package org.jsets.fastboot.security;

import java.util.List;
import org.jsets.fastboot.security.model.AuthRule;

/**
 * 过滤规则提供者接口
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月31日
 */
public interface IAuthRuleProvider {
	/**
	 * 加载动态鉴权规则
	 *
	 * <br>大部分系统的安全体系都是RBAC(Role-based access control，基于角色的权限访问控制)授权模型。
	 * <br>即：用户--角色--资源(URL),对应关系可配并且存储在数据库中。
	 * <br>此方法提供的数据为：DynamicAuthRule{url资源地址、roles访问此资源需要的角色}
	 * <br>对应的静态配置为：url-->roles[角色1、角色2、角色n]
	 * <br>当用户持有[角色1、角色2、角色n]中的任何一个角色，则可以访问url，否则不予访问
	 * 
	 * <br>权限指用户能操作资源的统称、角色为权限的集合
	 * <br>权限授权模型直接表示为：用户--资源(URL)。
	 * <br>此方法提供的数据格为：DynamicAuthRule{url资源地址、perms访问此资源需要的权限}
	 * <br>对应的静态配置为：url=perms[权限编码1、权限编码2、权限编码n]
	 * <br>当用户持有[权限编码1、权限编码2、权限编码n]中的任何一个权限，则给予访问，否则不予访问
	 * <br>基于权限的访问控制比基于角色的访问控制，粒度更细
	 * 
	 * @return  List<DynamicAuthRule>
	 *
	 */
	List<AuthRule> loadAuthRuleList();
}
