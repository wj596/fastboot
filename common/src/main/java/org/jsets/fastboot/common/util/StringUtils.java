package org.jsets.fastboot.common.util;

import java.util.*;

/**
 * 字符串工具
 * 
 * @author wj596
 *
 */
public class StringUtils {
	/**
	 * UTF-8 编码格式
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * 空字符
	 */
	public static final String EMPTY = "";

	/**
	 * 占位符
	 */
	public static final String PLACE_HOLDER = "{%s}";

	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = ",";

	/**
	 * 获取UUID
	 * 
	 * @return String
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 去除两端空格 <br>
	 *
	 * @param string 字符串
	 * @return String
	 */
	public static String trim(final String string) {
		if (Objects.isNull(string)) {
			return "";
		}
		return string.trim();
	}

	/**
	 * 是否为空 <br>
	 * *isEmpty(" ") = false*
	 * 
	 * @param string 字符串
	 * @return boolean
	 */
	public static boolean isEmpty(final String string) {
		return string == null || string.isEmpty();
	}

	/**
	 * 是否为空 <br>
	 * *isBlank(" ") = true*
	 * 
	 * @param string 字符串
	 * @return boolean
	 */
	public static boolean isBlank(final String string) {
		int strLen;
		if (string != null && (strLen = string.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(string.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}

	/**
	 * 断言不为空
	 * 
	 * @param string 字符串
	 * @return boolean
	 */
	public static boolean notEmpty(final String string) {
		return string != null && string.length() > 0;
	}

	/**
	 * 分割字符串，使用‘,’
	 * 
	 * @param str 字符串
	 * @return List<String>
	 */
	public static List<String> split(final String str) {
		if (null == str || "".equals(str)) {
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(str.split(SEPARATOR));
	}

	/**
	 * 分割字符串，使用指定的分隔符
	 * 
	 * @param str            字符串
	 * @param separatorChars 分隔符
	 * @return List<String>
	 */
	public static List<String> split(final String str, final String separatorChars) {
		if (null == str || "".equals(str)) {
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(str.split(separatorChars));
	}

	/**
	 * 连接字符串，使用‘,’
	 * 
	 * @param collection 集合
	 * @return String
	 */
	public static String join(final Collection<String> collection) {
		StringBuilder sb = new StringBuilder();
		for (String str : collection) {
			if (sb.length() > 0)
				sb.append(SEPARATOR);
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 连接字符串，使用‘,’
	 * 
	 * @param array 字符串数组
	 * @return String
	 */
	public static String join(final String[] array) {
		StringBuilder sb = new StringBuilder();
		for (String str : array) {
			if (sb.length() > 0) {
				sb.append(SEPARATOR);
			}
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 连接字符串，使用指定分隔符
	 * 
	 * @param collection     集合
	 * @param separatorChars 分隔符
	 * @return String
	 */
	public static String join(final Collection<String> collection, final String separatorChars) {
		StringBuilder sb = new StringBuilder();
		for (String str : collection) {
			if (sb.length() > 0)
				sb.append(separatorChars);
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 转布尔值
	 *
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean toBoolean(final String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}

		if ("1".equals(str) || "ok".equals(str) || "true".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串"null"转为""
	 * 
	 * @param string 字符串
	 * @return string
	 */
	public static String nullToBlank(final String string) {
		return string == null ? "" : string;
	}

	/**
	 * 字符串转小写(null安全)
	 * 
	 * @param string 字符串
	 * @return string
	 */
	public static String toLowerCase(final String string) {
		return string == null ? "" : string.toLowerCase();
	}

	/**
	 * 字符串转大写(null安全)
	 * 
	 * @param string 字符串
	 * @return string
	 */
	public static String toUpperCase(final String string) {
		return string == null ? "" : string.toUpperCase();
	}

	/**
	 * 首字母大写
	 * 
	 * @param str 字符串
	 * @return String
	 */
	public static String capitalize(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		final char firstChar = str.charAt(0);
		final char newChar = Character.toTitleCase(firstChar);
		if (firstChar == newChar) {
			// already capitalized
			return str;
		}

		char[] newChars = new char[strLen];
		newChars[0] = newChar;
		str.getChars(1, strLen, newChars, 1);
		return String.valueOf(newChars);
	}

	/**
	 * 符串转驼峰格式转下划线格式
	 * 
	 * @param param 字符串
	 * @return String
	 */
	public static String camelToUnderline(final String param) {
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 符串下划线格式转转驼峰格式
	 * 
	 * @param param 字符串
	 * @return String
	 */
	public static String underlineToCamel(String param) {
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		boolean flag = false; // "_" 后转大写标志,默认字符前面没有"_"
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == '_') {
				flag = true;
				continue; // 标志设置为true,跳过
			} else {
				char tt = Character.toLowerCase(param.charAt(i));
				if (flag && sb.length() > 0) {
					sb.append(Character.toUpperCase(tt));
				} else {
					sb.append(tt);
				}
				flag = false; // 重置标识
			}
		}
		return sb.toString();
	}
}
