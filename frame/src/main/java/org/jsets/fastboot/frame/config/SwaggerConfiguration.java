package org.jsets.fastboot.frame.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import org.jsets.fastboot.common.util.StringUtils;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.List;

/**
 * Swagger配置
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.12 21:49
 * @since 0.1
 */
@Configuration
@EnableOpenApi
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

	@Bean
	public Docket createRestApi(SwaggerProperties properties) {
		System.out.println("SwaggerConfiguration createRestApi");
		return new Docket(DocumentationType.OAS_30).pathMapping("/").enable(properties.getEnable())// 是否开启swagger，false为关闭
				.apiInfo(buildApiInfo(properties)) // 设置文档描述信息
				.host(properties.getAppDebugAddress())// 调试地址
				.select()// 选择哪些接口作为swagger的doc发布
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
				.build().protocols(Sets.newHashSet("https", "http"))// 协议集合
				.securitySchemes(buildSecuritySchemes())// 授权信息设置，header token等认证信息
				.securityContexts(buildSecurityContexts());// 全局授权信息
	}

	/**
	 * 文档描述信息
	 */
	private ApiInfo buildApiInfo(SwaggerProperties properties) {
		ApiInfoBuilder builder = new ApiInfoBuilder().title(properties.getAppName() + " Api Doc")
				.description(properties.getAppDesc()).version("Application Version: " + properties.getAppVersion()
						+ ", Spring Boot Version: " + SpringBootVersion.getVersion());
		if (StringUtils.notEmpty(properties.getContactName()) || StringUtils.notEmpty(properties.getContactUrl())
				|| StringUtils.notEmpty(properties.getContactEmail())) {
			builder.contact(
					new Contact(properties.getContactName(), properties.getContactUrl(), properties.getContactEmail()));
		}
		return builder.build();
	}

	private List<SecurityScheme> buildSecuritySchemes() {
		List<SecurityScheme> ls = Lists.newArrayList();
		ls.add(new ApiKey("Authorization", "Authorization", "header"));
		return ls;
	}

	/**
	 * 授权信息全局应用
	 */
	private List<SecurityContext> buildSecurityContexts() {
		SecurityReference securityReference = new SecurityReference("Authorization",
				new AuthorizationScope[] { new AuthorizationScope("global", "") });
		SecurityContext securityContext = SecurityContext.builder()
				.securityReferences(Lists.newArrayList(securityReference)).build();
		return Lists.newArrayList(securityContext);
	}

}