package com.dum.design.observer.v1_state;

import com.dum.design.observer.ActionType;
import com.dum.design.observer.ActivityService;
import com.dum.design.observer.TaskManager;
import lombok.extern.slf4j.Slf4j;

// 任务进行状态
@Slf4j
public class TaskGoing implements State {
    private final ActivityService activityService =new ActivityService();
    // 任务管理器
    private final TaskManager taskManager = new TaskManager();

    @Override
    public void updateState(Task task, ActionType type) {
        log.info("{} updateState taskId={}",this.getClass().getSimpleName(),task.getTaskId());
        if (type == ActionType.ACHIEVE) {
            task.setState(new TaskFinish());
            // 任务完成后进对外部服务进行通知
            activityService.notifyFinished(task.getTaskId());
            taskManager.release(task.getTaskId());
        } else if (type == ActionType.STOP) {
            task.setState(new TaskPause());
        } else if (type == ActionType.EXPIRE) {
            task.setState(new TaskExpire());
        }
    }
}
