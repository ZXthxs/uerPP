package com.zx.play.bean;

import lombok.Data;

/**
 * @author zx
 * @create 2022/9/11 15:05
 */
@Data
public class TaskTagRule {
    Long id;
    Long tagId;
    Long taskId;
    String queryValue;
    Long subTagId;
    String subTagValue;
}
