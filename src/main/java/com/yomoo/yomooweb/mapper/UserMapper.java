package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-13
 * @Time: 12:10
 */
@Mapper
@Repository
public interface UserMapper {
    void addUser(User user);

    void completeUser(User user);

    User selectById(long id);

    User selectByPhone(String phone);

    void updatePassword(User user);

    void deleteById(long id);

    List<User> selectAllByType(@Param("type") String type, @Param("offset") int offset);

    List<User> selectAllUsers(int offset);

    List<User> selectUsersByKeyword(@Param("keyword") String keyword, @Param("type") String type, @Param("offset") int offset);

    List<User> selectUsersByKeywordNoType(@Param("keyword") String keyword, @Param("offset") int offset);
}
