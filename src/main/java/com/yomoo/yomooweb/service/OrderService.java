package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.FodderOfVendor;
import com.yomoo.yomooweb.entity.Order;
import com.yomoo.yomooweb.entity.OrderEntry;
import com.yomoo.yomooweb.mapper.FodderMapper;
import com.yomoo.yomooweb.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 20:01
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FodderMapper fodderMapper;

    public void addOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public void addOrderEntry(Long fvId, OrderEntry entry) {
        orderMapper.insertOrderEntry(entry);
        fodderMapper.updateFodderOfVendorAfterSell(fvId, entry.getQuantity());
    }

    public List<Order> getAllOrdersByFarmer(long farmerId, int offset) {
        List<Order> orders = orderMapper.selectOrdersByFarmerId(farmerId, offset);
//        for (Order order : orders) {
//            List<OrderEntry> entries = order.getOrderEntries();
//            float totalPrice = 0;
//            for (OrderEntry entry : entries) {
//                totalPrice += entry.getSellPrice() * entry.getQuantity();
//            }
//            order.setTotalPrice(totalPrice);
//        }
        return orders;
    }

    public List<Order> getAllOrdersByVendor(long vendorId, int offset) {
        List<Order> orders = orderMapper.selectOrdersByVendorId(vendorId, offset);
//        for (Order order : orders) {
//            List<OrderEntry> entries = order.getOrderEntries();
//            float totalPrice = 0;
//            for (OrderEntry entry : entries) {
//                totalPrice += entry.getSellPrice() * entry.getQuantity();
//            }
//            order.setTotalPrice(totalPrice);
//        }
        return orders;
    }

}
