package com.jokerxin.x509ca.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.http.Result;
import com.jokerxin.x509ca.utils.JWTUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws IOException {
        // 判断对象是否是映射到一个方法，如果不是则直接通过
        // 可以判断是否是 pre-flight 请求，则如果是则直接返回，可以解决跨域
        if (!(handler instanceof HandlerMethod)) {
            // instanceof运算符是用来在运行时指出对象是否是特定类的一个实例
            return true;
        }

        // 设置response响应数据类型为json和编码为utf-8
        // 防止使用response.getWriter().write()直接写入数据时，中文变成问号(???)的情况
        // 使用@RestController直接返回数据，自动写入时一般不会有这样的情况，原理暂时不清楚
        response.setContentType("application/json;charset=utf-8");

        // 如果标注了 PassLogin 注解，则不需要登录
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassLogin.class)) {
            return true;
        }

        // 如果是pre-flight请求，则直接返回
        // 已通过上面的handler instanceof HandlerMethod判断是否是pre-flight请求
        //if (request.getMethod().equals("OPTIONS")) {
        //    response.setHeader("Access-Control-Allow-Origin", "*");
        //    response.setHeader("Access-Control-Allow-Headers", "*");
        //    return false;
        //}

        // 其他请求则需要登录
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().write(JSONObject.toJSONString(Result.unauthorized()));
            return false;
        }

        // 验证 token
        int id = JWTUtil.getUserId(token);
        String role = JWTUtil.getUserRole(token);
        // 如果验证失败，则视为token失效，返回 403
        if (id == -1 || role.equals("error")) {
            response.getWriter().write(JSONObject.toJSONString(Result.unauthorized()));
            return false;
        }
        // 验证成功，将用户id和role存入request中
        request.setAttribute("userId", id);
        request.setAttribute("userRole", role);
        return true;
    }
}
