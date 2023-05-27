package com.dum.design.strategy.v1.personalService;

import com.dum.design.strategy.v1.RewardStrategy;
import lombok.extern.slf4j.Slf4j;
import com.dum.design.strategy.FoodService;

/**
 * 美食发送策略
 */
@Slf4j
public class Food implements RewardStrategy {
    private static final Food food = new Food();

    public static Food getFood() {
        log.info("getFood");
        return food;
    }

    private FoodService foodService = new FoodService();

    @Override
    public void issue() {
        log.info("Food getCoupon");
        foodService.getCoupon();
    }
}
