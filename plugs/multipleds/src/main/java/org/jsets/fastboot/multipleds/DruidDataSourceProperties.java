package org.jsets.fastboot.multipleds;

import lombok.Data;

/**
 * 
 * Druid数据源配置
 * @author wj596
 *
 */
@Data
public class DruidDataSourceProperties {
	
	private String name; // 数据源名称
	private boolean primary;// 是否主数据源
	// ------必填项目------
	private String driverClassName; // 数据库驱动名称
	private String url;// 数据库链接地址
	private String username;// 数据库用户名
	private String password;// 数据库密码

	// ------选填项目------
	private int initialSize = 10;// 初始化大小
	private int minIdle = 10;// 最小
	private int maxActive = 20;// 最大
	private int maxWait = 50000;// 获取连接等待超时的时间
	private int minEvictableIdleTimeMillis = 300000; // 连接保持空闲而不被驱逐的最长时间
	private int timeBetweenEvictionRunsMillis = 90000;// 间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
	private boolean testOnBorrow = false; // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
	private boolean testOnReturn = false;// 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
	// 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，
	// 如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效
	private boolean testWhileIdle = true;
	// 是否缓存preparedStatement，也就是PSCache。
	// PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
	private Boolean poolPreparedStatements;
	// 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。
	// 在Druid中，不会存在Oracle下PSCache占用内存过多的问题
	private Integer maxPoolPreparedStatementPerConnectionSize;
	// 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。
	// 如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会启作用。
	private String validationQuery;
}
