package com.yjtoon.school.enums;

import org.apache.commons.lang.StringUtils;

/**
 * Created by 136931 on 2016/3/22.
 * 是否冒泡：0冒泡，1不冒泡
 */
public enum BubbleEnum {

    IMPORTANT(0, "IMPORTANT", "冒泡"), UNIMPORTANT(1, "UNIMPORTANT", "不冒泡");
    private Integer numCode;
    private String code;
    private String name;

    BubbleEnum(Integer numCode, String code, String name) {
        this.numCode = numCode;
        this.code = code;
        this.name = name;
    }
    public static BubbleEnum getEnum(Integer value) {
        for (BubbleEnum e : BubbleEnum.values()) {
            if (e.getNumCode().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static BubbleEnum getEnum(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (BubbleEnum e : BubbleEnum.values()) {
                if (e.getCode().equals(value)) {
                    return e;
                }
            }
        }
        return null;
    }

    public Integer getNumCode() {
        return numCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
