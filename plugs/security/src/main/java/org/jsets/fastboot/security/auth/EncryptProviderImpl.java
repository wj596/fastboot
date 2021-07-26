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
package org.jsets.fastboot.security.auth;

import org.jsets.fastboot.security.IEncryptProvider;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * 加密实现
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.05 14:46
 * @since 0.1
 */
public class EncryptProviderImpl implements IEncryptProvider {

    /**
     * 基于安全性不推荐使用MD5，推荐用SHA256
     * 如果需要与其他使用MD5的系统系统互操作，可放开MD5注解
     */
    @Override
    public String encrypt(String plain) {
		// Hashing.md5().newHasher().putString(s, Charsets.UTF_8).hash().toString();
		return Hashing.sha256().newHasher().putString(plain, Charsets.UTF_8).hash().toString();
    }
}
