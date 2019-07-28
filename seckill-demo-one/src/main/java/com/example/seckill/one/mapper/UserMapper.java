package com.example.seckill.one.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.one.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
