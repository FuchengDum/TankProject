package com.dum.design.strategy.v2;

import com.dum.design.strategy.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class Hotel extends AbstractStrategy implements Strategy{
    @Resource
    private HotelService hotelService;
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
