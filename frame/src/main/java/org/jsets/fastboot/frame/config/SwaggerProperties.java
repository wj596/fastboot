package org.jsets.fastboot.frame.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger配置属性
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.12 21:49
 * @since 0.1
 */
@Data
@ConfigurationProperties(prefix = "fastboot.swagger")
public class SwaggerProperties {

	private Boolean enable;// 是否开启swagger，生产建议关闭
	private String appName;// 项目名称
	private String appVersion;// 项目版本
	private String appDesc;// 项目描述
	private String appDebugAddress;// 项目调试地址
	private String contactName;// 联系人姓名
	private String contactUrl;// 联系地址
	private String contactEmail;// 联系人邮件

}
