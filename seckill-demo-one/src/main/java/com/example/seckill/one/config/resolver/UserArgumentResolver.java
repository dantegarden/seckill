package com.example.seckill.one.config.resolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private UserService userService;

	/**只有method入参里有User类型才做处理*/
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz == User.class;
	}

	/**处理逻辑，将请求里的cookie拿到，去redis里取user，返回值会被绑定到方法入参上**/
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		String paramToken = request.getParameter(GlobalConstants.COOKIE_NAME_TOKEN);
		Cookie tokenCookie = CookieUtils.get(request, GlobalConstants.COOKIE_NAME_TOKEN);
		if(tokenCookie == null || (StringUtils.isEmpty(tokenCookie.getValue()) && StringUtils.isEmpty(paramToken))) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?tokenCookie.getValue():paramToken;
		return userService.getCurUser(response, token);
	}

}
