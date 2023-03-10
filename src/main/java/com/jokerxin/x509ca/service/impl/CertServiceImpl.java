package com.jokerxin.x509ca.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jokerxin.x509ca.entity.License;
import com.jokerxin.x509ca.entity.Request;
import com.jokerxin.x509ca.entity.Subject;
import com.jokerxin.x509ca.entity.UserKey;
import com.jokerxin.x509ca.mapper.*;
import com.jokerxin.x509ca.service.CertService;
import com.jokerxin.x509ca.utils.CertUtil;
import com.jokerxin.x509ca.utils.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.*;

@Service
@Slf4j
public class CertServiceImpl implements CertService {
    final RequestMapper requestMapper;
    final SubjectMapper subjectMapper;
    final LicenseMapper licenseMapper;
    final UserKeyMapper userKeyMapper;
    final UserMapper userMapper;

    public CertServiceImpl(RequestMapper requestMapper, SubjectMapper subjectMapper, LicenseMapper licenseMapper, UserKeyMapper userKeyMapper, UserMapper userMapper) {
        this.requestMapper = requestMapper;
        this.subjectMapper = subjectMapper;
        this.licenseMapper = licenseMapper;
        this.userKeyMapper = userKeyMapper;
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

    @Override
    public List<Map<String, Object>> list(LambdaQueryWrapper<Request> wrapper) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Request> requests = requestMapper.selectList(wrapper);
        requests.forEach(request -> {
            Map<String, Object> map = new HashMap<>();
            map.put("request", request);
            map.put("subject", subjectMapper.selectById(request.getSubjectId()));
            map.put("license", licenseMapper.selectById(request.getLicenseId()));
            map.put("key", userKeyMapper.selectById(request.getKeyId()));
            list.add(map);
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> listByState(String stateMessage) {
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getState, stateMessage);
        return this.list(wrapper);
    }

    @Override
    public List<Map<String, Object>> listByUserId(int userId, boolean uncompleted) {
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getUserId, userId);
        if (uncompleted) {
            wrapper.eq(Request::getState, "待完善");
        }
        return this.list(wrapper);
    }

    @Override
    public List<Map<String, Object>> approveCert(int id, boolean passed) {
        // 查询申请信息
        Request request = requestMapper.selectById(id);
        // 更新申请信息
        if (passed) {
            request.setState("已通过");
            request.setSerialNumber(System.currentTimeMillis());
            request.setNotBefore(new Date(System.currentTimeMillis()));
            request.setNotAfter(new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
        } else {
            request.setState("未通过");
        }
        requestMapper.updateById(request);
        // 返回申请记录，方便重新渲染页面
        return this.listByState("待审核");
    }

    @Override
    public byte[] getCertification(int id) throws Exception {
        Request request = requestMapper.selectById(id);
        UserKey userKey = userKeyMapper.selectById(request.getKeyId());
        Subject subject = subjectMapper.selectById(request.getSubjectId());
        License license = null;
        if (request.getLicenseId() != null) {
            license = licenseMapper.selectById(request.getLicenseId());
        }
        PublicKey publicKey;
        if (userKey.getAlgorithm().equals("RSA-2048")) {
            publicKey = CertUtil.customRSAPublicKey(userKey.getParam1(), userKey.getParam2());
        } else {
            publicKey = CertUtil.customECPublicKey("prime256v1", userKey.getParam1(), userKey.getParam2());
        }
        Map<String, Object> subjectDN = CertUtil.getSubjectDN(subject, license);
        X509Certificate x509Certificate = CertUtil.generateUserCert(subjectDN,
                BigInteger.valueOf(request.getSerialNumber()),
                request.getNotBefore(),
                request.getNotAfter(),
                publicKey);
        return CertUtil.X509CertificateToPem(x509Certificate).getBytes();
    }

    @Override
    public byte[] getCRL() throws Exception {
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getState, "已撤销");
        List<Request> requests = requestMapper.selectList(wrapper);
        List<BigInteger> revokedSerialNumbers = new ArrayList<>();
        for (Request request : requests) {
            revokedSerialNumbers.add(BigInteger.valueOf(request.getSerialNumber()));
        }
        X509CRL x509CRL = CertUtil.generateCRL(revokedSerialNumbers);
        return x509CRL.getEncoded();
    }

    @Override
    public byte[] getLicense(String hash) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("license/" + hash + ".pdf");
        byte[] license = new byte[fileInputStream.available()];
        if (fileInputStream.read(license) == -1) {
            throw new Exception("文件读取失败");
        }
        fileInputStream.close();
        return license;
    }

    @Override
    public void saveSubject(Subject subject, int userId) {
        // 查询申请是否存在
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getUserId, userId);
        wrapper.eq(Request::getState, "待完善");
        Request request = requestMapper.selectOne(wrapper);
        // 如果是第一次申请，则创建新的申请
        if (request == null) {
            request = new Request();
            request.setUserId(userId);
            request.setState("待完善");
            requestMapper.insert(request);
        }
        // 如果已经有申请记录，则删除已有主体信息，插入新的主体信息
        if (request.getSubjectId() != null) {
            subjectMapper.deleteById(request.getSubjectId());
        }
        // 插入主体信息
        subjectMapper.insert(subject);
        // 更新申请记录
        request.setSubjectId(subject.getId());
        requestMapper.updateById(request);
    }

    @Override
    public void savePublicKey(UserKey userKey, int userId) {
        // 查询申请信息（此处是第二步，所以一定存在）
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getUserId, userId);
        wrapper.eq(Request::getState, "待完善");
        Request request = requestMapper.selectOne(wrapper);
        // 如果已经有申请记录，则删除已有密钥信息，插入新的密钥信息
        if (request.getKeyId() != null) {
            userKeyMapper.deleteById(request.getKeyId());
        }
        // 插入公钥
        userKeyMapper.insert(userKey);
        // 更新申请记录
        request.setKeyId(userKey.getId());
        requestMapper.updateById(request);
    }

    @Override
    public void saveLicense(MultipartFile file, int userId) throws IOException {
        // 查询申请信息（此处是第三步，所以一定存在）
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getUserId, userId);
        wrapper.eq(Request::getState, "待完善");
        Request request = requestMapper.selectOne(wrapper);
        // 如果上传了许可证，则保存许可证
        if (file != null) {
            // 如果已有许可证，则删除已有许可证
            if (request.getLicenseId() != null) {
                licenseMapper.deleteById(request.getLicenseId());
            }
            // 插入许可证
            License license = new License();
            license.setContentHash(HashUtil.sha256(file.getBytes()));
            license.setOriginName(file.getOriginalFilename());
            licenseMapper.insert(license);
            // 保存许可证文件
            file.transferTo(new File("license/" + license.getContentHash() + ".pdf"));
            // 更新申请记录
            request.setLicenseId(license.getId());
        }
        request.setState("待审核");
        requestMapper.updateById(request);
    }

    @Override
    public void revokeById(int requestId, int userId) {
        LambdaQueryWrapper<Request> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Request::getId, requestId);
        wrapper.eq(Request::getUserId, userId);
        Request request = requestMapper.selectOne(wrapper);
        request.setState("已撤销");
        request.setRevokeTime(new Date(System.currentTimeMillis()));
        requestMapper.updateById(request);
    }
}
