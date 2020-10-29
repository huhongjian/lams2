package com.bupt.lams.service;

import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.model.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderAssetService {
    @Resource
    OrderAssetMapper orderAssetMapper;

    @Resource
    OrderService orderService;

    public Order getLatestOrderByAid(Long aid) {
        Long oid = orderAssetMapper.getLatestOidByAid(aid);
        return orderService.selectBaseOrderInfoById(oid);
    }
}
