package com.zx.play.mappers;

import com.zx.play.bean.TaskTagRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zx
 * @create 2022/9/11 15:11
 */
public interface TaskTAgRuleMapper {
    List<TaskTagRule> getTaskTagRuleById(@Param("id") Integer taskId);
}
