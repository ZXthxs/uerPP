package com.zx.play.bean;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author zx
 * @create 2022/9/11 15:03
 */
@Data
public class TagInfo {
    Long id;
    String tagCode;
    String tagName;
    Long tagLevel;
    Long parentTagId;
    String tagType;
    String tagValueType;
    Long tagTaskId;
    String tagComment;
    Timestamp createTime;
    Timestamp updateTime;
}
