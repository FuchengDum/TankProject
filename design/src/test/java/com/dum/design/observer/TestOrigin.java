package com.dum.design.observer;

import com.dum.design.observer.v1_state.TaskInit;
import com.dum.design.observer.v2.TaskInitV2;
import org.junit.jupiter.api.Test;

public class TestOrigin {
    @Test
    public void observer0(){
        Task task = new Task();
        task.updateState(ActionType.START);
    }

    @Test
    public void observer1(){
        com.dum.design.observer.v1_state.Task task = new com.dum.design.observer.v1_state.Task(0L,new TaskInit());
        task.updateState(ActionType.START);
        task.updateState(ActionType.ACHIEVE);
    }

    @Test
    public void observer2(){
        com.dum.design.observer.v1_state.Task task = new com.dum.design.observer.v1_state.Task(0L,new TaskInitV2());
        task.updateState(ActionType.START);
        task.updateState(ActionType.ACHIEVE);
    }
}
