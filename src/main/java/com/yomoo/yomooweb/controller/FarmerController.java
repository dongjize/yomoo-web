package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.Farmer;
import com.yomoo.yomooweb.entity.Order;
import com.yomoo.yomooweb.service.FarmerService;
import com.yomoo.yomooweb.utils.Constants;
import com.yomoo.yomooweb.utils.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 19:22
 */
@Controller
public class FarmerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FarmerController.class);

    @Autowired
    private FarmerService farmerService;

    /**
     * 养殖户个人详情
     *
     * @param id
     * @param response
     */
    @RequestMapping(path = {"/farmer/{id}"}, method = RequestMethod.GET)
    public void farmerInfo(@PathVariable("id") long id,
                           HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            Farmer farmer = farmerService.getFarmerById(id);
            dataMap.put("farmer", farmer);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }
}
