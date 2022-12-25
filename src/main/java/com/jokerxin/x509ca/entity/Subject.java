package com.jokerxin.x509ca.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Subject {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer requestID;
    private String commonName;
    private String organization;
    private String organizationalUnit;
    private String countryName;
    private String provinceName;
    private String email;
}
