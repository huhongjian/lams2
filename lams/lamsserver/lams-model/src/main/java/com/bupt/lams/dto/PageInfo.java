package com.bupt.lams.dto;

import lombok.Data;

@Data
public class PageInfo {
    private Long pageNo;

    private Long pageSize;

    private Long begin;

    private Long recordCount;
}
