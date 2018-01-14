package com.yomoo.yomooweb.service;

import com.yomoo.yomooweb.entity.User;
import com.yomoo.yomooweb.mapper.UserMapper;
import com.yomoo.yomooweb.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 14:31
 */
@Service
public class UserService extends BaseSolrService {

    @Resource
    private HttpSolrClient solrClient;

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

    public List<User> getUsersByTypeBySolr(String keyword, String type, int offset) {
        if (solrClient == null) {
            solrClient = getSolrClient("users");
        }
        SolrQuery query = new SolrQuery();

        query.setStart(offset);
        query.setRows(20);


        StringBuffer buffer = new StringBuffer();
//        String fbfmc = "何寨街道季家村高北组"; //查询条件
        String[] keyArray = null;
        if (!StringUtils.isEmpty(keyword)) {
            buffer.append("type:").append(type).append(" AND ");
            keyArray = keyword.split("\\s+");
            for (String key : keyArray) {
                buffer.append("*:*").append(key).append("* AND ");
            }
            String bufferStr = buffer.toString();
            if (bufferStr.endsWith(" AND ")) {
                bufferStr = bufferStr.substring(0, bufferStr.lastIndexOf(" AND "));
            }
//            buffer.append("*:" + keyword); //如果你的fbfmc字段在solrHome/fbf/conf/manage-schema文件中定义的类型是text_ik，即已经分词了，那么这里可以这么写,如果你定义的是string类型，即没有分词，那这句话的append中的内容需要写成这样buffer.append("fbfmc:*"+fbfmc+"*"),这是solr的查询规则，没有分词最好是加上模糊查询符号"*"
            query.set("q", bufferStr);
        } else {
            query.set("q", "*:*"); //没有传入参数则全部查询
        }
        QueryResponse resp = null;
        try {
            resp = solrClient.query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SolrDocumentList results = resp.getResults();
        System.out.println(results.getNumFound());//查询总条数，该总条数是符合该条件下的总条数，并不是pageSize的数量。
        List<User> userList = resp.getBeans(User.class);//该方法将返回结果转换为对象，很方便。
        System.out.println(userList.get(0).getPhone());

        return userList;
    }

    public List<User> getUsersBySolr(String keyword, int offset) {
        if (solrClient == null) {
            solrClient = getSolrClient("users");
        }
        SolrQuery query = new SolrQuery();

        query.setStart(offset);
        query.setRows(20);


        StringBuffer buffer = new StringBuffer();
//        String fbfmc = "何寨街道季家村高北组"; //查询条件
        String[] keyArray = null;
        if (!StringUtils.isEmpty(keyword)) {
            keyArray = keyword.split("\\s+");
            for (String key : keyArray) {
                buffer.append("*:*").append(key).append("* AND ");
            }
            String bufferStr = buffer.toString();
            if (bufferStr.endsWith(" AND ")) {
                bufferStr = bufferStr.substring(0, bufferStr.lastIndexOf(" AND "));
            }
//            buffer.append("*:" + keyword); //如果你的fbfmc字段在solrHome/fbf/conf/manage-schema文件中定义的类型是text_ik，即已经分词了，那么这里可以这么写,如果你定义的是string类型，即没有分词，那这句话的append中的内容需要写成这样buffer.append("fbfmc:*"+fbfmc+"*"),这是solr的查询规则，没有分词最好是加上模糊查询符号"*"
            query.set("q", bufferStr);
        } else {
            query.set("q", "*:*"); //没有传入参数则全部查询
        }
        QueryResponse resp = null;
        try {
            resp = solrClient.query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SolrDocumentList results = resp.getResults();
        System.out.println(results.getNumFound());//查询总条数，该总条数是符合该条件下的总条数，并不是pageSize的数量。
        List<User> userList = resp.getBeans(User.class);//该方法将返回结果转换为对象，很方便。
        System.out.println(userList.get(0).getPhone());

        return userList;
    }

}
