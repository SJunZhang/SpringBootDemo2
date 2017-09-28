package org.springboot.sample.entity;

import java.io.Serializable;

/**
 * 数据库存储weedfs返回的fid以及文件一些信息，封装到该类下面。
 * @author DELL
 *
 */
public class TWeedfsFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String fid;
	private String fileName;
	private String uuidName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUuidName() {
		return uuidName;
	}

	public void setUuidName(String uuidName) {
		this.uuidName = uuidName;
	}

}
