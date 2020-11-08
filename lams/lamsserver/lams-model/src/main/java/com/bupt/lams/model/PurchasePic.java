package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 订单图片
 */
@Data
public class PurchasePic {
    private Long id;
    private Long poid;
    private String name;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;

    public PurchasePic() {
    }

    public PurchasePic(Long poid, String name, String url, Date createTime) {
        this.poid = poid;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
    }
}
