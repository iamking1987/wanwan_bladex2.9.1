package com.fun.common.utils.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * @author zz
 * @date 2021/9/14
 */
public class FileTransferUtils {

	/*public static void main(String[] args) {
		//获取类路径-spring自带
		//输出：/E:/Desktop/SpringBlade/blade-demo/web-common-20210720/target/classes/
		String cp1= ClassUtils.getDefaultClassLoader().getResource("").getPath();
		//获取类路径-hutool
		//输出：E:/Desktop/SpringBlade/blade-demo/web-common-20210720/target/classes/
		String cp2 = ClassUtil.getClassPath();
	}*/

	private static final Logger logger = LoggerFactory.getLogger(FileTransferUtils.class);

	/**
	 * 下载文件
	 * @param response 响应
	 * @param filePath 文件路径（注意带上分隔符）
	 * @param fileName 文件名（注意带上后缀）
	 */
	public static Boolean download(HttpServletResponse response, String filePath, String fileName) {

		//支持中文
		try {
			fileName = URLEncoder.encode(fileName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		// 设置强制下载不打开
		response.setContentType("application/force-download");
		// 设置文件名
//			response.addHeader("Content-Disposition", "attachment;fileName=" + filePath.substring(filePath.lastIndexOf(File.separator)));
		response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
		//路径是否存在，不存在创建
		File file = new File(filePath);

		if (!file.exists()) {
			file.mkdirs();
		}

		try (FileInputStream inputStream = new FileInputStream(filePath+fileName);
			 ServletOutputStream outputStream = response.getOutputStream()){
			IOUtils.copy(inputStream, outputStream);
			logger.info("文件开始下载...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("文件下载失败...");
			return false;
		}
	}

	/**
	 * 将File对象转为MultipartFile
	 * @param sourceDir 目标路径
	 * @param sourceFile 目标文件名（带后缀）
	 */
	public static MultipartFile File2MultipartFile(String sourceDir, String sourceFile) {

		File targetFile = new File(sourceDir+sourceFile);
		if (!targetFile.exists()) {
			logger.error("未找到文件！");
			return null;
		}
		FileItem fileItem;
		try {
			fileItem = new DiskFileItem(targetFile.getName(), Files.probeContentType(targetFile.toPath()),
				false, targetFile.getName(), (int) targetFile.length(), targetFile.getParentFile());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		try (InputStream input = new FileInputStream(targetFile);
			 OutputStream os = fileItem.getOutputStream()) {
			MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
			org.apache.commons.io.IOUtils.copy(input, os);
			return multipartFile;
		} catch (Exception e) {
			e.getStackTrace();
			throw new RuntimeException("File转MultipartFile失败");
		}
	}
}
