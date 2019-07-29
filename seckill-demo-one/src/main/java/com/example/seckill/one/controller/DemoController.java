package com.example.seckill.one.controller;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	private RedisUtils redisUtils;

	@RequestMapping("/")
	@ResponseBody
	public Result<String> home() {
		return Result.ok("Hello World!");
	}

	@RequestMapping("/set")
	@ResponseBody
	public Result<String> setRedis() {
		User user = new User();
		user.setId(4L).setLoginName("test");
		redisUtils.set("test", "value");
		redisUtils.set("test2", user);
		return Result.ok();
	}

	@RequestMapping("/get")
	@ResponseBody
	public Result<User> getRedis() {
		redisUtils.set("test", "value");
		String a = redisUtils.get("test");
		User test2 = redisUtils.get("test2", User.class);
		System.out.println(a);
		return Result.ok(test2);
	}

//	@RequestMapping("/thymeleaf")
//	public String  thymeleaf(Model model) {
//		model.addAttribute("name", "Joshua");
//		return "hello";
//	}
	 	
}
