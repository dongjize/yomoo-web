package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.*;
import com.yomoo.yomooweb.service.FodderService;
import com.yomoo.yomooweb.service.PurchaseService;
import com.yomoo.yomooweb.service.UserService;
import com.yomoo.yomooweb.utils.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 20:05
 */
@Controller
public class PurchaseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private FodderService fodderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping(path = {"/vendor/{vender_id}/add_purchase"}, method = RequestMethod.POST)
    public void addPurchase(@PathVariable("vendor_id") long vendorId,
                            @RequestParam("fodder_name") String fodderName,
                            @RequestParam("fodder_spec") String fodderSpec,
                            @RequestParam("quantity") int quantity,
                            @RequestParam("purchase_price") float purchasePrice,
                            @RequestParam("sell_price") float sellPrice,
                            HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            Fodder fodder;
            if (fodderService.getFodderByNameSpec(fodderName, fodderSpec) == null) {
                fodder = new Fodder(fodderName, fodderSpec);
                fodderService.addFodder(fodder);
            } else {
                fodder = fodderService.getFodderByNameSpec(fodderName, fodderSpec);
            }

            FodderOfVendor fv;
            User vendor = userService.getUserById(vendorId);
            if (fodderService.getFodderOfVendorByFodderVendor(fodder, vendor) == null) {
                fv = new FodderOfVendor(fodder, vendor, sellPrice, quantity);
                fodderService.addFodderOfVendor(fv);
            }

            // 添加purchase
            Purchase purchase = new Purchase();

            PurchaseEntry entry = new PurchaseEntry(fodder, quantity, purchasePrice, sellPrice);
            List<PurchaseEntry> entries = new ArrayList<>();
            entries.add(entry); // TODO: 暂且每个Purchase只有一个Entry

            purchase.setPurchaseEntries(entries);
            purchase.setBuyer(vendor);
            purchase.setTips("");

            purchaseService.addPurchase(purchase);
            dataMap.put("purchase", purchase);
            result = resultMapping(HttpStatusCode.SUCCESS, "录入成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }
}
