package org.jsets.fastboot.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 日期时间工具
 * @author wangjie
 *
 */
@Slf4j
public class DateUtils {
	
	public final static String FORMATER_DAY = "yyyy-MM-dd"; //日期格式--天
	public final static String FORMATER_DAY_TIME_MINUTE = "yyyy-MM-dd HH:mm"; //日期时间格式--分
	public final static String FORMATER_DAY_TIME_SECOND = "yyyy-MM-dd HH:mm:ss"; //日期时间格式--秒
	public final static String FORMATER_DAY_TIME_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS"; //日期时间格式--毫秒
	public final static String FORMATER_TIME_SECOND = "HH:mm:ss"; //时间格式--秒
	private static final String[] FORMATERS = { //格式
			"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd'T'HH:mm:ss'Z'", 
			"yyyy-MM-dd'T'HH:mm:ssZ","yyyy-MM-dd'T'HH:mm:ss", 
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd'T'HH:mm:ss.SSSZ", 
			"MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
			"MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
			"MM/dd/yyyy'T'HH:mm:ssZ", "MM/dd/yyyy'T'HH:mm:ss",
			"yyyy:MM:dd HH:mm:ss", "yyyy-MM-dd","yyyyMMdd"};
	
	/**
	 * 现在
	 * @return Date
	 */
	public static Date now() {
		Date now = new Date(System.currentTimeMillis());
		return now;
	}
	
	/**
	 * 现在,yyyy-MM-dd HH:mm:ss格式字符串
	 * @return String
	 */
	public static String nowString() {
		return new SimpleDateFormat(FORMATER_DAY_TIME_SECOND).format(now());
	}
	
	/**
	 * 现在,指定格式字符串
	 * @param formater 指定格式
	 * @return String
	 */
	public static String nowString(String formater) {
		return new SimpleDateFormat(formater).format(now());
	}
	
	/**
	 * 格式化日期，使用yyyy-MM-dd HH:mm:ss格式
	 * @param date 日期
	 * @return String
	 */
	public static String format(Date date) {
		if(Objects.isNull(date)) {
			return "";
		}
		return new SimpleDateFormat(FORMATER_DAY_TIME_SECOND).format(date);
	}
	 
	/**
	 * 格式化日期
	 * @param date 日期 
	 * @param formater 格式
	 * @return String
	 */
	public static String format(Date date,String formater) {
		if(Objects.isNull(date)||StringUtils.isEmpty(formater)) {
			return "";
		}
		return new SimpleDateFormat(formater).format(date);
	}
	
	/**
	 * 解析日期，使用yyyy-MM-dd格式
	 * @param str 日期字符串
	 * @return Date
	 */
	public static Date parseDay(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		try {
			return new SimpleDateFormat(FORMATER_DAY).parse(str);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 解析日期时间，使用yyyy-MM-dd HH:mm:ss格式
	 * @param str 日期字符串
	 * @return Date
	 */
	public static Date parseDayTime(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		try {
			return new SimpleDateFormat(FORMATER_DAY_TIME_SECOND).parse(str);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 解析日期字符串
	 * @param str 日期字符串
	 * @param formater 格式
	 * @return Date
	 */
	public static Date parse(String str,String formater) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		if(StringUtils.isEmpty(formater)) {
			return null;
		}
		try {
			return new SimpleDateFormat(formater).parse(str);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 解析日期字符串，自动探测格式
	 * @param str 日期字符串
	 * @return Date
	 */
	public static Date parse(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		for (String formater : FORMATERS) {
			try {
				Date result = new SimpleDateFormat(formater).parse(str);
				log.info("日前字符串：{}，探测到的格式为：{}", str, formater);	
				return result;
			} catch (ParseException e) {
				// 忽略
			}
		}
		return null;
	}
	
	/**
	 * days天后的日期
	 * @param currentDate 当前日期
	 * @param days 加量
	 * @return Date
	 */
	public static Date plusDays(Date currentDate,int days) {
		LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.plusDays(days);
		Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		return result;
	}
	
	/**
	 * days天前的日期
	 * @param currentDate 当前日期
	 * @param days 减量
	 * @return Date
	 */
	public static Date minusDays(Date currentDate,int days) {
		LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.minusDays(days);
		Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		return result;
	}

}