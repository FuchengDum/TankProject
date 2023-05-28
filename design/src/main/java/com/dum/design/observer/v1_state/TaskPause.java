package com.dum.design.observer.v1_state;

import com.dum.design.observer.ActionType;

// 任务暂停状态
public class TaskPause implements State {
    @Override
    public void updateState(Task task, ActionType type) {
        if (type == ActionType.START) {
            task.setState(new TaskGoing());
        } else if (type == ActionType.EXPIRE) {
            task.setState(new TaskExpire());
        }
    }
}
