package org.jsets.fastboot.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该方法需要HMAC验证
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 23:56
 * @since 0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HMACAuthenticate {

}
