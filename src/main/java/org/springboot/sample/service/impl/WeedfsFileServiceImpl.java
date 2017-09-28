package org.springboot.sample.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.util.Streams;
import org.springboot.sample.entity.TWeedfsFile;
import org.springboot.sample.mapper.TWeedfsFileMapper;
import org.springboot.sample.service.WeedfsFileService;
import org.springboot.sample.weedfsClient.RequestResult;
import org.springboot.sample.weedfsClient.WeedFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Service
public class WeedfsFileServiceImpl implements WeedfsFileService {

	@Value("${masterUrl}")
	private String masterUrl;// 注入配置文件的masterurl
	
	@Autowired
	private TWeedfsFileMapper tWeedfsFileMapper;

	// 下载文件并保存在服务器上时，最好使用当前操作系统的分隔符
	private String sepa = java.io.File.separator;

	
	//通过weedfs文件上传的逻辑实现
	@Override
	public int doUpload(HttpServletRequest request) throws Exception {
		Long entityId=0l;//文件信息插入表中返回自动递增的注解id值
		TWeedfsFile tWeedfsFile = new TWeedfsFile();
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
					String uuidfileName = UUID.randomUUID() + suffixName;
					String path = savedDir + sepa + "src" + sepa + "main" + sepa + "resources" + sepa + "config" + sepa
							+ uuidfileName;
					File dest = new File(path);
					// 检测是否存在目录
					if (!dest.getParentFile().exists()) {
						dest.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(dest);
					Streams.copy(file.getInputStream(), fos, true);
					RequestResult result = weedfsClient.write(dest.getAbsolutePath());
					String fid = result.getFid();//weedfs返回一个fid
					
					tWeedfsFile.setFid(fid);
					tWeedfsFile.setFileName(fileName);
					tWeedfsFile.setUuidName(uuidfileName);
					
					tWeedfsFileMapper.insertFile(tWeedfsFile);
					entityId=tWeedfsFile.getId();//获取当前插入数据的主键id
					//删除临时文件
					if(dest.exists()){
						dest.delete();
					}
				}
			}
		}
		
		return entityId.intValue();
	}

	
	//通过weedfs文件下载的逻辑实现
	@Override
	public HttpServletResponse doDownload(HttpServletResponse response,Long id) throws Exception {
		String host = masterUrl.substring(7, masterUrl.lastIndexOf(":"));
		String port = masterUrl.substring(masterUrl.lastIndexOf(":") + 1);
		WeedFSClient weedfsClient = new WeedFSClient(host, port);
		TWeedfsFile tWeedfsFile= tWeedfsFileMapper.selectById(id);
		InputStream input = weedfsClient.read(tWeedfsFile.getFid());
        String newName = URLEncoder.encode(tWeedfsFile.getFileName(), "utf-8").replaceAll("\\+", "%20")
				.replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@")
				.replaceAll("%23", "\\#").replaceAll("%26", "\\&");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
		OutputStream output = response.getOutputStream();
		int len;
        byte buffer[] = new byte[1024];
        while ((len = input.read(buffer)) != -1) {
          output.write(buffer, 0, len);
        }
        output.flush();
        input.close();
        output.close();
		return null;
	}

}
