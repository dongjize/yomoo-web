package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.BreedingInfo;
import com.yomoo.yomooweb.mapper.BreedingInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 15:07
 */
@Service
public class BreedingInfoService {

    @Autowired
    private BreedingInfoMapper breedingInfoMapper;

    public void publishBreedingInfo(BreedingInfo breedingInfo) {
        breedingInfoMapper.insert(breedingInfo);
    }

    public List<BreedingInfo> getBreedingInfoList(int offset) {
        return breedingInfoMapper.selectAll(offset);
    }

    public BreedingInfo getBreedingInfo(long id) {
        return breedingInfoMapper.selectById(id);
    }
}
