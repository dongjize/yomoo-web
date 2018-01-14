package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.Farmer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    List<Farmer> selectFarmersByKeyword(@Param("keyword") String keyword, @Param("offset") int offset);
}
