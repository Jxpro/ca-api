package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Revoke {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer requestID;
    private Date revokeTime;
}
