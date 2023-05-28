package com.dum.design.observer.v2;

import com.dum.design.observer.ActionType;
import com.dum.design.observer.v1_state.*;
import lombok.extern.slf4j.Slf4j;
//任务进行状态
@Slf4j
public class TaskGoingV2 extends Subject implements State {
    @Override
    public void updateState(Task task, ActionType type) {
        log.info("{} updateState taskId={}",this.getClass().getSimpleName(),task.getTaskId());
        if (type == ActionType.ACHIEVE) {
            task.setState(new TaskFinish());
            // 任务完成后进对外部服务进行通知
            notifyMsgAll(task.getTaskId());
        } else if (type == ActionType.STOP) {
            task.setState(new TaskPause());
        } else if (type == ActionType.EXPIRE) {
            task.setState(new TaskExpire());
        }

    }
}
