package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class License {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String licenseUrl;
    private String originName;
    private String contentHash;
}
