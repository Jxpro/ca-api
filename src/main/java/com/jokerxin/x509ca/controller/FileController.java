package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.service.CertService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@Controller
@CrossOrigin
public class FileController {

    final CertService certService;

    public FileController(CertService certService) {
        this.certService = certService;
    }

    @PassLogin
    @GetMapping("/file/license/{hash}")
    public void license(@PathVariable String hash,
                        HttpServletResponse response) {
        byte[] file = certService.getLicenseFile(hash);
        String name = certService.getLicenseName(hash);
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
        response.setContentType("application/pdf");
        response.setContentLength(file.length);
        try {
            response.getOutputStream().write(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PassLogin
    @GetMapping("file/cert/{id}")
    public void cert(@PathVariable int id,
                     HttpServletResponse response) throws Exception {
        byte[] file = certService.getCertification(id);
        String name = certService.getCommonName(id) + ".cer";
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
        response.setContentType("application/x-x509-user-cert");
        response.setContentLength(file.length);
        try {
            response.getOutputStream().write(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PassLogin
    @GetMapping("file/crl")
    public void crl(HttpServletResponse response) throws Exception {
        byte[] file = certService.getCRL();
        String name = new Date(System.currentTimeMillis()) + ".crl";
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
        response.setContentType("application/pkix-crl");
        response.setContentLength(file.length);
        try {
            response.getOutputStream().write(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
