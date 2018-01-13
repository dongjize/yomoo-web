package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.Fodder;
import com.yomoo.yomooweb.entity.FodderOfVendor;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.mapper.FodderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 17:05
 */
@Service
public class FodderService {

    @Autowired
    private FodderMapper fodderMapper;

    public Fodder getFodderById(long id) {
        return fodderMapper.selectFodderById(id);
    }

    public Fodder getFodderByNameSpec(String name, String spec) {
        return fodderMapper.selectFodderByNameSpec(name, spec);
    }

    public void addFodderOfVendor(FodderOfVendor fv) {
        fodderMapper.insertFodderOfVendor(fv);
    }

    public FodderOfVendor getFodderOfVendorByFodderVendor(Fodder fodder, User vendor) {
        return fodderMapper.selectFodderOfVendorByFodderAndVendor(fodder.getId(), vendor.getId());
    }

    public void addFodder(Fodder fodder) {
        fodderMapper.insertFodder(fodder);
    }

    public void selectFodderById(long id) {
        fodderMapper.selectFodderById(id);
    }

    public FodderOfVendor getFodderVendorById(long id) {
        return fodderMapper.selectFodderOfVendorById(id);
    }

    /**
     * 根据饲料id找出销售该饲料和销售商的关系
     *
     * @param fodderId
     * @return
     */
    public List<FodderOfVendor> getFodderVendorByFodderId(long fodderId, int offset) {
        return fodderMapper.selectFodderOfVendorByFodderId(fodderId, offset);
    }

    public List<FodderOfVendor> getFodderVendorByVendorId(long vendorId, int offset) {
        return fodderMapper.selectFodderOfVendorByVendorId(vendorId, offset);
    }

}
