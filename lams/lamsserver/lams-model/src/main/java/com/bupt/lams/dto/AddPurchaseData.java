package com.bupt.lams.dto;

import com.bupt.lams.model.PurchaseOrder;
import lombok.Data;

import java.util.List;

@Data
public class AddPurchaseData {
    PurchaseOrder purchaseOrder;
    List<Long> aids;
}
