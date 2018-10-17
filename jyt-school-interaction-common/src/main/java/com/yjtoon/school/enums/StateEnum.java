package com.yjtoon.school.enums;

import org.apache.commons.lang3.StringUtils;

public enum StateEnum {
    ENABLE(1, "ENABLE", "启用"), DISABLE(2, "DISABLE", "禁用");
    private Integer numCode;
    private String code;
    private String name;

    StateEnum(Integer numCode, String code, String name) {
        this.numCode = numCode;
        this.code = code;
        this.name = name;
    }
    public static StateEnum getEnum(Integer value) {
        for (StateEnum e : StateEnum.values()) {
            if (e.getNumCode().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static StateEnum getEnum(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (StateEnum e : StateEnum.values()) {
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

