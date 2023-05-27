package com.dum.design.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HotelService {

    public void sendPrize(){
        log.info("sendPrize");
    }
}
