package com.jokerxin.x509ca.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jokerxin.x509ca.entity.License;
import com.jokerxin.x509ca.entity.Subject;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

public class CertUtil {
    private static final String ROOT_SEED = "root12138";
    private static final String ROOT_ALG = "RSA";
    private static final int ROOT_SIZE = 2048;
    private static final String ROOT_CRT_PATH = "root.crt";
    private static final String ROOT_PRIVATE_KEY = "root.privateKey";
    private static final String SIG_ALG = "SHA256withRSA";
    private static final String PROVIDER = "BC";

    private static final String CRL_URL = "https://www.example.com/crl";
    private static final String ROOT_DN = "CN=SelfSign Root CA,O=HNU,OU=CS,C=CN,ST=海南省";
    private static final DistributionPoint[] distributionPoints = new DistributionPoint[1];
    private static final Date NotBefore = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
    private static final Date NotAfter = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 10);

    static {
        // 添加BouncyCastle支持
        Security.addProvider(new BouncyCastleProvider());
        // 构造CRL distribution points扩展值
        GeneralName generalName = new GeneralName(GeneralName.uniformResourceIdentifier, CRL_URL);
        DistributionPointName distributionPointName = new DistributionPointName(new GeneralNames(generalName));
        distributionPoints[0] = new DistributionPoint(distributionPointName, null, null);
    }

    /**
     * 生成根CA的RSA密钥对
     *
     * @return keyPair 密钥对
     */
    public static KeyPair generateFixedKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ROOT_ALG, PROVIDER);
        keyPairGenerator.initialize(ROOT_SIZE, new SecureRandom(ROOT_SEED.getBytes()));
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 使用JcaX509v3CertificateBuilder生成根CA数字证书，并保存到文件
     */
    public static void generateRootCert() throws Exception {
        // 生成RSA密钥对
        KeyPair keyPair = generateFixedKeyPair();
        // 保存私钥
        saveEncodedFile(ROOT_PRIVATE_KEY, keyPair.getPrivate().getEncoded());
        // 证书构造器
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                new X500Principal(ROOT_DN),
                BigInteger.ONE,
                NotBefore,
                NotAfter,
                new X500Principal(ROOT_DN),
                keyPair.getPublic())
                .addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign))
                .addExtension(Extension.basicConstraints, true, new BasicConstraints(true))
                .addExtension(Extension.cRLDistributionPoints, false, new CRLDistPoint(distributionPoints));
        // 签名构造器
        ContentSigner sigGen = new JcaContentSignerBuilder(SIG_ALG).setProvider("BC").build(keyPair.getPrivate());
        // 生成证书
        X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen));
        // 保存证书
        saveEncodedFile(ROOT_CRT_PATH, cert.getEncoded());
    }

    /**
     * 根据n和d生成RSA公钥
     *
     * @param n 模数n
     * @param e 公钥e，一般为65537
     * @return publicKey 公钥
     */
    public static PublicKey customRSAPublicKey(String n, String e) throws Exception {
        // 构造RSA公钥参数RSAPublicKeySpec
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(n, 16), new BigInteger(e, 16));
        // 使用RSAPublicKeySpec构造RSA公钥
        return KeyFactory.getInstance(ROOT_ALG, PROVIDER).generatePublic(spec);
    }

    /**
     * 根据参数生成ECC公钥
     *
     * @param curve ECC曲线名称
     * @param x     ECPoint:X
     * @param y     ECPoint:Y
     * @return publicKey 公钥
     */
    public static PublicKey customECPublicKey(String curve, String x, String y) throws Exception {
        // 根据params构造ECPublicKeyParameters
        ECParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec(curve);
        // 根据x和y构造ECPoint
        ECPoint ecPoint = ecParameterSpec.getCurve().createPoint(new BigInteger(x, 16), new BigInteger(y, 16));
        // 根据ECPublicKeyParameters和ECPoint构造ECPublicKeySpec
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);
        // 使用ECPublicKeySpec构造ECC公钥
        return KeyFactory.getInstance("EC", PROVIDER).generatePublic(ecPublicKeySpec);
    }

    /**
     * 根据 Subject 实体生成 X500Principal 类型的 SubjectDN
     *
     * @param subject subject实体
     * @return subjectDN
     */
    public static Map<String, Object> getSubjectDN(Subject subject, License license) {
        // 构建 X500Principal
        Map<String, Object> map = new HashMap<>();
        String commonName = subject.getCommonName();
        String organization = subject.getOrganization();
        String organizationalUnit = subject.getOrganizationalUnit();
        String country = subject.getCountry();
        String stateOrProvinceName = subject.getStateOrProvinceName();
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        if (StringUtils.isNotBlank(commonName)) {
            builder.addRDN(BCStyle.CN, commonName);
        }
        if (StringUtils.isNotBlank(organization)) {
            builder.addRDN(BCStyle.O, organization);
        }
        if (StringUtils.isNotBlank(organizationalUnit)) {
            builder.addRDN(BCStyle.OU, organizationalUnit);
        }
        if (StringUtils.isNotBlank(country)) {
            builder.addRDN(BCStyle.C, country);
        }
        if (StringUtils.isNotBlank(stateOrProvinceName)) {
            builder.addRDN(BCStyle.ST, stateOrProvinceName);
        }
        map.put("DNString", new X500Principal(builder.build().toString()));

        // 构建 subjectAlternativeNames
        List<GeneralName> subjectAlternativeNames = new ArrayList<>();
        if (subject.getEmail() != null) {
            subjectAlternativeNames.add(new GeneralName(GeneralName.rfc822Name, subject.getEmail()));
        }
        if (license != null && license.getContentHash() != null) {
            String licenseUrl = "https://www.jokerxin.com/license/" + license.getContentHash();
            subjectAlternativeNames.add(new GeneralName(GeneralName.uniformResourceIdentifier, licenseUrl));
        }
        if(subjectAlternativeNames.size() > 0) {
            map.put("subjectAlternativeNames", new GeneralNames(subjectAlternativeNames.toArray(new GeneralName[0])));
        }
        return map;
    }

    /**
     * 使用JcaX509v3CertificateBuilder生成用户数字证书
     *
     * @param subjectDN 使用者DN
     * @param publicKey 用户自定义公钥
     * @return X509Certificate证书
     */
    public static X509Certificate generateUserCert(Map<String, Object> subjectDN,
                                                   BigInteger serialNumber,
                                                   Date notBefore,
                                                   Date notAfter,
                                                   PublicKey publicKey) throws Exception {
        // 读取CA私钥
        PrivateKey privateKey = readPrivateKey(ROOT_PRIVATE_KEY, ROOT_ALG);
        // 证书构造器
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                new X500Principal(ROOT_DN),
                serialNumber,
                notBefore,
                notAfter,
                (X500Principal) subjectDN.get("DNString"),
                publicKey)
                .addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature))
                .addExtension(Extension.basicConstraints, false, new BasicConstraints(true))
                .addExtension(Extension.cRLDistributionPoints, false, new CRLDistPoint(distributionPoints));
        if(subjectDN.get("subjectAlternativeNames") != null) {
            certGen.addExtension(Extension.subjectAlternativeName, false, (GeneralNames) subjectDN.get("subjectAlternativeNames"));
        }
        // 签名构造器
        ContentSigner sigGen = new JcaContentSignerBuilder(SIG_ALG).setProvider("BC").build(privateKey);
        // 生成证书
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen));
    }

    /**
     * 将certificate转换为PEM格式
     *
     * @param certificate 证书
     * @return PEM格式证书
     */
    public static String X509CertificateToPem(X509Certificate certificate) throws Exception {
        StringWriter sw = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
        pemWriter.writeObject(certificate);
        pemWriter.close();
        return sw.toString();
    }

    /**
     * 生成CRL证书吊销列表
     *
     * @param serials 吊销的证书序列号
     * @return X509CRL证书吊销列表
     */
    public static X509CRL generateCRL(List<BigInteger> serials) throws Exception {
        // 读取CA私钥
        PrivateKey privateKey = readPrivateKey(ROOT_PRIVATE_KEY, ROOT_ALG);
        // 证书吊销列表构造器
        JcaX509v2CRLBuilder crlGen = new JcaX509v2CRLBuilder(new X500Principal(ROOT_DN), new Date());
        // 设置下次更新时间
        crlGen.setNextUpdate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24));
        // 设置吊销证书序列号
        for (BigInteger serial : serials) {
            crlGen.addCRLEntry(serial, new Date(), CRLReason.affiliationChanged);
        }
        // 签名构造器
        ContentSigner sigGen = new JcaContentSignerBuilder(SIG_ALG).setProvider("BC").build(privateKey);
        // 生成证书吊销列表
        return new JcaX509CRLConverter().setProvider("BC").getCRL(crlGen.build(sigGen));
    }

    /**
     * 从文件中读取证书
     *
     * @param certPath 证书文件路径
     * @return X509证书
     */
    public static X509Certificate readCert(String certPath) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(Files.newInputStream(Paths.get(certPath)));
    }

    /**
     * 从文件中读取私钥
     *
     * @param privateKeyPath 私钥文件路径
     * @param algorithm      签名算法
     * @return 私钥
     */
    public static PrivateKey readPrivateKey(String privateKeyPath, String algorithm) throws Exception {
        // 读取文件
        FileInputStream fileInputStream = new FileInputStream(privateKeyPath);
        byte[] bytes = new byte[fileInputStream.available()];
        if (fileInputStream.read(bytes) == -1) {
            throw new Exception("文件读取失败");
        }
        fileInputStream.close();
        // 生成私钥
        KeyFactory kf = KeyFactory.getInstance(algorithm, "BC");
        return kf.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    /**
     * 保存密钥或证书到文件
     *
     * @param filePath    文件路径
     * @param encodedFile 编码后的内容
     */
    public static void saveEncodedFile(String filePath, byte[] encodedFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(encodedFile);
        fileOutputStream.close();
    }

    /**
     * 判断根CA证书和私钥是否存在
     */
    public static boolean isRootExist() {
        return Files.exists(Paths.get(ROOT_CRT_PATH)) && Files.exists(Paths.get(ROOT_PRIVATE_KEY));
    }
}
