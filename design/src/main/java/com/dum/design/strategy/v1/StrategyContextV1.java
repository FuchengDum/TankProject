package com.dum.design.strategy.v1;

import lombok.extern.slf4j.Slf4j;
import com.dum.design.strategy.v1.personalService.Food;
import com.dum.design.strategy.v1.personalService.Hotel;
import com.dum.design.strategy.v1.personalService.Waimai;

/**
 * 策略上下文，用于给客户端使用
 * v1:一定程度上降低了耦合，使用了单例的方式。同时使用了适配器模式，将一个类的接口转化成客户希望的另外一个接口
 * 问题：但是每增加一种个性化服务，就需要在策略上下文中进行维护
 * 方案：通过每个个性化服务自注册，解决分支的判断
 */
@Slf4j
public class StrategyContextV1{
    public static RewardStrategy getStrategy(String type){
        switch (type) {
            case "waimai":
                log.info("waimai service");
                return Waimai.getInstance();
            case "hotel":
                log.info("hotel service");
                return new Hotel();
            case "food":
                log.info("food service");
                return new Food();
            default:
                throw new IllegalArgumentException("非法入参类型");
        }
    }
}
