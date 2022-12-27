package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.entity.UserKey;
import com.jokerxin.x509ca.entity.Subject;
import com.jokerxin.x509ca.service.CertService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CertController {
    final CertService certService;

    public CertController(CertService certService) {
        this.certService = certService;
    }

    @PassLogin
    @GetMapping("/cert/valid")
    public List<Map<String, Object>> allValid() {
        return certService.getAllByState("已通过");
    }

    @PassLogin
    @GetMapping("/cert/revoke")
    public List<Map<String, Object>> allRevoke() {
        return certService.getAllByState("已撤销");
    }

    @GetMapping("/cert/unapproved")
    public List<Map<String, Object>> allUnapproved(@RequestAttribute String userRole, HttpServletResponse response) {
        if (!userRole.equals("admin")) {
            response.setStatus(403);
            return null;
        }
        return certService.getAllByState("待审核");
    }

    @GetMapping("/cert/user")
    public List<Map<String, Object>> allUserCert(@RequestAttribute int userId) {
        return certService.getAllByUserId(userId);
    }

    @PassLogin
    @GetMapping("/cert/valid/{number}")
    public List<Map<String, Object>> validPage(@PathVariable long number) {
        return certService.getPageByState(number, "已通过");
    }

    @PassLogin
    @GetMapping("/cert/revoke/{number}")
    public List<Map<String, Object>> revokePage(@PathVariable long number) {
        return certService.getPageByState(number, "已撤销");
    }

    @GetMapping("/cert/unapproved/{number}")
    public List<Map<String, Object>> unapprovedPage(@PathVariable long number,
                                                @RequestAttribute String userRole,
                                                HttpServletResponse response) {
        if (!userRole.equals("admin")) {
            response.setStatus(403);
            return null;
        }
        return certService.getPageByState(number, "待审核");
    }

    @GetMapping("/cert/user/{number}")
    public List<Map<String, Object>> userCertPage(@PathVariable long number, @RequestAttribute int userId) {
        return certService.getPageByUserId(number, userId);
    }

    @PostMapping("/cert/approve")
    public List<Map<String, Object>> approve(@RequestParam int id,
                                             @RequestParam boolean passed,
                                             @RequestAttribute String userRole,
                                             HttpServletResponse response) {
        if (!userRole.equals("admin")) {
            response.setStatus(403);
            return null;
        }
        return certService.approveCert(id, passed);
    }

    @PostMapping("/cert/apply/subject")
    public Map<String, Object> applySubject(Subject subject, @RequestAttribute int userId) {
        return certService.insertSubject(subject, userId);
    }

    @PostMapping("/cert/apply/license")
    public Map<String, Object> applyLicense(MultipartFile license, @RequestAttribute int userId) throws IOException {
        return certService.insertLicense(license, userId);
    }

    @PostMapping("/cert/apply/publicKey")
    public Map<String, Object> applyPublicKey(UserKey userKey, @RequestAttribute int userId) {
        return certService.insertPublicKey(userKey, userId);
    }

    @PostMapping("/cert/revoke")
    public Map<String, Object> revokeCert(@RequestParam int requestId,
                                          @RequestAttribute int userId) {
        return certService.revokeById(requestId, userId);
    }
}
