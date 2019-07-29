package com.example.seckill.one.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.enums.ResultErrorEnum;
import com.example.seckill.one.exception.GlobalException;
import com.example.seckill.one.mapper.UserMapper;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.redis.key.UserKey;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.CookieUtils;
import com.example.seckill.one.utils.MD5Utils;
import com.example.seckill.one.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public String login(HttpServletResponse response, LoginVO loginVO) {
        if(loginVO == null) {
            throw new GlobalException(ResultErrorEnum.SERVER_ERROR);
        }
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.eq(StringUtils.isNotBlank(loginVO.getMobile()), "mobile", loginVO.getMobile());
        User user = this.getOne(queryWrapper);
        //用户手机号是否存在
        if(user == null) {
            throw new GlobalException(ResultErrorEnum.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Utils.formPassToDBPass(loginVO.getPassword(), saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(ResultErrorEnum.PASSWORD_ERROR);
        }

        String token = UUID.randomUUID().toString();
        refreshToken(response, token, user);
        return token;
    }

    @Override
    public User getCurUser(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.USER_TOKEN, token, User.class);
        //延长有效期
        if(user != null) {
            refreshToken(response, token, user);
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        //取缓存
        User user = redisService.get(UserKey.USER_ID, id.toString(), User.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = userMapper.selectById(id);
        if(user != null) {
            redisService.set(UserKey.USER_ID, id.toString(), user);
        }
        return user;
    }

    @Override
    public Boolean updatePassword(String token, Long id, String formPass) {
        //取user
        User user = getById(id);
        if(user == null) {
            throw new GlobalException(ResultErrorEnum.MOBILE_NOT_EXIST);
        }
        //先更新数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Utils.formPassToDBPass(formPass, user.getSalt()));
        userMapper.updateById(toBeUpdate);
        //再处理缓存
        redisService.del(UserKey.USER_ID, id.toString());
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(UserKey.USER_TOKEN, token, user);
        //这个顺序不可更改 参考 http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
        return Boolean.TRUE;
    }

    private void refreshToken(HttpServletResponse response, String token, User user){
        //存入redis
        redisService.set(UserKey.USER_TOKEN, token, user);
        //生成cookie
        CookieUtils.set(response, GlobalConstants.COOKIE_NAME_TOKEN, token, UserKey.USER_TOKEN.expireSeconds());
    }
}
