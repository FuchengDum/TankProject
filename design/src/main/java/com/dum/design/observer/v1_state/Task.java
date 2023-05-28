package com.dum.design.observer.v1_state;

import com.dum.design.observer.ActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Task {
    private Long taskId;
    //初始化为初始态
    private State state = new TaskInit();
    //更新状态
    public void updateState(ActionType actionType){
        state.updateState(this,actionType);
    }
}
