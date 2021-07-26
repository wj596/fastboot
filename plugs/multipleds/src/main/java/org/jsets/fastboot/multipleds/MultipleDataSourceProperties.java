package org.jsets.fastboot.multipleds;

import java.util.List;

import org.jsets.fastboot.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * 
 * 多数据源配置
 * 
 * @author wangjie
 *
 */
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class MultipleDataSourceProperties {

	@Autowired
	private Environment environment;

	private List<DruidDataSourceProperties> multiple;

	public List<DruidDataSourceProperties> getMultiple() {
		return multiple;
	}

	public void setMultiple(List<DruidDataSourceProperties> multiple) {
		this.multiple = multiple;
	}

	public String getFilters() {
		List<String> filters = Lists.newArrayList();
		String stat = environment.getProperty("spring.datasource.druid.filter.stat.enabled");
		if (this.isEnabled(stat)) {
			filters.add("stat");
		}
		String config = environment.getProperty("spring.datasource.druid.filter.config.enabled");
		if (this.isEnabled(config)) {
			filters.add("config");
		}
		String encoding = environment.getProperty("spring.datasource.druid.filter.encoding.enabled");
		if (this.isEnabled(encoding)) {
			filters.add("encoding");
		}
		String slf4j = environment.getProperty("spring.datasource.druid.filter.slf4j.enabled");
		if (this.isEnabled(slf4j)) {
			filters.add("slf4j");
		}
		String log4j = environment.getProperty("spring.datasource.druid.filter.log4j.enabled");
		if (this.isEnabled(log4j)) {
			filters.add("log4j");
		}
		String log4j2 = environment.getProperty("spring.datasource.druid.filter.log4j2.enabled");
		if (this.isEnabled(log4j2)) {
			filters.add("log4j2");
		}
		String wall = environment.getProperty("spring.datasource.druid.filter.wall.enabled");
		if (this.isEnabled(wall)) {
			filters.add("wall");
		}
		return StringUtils.join(filters);
	}

	public boolean isStatFilterEnabled() {
		String enabled = environment.getProperty("spring.datasource.druid.filter.stat.enabled");
		return this.isEnabled(enabled);
	}

	private boolean isEnabled(String enabled) {
		if (Strings.isNullOrEmpty(enabled)) {
			return false;
		}
		if ("true".equals(enabled)) {
			return true;
		}
		return false;
	}
}