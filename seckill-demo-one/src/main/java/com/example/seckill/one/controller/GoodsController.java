package com.example.seckill.one.controller;

import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.DateUtils;
import com.example.seckill.one.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private GoodsService goodsService;

    @RequestMapping("/toList")
    public String list(Model model, User user) {
		//查询商品列表
		List<GoodsVO> goodsList = goodsService.listGoodsVO();
		model.addAttribute("goodsList", goodsList);
    	model.addAttribute("user", user);
        return "goods_list";
    }

	@RequestMapping("/toDetail/{goodsId}")
	public String list(Model model, User user, @PathVariable("goodsId") Long goodsId) {
		model.addAttribute("user", user);
		GoodsVO goods = goodsService.fetchGoodsvoById(goodsId);
		model.addAttribute("goods", goods);

		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();

		//为了测试方便，不使用数据库里的startAt好endAt，若想使用数据库里的把这两行删除
        startAt = DateUtils.addSeconds(new Date(now), 10).getTime();
        endAt = DateUtils.addSeconds(new Date(now), 10 + 600).getTime();

		int seckillStatus = 0;
		int remainSeconds = 0;
		if(now < startAt ) {//秒杀还没开始，倒计时
			seckillStatus = 0;
			remainSeconds = (int)((startAt - now )/1000);
		}else  if(now > endAt){//秒杀已经结束
			seckillStatus = 2;
			remainSeconds = -1;
		}else {//秒杀进行中
			seckillStatus = 1;
			remainSeconds = 0;
		}
		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		return "goods_detail";
	}
    
}
