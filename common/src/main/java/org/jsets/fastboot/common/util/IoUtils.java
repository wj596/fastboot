package org.jsets.fastboot.common.util;

/**
 * IO相关工具
 * 
 * @author wj596
 *
 */
public class IoUtils {

	/**
	 * 关闭，吞异常
	 * 
	 * @param closeable @see AutoCloseable
	 */
	public static void closeQuietly(AutoCloseable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// ignore, for backwards compatibility
			}
		}
	}
}
