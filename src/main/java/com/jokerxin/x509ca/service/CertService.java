package com.jokerxin.x509ca.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jokerxin.x509ca.entity.Request;
import com.jokerxin.x509ca.entity.Subject;
import com.jokerxin.x509ca.entity.UserKey;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CertService {
    List<Map<String, Object>> list(LambdaQueryWrapper<Request> wrapper);

    List<Map<String, Object>> listByState(String stateMessage);

    List<Map<String, Object>> listByUserId(int userId, boolean uncompleted);

    List<Map<String, Object>> approveCert(int id, boolean passed);

    byte[] getCertification(int id) throws Exception;

    byte[] getCRL() throws Exception;

    byte[] getLicense(String hash) throws Exception;

    void saveSubject(Subject subject, int userId);

    void savePublicKey(UserKey userKey, int userId);

    void saveLicense(MultipartFile file, int userId) throws IOException;

    void revokeById(int requestId, int userId);
}
