package com.coder520.mamabike.security;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 黄俊聪 on 2018/6/6.
 */
public class AESUtil {

    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";

    /**
     *@Author 黄俊聪
     *@Date 2017/12/15 15:44
     *@Description AES对称加密
     * @param data
     * @param key key需要16位
     */

    public static String encrypt(String data,String key){
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"),KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE , spec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     *@Author 黄俊聪
     *@Date 2017/12/15 15:50
     *@Description
     * AES对称解密 key需要16位
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE , spec , new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    public static void main(String[] args) throws Exception {
        /**AES加密数据**/
        String key = "123456789abcdfgt";
        String dataToEn = "{'mobile':'15363382075','code':'6666','platform':'android'}";
        String enResult = encrypt(dataToEn, key);
        System.out.println(enResult);
//        String deResult = decrypt(enResult,key);
//        System.out.println(deResult);

        /**RSA 加密AES的密钥**/
        byte[] enKey = RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuVRY8B3+Af5euC9WbgNkJKAiBzqOvrYi9mSST78jd4clpn7vkYHDfHzJiqFz9wjNRLzg9MUREF53bw9yhSljZ7F8JPMryfe8RR2Ed6CJq5nCy/2hvTTw4L6ypDemwe9f9yjIg52oPRPwU8lm8Uj3wKhjedDmZrkO1TAmt3sbQtwIDAQAB");
        String baseKey = Base64Util.encode(enKey);
        System.out.println(baseKey);

        //服务端RSA解密AES的key
        byte[] de = Base64Util.decode(baseKey);
        byte[] deKeyResult = RSAUtil.decryptByPrivateKey(de);
        System.out.println(new String(deKeyResult,"UTF-8"));

        String deResult = decrypt(enResult,key);
        System.out.println(deResult);
    }

}
