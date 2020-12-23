package com.bupt.lams.dto;

import com.bupt.lams.model.Asset;
import lombok.Data;

@Data
public class DeleteAssetData {
    Asset asset;
    Long oid;
}
