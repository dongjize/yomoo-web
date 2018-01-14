package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.*;
import com.yomoo.yomooweb.service.FarmerService;
import com.yomoo.yomooweb.service.FodderService;
import com.yomoo.yomooweb.service.OrderService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 19:59
 */
@Controller
public class OrderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private FodderService fodderService;

    @Autowired
    private FarmerService farmerService;


    /**
     * 养殖户下订单
     *
     * @param farmerId
     * @param fvId
     * @param quantity
     * @param orderType
     * @param response
     */
    @RequestMapping(path = {"/farmer/order_fodder"}, method = RequestMethod.POST)
    public void postFodderOrder(@RequestParam("farmer_id") long farmerId,
                                @RequestParam("fv_id") long fvId,
                                @RequestParam("quantity") int quantity,
                                @RequestParam("order_type") String orderType,
                                HttpServletResponse response) {
        String result = "";
        Map<String, Object> dataMap = new HashMap<>();
        try {
            FodderOfVendor fv = fodderService.getFodderVendorById(fvId);
            if (fv.getStock() < quantity) {
                result = resultMapping(HttpStatusCode.SERVER_ERROR, "库存不足", dataMap);
                printResult(response, result);
                return;
            }
            User vendor = fv.getVendor();
            Farmer farmer = farmerService.getFarmerById(farmerId);

            Order order = new Order();
            order.setBuyer(farmer);
            order.setVendor(vendor);
            order.setOrderType(orderType);
            order.setTips("");
            orderService.addOrder(order);

            List<OrderEntry> orderEntries = new ArrayList<>();
            OrderEntry entry = new OrderEntry();
            entry.setOrderId(order.getId());
            entry.setQuantity(quantity);
            entry.setFv(fv);
            entry.setSellPrice(fv.getSellPrice());
            orderEntries.add(entry); // TODO: 暂且每个Order只有一个Entry
            orderService.addOrderEntry(fv.getId(), entry);

            order.setOrderEntries(orderEntries);

            dataMap.put("order", order);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }


    /**
     * 养殖户查看订单列表
     *
     * @param farmerId
     * @param offset
     * @param response
     */
    @RequestMapping(path = {"/farmer/orders"}, method = RequestMethod.GET)
    public void farmerOrderAccountInfo(@RequestParam("farmer_id") long farmerId,
                                       @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                       HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<Order> orders = orderService.getAllOrdersByFarmer(farmerId, parsedOffset);
            dataMap.put("list", orders);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "查询成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }


    /**
     * 供应商查看订单列表
     *
     * @param vendorId
     * @param offset
     * @param response
     */
    @RequestMapping(path = {"/vendor/orders"}, method = RequestMethod.GET)
    public void vendorOrderAccountInfo(@RequestParam("vendor_id") long vendorId,
                                       @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                       HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<Order> orders = orderService.getAllOrdersByVendor(vendorId, parsedOffset);
            dataMap.put("list", orders);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "查询成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }


}
