package org.jsets.fastboot.multipleds;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.jsets.fastboot.common.util.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * 多样可切换数据源配置
 * 
 * @author wj596
 *
 */
@Slf4j
@EnableConfigurationProperties({ MultipleDataSourceProperties.class })
public class MultipleDataSourceConfiguration {

	@Autowired
	private MultipleDataSourceProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public DataSource dataSource() {
		if (null == this.properties.getMultiple() || this.properties.getMultiple().isEmpty()) {
			throw new BeanCreationException("MultipleDataSource 配置错误");
		}

		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		DruidDataSource primaryDataSource = null;
		for (DruidDataSourceProperties p : this.properties.getMultiple()) {
			DruidDataSource dds = this.buildDruidDataSource(p);
			if (p.isPrimary()) {
				primaryDataSource = dds;
			}
			String name = p.getName();
			if (Strings.isNullOrEmpty(name)) {
				name = p.getUrl();
			}
			targetDataSources.put(name, dds);
		}
		if (null == primaryDataSource) {
			throw new BeanCreationException("请设置一个主数据源");
		}

		// 添加数据源
		MultipleDataSource multipleDataSource = new MultipleDataSource();
		multipleDataSource.setTargetDataSources(targetDataSources);
		multipleDataSource.setDefaultTargetDataSource(primaryDataSource);
		return multipleDataSource;
	}

	@Bean
	public MultipleDataSourceAspect multipleDataSourceAspect() {
		System.out.println("MultipleDataSourceConfig multipleDataSourceAspect");
		return new MultipleDataSourceAspect();
	}
	
	private DruidDataSource buildDruidDataSource(DruidDataSourceProperties p) {
		DruidDataSource dds = DruidDataSourceBuilder.create().build();
		dds.setUrl(p.getUrl());
		dds.setUsername(p.getUsername());
		dds.setPassword(p.getPassword());
		dds.setDriverClassName(p.getDriverClassName());
		dds.setInitialSize(p.getInitialSize());
		dds.setMinIdle(p.getMinIdle());
		dds.setMaxActive(p.getMaxActive());
		dds.setMaxWait(p.getMaxWait());
		dds.setTimeBetweenEvictionRunsMillis(p.getTimeBetweenEvictionRunsMillis());
		dds.setMinEvictableIdleTimeMillis(p.getMinEvictableIdleTimeMillis());
		dds.setValidationQuery(p.getValidationQuery());
		dds.setTestWhileIdle(p.isTestWhileIdle());
		dds.setTestOnBorrow(p.isTestOnBorrow());
		dds.setTestOnReturn(p.isTestOnReturn());
		// dds.setUseGlobalDataSourceStat(true);

		if (Objects.nonNull(p.getPoolPreparedStatements())) {
			dds.setPoolPreparedStatements(p.getPoolPreparedStatements());
		}
		if (Objects.nonNull(p.getMaxPoolPreparedStatementPerConnectionSize())) {
			dds.setMaxPoolPreparedStatementPerConnectionSize(p.getMaxPoolPreparedStatementPerConnectionSize());
		}
		String filters = this.properties.getFilters();
		if (StringUtils.notEmpty(filters)) {
			try {
				dds.setFilters(filters);
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}

		return dds;
	}
}