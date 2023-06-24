package com.a3fun.pudding.util;

import com.a3fun.core.config.model.AbstractMeta;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MetaUtils {
    public static int[] parseInts(String text) {
        return parseInts(text, AbstractMeta.META_SEPARATOR_1);
    }
    public static int[] parseInts(String text, char sep) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        String[] splitTmp1 = StringUtils.split(text, sep);
        int[] ints = new int[splitTmp1.length];
        for (int i = 0; i < splitTmp1.length; i++) {
            ints[i] = Integer.parseInt(splitTmp1[i]);
        }
        return ints;
    }

    public static String[] parse(String text, char separator) {
        List<String> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (end < text.length()) {
            if (text.charAt(end) == separator) {
                result.add(text.substring(start, end));
                start = end + 1;
            }
            end++;
        }
        if (start < end) {
            result.add(text.substring(start, end));
        }
        return result.toArray(new String[0]);
    }

    public static String[] parse(String text) {
        return parse(text, AbstractMeta.META_SEPARATOR_1);
    }

    /**
     * 解析double数组
     * @param doublesRaw
     * @param sep
     * @return
     */
    public static double[] parseDoubles(String doublesRaw, char sep) {
        if (StringUtils.isEmpty(doublesRaw)) {
            return null;
        }
        String[] splitTmp1 = StringUtils.split(doublesRaw, sep);
        double[] ds = new double[splitTmp1.length];
        for (int i = 0; i < splitTmp1.length; i++) {
            ds[i] = Double.parseDouble(splitTmp1[i]);
        }
        return ds;
    }

    /**
     * 解析double数组
     * @param doublesRaw
     * @return
     */
    public static double[] parseDoubles(String doublesRaw) {
        return parseDoubles(doublesRaw, AbstractMeta.META_SEPARATOR_1);
    }
}
