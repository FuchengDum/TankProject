package com.dum.design.strategy.v2;

import com.dum.design.strategy.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

//具体酒店服务策略
@Component
@Slf4j
public class Hotel extends AbstractStrategy implements Strategy{
    @Resource
    private HotelService hotelService;
    //Hotel服务类注册到容器后，初始化并注册服务到策略列表
    @PostConstruct
    private void init(){
        register();
    }

    @Override
    public void issue() {
        log.info("Hotel Strategy issue");
        hotelService.sendPrize();
    }
}
