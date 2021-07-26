package org.jsets.fastboot.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class CollectionUtils {

    /**
     * 是否为空
     * @param coll 集合
     * @return boolean
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }
    
    /**
     * 断言集合不为空
     *
     * @param coll 集合
     * @return boolean
     */
    public static boolean notEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
    
    
    /**
     * MAP是否为空
     *
     * @param map 哈希表
     * @return boolean
     */
    public static boolean isEmpty(Map<?,?> map) {
        return (map == null || map.isEmpty());
    }
    
    /**
     * 断言MAP不为空
     *
     * @param map 哈希表
     * @return boolean
     */
    public static boolean notEmpty(Map<?,?> coll) {
        return !isEmpty(coll);
    }
    
    /**
     * List切片
     * @param list 列表
     * @param fromIndex 开始下标
     * @param toIndex 结束下标
     * @return List<T>
     */
    public static <T> List<T> slice(List<T> list,int fromIndex, int toIndex){
		List<T> l = Lists.newLinkedList();
		if (fromIndex>list.size()) {
			return l;
		}
		for(int i = fromIndex;i<toIndex;i++) {
			if(i<list.size()) {
				l.add(list.get(i));
			}
		}
		return l;
	}
}
