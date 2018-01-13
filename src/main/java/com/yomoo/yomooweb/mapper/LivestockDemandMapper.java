package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.LivestockDemand;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 15:21
 */
@Mapper
@Repository
public interface LivestockDemandMapper {

    void insert(LivestockDemand demand);

    List<LivestockDemand> selectAll(int offset);

    LivestockDemand selectById(Long id);
}
