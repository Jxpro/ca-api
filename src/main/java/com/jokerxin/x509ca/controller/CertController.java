package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.entity.Subject;
import com.jokerxin.x509ca.entity.UserKey;
import com.jokerxin.x509ca.http.Result;
import com.jokerxin.x509ca.service.CertService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
public class CertController {
    final CertService certService;

    public CertController(CertService certService) {
        this.certService = certService;
    }

    @PassLogin
    @GetMapping("/cert/valid")
    public Result<Object> allValid() {
        return Result.ok(certService.listByState("已通过"));
    }

    @PassLogin
    @GetMapping("/cert/revoke")
    public Result<Object> allRevoke() {
        return Result.ok(certService.listByState("已撤销"));
    }

    @GetMapping("/cert/unapproved")
    public Result<Object> allUnapproved(@RequestAttribute String userRole) {
        return userRole.equals("admin") ?
                Result.ok(certService.listByState("待审核")) :
                Result.forbidden();
    }

    @GetMapping("/cert/user")
    public Result<Object> allUserCert(@RequestAttribute int userId) {
        return Result.ok(certService.listByUserId(userId, false));
    }

    @GetMapping("/cert/user/uncompleted")
    public Result<Object> uncompletedUserCert(@RequestAttribute int userId) {
        return Result.ok(certService.listByUserId(userId, true));
    }

    @PostMapping("/cert/apply/subject")
    public Result<Object> applySubject(Subject subject, @RequestAttribute int userId) {
        certService.saveSubject(subject, userId);
        return Result.created();
    }

    @PostMapping("/cert/apply/publicKey")
    public Result<Object> applyPublicKey(UserKey userKey, @RequestAttribute int userId) {
        certService.savePublicKey(userKey, userId);
        return Result.created();
    }

    @PostMapping("/cert/approve")
    public Result<Object> approveCert(@RequestParam int id,
                                      @RequestParam boolean passed,
                                      @RequestAttribute String userRole) {
        return userRole.equals("admin") ?
                Result.ok(certService.approveCert(id, passed)) :
                Result.forbidden();
    }

    @PostMapping("/cert/revoke")
    public Result<Object> revokeCert(@RequestParam int requestId,
                                     @RequestAttribute int userId) {
        certService.revokeById(requestId, userId);
        return Result.ok();
    }
}
