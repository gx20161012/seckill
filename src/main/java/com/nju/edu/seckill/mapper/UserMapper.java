package com.nju.edu.seckill.mapper;

import com.nju.edu.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") Integer id);

    @Insert("insert into user (id, name) values (#{id}, #{name})")
    public int insert(User user);
}
