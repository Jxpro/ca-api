package com.jokerxin.x509ca.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = request.getHeader("Authorization");
		if (token == null || token.equals("")) {
			response.setStatus(401);
			return false;
		}
		return true;
	}
}
