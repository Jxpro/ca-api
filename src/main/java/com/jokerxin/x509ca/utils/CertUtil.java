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
    private static final String ROOT_DN = "CN=SelfSign Root CA,O=HNU,OU=CS,C=CN,ST=?????????";
    private static final DistributionPoint[] distributionPoints = new DistributionPoint[1];
    private static final Date NotBefore = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
    private static final Date NotAfter = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 10);

    static {
        // ??????BouncyCastle??????
        Security.addProvider(new BouncyCastleProvider());
        // ??????CRL distribution points?????????
        GeneralName generalName = new GeneralName(GeneralName.uniformResourceIdentifier, CRL_URL);
        DistributionPointName distributionPointName = new DistributionPointName(new GeneralNames(generalName));
        distributionPoints[0] = new DistributionPoint(distributionPointName, null, null);
    }

    /**
     * ?????????CA???RSA?????????
     *
     * @return keyPair ?????????
     */
    public static KeyPair generateFixedKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ROOT_ALG, PROVIDER);
        keyPairGenerator.initialize(ROOT_SIZE, new SecureRandom(ROOT_SEED.getBytes()));
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * ??????JcaX509v3CertificateBuilder?????????CA?????????????????????????????????
     */
    public static void generateRootCert() throws Exception {
        // ??????RSA?????????
        KeyPair keyPair = generateFixedKeyPair();
        // ????????????
        saveEncodedFile(ROOT_PRIVATE_KEY, keyPair.getPrivate().getEncoded());
        // ???????????????
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
        // ???????????????
        ContentSigner sigGen = new JcaContentSignerBuilder(SIG_ALG).setProvider(PROVIDER).build(keyPair.getPrivate());
        // ????????????
        X509Certificate cert = new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(certGen.build(sigGen));
        // ????????????
        saveEncodedFile(ROOT_CRT_PATH, cert.getEncoded());
    }

    /**
     * ??????n???d??????RSA??????
     *
     * @param n ??????n
     * @param e ??????e????????????65537
     * @return publicKey ??????
     */
    public static PublicKey customRSAPublicKey(String n, String e) throws Exception {
        // ??????RSA????????????RSAPublicKeySpec
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(n, 16), new BigInteger(e, 16));
        // ??????RSAPublicKeySpec??????RSA??????
        return KeyFactory.getInstance(ROOT_ALG, PROVIDER).generatePublic(spec);
    }

    /**
     * ??????????????????ECC??????
     *
     * @param curve ECC????????????
     * @param x     ECPoint:X
     * @param y     ECPoint:Y
     * @return publicKey ??????
     */
    public static PublicKey customECPublicKey(String curve, String x, String y) throws Exception {
        // ??????params??????ECPublicKeyParameters
        ECParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec(curve);
        // ??????x???y??????ECPoint
        ECPoint ecPoint = ecParameterSpec.getCurve().createPoint(new BigInteger(x, 16), new BigInteger(y, 16));
        // ??????ECPublicKeyParameters???ECPoint??????ECPublicKeySpec
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);
        // ??????ECPublicKeySpec??????ECC??????
        return KeyFactory.getInstance("EC", PROVIDER).generatePublic(ecPublicKeySpec);
    }

    /**
     * ?????? Subject ???????????? X500Principal ????????? SubjectDN
     *
     * @param subject subject??????
     * @return subjectDN
     */
    public static Map<String, Object> getSubjectDN(Subject subject, License license) {
        // ?????? X500Principal
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

        // ?????? subjectAlternativeNames
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
     * ??????JcaX509v3CertificateBuilder????????????????????????
     *
     * @param subjectDN ?????????DN
     * @param publicKey ?????????????????????
     * @return X509Certificate??????
     */
    public static X509Certificate generateUserCert(Map<String, Object> subjectDN,
                                                   BigInteger serialNumber,
                                                   Date notBefore,
                                                   Date notAfter,
                                                   PublicKey publicKey) throws Exception {
        // ??????CA??????
        PrivateKey privateKey = readPrivateKey(ROOT_PRIVATE_KEY, ROOT_ALG);
        // ???????????????
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
        // ???????????????
        ContentSigner sigGen = new JcaContentSignerBuilder(SIG_ALG).setProvider(PROVIDER).build(privateKey);
        // ????????????
        return new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(certGen.build(sigGen));
    }

    /**
     * ???certificate?????????PEM??????
     *
     * @param certificate ??????
     * @return PEM????????????
     */
    public static String X509CertificateToPem(X509Certificate certificate) throws Exception {
        StringWriter sw = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
        pemWriter.writeObject(certificate);
        pemWriter.close();
        return sw.toString();
    }

    /**
     * ??????CRL??????????????????
     *
     * @param serials ????????????????????????
     * @return X509CRL??????????????????
     */
    public static X509CRL generateCRL(List<BigInteger> serials) throws Exception {
        // ??????CA??????
        PrivateKey privateKey = readPrivateKey(ROOT_PRIVATE_KEY, ROOT_ALG);
        // ???????????????????????????
        JcaX509v2CRLBuilder crlGen = new JcaX509v2CRLBuilder(new X500Principal(ROOT_DN), new Date());
        // ????????????????????????
        crlGen.setNextUpdate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24));
        // ???????????????????????????
        for (BigInteger serial : serials) {
            crlGen.addCRLEntry(serial, new Date(), CRLReason.affiliationChanged);
        }
        // ???????????????
        ContentSigner sigGen = new JcaContentSignerBuilder(SIG_ALG).setProvider(PROVIDER).build(privateKey);
        // ????????????????????????
        return new JcaX509CRLConverter().setProvider(PROVIDER).getCRL(crlGen.build(sigGen));
    }

    /**
     * ????????????????????????
     *
     * @param certPath ??????????????????
     * @return X509??????
     */
    public static X509Certificate readCert(String certPath) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(Files.newInputStream(Paths.get(certPath)));
    }

    /**
     * ????????????????????????
     *
     * @param privateKeyPath ??????????????????
     * @param algorithm      ????????????
     * @return ??????
     */
    public static PrivateKey readPrivateKey(String privateKeyPath, String algorithm) throws Exception {
        // ????????????
        FileInputStream fileInputStream = new FileInputStream(privateKeyPath);
        byte[] bytes = new byte[fileInputStream.available()];
        if (fileInputStream.read(bytes) == -1) {
            throw new Exception("??????????????????");
        }
        fileInputStream.close();
        // ????????????
        KeyFactory kf = KeyFactory.getInstance(algorithm, PROVIDER);
        return kf.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    /**
     * ??????????????????????????????
     *
     * @param filePath    ????????????
     * @param encodedFile ??????????????????
     */
    public static void saveEncodedFile(String filePath, byte[] encodedFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(encodedFile);
        fileOutputStream.close();
    }

    /**
     * ?????????CA???????????????????????????
     */
    public static boolean isRootExist() {
        return Files.exists(Paths.get(ROOT_CRT_PATH)) && Files.exists(Paths.get(ROOT_PRIVATE_KEY));
    }
}
