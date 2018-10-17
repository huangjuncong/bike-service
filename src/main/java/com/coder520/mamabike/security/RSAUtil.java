package com.coder520.mamabike.security;

/**
 * Created by 黄俊聪 on 2017/12/15.
 */


import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 *@Author 黄俊聪
 *@Date 2018/6/6.
 *@Description 非对称加密解密工具类
 */
public class RSAUtil {

    /**
     * 私钥字符串
     */
    private static String PRIVATE_KEY ="";
    /**
     * 公钥字符串
     */
    private static String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuVRY8B3+Af5euC9WbgNkJKAiBzqOvrYi9mSST78jd4clpn7vkYHDfHzJiqFz9wjNRLzg9MUREF53bw9yhSljZ7F8JPMryfe8RR2Ed6CJq5nCy/2hvTTw4L6ypDemwe9f9yjIg52oPRPwU8lm8Uj3wKhjedDmZrkO1TAmt3sbQtwIDAQAB";


    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 读取密钥字符串
     * @throws Exception
     */

    public static void convert() throws Exception {
        byte[] data = null;

        try {
            InputStream is = RSAUtil.class.getResourceAsStream("/enc_pri");
            int length = is.available();
            data = new byte[length];
            is.read(data);
        } catch (Exception e) {
        }

        String dataStr = new String(data);
        try {
            PRIVATE_KEY = dataStr;
        } catch (Exception e) {
        }

        if (PRIVATE_KEY == null) {
            throw new Exception("Fail to retrieve key");
        }
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/15 18:09
     *@Description RSA私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
        //从流中读取密钥字符串
        convert();
//        //base64解码
//        byte[] keyBytes = Base64Util.decode(PRIVATE_KEY);
//        //用PKCS8EncodedKeySpec这种模式解密
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//        //选用RSA非对称算法进行解密
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //获取私钥
//        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Key privateKey = makePrivateKey(PRIVATE_KEY);

//        //用RSA算法获取Cipher
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //用RSA算法获取Cipher
        Cipher cipher = Cipher.getInstance("RSA");
        //初始化解密模式
        cipher.init(Cipher.DECRYPT_MODE,privateKey);

        return cipher.doFinal(data);
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/15 18:18
     *@Description RSA公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        //base64解码
        byte[] keyBytes = Base64Util.decode(key);
        //用X509EncodedKeySpec这种模式加密
        X509EncodedKeySpec pkcs8KeySpec = new X509EncodedKeySpec(keyBytes);
        //选用RSA非对称算法进行加密
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //获取公钥
        Key publicKey = keyFactory.generatePublic(pkcs8KeySpec);

        //用RSA算法获取Cipher
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //初始化加密模式
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     *  引入第三方密码工具包 处理编码
     * @param stored
     * @return
     * @throws GeneralSecurityException
     * @throws Exception
     */
    public static PrivateKey makePrivateKey(String stored) throws GeneralSecurityException, Exception {
        /*byte[] data = Base64.getDecoder().decode(stored);
        PKCS8EncodedKeySpec spec = new  PKCS8EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePrivate(spec);*/
        byte[] data = Base64Util.decode(stored);
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(0));
        ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
        v2.add(DERNull.INSTANCE);
        v.add(new DERSequence(v2));
        v.add(new DEROctetString(data));
        ASN1Sequence seq = new DERSequence(v);
        byte[] privKey = seq.getEncoded("DER");
        PKCS8EncodedKeySpec spec = new  PKCS8EncodedKeySpec(privKey);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey key = fact.generatePrivate(spec);

        return key;

    }


    public static void main(String[] args) throws Exception {
//        //根据RSA生成一对key,即公钥和私钥
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
//        //设置key的大小，1024K
//        keyPairGenerator.initialize(1024);
//        //获取key对的对象
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        //获取私钥
//        PrivateKey privateKey = keyPair.getPrivate();
//        //获取公钥
//        PublicKey publicKey = keyPair.getPublic();
//        System.out.println(Base64Util.encode(publicKey.getEncoded()));
//        System.out.println(Base64Util.encode(privateKey.getEncoded()));

        String data = "黄俊聪来了";
        byte[] enResult = encryptByPublicKey(data.getBytes("UTF-8"), PUBLIC_KEY);
        System.out.println(enResult.toString());
        byte[] deResult = decryptByPrivateKey(enResult);
        System.out.println(new String(deResult,"UTF-8"));
    }


}
