package org.springboot.sample.controller;

import javax.servlet.http.HttpServletRequest;

import org.springboot.sample.service.WeedfsUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * weedfs文件上传的UI层
 * @author DELL
 *
 */
@RestController
@RequestMapping("weedfs/")
public class WeedfsController {
	
	@Autowired
	private WeedfsUploadService weedfsUploadService;
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public int upload(HttpServletRequest request) throws Exception{
		return weedfsUploadService.doUpload(request);
	}

}
