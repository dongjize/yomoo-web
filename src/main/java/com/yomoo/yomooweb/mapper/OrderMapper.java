package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.Order;
import com.yomoo.yomooweb.entity.OrderEntry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 19:26
 */
@Mapper
@Repository
public interface OrderMapper {

    void insertOrder(Order order);

    List<Order> selectOrdersByFarmerId(@Param("farmerId") Long farmerId, @Param("offset") int offset);

    List<Order> selectOrdersByVendorId(@Param("vendorId") Long vendorId, @Param("offset") int offset);


    void insertOrderEntry(OrderEntry orderEntry);

    List<OrderEntry> selectOrderEntriesByOrderId(long orderId);

}
