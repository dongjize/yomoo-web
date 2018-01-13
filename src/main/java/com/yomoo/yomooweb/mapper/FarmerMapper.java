package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.Farmer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 养殖户DAO
 */
@Mapper
@Repository
public interface FarmerMapper {

    int insertFarmer(Farmer farmer);

    int updateFarmer(Farmer farmer);

    Farmer selectByPhone(String phone);

    Farmer selectById(long id);

}
