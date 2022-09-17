package com.zx.play.bean;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author zx
 * @create 2022/9/11 15:05
 */
@Data
public class TaskInfo {
    Long id;
    String taskName;
    String taskStatus;
    String taskComment;
    String taskType;
    String execType;
    String mainClass;
    Long fileId;
    String taskArgs;
    String taskSql;
    Long taskExecLevel;
    Timestamp createTime;
}
