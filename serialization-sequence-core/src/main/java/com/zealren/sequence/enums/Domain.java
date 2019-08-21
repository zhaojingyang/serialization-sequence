package com.zealren.sequence.enums;

public enum Domain {
    WMS("WMS", "仓库"), PMS("PMS", "采购"),
    OMS("OMS", "订单"), PRODUCT("PRODUCT", "商品"),
    TMS("TMS", "配送");

    private String value;
    private String desc;

    Domain(String value, String desc) {
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