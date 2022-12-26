package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Request {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Date notBefore;
    private Date notAfter;
    private Date revokeTime;
    private Integer state;
}
