package com.dum.design.strategy.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//环境类，持有一个策略类的引用，提供给客户端调用
@Service
@Slf4j
public class StrategyContext {
    //遍历容器中的策略，并注册到策略列表
    //该方法可以替换registerStrategy
/*    @Autowired
    List<Strategy> strategies;
    @PostConstruct
    public void setStategyMap(){
        for (Strategy strategy : strategies) {
            stategyMap.putIfAbsent(strategy.getClass().getSimpleName(),strategy);
        }
    }*/
    //存储策略列表
    private static final Map<String,Strategy> stategyMap = new HashMap<>(8);
    //获取策略
    public Strategy getStrategy(String type){
        if (stategyMap.isEmpty()){
            log.error("no Strategy register,please Register first");
        }
        return stategyMap.get(type);
    }
    //注册策略
    public static void registerStrategy(String type,Strategy strategy){
        stategyMap.putIfAbsent(type,strategy);
    }


}
