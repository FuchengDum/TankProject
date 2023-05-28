package com.dum.design.observer.v2;

import com.dum.design.observer.ActionType;
import com.dum.design.observer.v1_state.State;
import com.dum.design.observer.v1_state.Task;
import com.dum.design.observer.v1_state.TaskGoing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskInitV2 extends Subject implements State {
    @Override
    public void updateState(Task task, ActionType type) {
        log.info("TaskInitV2 updateState... taskId ={}",task.getTaskId());
        if (ActionType.START==type){
            task.setState(new TaskGoingV2());
            add(new TaskManageObserver());
            add(new ActivityObserver());
        }
    }
}
