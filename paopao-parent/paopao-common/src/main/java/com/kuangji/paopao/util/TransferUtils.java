package com.kuangji.paopao.util;


import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferUtils {
    public static List<Long> strToLongs(String ids) {
        List<Long> idList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            List<String> strings = Splitter.on(",").trimResults().splitToList(ids);
            idList.addAll(strings.stream().map(Long::parseLong).collect(Collectors.toList()));
        }
        return idList;
    }

    public static List<String> strToStrings(String ids) {
        List<String> idList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            List<String> strings = Splitter.on(",").trimResults().splitToList(ids);
            for (String idStr : strings) {
                if (StringUtils.isNotEmpty(idStr)) {
                    idList.add(idStr);
                }
            }
        }
        return idList;
    }

    public static List<Integer> strToIntegers(String ids) {
        List<Integer> idList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            List<String> strings = Splitter.on(",").trimResults().splitToList(ids);
            idList.addAll(strings.stream().map(Integer::parseInt).collect(Collectors.toList()));
        }
        return idList;
    }

    public static List<String> strToStrings(String ids, String regex) {
        List<String> idList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids.trim())) {
            List<String> strings = Splitter.on(regex).trimResults().splitToList(ids);
            for (String idStr : strings) {
                if (StringUtils.isNotEmpty(idStr.trim())) {
                    idList.add(idStr);
                }
            }
        }
        return idList;
    }

    public static BigDecimal ConvertToBigDecimal(BigDecimal data) {
        if (null == data) {
            return BigDecimal.ZERO;
        }
        return data;
    }

    public static String leftLike(String keyword) {
        return "%" + keyword;
    }

    public static String rightLike(String keyword) {
        return keyword + "%";
    }

    public static String allLike(String keyword) {
        return "%" + keyword + "%";
    }
}
