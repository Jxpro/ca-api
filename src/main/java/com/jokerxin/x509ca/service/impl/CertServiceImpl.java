package com.jokerxin.x509ca.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jokerxin.x509ca.entity.Request;
import com.jokerxin.x509ca.mapper.*;
import com.jokerxin.x509ca.service.CertService;
import com.jokerxin.x509ca.utils.CertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CertServiceImpl implements CertService {
    private static final long PAGE_SIZE = 6;
    final RequestMapper requestMapper;
    final SubjectMapper subjectMapper;
    final LicenseMapper licenseMapper;
    final PublicKeyMapper publicKeyMapper;
    final UserMapper userMapper;

    public CertServiceImpl(RequestMapper requestMapper, SubjectMapper subjectMapper, LicenseMapper licenseMapper, PublicKeyMapper publicKeyMapper, UserMapper userMapper) {
        this.requestMapper = requestMapper;
        this.subjectMapper = subjectMapper;
        this.licenseMapper = licenseMapper;
        this.publicKeyMapper = publicKeyMapper;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void init() throws Exception {
        // 如果根证书不存在则创建
        if (!CertUtil.isRootExist()) {
            CertUtil.generateRootCert();
        }
        log.info("Generate root CA");
    }

    public List<Map<String, Object>> page(long number, LambdaQueryWrapper<Request> wrapper) {
        List<Map<String, Object>> list = new ArrayList<>();
        Page<Request> page = new Page<>(number, PAGE_SIZE);
        requestMapper.selectPage(page, wrapper);
        page.getRecords().forEach(request -> {
            Map<String, Object> map = new HashMap<>();
            map.put("request", request);
            map.put("subject", subjectMapper.selectById(request.getSubjectId()));
            map.put("license", licenseMapper.selectById(request.getLicenseId()));
            map.put("key", publicKeyMapper.selectById(request.getKeyId()));
            list.add(map);
        });
        return list;
    }

    public List<Map<String, Object>> getPageByState(long number, String stateMessage) {
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getState, stateMessage);
        return page(number, wrapper);
    }

    @Override
    public List<Map<String, Object>> getPageByUserId(long number, int userId) {
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getUserId, userId);
        return page(number, wrapper);
    }
}
