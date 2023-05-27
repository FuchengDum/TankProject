package com.dum.design.strategy.v1.personalService;

import com.dum.design.strategy.HotelService;
import com.dum.design.strategy.v1.RewardStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * 酒店发送策略
 */
@Slf4j
public class Hotel implements RewardStrategy {
    private static final Hotel hotel = new Hotel();

    public static Hotel getHotel() {
        log.info("getHotel");
        return hotel;
    }

    private HotelService hotelService = new HotelService();

    @Override
    public void issue() {
        log.info("Hotel sendPrize");
        hotelService.sendPrize();
    }
}
