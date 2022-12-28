package com.jokerxin.x509ca.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jokerxin.x509ca.entity.UserKey;
import com.jokerxin.x509ca.entity.Request;
import com.jokerxin.x509ca.entity.Subject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CertService {
    List<Map<String, Object>> list(LambdaQueryWrapper<Request> wrapper);

    List<Map<String, Object>> listByState(String stateMessage);

    List<Map<String, Object>> listByUserId(int userId, boolean uncompleted);

    Map<String, Object> saveSubject(Subject subject, int userId);

    Map<String, Object> saveLicense(MultipartFile file, int userId) throws IOException;

    Map<String, Object> savePublicKey(UserKey userKey, int userId);

    List<Map<String, Object>> approveCert(int id, boolean passed);

    Map<String, Object> revokeById(int requestId, int userId);

    byte[] getLicense(String hash);

    byte[] getCertification(int id) throws Exception;

    byte[] getCRL() throws Exception;
}
