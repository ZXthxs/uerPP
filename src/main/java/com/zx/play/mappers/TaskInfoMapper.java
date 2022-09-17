package com.zx.play.mappers;

import com.zx.play.bean.TaskInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zx
 * @create 2022/9/11 15:10
 */
public interface TaskInfoMapper {
    TaskInfo getTaskInfoById(@Param("id") Integer taskId);

    List<TaskInfo> getAllTasks();

}
