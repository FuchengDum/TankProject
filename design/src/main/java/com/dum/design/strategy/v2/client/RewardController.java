package com.dum.design.strategy.v2.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dum.design.strategy.v2.Strategy;
import com.dum.design.strategy.v2.StrategyContext;

import javax.annotation.Resource;

@RestController
@RequestMapping("/strategy")
public class RewardController {
    @Resource
    private StrategyContext strategyContext;
    @GetMapping("/type")
    public String issue(@RequestParam String name ){
        Strategy strategy = strategyContext.getStrategy(name);
        strategy.issue();
        return "success";
    }
}
