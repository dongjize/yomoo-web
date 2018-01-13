package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.FodderOfVendor;
import com.yomoo.yomooweb.entity.Purchase;
import com.yomoo.yomooweb.entity.PurchaseEntry;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.mapper.FodderMapper;
import com.yomoo.yomooweb.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    }

    public void addPurchaseEntry(PurchaseEntry entry, User vendor) {
        purchaseMapper.insertPurchaseEntry(entry);
        Long fodderId = entry.getFodder().getId();
        FodderOfVendor fv;
        // 如果没有则插入新数据；之前有记录则更新数据
        if (fodderMapper.selectFodderOfVendorByFodderAndVendor(fodderId, vendor.getId()) == null) {
            fv = new FodderOfVendor(entry.getFodder(), vendor, entry.getSellPrice(), entry.getQuantity());
            fodderMapper.insertFodderOfVendor(fv);
        } else {
            fv = fodderMapper.selectFodderOfVendorByFodderAndVendor(fodderId, vendor.getId());
            fodderMapper.updateFodderOfVendorAfterPurchase(fv.getId());
        }
    }

    public List<Purchase> getPurchasesByVendorId(long vendorId) {
        return purchaseMapper.selectPurchasesByVendorId(vendorId);
    }
}