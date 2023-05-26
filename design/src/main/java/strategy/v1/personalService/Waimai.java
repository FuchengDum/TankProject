package strategy.v1.personalService;

import lombok.extern.slf4j.Slf4j;
import strategy.WaimaiService;
import strategy.v1.RewardStrategy;

/**
 * 外卖发送策略
 */
@Slf4j
public class Waimai implements RewardStrategy {
    private static final Waimai instance = new Waimai();

    private WaimaiService waimaiService = new WaimaiService();

    public static Waimai getInstance(){
        log.info("getInstance");
        return instance;
    }

    @Override
    public void issue() {
        waimaiService.issueWaimai();
    }
}
