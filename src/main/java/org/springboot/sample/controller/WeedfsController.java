package org.springboot.sample.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springboot.sample.service.WeedfsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * weedfs文件上传下载的UI层
 * @author DELL
 *
 */
@RestController
@RequestMapping("weedfs/")
public class WeedfsController {
	
	@Autowired
	private WeedfsFileService weedfsFileService;
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public int upload(HttpServletRequest request) throws Exception{
		return weedfsFileService.doUpload(request);
	}
	
	/**
	 * 参数id是t_weedfs_file表的主键id
	 * @param response
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="download/{id}",method=RequestMethod.GET)
	public HttpServletResponse download(HttpServletResponse response,@PathVariable("id") String id) throws Exception{
		Long lid = Long.parseLong(id);
		return weedfsFileService.doDownload(response, lid);
	}

}
