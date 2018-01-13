package com.yomoo.yomooweb.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 12:39
 */
@Controller
public class BaseController {

    protected static final String RESPONSE_CODE = "code";
    protected static final String MESSAGE = "message";
    protected static final String DATA = "data";

    protected String resultMapping(int responseCode, String message) {
        return resultMapping(responseCode, message, null);
    }

    protected String resultMapping(int responseCode, String message, Map<String, Object> data) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(RESPONSE_CODE, responseCode);
        resultMap.put(MESSAGE, message);
        resultMap.put(DATA, data);
        return new Gson().toJson(resultMap);
    }

    public void printResult(HttpServletResponse response, String result) {
        PrintWriter printWriter;
        try {
            response.setCharacterEncoding("UTF-8");
            printWriter = response.getWriter();
            printWriter.write(result);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
