package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.Farmer;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.mapper.FarmerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 14:42
 */
@Service
public class FarmerService {
    @Autowired
    private FarmerMapper farmerMapper;

    public Farmer insertFarmer(User user) {
        Farmer farmer = new Farmer(user);
        farmerMapper.insertFarmer(farmer);
        return farmer;
    }

    public void updateFarmer(Farmer farmer) {
        farmerMapper.updateFarmer(farmer);
    }

    public Farmer getFarmerById(long id) {
        return farmerMapper.selectById(id);
    }

}
