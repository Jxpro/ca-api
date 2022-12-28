package com.jokerxin.x509ca.http;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    // 序列化版本号
    private static final long serialVersionUID = 1L;
    // 响应编码
    private final int code;
    // 提示信息
    private final String msg;
    // 响应数据
    private final T data;

    private static <T> Result<T> build(HttpEnum httpEnum, T data) {
        return new Result<>(httpEnum.getCode(), httpEnum.getMsg(), data);
    }

    // 请求正常
    public static <T> Result<T> ok(){
        return build(HttpEnum.OK,null);
    }

    // 请求正常
    public static <T> Result<T> ok(T data){
        return build(HttpEnum.OK,data);
    }

    // 创建成功
    public static <T> Result<T> created(){
        return build(HttpEnum.CREATED,null);
    }

    // 权限错误
    public static <T> Result<T> unauthorized(){
        return build(HttpEnum.UNAUTHORIZED,null);
    }

    // 权限不足
    public static <T> Result<T> forbidden(){
        return build(HttpEnum.FORBIDDEN,null);
    }

    // 系统内部错误
    public static <T> Result<T> internal_server_error(){
        return build(HttpEnum.INTERNAL_SERVER_ERROR,null);
    }
}
