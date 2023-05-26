package com.dum.design;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import strategy.v1.RewardStrategy;

import javax.annotation.Resource;

@Component
public class CustomerRunner implements ApplicationRunner {
    /*@Resource
    private DesignApplication.StrategyContext strategyContext;*/

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //RewardStrategy strategy = strategyContext.getStrategy("waimai");
        //strategy.issue();
    }
}
