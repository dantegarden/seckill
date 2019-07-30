package com.example.seckill.one.config.annotation;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.enums.ResultErrorEnum;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.redis.key.AccessKey;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.CookieUtils;
import com.example.seckill.one.utils.FastjsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    /**方法执行前拦截*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //通过cookies里的token得到user，更新到ThreadLocal里，拦截器先于resolver执行
            User user = getUser(request, response);
            UserContext.setUser(user);

            //拿到方法上的注解
            AccessLimit accessLimt = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimt==null){
                return Boolean.TRUE; //不做限制
            }else{
                int seconds = accessLimt.seconds();
                int maxCount = accessLimt.maxCount();
                boolean needLogin = accessLimt.needLogin();

                //统一的登录状态校验
                String key = request.getRequestURI();
                if(needLogin) {
                    if(user == null) {
                        render(response, Result.failByEnum(ResultErrorEnum.USER_NOT_LOGIN));
                        return Boolean.FALSE;
                    }
                    key += "_" + user.getId();
                }

                AccessKey accessKey = AccessKey.ACCESS_LIMIT(seconds);
                //从缓存取限定时间内用户访问某url多少次
                Integer count = redisService.get(accessKey, key, Integer.class);
                if(count  == null) {
                    redisService.set(accessKey, key, 1); //第一次访问
                }else if(count < maxCount) {
                    redisService.incr(accessKey, key,1);
                }else {
                    render(response, Result.failByEnum(ResultErrorEnum.ACCESS_LIMIT));
                    return Boolean.FALSE;  //阻止进入接口方法
                }
            }
        }
        return Boolean.TRUE;
    }

    /**不进入方法，直接返回结果**/
    private void render(HttpServletResponse response, Result result)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String json  = FastjsonUtils.convertObjectToJSON(result);
        out.write(json.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(GlobalConstants.COOKIE_NAME_TOKEN);
        Cookie tokenCookie = CookieUtils.get(request, GlobalConstants.COOKIE_NAME_TOKEN);
        if(tokenCookie == null || (StringUtils.isEmpty(tokenCookie.getValue()) && StringUtils.isEmpty(paramToken))) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?tokenCookie.getValue():paramToken;
        return userService.getCurUser(response, token);
    }
}
