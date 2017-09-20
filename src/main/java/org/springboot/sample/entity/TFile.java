package org.springboot.sample.entity;

import java.io.Serializable;
import java.sql.Blob;

public class TFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private byte[] fileContent;
	private String fileName;
	private String fileSuffix;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

}
