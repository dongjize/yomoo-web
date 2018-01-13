package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.BreedingInfoDemand;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2017-12-28
 * @Time: 22:04
 */
@Mapper
@Repository
public interface BreedingInfoDemandMapper {

    List<BreedingInfoDemand> selectAll(int offset);

    BreedingInfoDemand selectById(Long id);

    void insert(BreedingInfoDemand demand);
}
