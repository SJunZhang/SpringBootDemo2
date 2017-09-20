package org.springboot.sample.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springboot.sample.entity.TFile;
import org.springboot.sample.service.TFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 写的文件存储到数据库的代码，上传下载
 * @author DELL
 *
 */
@RestController
@RequestMapping("tfile/")
public class TFileController {
	private Logger logger = LoggerFactory.getLogger(TFileController.class);
	@Autowired
	private TFileService tfileService;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public Long insertFile(HttpServletRequest request) throws Exception {
		TFile tfile = new TFile();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			Iterator iter = multipartHttpServletRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multipartHttpServletRequest.getFile(iter.next().toString());
				if (file != null) {
					byte[] b = FileCopyUtils.copyToByteArray(file.getInputStream());
					if (b == null || b.length == 0) {
						logger.warn("上传失败");
						return 0l;
					}
					// 获取文件名
					String fileName = file.getOriginalFilename();
					// 获取文件的后缀名
					String suffixName = fileName.substring(fileName.lastIndexOf("."));
					tfile.setFileContent(b);// mysql数据库中存储文件用blob类型，在Javabean中用byte[]类型
					tfile.setFileName(fileName);
					tfile.setFileSuffix(suffixName);

				}
			}
		}

		return tfileService.insertFile(tfile);

	}

	/**
	 * http://blog.csdn.net/linwei_1029/article/details/7010573
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public HttpServletResponse getTFileById(@PathVariable ("id") String id, HttpServletResponse response) throws Exception {
		Long lid = Long.parseLong(id);
		logger.info(id);
		TFile tfile = tfileService.selectById(lid);//从数据库查询出来
		byte[] b = tfile.getFileContent();
		String newName = URLEncoder.encode(tfile.getFileName(), "utf-8").replaceAll("\\+", "%20")
				.replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@")
				.replaceAll("%23", "\\#").replaceAll("%26", "\\&");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
		OutputStream ost = response.getOutputStream();
		ost.write(b);//写到流中
		ost.flush();
		ost.close();
		return null;//解决getOutputStream() has already been called for this respons报错的问题。
	}

}
