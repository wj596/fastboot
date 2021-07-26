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
package org.jsets.fastboot.security.dao;

import java.util.Collection;
import java.util.Locale;
import org.jsets.fastboot.security.cache.InnerCache;
import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.CaptchaStore;

public class CaptchaDao implements CaptchaStore {

	private final InnerCache cache;

	public CaptchaDao(InnerCache cache) {
		this.cache = cache;
	}

	
	@Override
	public boolean hasCaptcha(String key) {
		return this.cache.hasKey(key);
	}
	
	@Override
	public void storeCaptcha(String key, Captcha captcha) throws CaptchaServiceException {
		this.cache.put(key, captcha);
	}
	
	@Override
	public void storeCaptcha(String key, Captcha captcha, Locale locale) throws CaptchaServiceException {
		this.cache.put(key, new CaptchaAndLocale(captcha,locale));
	}
	
	@Override
	public Captcha getCaptcha(String key) throws CaptchaServiceException {
        Object captchaAndLocale = this.cache.get(key, CaptchaAndLocale.class);
        return captchaAndLocale!=null?((CaptchaAndLocale) captchaAndLocale).getCaptcha():null;
	}
	
    public Locale getLocale(String key) throws CaptchaServiceException {
    	Object captchaAndLocale = this.cache.get(key, CaptchaAndLocale.class);
        return captchaAndLocale!=null?((CaptchaAndLocale) captchaAndLocale).getLocale():null;
    }

    public boolean removeCaptcha(String key) {
        if (this.cache.hasKey(key)) {
        	this.cache.delete(key);
            return true;
        }
        return false;
    }

    public int getSize() {
        return this.cache.getKeys().size();
    }

    public Collection getKeys() {
        return this.cache.getKeys();
    }

    public void empty() {
    	this.cache.cleanUp();
    }

	@Override
	public void initAndStart() {
	}
    
	@Override
	public void cleanAndShutdown() {
		this.cache.cleanUp();
	}

}
