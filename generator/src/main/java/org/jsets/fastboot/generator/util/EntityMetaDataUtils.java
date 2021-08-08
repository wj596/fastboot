package org.jsets.fastboot.generator.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jsets.fastboot.generator.model.EntityInfo;
import org.jsets.fastboot.generator.model.FieldInfo;
import org.jsets.fastboot.generator.model.TableInfo;
import org.jsets.fastboot.generator.model.User;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.jsets.fastboot.common.util.StringUtils;

public class EntityMetaDataUtils {
	
	// 实例化工具
	private static final Objenesis OBJENESIS = new ObjenesisStd();
	// 实例构造器缓存
	private static final ConcurrentHashMap<String, ObjectInstantiator<?>> INSTANTIATORS = new ConcurrentHashMap<String, ObjectInstantiator<?>>();

	public static final String SETTER_PREFIX = "set";
	public static final String GETTER_PREFIX = "get";
	public static final String IS_PREFIX = "is";
	
	/**
	 * 获取父类
	 * @param cls 类
	 * @return List<Class<?>>
	 */
	public static List<Class<?>> getSupers(final Class<?> cls) {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> superclass = cls.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return classes;
	}
	
	/**
	 * 获取字段
	 * @param classes 类列表
	 * @return
	 */
	public static Set<Field> getFields(final List<Class<?>> classes) {
		Set<Field> fields = new HashSet<Field>();
		for (Class<?> type : classes) {
			fields.addAll(Arrays.asList(type.getDeclaredFields()));
		}
		return Collections.unmodifiableSet(fields);
	}
	
	/**
	 * 获取方法
	 * @param classes 类列表
	 * @param methodName 方法名称
	 * @param parameterTypes 参数类型
	 * @return Method
	 */
	public static Method getMethod(final List<Class<?>> classes, final String methodName,Class<?>... parameterTypes) {
		for (Class<?> i : classes) {
			try {
				Method method = i.getDeclaredMethod(methodName, parameterTypes);
				if ((!Modifier.isPublic(method.getModifiers())
						|| !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
					method.setAccessible(true);
				}
				return method;
			} catch (NoSuchMethodException e) {}
		}
		return null;
	}
	
	/**
	 * 获取getter方法
	 * @param classes 类
	 * @param fieldName 字段名称
	 * @param parameterTypes 字段类型
	 * @return Method
	 */
	public static Method getterMethod(final List<Class<?>> classes, final String fieldName,
			Class<?>... parameterTypes) {
		String methodName = GETTER_PREFIX + StringUtils.capitalize(fieldName);
		Method method = getMethod(classes, methodName,parameterTypes);
		if(null == method){
			methodName = IS_PREFIX + StringUtils.capitalize(fieldName);
			method = getMethod(classes, methodName,parameterTypes);
		}
		return method;
	}
	
	/**
	 * 调用setter方法
	 */
	public static void invokeSetter(final String entityName, final String fieldName, final Object obj,
			final Method method, final Class<?> setType, final Object args) {
		try {
			method.invoke(obj, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("entity:" + entityName + ", field：" + fieldName + ", setter type:" + setType.getName()
					+ ", value type:" + args.getClass(), e);
		}
	}

	/**
	 * 调用gsetter方法
	 */
	public static <T> T invokeGetter(final Object obj, final Method method) {
		try {
			return (T) method.invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static <T> T newInstance(Class<?> persistentClass) {
		if (INSTANTIATORS.contains(persistentClass.getName())) {
			return (T) INSTANTIATORS.get(persistentClass.getName()).newInstance();
		}
		ObjectInstantiator instantiator = OBJENESIS.getInstantiatorOf(persistentClass);
		INSTANTIATORS.putIfAbsent(persistentClass.getName(), instantiator);
		return (T) instantiator.newInstance();
	}
	
	/**
	 * 获取类信息
	 * @param clazz 类
	 * @return EntityInfo
	 */
	public static EntityInfo getEntityInfo(final Class<?> clazz) {
		EntityInfo entityInfo = new EntityInfo();
		entityInfo.setName(clazz.getSimpleName());
		List<Class<?>> supers = getSupers(clazz);
		supers.add(clazz);
		Set<Field> fields = getFields(supers);
		for(Field field:fields) {
			FieldInfo fieldInfo = new FieldInfo();
			fieldInfo.setName(field.getName());
			String type = field.getGenericType().getTypeName();
			type = type.substring(type.lastIndexOf(".")+1, type.length());
			fieldInfo.setType(type);
			entityInfo.getFieldInfos().add(fieldInfo);
		}
		return entityInfo;
	}
	
	public static void main(String[] args) {
		getEntityInfo(User.class);
	}
}
