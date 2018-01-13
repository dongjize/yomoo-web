package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.Purchase;
import com.yomoo.yomooweb.entity.PurchaseEntry;
import com.yomoo.yomooweb.mapper.FodderMapper;
import com.yomoo.yomooweb.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 20:24
 */
@Service
public class PurchaseService {
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private FodderMapper fodderMapper;

    public void addPurchase(Purchase purchase) {
        purchaseMapper.insertPurchase(purchase);
        PurchaseEntry entry = purchase.getPurchaseEntries().get(0);
        purchaseMapper.insertPurchaseEntry(entry);
        fodderMapper.updateFodderOfVendorAfterPurchase(entry);
    }

    public List<Purchase> getPurchasesByVendorId(long vendorId) {
        return purchaseMapper.selectPurchasesByVendorId(vendorId);
    }
}