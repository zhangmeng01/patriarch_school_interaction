package com.yjtoon.school.enums;

import org.apache.commons.lang3.StringUtils;

public enum NoticeTypeEnum {
    NOTICE(1, "NOTICE", "通知"), WORK(2, "WORK", "作业"), SCORE(3, "SCORE", "成绩");
    private Integer numCode;
    private String code;
    private String name;

    NoticeTypeEnum(Integer numCode, String code, String name) {
        this.numCode = numCode;
        this.code = code;
        this.name = name;
    }
    public static NoticeTypeEnum getEnum(Integer value) {
        for (NoticeTypeEnum e : NoticeTypeEnum.values()) {
            if (e.getNumCode().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static NoticeTypeEnum getEnum(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (NoticeTypeEnum e : NoticeTypeEnum.values()) {
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

