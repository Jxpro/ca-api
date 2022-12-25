package com.jokerxin.x509ca.service.impl;

import com.jokerxin.x509ca.service.CertService;
import com.jokerxin.x509ca.utils.CertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class CertServiceImpl implements CertService {
    @PostConstruct
    public void init() throws Exception {
        // 如果根证书不存在则创建
        if (!CertUtil.isRootExist()) {
            CertUtil.generateRootCert();
        }
        log.info("Generate root CA");
    }
}
