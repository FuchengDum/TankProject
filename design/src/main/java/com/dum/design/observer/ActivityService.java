package com.dum.design.observer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivityService {
    public void notifyFinished(Long taskId) {
        log.info("ActivityService notifyFinished taskId={}",taskId);
    }
}
