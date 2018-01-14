package com.yomoo.yomooweb.controller;

import com.yomoo.yomooweb.entity.Farmer;
import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.service.FarmerService;
import com.yomoo.yomooweb.service.UserService;
import com.yomoo.yomooweb.utils.Constants;
import com.yomoo.yomooweb.utils.HttpStatusCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 14:42
 */
@Controller
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @Autowired
    FarmerService farmerService;

    /**
     * 用户注册
     *
     * @param model
     * @param phone    手机号
     * @param password 密码
     * @param type     用户类型
     * @param response http response
     */
    @RequestMapping(path = {"/register"}, method = {RequestMethod.POST})
    public void register(Model model,
                         @RequestParam("phone") String phone,
                         @RequestParam("password") String password,
                         @RequestParam("type") String type,
                         HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String result = "";
        try {
            map = userService.register(phone, password, type);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                cookie.setMaxAge(3600 * 24 * 5);
                response.addCookie(cookie);
//                if (StringUtils.isNotBlank(next)) {
//                    return "redirect:" + next;
//                }
            } else {
                model.addAttribute("msg", map.get("msg"));
            }
            User user = (User) map.get("user");
            if (user.getType().equals(User.FARMER)) {
                Farmer farmer = farmerService.insertFarmer(user);
                dataMap.put("is_farmer", true);
                dataMap.put("user", farmer);
            } else {
                dataMap.put("is_farmer", false);
                dataMap.put("user", user);
            }
            result = resultMapping(HttpStatusCode.SUCCESS, "注册成功", dataMap);
        } catch (Exception e) {
            logger.error("注册异常: " + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            result = resultMapping(HttpStatusCode.SERVER_ERROR, (String) map.get("msg"), dataMap);
        } finally {
            printResult(response, result);
        }
    }


    /**
     * 用户完善个人信息
     *
     * @param id
     * @param name         用户名
     * @param village      村
     * @param group        组
     * @param streetNum    街道号
     * @param livestock    牲畜数量
     * @param expLivestock 预计饲养牲畜数量
     * @param intro        自我介绍
     * @param response     http response
     */
    @RequestMapping(path = {"/complete_info"}, method = RequestMethod.POST)
    public void completeInfo(@RequestParam("id") long id,
                             @RequestParam("name") String name,
                             @RequestParam(value = "village", defaultValue = "", required = false) String village,
                             @RequestParam(value = "group", defaultValue = "", required = false) String group,
                             @RequestParam(value = "street_num", defaultValue = "", required = false) String streetNum,
                             @RequestParam(value = "livestock", defaultValue = "", required = false) String livestock,
                             @RequestParam(value = "exp_livestock", defaultValue = "", required = false) String expLivestock,
                             @RequestParam(value = "intro", defaultValue = "", required = false) String intro,
                             HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            User user = userService.getUserById(id);
            user.setName(name);
            user.setIntro(intro);
            userService.updateUser(user);
            String type = user.getType();
            if (type.equals(User.FARMER)) {
                Farmer farmer = farmerService.getFarmerById(user.getId());
                farmer.setVillage(village);
                farmer.setGroup(group);
                farmer.setStreetNum(streetNum);
                farmer.setLivestock(livestock);
                farmer.setExpLivestock(expLivestock);
                farmerService.updateFarmer(farmer);
                dataMap.put("user", farmer);
                dataMap.put("is_farmer", true);
            } else {
                dataMap.put("user", user);
                dataMap.put("is_farmer", false);
            }
            result = resultMapping(HttpStatusCode.SUCCESS, "完善资料成功", dataMap);
        } catch (Exception e) {
            logger.error("完善资料异常" + e.getMessage());
//            model.addAttribute("msg", "服务器错误");
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }


    /**
     * 用户登录
     *
     * @param model
     * @param phone      手机号
     * @param password   密码
     * @param rememberMe 记住我
     * @param response   http response
     */
    @RequestMapping(path = {"/login"}, method = {RequestMethod.POST})
    public void login(Model model,
                      @RequestParam("phone") String phone,
                      @RequestParam("password") String password,
                      @RequestParam(value = "remember_me", required = false, defaultValue = "false") boolean rememberMe,
                      HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            Map<String, Object> map = userService.login(phone, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberMe) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);

                // TODO
//                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
//                        .setExt("username", username).setExt("email", "zjuyxy@qq.com")
//                        .setActorId((int) map.get("userId")));

//                if (StringUtils.isNotBlank(next)) {
//                    return "redirect:" + next;
//                }
            } else {
                model.addAttribute("msg", map.get("msg"));
            }

            User user = (User) map.get("user");

            String type = user.getType();
            if (type.equals(User.FARMER)) {
                Farmer farmer = farmerService.getFarmerById(user.getId());
                dataMap.put("is_farmer", true);
                dataMap.put("user", farmer);
            } else {
                dataMap.put("is_farmer", false);
                dataMap.put("user", user);
            }
            result = resultMapping(HttpStatusCode.SUCCESS, "登录成功", dataMap);
        } catch (Exception e) {
            logger.error("登录异常: " + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }

    /**
     * 用户退出登录
     *
     * @param ticket
     * @param response http response
     */
//    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
//    public void logout(@CookieValue("ticket") String ticket,
//                       HttpServletResponse response) {
//        String result = "";
//        try {
//            userService.logout(ticket);
//            result = resultMapping(HttpStatusCode.SUCCESS, "退出登录成功");
//        } catch (Exception e) {
//            logger.error("退出登录异常: " + e.getMessage());
//            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage());
//        } finally {
//            printResult(response, result);
//        }
//    }

    /**
     * 获取用户列表（可指定用户类型）
     *
     * @param offset   偏移
     * @param type     用户类型
     * @param response http response
     */
    @RequestMapping(path = {"/users"}, method = {RequestMethod.GET})
    public void getUserList(@RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                            @RequestParam(value = "type", required = false) String type,
                            HttpServletResponse response) {

        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<User> userList;
            if (StringUtils.isEmpty(type)) {
                userList = userService.getAllUsers(parsedOffset);
            } else {
                userList = userService.getUsersByType(type, parsedOffset);
            }
            dataMap.put("list", userList);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }

    @CrossOrigin
    @RequestMapping(path = {"/solr/users"}, method = {RequestMethod.GET})
    public void queryUserList(@RequestParam(value = "key", required = false, defaultValue = "") String keyword,
                              @RequestParam(value = "offset", required = false, defaultValue = "0") String offset,
                              @RequestParam(value = "type", required = false) String type,
                              HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<>();
        String result = "";
        try {
            int parsedOffset = Integer.parseInt(offset);
            int nextOffset = parsedOffset + Constants.LIMIT;
            List<User> userList;
            if (StringUtils.isEmpty(type)) {
                userList = userService.getUsersByKeyword(keyword, parsedOffset);
            } else {
                userList = userService.getUsersByKeyword(keyword, type, parsedOffset);
//                userList = userService.getUsersByTypeBySolr(keyword, type, parsedOffset);
            }
            dataMap.put("list", userList);
            dataMap.put("offset", nextOffset);
            result = resultMapping(HttpStatusCode.SUCCESS, "请求成功", dataMap);
        } catch (Exception e) {
            logger.error("EXCEPTION: " + e.getMessage());
            result = resultMapping(HttpStatusCode.SERVER_ERROR, e.getMessage(), dataMap);
        } finally {
            printResult(response, result);
        }
    }

}
