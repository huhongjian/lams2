package com.bupt.lams.model;

import lombok.Data;

import java.util.List;

/**
 * 分页返回体
 */
@Data
public class RespPageBean {
    private Long total;
    private List<?> data;
}
