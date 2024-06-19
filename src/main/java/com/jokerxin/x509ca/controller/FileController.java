package com.jokerxin.x509ca.controller;

import com.jokerxin.x509ca.annotation.PassLogin;
import com.jokerxin.x509ca.service.CertService;
import com.jokerxin.x509ca.utils.CertUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin
public class FileController {
    private static final String ROOT_CRT_PATH = "root.crt";
    final CertService certService;

    public FileController(CertService certService) {
        this.certService = certService;
    }

    @PassLogin
    @GetMapping("file/cert/{id}")
    public void cert(@PathVariable int id,
                     HttpServletResponse response) {
        try {
            byte[] file = certService.getCertification(id);
            response.setContentLength(file.length);
            response.getOutputStream().write(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PassLogin
    @GetMapping("file/cert/root")
    public void rootCert(HttpServletResponse response) {
        try {
            byte[] file = CertUtil.X509CertificateToPem(CertUtil.readCert(ROOT_CRT_PATH)).getBytes();
            response.setContentLength(file.length);
            response.getOutputStream().write(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PassLogin
    @GetMapping("file/crl")
    public void crl(HttpServletResponse response) {
        try {
            byte[] file = certService.getCRL();
            response.setContentLength(file.length);
            response.getOutputStream().write(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
