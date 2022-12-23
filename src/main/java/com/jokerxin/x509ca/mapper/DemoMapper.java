package com.jokerxin.x509ca.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jokerxin.x509ca.entity.Demo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
// 在启动类上添加 @MapperScan("com.jokerxin.x509ca.mapper") 注解
// 则不需要在每个 Mapper 接口上添加 @Mapper 注解
// 这里添加 @Mapper 注解是为了让 IDE 识别该接口为 Mapper 接口
// 防止使用@AutoWired注入时，IDE 提示找不到 Bean
public interface DemoMapper extends BaseMapper<Demo> {

}
