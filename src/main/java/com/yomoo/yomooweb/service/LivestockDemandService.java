package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.BreedingInfo;
import com.yomoo.yomooweb.entity.LivestockDemand;
import com.yomoo.yomooweb.mapper.LivestockDemandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 15:44
 */
@Service
public class LivestockDemandService {
    @Autowired
    private LivestockDemandMapper livestockDemandMapper;

    public void publishLivestockDemand(LivestockDemand livestockDemand) {
        livestockDemandMapper.insert(livestockDemand);
    }

    public List<LivestockDemand> getLivestockDemandList(int offset) {
        return livestockDemandMapper.selectAll(offset);
    }

    public LivestockDemand getLivestockDemand(long id) {
        return livestockDemandMapper.selectById(id);
    }

}
