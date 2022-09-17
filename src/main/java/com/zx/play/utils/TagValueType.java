package com.zx.play.utils;

import lombok.Data;

/**
 * @author zx
 * @create 2022/9/11 16:50
 */
public enum TagValueType {
    TAG_VALUE_TYPE_LONG("1"),TAG_VALUE_TYPE_DECIMAL("2"),
    TAG_VALUE_TYPE_STRING("3"),TAG_VALUE_TYPE_DATE("4");
    private String info;
    private TagValueType(){

    }
    private TagValueType(String info){
        this.info=info;
    }
    public String getInfo(){
        return this.info;
    }
    public static TagValueType getTagValueTypeByInfo(String info){
        for (TagValueType value : TagValueType.values()) {
            if (value.getInfo().equals(info)){
                return value;
            }
        }
        return null;
    }

}
