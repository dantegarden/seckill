package com.example.seckill.one.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.vo.LoginVO;

import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {

    String login(HttpServletResponse response, LoginVO loginVO);

    User getCurUser(HttpServletResponse response, String token);
}
