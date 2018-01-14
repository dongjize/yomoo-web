package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.BreedingInfoDemand;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.service.BreedingInfoDemandService;
import com.yomoo.yomooweb.service.UserService;
import com.yomoo.yomooweb.utils.Constants;
import com.yomoo.yomooweb.utils.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 15:33
 */
@Controller
public class BreedingInfoDemandController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BreedingInfoDemandController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BreedingInfoDemandService breedingInfoDemandService;

    /**
     * 发布一条养殖技术信息 TODO 加base authentication
     *
     * @param title
     * @param content
     * @param publisherId
     * @param response
     */
    @RequestMapping(path = {"/post_breeding_info_demand"}, method = RequestMethod.POST)
    public void postBreedingInfoDemand(@RequestParam("title") String title,
                                       @RequestParam("content") String content,
                                       @RequestParam("publisher") long publisherId,
                                       HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            User publisher = userService.getUserById(publisherId);
            BreedingInfoDemand breedingInfoDemand = new BreedingInfoDemand(title, content, publisher);
            breedingInfoDemandService.publishBreedingInfoDemand(breedingInfoDemand);

            dataMap.put("breeding_info_demand", breedingInfoDemand);

            result = resultMapping(HttpStatusCode.SUCCESS, "发布成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }

    }


    /**
     * 获取养殖方法需求列表
     *
     * @param offset
     * @param response
     */
    @RequestMapping(path = {"/breeding_info_demand"}, method = {RequestMethod.GET})
    public void getBreedingInfoDemandList(@RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                          HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<BreedingInfoDemand> demandList = breedingInfoDemandService.getBreedingInfoDemandList(parsedOffset);
            dataMap.put("list", demandList);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }

    }


    /**
     * 获取某条养殖技术需求
     *
     * @param id
     * @param response
     */
    @RequestMapping(path = {"/breeding_info_demand/{id}"}, method = {RequestMethod.GET})
    public void getBreedingInfoDemand(@PathVariable("id") long id,
                                      HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            BreedingInfoDemand breedingInfoDemand = breedingInfoDemandService.getBreedingInfoDemand(id);
            dataMap.put("breeding_info_demand", breedingInfoDemand);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }

    }

}
