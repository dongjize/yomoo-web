package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.*;
import com.yomoo.yomooweb.service.FodderService;
import com.yomoo.yomooweb.service.PurchaseService;
import com.yomoo.yomooweb.service.UserService;
import com.yomoo.yomooweb.utils.Constants;
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
import javax.websocket.server.PathParam;
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

    @RequestMapping(path = {"vendor/add_purchase"}, method = RequestMethod.POST)
    public void addPurchase(@RequestParam("vendor_id") Long vendorId,
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
            purchase.setBuyer(vendor);
            purchase.setTips("");
            purchaseService.addPurchase(purchase);

            PurchaseEntry entry = new PurchaseEntry(fodder, quantity, purchasePrice, sellPrice);
            entry.setPurchaseId(purchase.getId());
            List<PurchaseEntry> entries = new ArrayList<>();
            entries.add(entry); // TODO: 暂且每个Purchase只有一个Entry
            purchaseService.addPurchaseEntry(entry, vendor);

            purchase.setPurchaseEntries(entries);

            dataMap.put("purchase", purchase);
            result = resultMapping(HttpStatusCode.SUCCESS, "录入成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }


    @RequestMapping(path = {"vendor/purchase_list"}, method = RequestMethod.GET)
    public void getPurchaseList(@RequestParam("vendor_id") Long vendorId,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<Purchase> purchaseList = purchaseService.getPurchasesByVendorId(vendorId);
            dataMap.put("list", purchaseList);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "录入成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);

        }
    }


}
