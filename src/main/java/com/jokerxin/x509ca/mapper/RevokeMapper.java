package com.jokerxin.x509ca.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jokerxin.x509ca.entity.Revoke;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RevokeMapper extends BaseMapper<Revoke> {

}
