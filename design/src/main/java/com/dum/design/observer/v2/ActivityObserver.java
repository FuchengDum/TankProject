package com.dum.design.observer.v2;

import com.dum.design.observer.ActivityService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivityObserver implements Observer{
    private final ActivityService activityService = new ActivityService();
    @Override
    public void response(Long taskId) {
      log.info("{} response taskId={}",this.getClass().getSimpleName(),taskId);
      activityService.notifyFinished(taskId);
    }
}
