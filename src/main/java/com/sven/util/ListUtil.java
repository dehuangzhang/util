package com.sven.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author sven.zhang
 * @since 2018/3/9
 */
public class ListUtil {
    /**
     * set去重转 list 集合
     *
     * @param sets
     * @return
     */
    public static <V> List<V> setToList(Set<V> sets) {
        if (CollectionUtils.isEmpty(sets)) {
            return Collections.emptyList();
        }
        List<V> list = new ArrayList<>();
        list.addAll(sets);
        return list;
    }

    /**
     * 根据对象获取某个属性的 集合
     *
     * @param list
     * @return
     */
    public static <K, V> List<V> getList(List<K> list, Function<K, V> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<V> fieldList = new ArrayList<>();
        for (K k : list) {
            V v = function.apply(k);
            fieldList.add(v);
        }
        return fieldList;
    }
}
