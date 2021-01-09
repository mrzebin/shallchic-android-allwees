package com.hb.basemodel.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUtil {
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Set<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static <E> E get(List<E> list, int index) {
        if (isEmpty(list))
            return null;
        if (list.size() > index)
            return list.get(index);
        return null;
    }

    public static int getSize(List<?> list) {
        if (isEmpty(list))
            return 0;

        return list.size();
    }

    public static boolean getSize2(List<?> list) {
        if (isEmpty(list))
            return false;

        return list.size() > 0;
    }

    public static boolean idNotNull(List<?> list) {
        if (isEmpty(list))
            return false;
        return list.size() > 0;
    }
}
