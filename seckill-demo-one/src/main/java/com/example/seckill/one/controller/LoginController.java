package com.example.seckill.one.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

	@Autowired
    private UserService userService;
	
	@Autowired
	private RedisService redisService;

	/**跳转登录页面**/
//    @RequestMapping("/toLogin")
//    public String toLogin(Model model, HttpServletRequest request) {
//        return "login";
//    }

    /**登录逻辑**/
    @RequestMapping("/doLogin")
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVO loginVO) {
    	log.info(loginVO.toString());
    	//登录
    	String token = userService.login(response, loginVO);
    	return Result.ok(token);
    }
}
