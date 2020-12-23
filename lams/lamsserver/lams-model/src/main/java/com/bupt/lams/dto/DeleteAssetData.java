package com.bupt.lams.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteAssetData {
    List<Long> assetIds;
    Long oid;
}
