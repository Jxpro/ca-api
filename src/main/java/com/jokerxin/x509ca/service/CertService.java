package com.jokerxin.x509ca.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jokerxin.x509ca.entity.Request;

import java.util.List;
import java.util.Map;

public interface CertService {
    public List<Map<String, Object>> page(long number, LambdaQueryWrapper<Request> wrapper);

    public List<Map<String, Object>> getPageByState(long number, String stateMessage);

    List<Map<String, Object>> getPageByUserId(long number, int userId);
}
