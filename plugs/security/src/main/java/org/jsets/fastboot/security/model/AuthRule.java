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
package org.jsets.fastboot.security.model;

import lombok.Data;
import lombok.ToString;
import java.util.Set;

/**
 *
 * 鉴权规则，保存在数据库中，由AuthRuleProvider接口提供
 *
 * @author wangjie
 *
 */
@Data
@ToString
public class AuthRule {
    private String path;// 资源地址
    private String method;// 访问方法
    private String role;// 访问需要角色
    private String permission;// 访问需要权限
}
