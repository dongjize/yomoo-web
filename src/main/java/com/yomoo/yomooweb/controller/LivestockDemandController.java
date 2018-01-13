package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.LivestockDemand;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.service.LivestockDemandService;
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
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 15:44
 */
@Controller
public class LivestockDemandController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LivestockDemandController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LivestockDemandService livestockDemandService;

    /**
     * 发布一条养殖技术信息 TODO 加base authentication
     *
     * @param title
     * @param content
     * @param publisherId
     * @param response
     */
    @RequestMapping(path = {"/post_livestock_demand"}, method = RequestMethod.POST)
    public void postLivestockDemand(@RequestParam("title") String title,
                                       @RequestParam("content") String content,
                                       @RequestParam("publisher") long publisherId,
                                       HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            User publisher = userService.getUserById(publisherId);
            LivestockDemand livestockDemand = new LivestockDemand(title, content, publisher);
            livestockDemandService.publishLivestockDemand(livestockDemand);

            dataMap.put("livestock_demand", livestockDemand);

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
    @RequestMapping(path = {"/livestock_demand_list"}, method = {RequestMethod.GET})
    public void getLivestockDemandList(@RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                          HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<LivestockDemand> demandList = livestockDemandService.getLivestockDemandList(parsedOffset);
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
    @RequestMapping(path = {"/livestock_demand/{id}"}, method = {RequestMethod.GET})
    public void getLivestockDemand(@PathVariable("id") long id,
                                      HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            LivestockDemand LivestockDemand = livestockDemandService.getLivestockDemand(id);
            dataMap.put("livestock_demand", LivestockDemand);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }

    }
    
}
