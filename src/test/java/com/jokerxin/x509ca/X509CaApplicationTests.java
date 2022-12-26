package com.jokerxin.x509ca;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jokerxin.x509ca.entity.Demo;
import com.jokerxin.x509ca.entity.License;
import com.jokerxin.x509ca.entity.Subject;
import com.jokerxin.x509ca.service.DemoService;
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

    @Autowired
    private DemoService demoService;

    @Test
    void testHash() {
        System.out.println(HashUtil.sha256("admin".getBytes()));
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testSelectDemo() {
        System.out.println(("----- selectAll method test ------"));
        List<Demo> userList = demoService.list();
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectDemoPage() {
        System.out.println(("----- selectPage method test ------"));
        Page<Demo> demoUserPage = new Page<>(1, 2);
        Page<Demo> page = demoService.page(demoUserPage);
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.getCurrent());
        page.getRecords().forEach(System.out::println);
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
        License license = new License();
        subject.setCommonName("jokerxin");
        subject.setOrganization("HNU");
        subject.setOrganizationalUnit("CS");
        subject.setCountryName("CN");
        subject.setProvinceName("Hunan");
        subject.setEmail("jokerxin@jokerxin.com");
        Map<String, Object> subjectDN = CertUtil.getSubjectDN(subject, license);

        // 生成根证书
        CertUtil.generateRootCert();
        // 根据参数生成RSA公钥
        PublicKey rsaPublicKey = CertUtil.customRSAPublicKey("23942851064010346213536102616372514461758539530468226679046065021734636584436958829290455158509234895755974008162808739180500969653125868658784929835366184925346201664297960728004406986004492228252044475272828985983128450273474774737791946526690056881166146111833501827937227297535579777611430189634998227802759047528918991917960839552231005329621934011488364379817865804561318787840269819884365764673377924951839055210901678915668655362980762201745478607202046966346798420124342001447849391550311177768633493644311606465255087749009465995178524742197119605599822580797722544444894521358131954151142035293480046914131", "65537");
        // 根据参数生成EC公钥
        PublicKey ecPublicKey = CertUtil.customECPublicKey("prime256v1", "43402861010439923438948362831980934947833175921967755475820423341148777662561", "81787307312274621843680993027418882760249358609609586724278648046138262266910");
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
