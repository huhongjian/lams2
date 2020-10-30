package com.bupt.lams.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 资产图片
 */
@Data
public class AssetPic {
    private Long id;
    private Long aid;
    private String name;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;

    public AssetPic() {
    }

    public AssetPic(Long aid, String name, String url, Date createTime) {
        this.aid = aid;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
    }
}
