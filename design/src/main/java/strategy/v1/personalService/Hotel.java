package strategy.v1.personalService;

import lombok.extern.slf4j.Slf4j;
import strategy.HotelService;
import strategy.v1.RewardStrategy;

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
