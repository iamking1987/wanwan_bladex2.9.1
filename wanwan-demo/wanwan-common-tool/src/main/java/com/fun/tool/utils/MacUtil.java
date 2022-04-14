package com.fun.tool.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * mac加密工具类
 * @author wanwan 2022/1/30
 */
public class MacUtil {

	/*public static void main(String[] args) {
		//treeMap完成自动排序+自然排序
		Map<Object, Object> requestData = new TreeMap<>();
		Map<Object, Object> requestHeader = new TreeMap<>();
		requestHeader.put("userip", "127.0.0.1");
		requestHeader.put("msgid", "sj4owsh20pmdrmlnphu");
		requestHeader.put("sourceid", SOURCE_ID);
		requestHeader.put("systemtime", "20180717174032375");
		requestHeader.put("version", "1.0");
		requestHeader.put("apptype", "2");//不能是int型，一定要string型
		//...组装header报文
		Map<Object, Object> requestBody = new TreeMap<>();
		requestBody.put("token", "SSO_AT8a87319cc4bcce6071789c995b9c1b6f");
		requestData.put("header", requestHeader);
		requestData.put("body", requestBody);
		System.out.println("json串，得到mac:"+hmacsha256(SOURCEKEY, JSON.toJSONString(requestData)));
	}*/

	private static final String HMAC_ALGORITHM = "HmacSHA256";

	public static String hmacsha256(String secret, String data) {
		Mac mac;
		byte[] doFinal;
		try {
			mac = Mac.getInstance(HMAC_ALGORITHM);
			//先对排序后的字符串进行MD5
			byte[] dataBytes = DigestUtils.md5(data);
			//对sourcekey进行MD5,得到密钥
			SecretKey secretkey = new SecretKeySpec(DigestUtils.md5(secret), HMAC_ALGORITHM);
			mac.init(secretkey);
			//HmacSHA256加密
			doFinal = mac.doFinal(dataBytes);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
		String checksum = Hex.encodeHexString(doFinal).toLowerCase();
		return checksum;
	}
}
