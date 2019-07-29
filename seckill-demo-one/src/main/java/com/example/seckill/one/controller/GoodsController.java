package com.example.seckill.one.controller;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.DateUtils;
import com.example.seckill.one.vo.GoodsDetailVO;
import com.example.seckill.one.vo.GoodsListVO;
import com.example.seckill.one.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private GoodsService goodsService;

    @GetMapping("/list")
    public Result<List> list(Model model, User user) {
		//查询商品列表
		List<GoodsVO> goodsList = goodsService.listGoodsVO();
		GoodsListVO goodsListVO = new GoodsListVO();
		goodsListVO.setGoodsList(goodsList).setUser(user);
        return Result.ok(goodsListVO);
    }

	@GetMapping("/detail/{goodsId}")
	public Result<GoodsDetailVO> detail(Model model, User user, @PathVariable("goodsId") Long goodsId) {

    	GoodsVO goods = goodsService.fetchGoodsvoById(goodsId);

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
		GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
		goodsDetailVO.setUser(user).setGoods(goods).setRemainSeconds(remainSeconds).setSeckillStatus(seckillStatus);
		return Result.ok(goodsDetailVO);
	}
    
}
