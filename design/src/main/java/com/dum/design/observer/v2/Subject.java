package com.dum.design.observer.v2;

import java.util.ArrayList;
import java.util.List;

//抽象主题角色，也叫抽象目标 包含观察者列表，以及新增、删除观察者方法，以及通知所有观察者的抽象方法
public abstract class Subject {
    protected static final List<Observer>observers = new ArrayList<>();
    //添加观察者
    public void add(Observer observer){
        observers.add(observer);
    }
    //移除观察者
    public void remove(Observer observer){
        observers.remove(observer);
    }
    //通知所有观察者
    public void notifyMsgAll(Long taskId){
        for (Observer observer:observers){
            observer.response(taskId);
        }
    }
}
