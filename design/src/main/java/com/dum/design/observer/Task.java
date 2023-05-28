package com.dum.design.observer;

//问题：任务类不够高类聚，耦合度太高
//条件分支判断太多，处理逻辑太多，新增一种状态时，就会带来新的问题
//解决：1、使用状态模式，处理复杂的条件分支,处理状态流转的控制;2、使用观察者模式实现任务完成时通知
public class Task {
    private Long taskId;
    // 任务的默认状态为初始化
    private TaskState state = TaskState.INIT;
    // 活动服务
    private ActivityService activityService =new ActivityService();
    // 任务管理器
    private TaskManager taskManager = new TaskManager();
    // 使用条件分支进行任务更新
    public void updateState(ActionType actionType) {
        if (state == TaskState.INIT) {
            if (actionType == ActionType.START) {
                state = TaskState.ONGOING;
            }
        } else if (state == TaskState.ONGOING) {
            if (actionType == ActionType.ACHIEVE) {
                state = TaskState.FINISHED;
                // 任务完成后进对外部服务进行通知
                activityService.notifyFinished(taskId);
                taskManager.release(taskId);
            } else if (actionType == ActionType.STOP) {
                state = TaskState.PAUSED;
            } else if (actionType == ActionType.EXPIRE) {
                state = TaskState.EXPIRED;
            }
        } else if (state == TaskState.PAUSED) {
            if (actionType == ActionType.START) {
                state = TaskState.ONGOING;
            } else if (actionType == ActionType.EXPIRE) {
                state = TaskState.EXPIRED;
            }
        }
    }
}
