package com.yomoo.yomooweb.mapper;

import com.yomoo.yomooweb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    //    @Update({"UPDATE", TABLE_NAME, "SET name = #{name}, intro = #{intro} WHERE id = #{id}"})
    void completeUser(User user);

    //    @Select({"SELECT * FROM", TABLE_NAME, "WHERE id = #{id}"})
    User selectById(long id);

    //    @Select({"select * from ", TABLE_NAME, " where phone=#{phone}"})
    User selectByPhone(String phone);

    //    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    //    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(long id);

    //    @Select({"SELECT * FROM user WHERE type = #{type} LIMIT 20 OFFSET #{offset}"})
    List<User> selectAllByType(@Param("type") String type, @Param("offset") int offset);

    //    @Select({"SELECT * FROM user LIMIT 20 OFFSET #{offset}"})
    List<User> selectAllUsers(int offset);

    List<User> selectUsersByKeyword(@Param("keyword") String keyword, @Param("type") String type, @Param("offset") int offset);

    List<User> selectUsersByKeywordNoType(@Param("keyword") String keyword, @Param("offset") int offset);
}
