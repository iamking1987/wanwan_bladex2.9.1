package com.fun.tool.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName: AESUtil
 * @Descritption:
 * @Author: zhangguoming
 * @Create: 2022/2/25 18:31
 **/
public class AESUtil {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String PRIVKEY="c7ec3926e96d42e4b35bc84c366f0770";
    private static final int IV_SIZE = 16;

    public static String encryptBase64(byte[] data){
        return encryptBase64(PRIVKEY,data);
    }

    public static String decryptBase64(String data){
        return new String(decryptBase64(PRIVKEY,data));
    }

    public static String encryptBase64(String key, byte[] data) {
        return Base64.encodeBase64String(encrypt(key, data));
    }

    public static byte[] decryptBase64(String key, String data) {
        return decrypt(key, Base64.decodeBase64(data));
    }


    public static byte[] encrypt(String key, byte[] data) {
        byte[] keyBytes = DigestUtils.md5(key);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, ALGORITHM), new IvParameterSpec(keyBytes, 0, IV_SIZE));
            return cipher.doFinal(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] decrypt(String key, byte[] data) {
        byte[] keyBytes = DigestUtils.md5(key);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, ALGORITHM), new IvParameterSpec(keyBytes, 0, IV_SIZE));
            return cipher.doFinal(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

	/*public static void main(String[] args) {
		String encrypt = encryptBase64("19876134324".getBytes(StandardCharsets.UTF_8));
		System.out.println(encrypt);
	}*/
}
