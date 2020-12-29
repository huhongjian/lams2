package com.bupt.lams.dto;

import com.bupt.lams.model.Asset;
import lombok.Data;

@Data
public class AddAssetData {
    Asset asset;
    Integer amount;
    Long oid;
}
