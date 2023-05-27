package com.dum.design.strategy;

import javax.annotation.Resource;

// 奖励服务
//耦合度高，添加一个外部服务时，就需要加一层判断
//设计模式优化：策略模式、适配器模式
public class RewardService {
    // 外部服务
    @Resource
    private WaimaiService waimaiService;
    @Resource
    private HotelService hotelService;
    @Resource
    private FoodService foodService;
    // 使用对入参的条件判断进行发奖
    public void issueReward(String rewardType, Object ... params) {
        if ("Waimai".equals(rewardType)) {
            //WaimaiRequest request = new WaimaiRequest();
            // 构建入参
            //request.setWaimaiReq(params);
            waimaiService.issueWaimai();
        } else if ("Hotel".equals(rewardType)) {
            //HotelRequest request = new HotelRequest();
            //request.addHotelReq(params);
            hotelService.sendPrize();
        } else if ("Food".equals(rewardType)) {
            //FoodRequest request = new FoodRequest(params);
            foodService.getCoupon();
        } else {
            throw new IllegalArgumentException("rewardType error!");
        }
    }
}
