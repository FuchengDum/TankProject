package strategy.v1.personalService;

import lombok.extern.slf4j.Slf4j;
import strategy.FoodService;
import strategy.v1.RewardStrategy;

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
