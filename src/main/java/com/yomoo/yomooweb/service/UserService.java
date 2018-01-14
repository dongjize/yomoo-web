package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.mapper.UserMapper;
import com.yomoo.yomooweb.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 14:31
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param phone
     * @param password
     * @return
     */
    public Map<String, Object> register(String phone, String password, String type) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(phone)) {
            map.put("msg", "手机号不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        if (type == null) {
            map.put("msg", "身份不能为空");
            return map;
        }

        User user = userMapper.selectByPhone(phone);

        if (user != null) {
            map.put("msg", "手机号已经被注册");
            return map;
        }

        // 密码强度
        user = new User();
        user.setPhone(phone);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
//        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
//        user.setHeadUrl(head);
        user.setPassword(CommonUtils.MD5(password + user.getSalt()));
        user.setType(type);
        userMapper.addUser(user);

        // 登陆
//        String ticket = addLoginTicket(user.getId());
        map.put("user", user);
//        map.put("ticket", ticket);
        return map;
    }

    /**
     * 用户登录
     *
     * @param phone
     * @param password
     * @return
     */
    public Map<String, Object> login(String phone, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(phone)) {
            map.put("msg", "手机号");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userMapper.selectByPhone(phone);

        if (user == null) {
            map.put("msg", "手机号不存在");
            return map;
        }

        // TODO caution!
        if (!Objects.equals(CommonUtils.MD5(password + user.getSalt()), user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

//        String ticket = addLoginTicket(user.getId());
//        map.put("ticket", ticket);
        map.put("user", user);
        return map;
    }

    public User getUserById(long id) {
        return userMapper.selectById(id);
    }

//    public void logout(String ticket) {
//        loginTicketDao.updateStatus(ticket, 1);
//    }

    public void updateUser(User user) {
        userMapper.completeUser(user);
    }

    public List<User> getUsersByType(String type, int offset) {
        return userMapper.selectAllByType(type, offset);
    }

    public List<User> getAllUsers(int offset) {
        return userMapper.selectAllUsers(offset);
    }

    public List<User> getUsersByKeyword(String keyword, int offset) {
        return userMapper.selectUsersByKeywordNoType(keyword, offset);
    }

    public List<User> getUsersByKeyword(String keyword, String type, int offset) {
        return userMapper.selectUsersByKeyword(keyword, type, offset);
    }

}
