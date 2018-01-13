package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.BreedingInfo;
import com.yomoo.yomooweb.entity.BreedingInfoDemand;
import com.yomoo.yomooweb.mapper.BreedingInfoDemandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 15:33
 */
@Service
public class BreedingInfoDemandService {

    @Autowired
    private BreedingInfoDemandMapper breedingInfoDemandMapper;

    public BreedingInfoDemand publishBreedingInfoDemand(BreedingInfoDemand demand) {
        breedingInfoDemandMapper.insert(demand);
        return demand;
    }

    public List<BreedingInfoDemand> getBreedingInfoDemandList(int offset) {
        return breedingInfoDemandMapper.selectAll(offset);
    }

    public BreedingInfoDemand getBreedingInfoDemand(long id) {
        return breedingInfoDemandMapper.selectById(id);
    }


}
