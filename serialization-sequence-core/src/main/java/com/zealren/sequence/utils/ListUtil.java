package com.zealren.sequence.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class ListUtil {

    public static boolean isNullOrEmpty(List list) {
        return null == list ? true : list.size() == 0 ? true : false;
    }

    public static boolean isNotEmpty(List list) {
        return !isNullOrEmpty(list);
    }

    public static <T> List<T> asList(T[] tArray) {
        if (null == tArray || tArray.length <= 0) {
            return null;
        }
        List<T> list = new ArrayList<T>(tArray.length);
        Collections.addAll(list, tArray);
        return list;
    }
}
