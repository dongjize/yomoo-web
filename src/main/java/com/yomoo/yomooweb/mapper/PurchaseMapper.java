package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.Purchase;
import com.yomoo.yomooweb.entity.PurchaseEntry;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 16:40
 */
@Mapper
@Repository
public interface PurchaseMapper {

    void insertPurchase(Purchase purchase);

    void insertPurchaseEntry(PurchaseEntry entry);

    List<Purchase> selectPurchasesByVendorId(long vendorId);

    Purchase selectPurchaseById(long id);

    List<PurchaseEntry> selectPurchaseEntriesByPurchaseId(long purchaseId);

    PurchaseEntry selectPurchaseEntryById(long id);
}
