package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.BreedingInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 养殖技术DAO
 */
@Mapper
@Repository
public interface BreedingInfoMapper {

    List<BreedingInfo> selectAll(int offset);

    BreedingInfo selectById(long id);

    void insert(BreedingInfo info);

}

