package com.zealren.sequence.enums;

public enum SequenceLoopType {
    NO_LOOP("0", "不循环"), YEAR("1", "year"), MONTH("2", "month"),
    DAY("3", "day"), MINUTE("4", "minute");
    private String value;
    private String desc;

    SequenceLoopType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
