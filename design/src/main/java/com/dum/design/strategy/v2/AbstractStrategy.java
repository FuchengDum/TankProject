package com.dum.design.strategy.v2;

public abstract class AbstractStrategy implements Strategy{
    //策略类注册方法
    public void register(){
        StrategyContext.registerStrategy(getClass().getSimpleName(),this);
    }
}
