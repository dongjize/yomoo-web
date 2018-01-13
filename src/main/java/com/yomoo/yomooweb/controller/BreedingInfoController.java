package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.BreedingInfo;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.service.BreedingInfoService;
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
 * Description: 养殖方法Controller
 *
 * @Author: dong
 * @Date: 2017-12-25
 * @Time: 21:51
 */
@Controller
public class BreedingInfoController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BreedingInfoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BreedingInfoService breedingInfoService;

    /**
     * 发布一条养殖技术信息 TODO 加base authentication
     *
     * @param title
     * @param content
     * @param publisherId
     * @param response
     */
    @RequestMapping(path = {"/post_breeding_info"}, method = RequestMethod.POST)
    public void postBreedingInfo(@RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("publisher") long publisherId,
                                 HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            User publisher = userService.getUserById(publisherId);
            BreedingInfo breedingInfo = new BreedingInfo(title, content, publisher);
            breedingInfoService.publishBreedingInfo(breedingInfo);

            dataMap.put("breeding_info", breedingInfo);

            result = resultMapping(HttpStatusCode.SUCCESS, "发布成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }

    /**
     * 获取养殖方法列表
     *
     * @param offset
     * @param response
     */
    @RequestMapping(path = {"/breeding_info_list"}, method = {RequestMethod.GET})
    public void getBreedingInfoList(@RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                    HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<BreedingInfo> infoList = breedingInfoService.getBreedingInfoList(parsedOffset);
            dataMap.put("list", infoList);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }

    }


    @RequestMapping(path = {"/breeding_info/{id}"}, method = {RequestMethod.GET})
    public void getBreedingInfo(@PathVariable("id") long id,
                                    HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            BreedingInfo breedingInfo = breedingInfoService.getBreedingInfo(id);
            dataMap.put("breeding_info", breedingInfo);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }

    }
}
