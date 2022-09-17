package com.zx.play.service;

import com.zx.play.bean.TagInfo;
import com.zx.play.bean.TaskInfo;
import com.zx.play.bean.TaskTagRule;
import com.zx.play.mappers.TagInfoMapper;
import com.zx.play.mappers.TaskInfoMapper;
import com.zx.play.mappers.TaskTAgRuleMapper;
import org.apache.ibatis.session.SqlSession;
import org.omg.CORBA.INTERNAL;

import java.util.List;

/**
 * @author zx
 * @create 2022/9/11 15:23
 */
public class MysqlDBService {
    private TagInfoMapper tagInfoMapper;
    private TaskInfoMapper taskInfoMapper;
    private TaskTAgRuleMapper taskTAgRuleMapper;

    public MysqlDBService(SqlSession sqlSession){
        tagInfoMapper = sqlSession.getMapper(TagInfoMapper.class);
        taskInfoMapper =sqlSession.getMapper(TaskInfoMapper.class);
        taskTAgRuleMapper =sqlSession.getMapper(TaskTAgRuleMapper.class);
    }

    public TagInfo getTagInfoById(Integer id){
        if(id == null || id < 0){
            throw new RuntimeException("参数非法");
        }
        //System.out.println("先做点什么");
        return tagInfoMapper.getTagInfoById(id);

    }

    public TaskInfo getTaskInfoById(Integer id){
        if(id == null || id < 0){
            throw new RuntimeException("参数非法");
        }
        //System.out.println("先做带你什么");
        return taskInfoMapper.getTaskInfoById(id);
    }

    public List<TaskInfo> getAllTask(){
        //System.out.println("先干点啥");
        return taskInfoMapper.getAllTasks();
    }

    public List<TaskTagRule> getTaskTagRuleById(Integer id){
        if(id == null || id < 0){
            throw new RuntimeException("参数非法");
        }
        return taskTAgRuleMapper.getTaskTagRuleById(id);
    }

    public List<TagInfo> getTagOn(){
        return tagInfoMapper.getTagInfoOn();
    }


}
