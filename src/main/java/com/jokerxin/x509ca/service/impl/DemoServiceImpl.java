package com.jokerxin.x509ca.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jokerxin.x509ca.entity.Demo;
import com.jokerxin.x509ca.mapper.DemoMapper;
import com.jokerxin.x509ca.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {

}
