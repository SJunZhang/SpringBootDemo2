package org.springboot.sample.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WeedfsFileService {
	
	//通过weedfs文件上传的接口定义
	int doUpload(HttpServletRequest request) throws Exception;
	
	//通过weedfs文件下载的接口定义
	HttpServletResponse doDownload(HttpServletResponse response,Long id) throws Exception;
}
