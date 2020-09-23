package com.bupt.lams.service;

import com.bupt.lams.mapper.AssetInMapper;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 资产service
 */
@Service
public class AssetService {
    @Resource
    AssetMapper assetMapper;

    @Resource
    AssetInMapper assetInMapper;

    public RespPageBean getAssetByPage(Integer page, Integer size, Asset asset, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Asset> data = assetMapper.getAssetByPage(page, size, asset, beginDateScope);
        Long total = assetMapper.getTotal(asset, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public RespPageBean getAssetInByPage(Integer page, Integer size, AssetIn assetIn, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Asset> data = assetInMapper.getAssetInByPage(page, size, assetIn, beginDateScope);
        Long total = assetInMapper.getTotal(assetIn, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addAssetIn(AssetIn assetIn) {
        int result = assetInMapper.insertSelective(assetIn);
        return result;
    }

    public Asset selectByPrimaryKey(Long id){
        return assetMapper.selectByPrimaryKey(id);
    }
}
