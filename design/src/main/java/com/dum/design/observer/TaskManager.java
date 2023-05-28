package com.dum.design.observer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskManager {
    public void release(Long taskId) {
        log.info("TaskManager release taskId={}",taskId);
    }
}
