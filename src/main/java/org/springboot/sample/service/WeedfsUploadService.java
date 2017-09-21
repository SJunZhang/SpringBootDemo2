package org.springboot.sample.service;

import javax.servlet.http.HttpServletRequest;

public interface WeedfsUploadService {
	
	int doUpload(HttpServletRequest request) throws Exception;
}
