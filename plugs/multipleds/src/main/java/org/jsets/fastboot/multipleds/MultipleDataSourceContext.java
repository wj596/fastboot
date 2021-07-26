package org.jsets.fastboot.multipleds;

/**
 * 
 * 多数据源切换上下文
 * 
 * @author wj596
 *
 */
public class MultipleDataSourceContext {

	private static final ThreadLocal<String> ctx = new ThreadLocal<String>();

	public static void setDataSourceName(String dataSourceName) {
		ctx.set(dataSourceName);
	}

	public static String getDataSourceName() {
		return ctx.get();
	}

	public static void clear() {
		ctx.remove();
	}
}