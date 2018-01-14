package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.FodderOfVendor;
import com.yomoo.yomooweb.service.FodderService;
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
 * @Date: 2018-01-14
 * @Time: 11:11
 */
@Controller
public class FodderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FodderController.class);

    @Autowired
    private FodderService fodderService;

    /**
     * 获取销售商在售的饲料列表
     *
     * @param vendorId
     * @param offset
     * @param response
     */
    @RequestMapping(path = {"/fv_list"}, method = RequestMethod.GET)
    public void getFodderOfVendorList(@RequestParam(value = "vendor_id") long vendorId,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                                      HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<FodderOfVendor> fvList = fodderService.getFodderVendorByVendorId(vendorId, parsedOffset);
            dataMap.put("list", fvList);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "查询成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }

    @RequestMapping(path = {"/fodder_of_vendor/{id}"}, method = RequestMethod.GET)
    public void getFodderOfVendorDetail(@PathVariable("id") Long fvId,
                                        HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            FodderOfVendor fv = fodderService.getFodderVendorById(fvId);
            dataMap.put("fv", fv);
            result = resultMapping(HttpStatusCode.SUCCESS, "查询成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }

}
