package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.service.CertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class CertController {
    final CertService certService;

    public CertController(CertService certService) {
        this.certService = certService;
    }

    @PassLogin
    @GetMapping("/cert/valid/{number}")
    public List<Map<String, Object>> valid(@PathVariable long number) {
        return certService.getPageByState(number, "已通过");
    }

    @PassLogin
    @GetMapping("/cert/revoke/{number}")
    public List<Map<String, Object>> revoke(@PathVariable long number) {
        return certService.getPageByState(number, "已撤销");
    }

    @GetMapping("/cert/unapproved/{number}")
    public List<Map<String, Object>> unapproved(@PathVariable long number,
                                                @RequestAttribute String userRole,
                                                HttpServletResponse response) {
        if (!userRole.equals("admin")) {
            response.setStatus(403);
            return null;
        }
        return certService.getPageByState(number, "待审核");
    }

    @GetMapping("/cert/user/{number}")
    public List<Map<String, Object>> user(@PathVariable long number, @RequestAttribute int userId) {
        return certService.getPageByUserId(number, userId);
    }
}
