package com.dum.design.strategy.v1.personalService;

import com.dum.design.strategy.WaimaiService;
import com.dum.design.strategy.v1.RewardStrategy;
import lombok.extern.slf4j.Slf4j;

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
