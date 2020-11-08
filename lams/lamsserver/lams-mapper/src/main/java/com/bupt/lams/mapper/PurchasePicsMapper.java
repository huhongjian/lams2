package com.bupt.lams.mapper;

import com.bupt.lams.model.PurchasePic;

public interface PurchasePicsMapper {
    void deletePurchasePicById(Long id);

    void insertPurchasePics(PurchasePic pics);

    PurchasePic selectByPrimaryKey(Long id);
}
