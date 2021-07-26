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

import org.jsets.fastboot.security.ICaptchaProvider;
import org.jsets.fastboot.security.cache.InnerCache;
import org.jsets.fastboot.security.cache.InnerCacheManager;
import org.jsets.fastboot.security.config.SecurityProperties;
import org.jsets.fastboot.security.dao.CaptchaDao;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.NonLinearTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.engine.GenericCaptchaEngine;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Color;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 验证码实现
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.05 14:48
 * @since 0.1
 */
public class CaptchaProviderImpl implements ICaptchaProvider {
	
	private static final String RANDOMS = "1234567890abcdefghijklmnopkuvwxyz";//随机字符
	private static final GenericCaptchaEngine ENGINE = new GenericCaptchaEngine(//图片引擎
			new GimpyFactory[] { new GimpyFactory(new RandomWordGenerator(RANDOMS),
					new ComposedWordToImage(
							new RandomFontGenerator(20, 20, new Font[] { new Font("Arial", 20, 20) }),
							new UniColorBackgroundGenerator(90, 30, Color.white),
						new NonLinearTextPaster(5, 5,new RandomListColorGenerator(new Color[] { new Color(23, 170, 27),new Color(220, 34, 11), new Color(23, 67, 172) })))
					) 
			});
	
	private final CaptchaDao captchaDao;
	private final ImageCaptchaService service;

    public CaptchaProviderImpl(SecurityProperties properties,InnerCacheManager cacheManager) {
    	InnerCache cache = cacheManager.getCache(properties.getCaptchaCacheName(), properties.getCaptchaCacheTimeout());
		this.captchaDao = new CaptchaDao(cache);
		this.service =  new DefaultManageableImageCaptchaService(this.captchaDao, ENGINE, 180, 100000, 75000);
	}

	@Override
    public BufferedImage generateCaptcha(String captchaKey) {
		return this.service.getImageChallengeForID(captchaKey);
    }

    @Override
    public boolean validateCaptcha(String captchaKey, String captcha) {
		try {
			return this.service.validateResponseForID(captchaKey, captcha);
		} catch (CaptchaServiceException e) {
			return false;
		}
    }
    
}