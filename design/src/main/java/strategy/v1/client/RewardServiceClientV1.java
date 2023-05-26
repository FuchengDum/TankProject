package strategy.v1.client;


import strategy.v1.RewardStrategy;
import strategy.v1.StrategyContextV1;

public class RewardServiceClientV1 {

    public static void main(String[] args) {
        RewardStrategy strategy = StrategyContextV1.getStrategy("food");
        strategy.issue();
    }
}
