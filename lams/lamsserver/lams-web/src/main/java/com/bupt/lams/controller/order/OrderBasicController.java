package com.bupt.lams.controller.order;

import com.bupt.lams.config.FastDFSUtils;
import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.AssetPic;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.service.OrderService;
import com.bupt.lams.utils.POIUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工单信息相关
 */
@RestController
@RequestMapping("/order/basic")
public class OrderBasicController {

    private Logger logger = LoggerFactory.getLogger(OrderBasicController.class);

    @Resource
    OrderService orderService;

    @Resource
    AssetService assetService;

    @Value("${fastdfs.nginx.host}")
    String nginxHost;

    @GetMapping("/get")
    public RespPageBean getOrderByPage(OrderQueryCondition orderQueryCondition, Date[] dateScope) {
        // 维修中和报废的资产不在工单中展示
        List<Integer> assetStatus = new ArrayList<>();
        assetStatus.add(AssetStatusEnum.CREATE.getIndex());
        assetStatus.add(AssetStatusEnum.FREE.getIndex());
        assetStatus.add(AssetStatusEnum.INUSE.getIndex());
        orderQueryCondition.setAssetStatuses(assetStatus);
        if (dateScope != null && dateScope.length == 2) {
            orderQueryCondition.setStartDate(dateScope[0]);
            orderQueryCondition.setEndDate(dateScope[1]);
        }
        return orderService.getOrderByCondition(orderQueryCondition);
    }

    @PostMapping("/add")
    public RespBean addAsset(@RequestBody Order order) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("资产申请采购成功!");
        try {
            orderService.addOrderIn(order);
            response.setObj(order.getAsset().getId());
        } catch (Exception e) {
            logger.error("新增资产失败", e);
            return RespBean.error("资产申请采购失败！");
        }
        return response;
    }

    @PostMapping("/borrow")
    public RespBean borrowAsset(@RequestBody Order order) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            orderService.borrowAsset(order);
        } catch (Exception e) {
            logger.error("资产借出异常！", e);
            response.setStatus(500);
            response.setMsg("资产借出异常，请稍后重试");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updateOrder(@RequestBody Order order) {
        // 工单只允许管理员修改
        if (UserInfoUtils.isAdmin() == false) {
            return RespBean.error("您没有编辑权限，请联系管理员!");
        }
        try {
            orderService.updateOrder(order);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @DeleteMapping("/delete")
    public RespBean deleteOrders(@RequestBody List<Integer> oids) {
        // 工单只允许管理员删除
        if (UserInfoUtils.isAdmin() == false) {
            return RespBean.error("您没有删除权限，请联系管理员!");
        }
        try {
            orderService.deleteOrders(oids);
        } catch (Exception e) {
            return RespBean.error("删除失败!");
        }
        return RespBean.ok("删除成功!");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(OrderQueryCondition orderQueryCondition) {
        List<Order> list = (List<Order>) orderService.getOrderByCondition(orderQueryCondition).getData();
        return POIUtils.assetIn2Excel(list);
    }

    @PostMapping("/pic/add")
    public RespBean addAssetPics(MultipartFile file, Long aid) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("更新资产图片成功!");
        try {
            String fileId = FastDFSUtils.upload(file);
            String url = nginxHost + fileId;
            AssetPic pic = new AssetPic(aid, file.getName(), url, new Date());
            assetService.insertAssetPics(pic);
            return response;
        } catch (Exception e) {
            logger.error("更新资产图片失败！", e);
            return RespBean.error("更新资产图片失败!");
        }
    }

    @DeleteMapping("/pic/remove")
    public RespBean removeAssetPics(Long pid) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("删除资产图片成功!");
        try {
            assetService.deleteAssetPicById(pid);
            return response;
        } catch (Exception e) {
            logger.error("删除资产图片失败！", e);
            return RespBean.error("删除资产图片失败!");
        }
    }
}
