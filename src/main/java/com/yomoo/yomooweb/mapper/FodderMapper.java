package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.Fodder;
import com.yomoo.yomooweb.entity.FodderOfVendor;
import com.yomoo.yomooweb.entity.OrderEntry;
import com.yomoo.yomooweb.entity.PurchaseEntry;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 16:57
 */
@Mapper
@Repository
public interface FodderMapper {

    void insertFodder(Fodder fodder);

    void insertFodderOfVendor(FodderOfVendor fv);

    void updateFodderOfVendorAfterPurchase(@Param("id") Long id, @Param("quantity") int quantity);

    void updateFodderOfVendorAfterSell(@Param("id") Long id, @Param("quantity") int quantity);

    Fodder selectFodderById(long id);

    Fodder selectFodderByNameSpec(@Param("name") String name, @Param("fodderSpec") String fodderSpec);

    FodderOfVendor selectFodderOfVendorByFodderAndVendor(@Param("fodderId") long fodderId, @Param("vendorId") long vendorId);

    FodderOfVendor selectFodderOfVendorById(long id);

    List<FodderOfVendor> selectFodderOfVendorByFodderId(@Param("fodderId") long fodderId, @Param("offset") int offset);

    List<FodderOfVendor> selectFodderOfVendorByVendorId(@Param("vendorId") long vendorId, @Param("offset") int offset);

}
