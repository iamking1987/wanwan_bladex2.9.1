package com.fun.common.utils.utils;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

/**
 * @author wanwan 2021/12/23
 */
public class ImageUtils {

	public static void main(String[] args) {
		String bass64String = getBase64ByUrl("http://39.108.62.171:19000/000000-wo-ai/upload/20211222/3886bb43ac63e53119aa29d6a2d5699d.jpg");
		System.out.println(bass64String);
	}

	public static String getBase64ByUrl(String urlPath){
		BufferedInputStream bufferedInputStream = null;
		InputStream inputStream = null;

		try {
			URL url = new URL(urlPath);
			byte[] by = new byte[1024];
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			httpURLConnection.setConnectTimeout(1000*5);
			httpURLConnection.connect();
			inputStream = httpURLConnection.getInputStream();

			bufferedInputStream = IoUtil.toBuffered(inputStream);
			byte[] bytes = IoUtil.readBytes(bufferedInputStream);


			return Base64.getMimeEncoder().encodeToString(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(inputStream);
			IoUtil.close(bufferedInputStream);
		}
		//return Base64.getMimeEncoder().encodeToString(data.toByteArray());
		//return Base64.getEncoder().encodeToString(data.toByteArray());

		return null;
	}

	/**
	 * 将base64编码保存为图片
	 * 		图片的格式为 data:image/png;base64,iVBORw0KGgo...
	 * 			逗号的前面为图片的格式，逗号后们为图片二进制数据的 Base64 编码字符串
	 */
	public static Boolean uploadBase64SignImage(String base64Str, String targetDir, String fileName) {
		//解析出图片到项目目录
		File file = new File(targetDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		int commaIndex = base64Str.indexOf(",");
		String extension = base64Str.substring(0, commaIndex).replaceAll("data:image/(.+);base64", "$1");
		byte[] content = org.apache.commons.codec.binary.Base64.decodeBase64(base64Str.substring(commaIndex));

		try {
			FileUtils.writeByteArrayToFile(new File(targetDir, fileName +"."+extension), content);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
