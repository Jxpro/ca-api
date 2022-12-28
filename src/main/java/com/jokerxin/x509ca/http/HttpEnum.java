package com.jokerxin.x509ca.http;

import lombok.Getter;

@Getter
public enum HttpEnum {
    /**
     * 请求处理正常
     */
    OK(200, "请求成功"),

    /**
     * 请求成功并且服务器创建了新的资源。
     */
    CREATED(201, "创建成功"),
    /**
     * 表示用户没有权限，或token、用户名、密码错误
     */
    UNAUTHORIZED(401,"认证失败"),
    /**
     * 表示用户得到授权，但是访问是被禁止的
     */
    FORBIDDEN(403,"权限不足");

    private final Integer code;
    private final String msg;

    HttpEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
