package com.jokerxin.x509ca;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jokerxin.x509ca.entity.Subject;
import com.jokerxin.x509ca.utils.CertUtil;
import com.jokerxin.x509ca.utils.HashUtil;
import com.jokerxin.x509ca.utils.JWTUtil;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@SpringBootTest
class X509CaApplicationTests {
    @Autowired
    private DataSource dataSource;

    @Test
    void testHash() {
        System.out.println(HashUtil.sha256("admin".getBytes()));
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testToken() {
        String token = JWTUtil.createToken(1, "admin");
        int id = JWTUtil.getUserId(token);
        System.out.println(token);
        System.out.println(id);
    }

    @Test
    public void getAllCurveNames() {
        // 加<?>是为了避免警告：Raw use of parameterized class 'Enumeration'
        Enumeration<?> names = ECNamedCurveTable.getNames();
        while (names.hasMoreElements()) {
            String curveName = (String) names.nextElement();
            System.out.println(curveName);
        }
    }

    @Test
    public void testCertUtil() throws Exception {
        // 构建subjectDN
        Subject subject = new Subject();
        subject.setCommonName("jokerxin");
        subject.setOrganization("HNU");
        subject.setOrganizationalUnit("CS");
        subject.setCountry("CN");
        subject.setStateOrProvinceName("Hunan");
        subject.setEmail("jokerxin@jokerxin.com");
        Map<String, Object> subjectDN = CertUtil.getSubjectDN(subject);

        // 生成根证书
        CertUtil.generateRootCert();
        // 根据参数生成RSA公钥
        PublicKey rsaPublicKey = CertUtil.customRSAPublicKey("a3fb2640071820fce819a9beba949212b32f77084602ecefa966de7605e35a53af6c5dbdefb115947562e5346d3b01f860a10ee1e6321912843996d33ee2438e8052d0f36cfbbe73bc4418dbc41eca5686b18f91364f67e46bc199dcaa24ce5fa7b74d7a710978870750e4996d54748d30d109f83c022da194aed9aa7527320c02122d0c3acffd127382943dba490eb5aca74310caf8427fb80ef46ad4431d76bffe313a17dff855cbb77e5886c6c48766e7f70b938c95db08133c8db752531182cb0655342128e9d1e726ecc78c37573b2a3d3a208b220140a01a3d13388edbc82571d60375ac7fed33fc9ce52a6e3949dae3089b5f161194c8a9fd2427b543", "10001");
        // 根据参数生成EC公钥
        PublicKey ecPublicKey = CertUtil.customECPublicKey("prime256v1", "7b62b771ff5ac6050a01c6b5b8fc4d35f1819991585276bacd582bef511a1e3", "f062db8ed5bf84e6093a0715bb9d8b5c7585c20b7e7d519a070459bff361ffcf");
        System.out.println("rsaPublicKey: " + rsaPublicKey);
        System.out.println("ecPublicKey: " + ecPublicKey);

        // 生成用户证书
        X509Certificate rsaUserCert = CertUtil.generateUserCert(subjectDN,
                BigInteger.valueOf(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24),
                new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365),
                rsaPublicKey);
        X509Certificate ecUserCert = CertUtil.generateUserCert(subjectDN,
                BigInteger.valueOf(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24),
                new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365),
                ecPublicKey);
        // 保存用户证书
        CertUtil.saveEncodedFile("user_rsa.pem", CertUtil.X509CertificateToPem(rsaUserCert).getBytes());
        CertUtil.saveEncodedFile("user_ec.crt", ecUserCert.getEncoded());

        // 读取证书并输出
        System.out.println("root.crt: " + CertUtil.readCert("root.crt"));
        System.out.println("user_rsa.pem: " + CertUtil.readCert("user_rsa.pem"));
        System.out.println("user_ec.crt: " + CertUtil.readCert("user_ec.crt"));
    }
}
