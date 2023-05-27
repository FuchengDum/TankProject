package com.dum.design.strategy.v2;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//策略抽象接口
@Component
@Order(1)
public interface Strategy {
    //具体策略执行方法
    void issue();
}
