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
    List<Map<String, Object>> getCertsByRequests(List<Request> requests);

    List<Map<String, Object>> getAll( LambdaQueryWrapper<Request> wrapper);

    List<Map<String, Object>> getAllByState(String stateMessage);

    List<Map<String, Object>> getAllByUserId(int userId);

    public List<Map<String, Object>> page(long number, LambdaQueryWrapper<Request> wrapper);

    public List<Map<String, Object>> getPageByState(long number, String stateMessage);

    List<Map<String, Object>> getPageByUserId(long number, int userId);

    Map<String, Object> insertSubject(Subject subject, int userId);

    Map<String, Object> insertLicense(MultipartFile file, int userId) throws IOException;

    Map<String, Object> insertPublicKey(UserKey userKey, int userId);

    List<Map<String, Object>> approveCert(int id, boolean passed);

    Map<String, Object> revokeById(int requestId, int userId);

    byte[] getLicense(String hash);

    byte[] getCertification(int id) throws Exception;

    byte[] getCRL() throws Exception;
}
