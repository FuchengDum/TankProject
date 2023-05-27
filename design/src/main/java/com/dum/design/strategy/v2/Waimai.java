package com.dum.design.strategy.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.dum.design.strategy.WaimaiService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
//具体酒店服务策略
@Service
@Slf4j
public class Waimai extends AbstractStrategy implements Strategy{
    @Resource
    private  WaimaiService waimaiService;
    @PostConstruct
    private void init(){
        register();
    }
    @Override
    public void issue() {
        log.info("Waimai Strategy issue");
        waimaiService.issueWaimai();
    }
}
