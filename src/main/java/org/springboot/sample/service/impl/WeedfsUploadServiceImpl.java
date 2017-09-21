package org.springboot.sample.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.util.Streams;
import org.springboot.sample.service.WeedfsUploadService;
import org.springboot.sample.weedfsClient.RequestResult;
import org.springboot.sample.weedfsClient.WeedFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Service
public class WeedfsUploadServiceImpl implements WeedfsUploadService {

	@Value("${masterUrl}")
	private String masterUrl;// 注入配置文件的masterurl

	// 下载文件并保存在服务器上时，最好使用当前操作系统的分隔符
	private String sepa = java.io.File.separator;

	@Override
	public int doUpload(HttpServletRequest request) throws Exception {
		String host = masterUrl.substring(7, masterUrl.lastIndexOf(":"));
		String port = masterUrl.substring(masterUrl.lastIndexOf(":") + 1);
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multipartHttpServletRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multipartHttpServletRequest.getFile(iter.next());
				if (file != null) {
					WeedFSClient weedfsClient = new WeedFSClient(host, port);
					String fileName = file.getOriginalFilename();
					// 获取文件的后缀名
					String suffixName = fileName.substring(fileName.lastIndexOf("."));
					// 文件上传后的路径
					String savedDir = System.getProperty("user.dir");
					// 解决中文问题，liunx下中文路径，图片显示问题
					fileName = UUID.randomUUID() + suffixName;
					String path = savedDir + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "config" + sepa
							+ fileName;
					File dest = new File(path);
					// 检测是否存在目录
					if (!dest.getParentFile().exists()) {
						dest.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(dest);
					Streams.copy(file.getInputStream(), fos, true);
					RequestResult result = weedfsClient.write(dest.getAbsolutePath());
					result.getFid();

					//删除临时文件
					if(dest.exists()){
						dest.delete();
					}
				}
			}
		}
		return 0;
	}

}
