package com.dum.design.observer.v1_state;

import com.dum.design.observer.ActionType;

//状态抽象
public interface State {
    void updateState(Task task,ActionType type);
}
