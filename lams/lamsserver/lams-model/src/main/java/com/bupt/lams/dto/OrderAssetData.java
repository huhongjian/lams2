package com.bupt.lams.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderAssetData {
    List<Long> assetIds;
    Long oid;
}
