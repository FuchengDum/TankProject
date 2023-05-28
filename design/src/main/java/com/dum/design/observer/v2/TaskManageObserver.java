package com.dum.design.observer.v2;

import com.dum.design.observer.ActivityService;
import com.dum.design.observer.TaskManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskManageObserver implements Observer{
    private final TaskManager taskManager = new TaskManager();

    @Override
    public void response(Long taskId) {
      log.info("{} response taskId={}",this.getClass().getSimpleName(),taskId);
      taskManager.release(taskId);
    }
}
