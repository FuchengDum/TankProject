package com.dum.design.observer.v1_state;

import com.dum.design.observer.ActionType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

// 任务初始状态
@Slf4j
public class TaskInit implements State{
    @PostConstruct
    public void init(){
      log.info("TaskInit init...");
    }
    @Override
    public void updateState(Task task,ActionType type) {
        log.info("TaskInit updateState... taskId ={}",task.getTaskId());
        if (ActionType.START==type){
            task.setState(new TaskGoing());
        }
    }
}
