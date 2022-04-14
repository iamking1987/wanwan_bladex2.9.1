/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springzhisuan.resource.endpoint;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springzhisuan.core.oss.model.ZhisuanFile;
import org.springzhisuan.core.oss.model.OssFile;
import org.springzhisuan.core.secure.annotation.PreAuth;
import org.springzhisuan.core.tenant.annotation.NonDS;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.constant.RoleConstant;
import org.springzhisuan.core.tool.utils.FileUtil;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.resource.builder.oss.OssBuilder;
import org.springzhisuan.resource.entity.Attach;
import org.springzhisuan.resource.service.IAttachService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 对象存储端点
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
@RequestMapping("/oss/endpoint")
@Api(value = "对象存储端点", tags = "对象存储端点")
public class OssEndpoint {

	/**
	 * 对象存储构建类
	 */
	private final OssBuilder ossBuilder;

	/**
	 * 附件表服务
	 */
	private final IAttachService attachService;

	/**
	 * 创建存储桶
	 *
	 * @param bucketName 存储桶名称
	 * @return Bucket
	 */
	@SneakyThrows
	@PostMapping("/make-bucket")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R makeBucket(@RequestParam String bucketName) {
		ossBuilder.template().makeBucket(bucketName);
		return R.success("创建成功");
	}

	/**
	 * 创建存储桶
	 *
	 * @param bucketName 存储桶名称
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/remove-bucket")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R removeBucket(@RequestParam String bucketName) {
		ossBuilder.template().removeBucket(bucketName);
		return R.success("删除成功");
	}

	/**
	 * 拷贝文件
	 *
	 * @param fileName       存储桶对象名称
	 * @param destBucketName 目标存储桶名称
	 * @param destFileName   目标存储桶对象名称
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/copy-file")
	public R copyFile(@RequestParam String fileName, @RequestParam String destBucketName, String destFileName) {
		ossBuilder.template().copyFile(fileName, destBucketName, destFileName);
		return R.success("操作成功");
	}

	/**
	 * 获取文件信息
	 *
	 * @param fileName 存储桶对象名称
	 * @return InputStream
	 */
	@SneakyThrows
	@GetMapping("/stat-file")
	public R<OssFile> statFile(@RequestParam String fileName) {
		return R.data(ossBuilder.template().statFile(fileName));
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param fileName 存储桶对象名称
	 * @return String
	 */
	@SneakyThrows
	@GetMapping("/file-path")
	public R<String> filePath(@RequestParam String fileName) {
		return R.data(ossBuilder.template().filePath(fileName));
	}


	/**
	 * 获取文件外链
	 *
	 * @param fileName 存储桶对象名称
	 * @return String
	 */
	@SneakyThrows
	@GetMapping("/file-link")
	public R<String> fileLink(@RequestParam String fileName) {
		return R.data(ossBuilder.template().fileLink(fileName));
	}

	/**
	 * 上传文件
	 *
	 * @param file 文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file")
	public R<ZhisuanFile> putFile(@RequestParam MultipartFile file) {
		ZhisuanFile zhisuanFile = ossBuilder.template().putFile(file.getOriginalFilename(), file.getInputStream());
		return R.data(zhisuanFile);
	}

	/**
	 * 上传文件
	 *
	 * @param fileName 存储桶对象名称
	 * @param file     文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file-by-name")
	public R<ZhisuanFile> putFile(@RequestParam String fileName, @RequestParam MultipartFile file) {
		ZhisuanFile zhisuanFile = ossBuilder.template().putFile(fileName, file.getInputStream());
		return R.data(zhisuanFile);
	}

	/**
	 * 上传文件并保存至附件表
	 *
	 * @param file 文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file-attach")
	public R<ZhisuanFile> putFileAttach(@RequestParam MultipartFile file) {
		String fileName = file.getOriginalFilename();
		ZhisuanFile zhisuanFile = ossBuilder.template().putFile(fileName, file.getInputStream());
		Long attachId = buildAttach(fileName, file.getSize(), zhisuanFile);
		zhisuanFile.setAttachId(attachId);
		return R.data(zhisuanFile);
	}

	/**
	 * 上传文件并保存至附件表
	 *
	 * @param fileName 存储桶对象名称
	 * @param file     文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file-attach-by-name")
	public R<ZhisuanFile> putFileAttach(@RequestParam String fileName, @RequestParam MultipartFile file) {
		ZhisuanFile zhisuanFile = ossBuilder.template().putFile(fileName, file.getInputStream());
		Long attachId = buildAttach(fileName, file.getSize(), zhisuanFile);
		zhisuanFile.setAttachId(attachId);
		return R.data(zhisuanFile);
	}

	/**
	 * 构建附件表
	 *
	 * @param fileName  文件名
	 * @param fileSize  文件大小
	 * @param zhisuanFile 对象存储文件
	 * @return attachId
	 */
	private Long buildAttach(String fileName, Long fileSize, ZhisuanFile zhisuanFile) {
		String fileExtension = FileUtil.getFileExtension(fileName);
		Attach attach = new Attach();
		attach.setDomainUrl(zhisuanFile.getDomain());
		attach.setLink(zhisuanFile.getLink());
		attach.setName(zhisuanFile.getName());
		attach.setOriginalName(zhisuanFile.getOriginalName());
		attach.setAttachSize(fileSize);
		attach.setExtension(fileExtension);
		attachService.save(attach);
		return attach.getId();
	}

	/**
	 * 删除文件
	 *
	 * @param fileName 存储桶对象名称
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/remove-file")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R removeFile(@RequestParam String fileName) {
		ossBuilder.template().removeFile(fileName);
		return R.success("操作成功");
	}

	/**
	 * 批量删除文件
	 *
	 * @param fileNames 存储桶对象名称集合
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/remove-files")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R removeFiles(@RequestParam String fileNames) {
		ossBuilder.template().removeFiles(Func.toStrList(fileNames));
		return R.success("操作成功");
	}

}
