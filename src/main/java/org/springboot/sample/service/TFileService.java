package org.springboot.sample.service;

import org.springboot.sample.entity.TFile;

public interface TFileService {
	// 插入文件
	Long insertFile(TFile file);

	// 根据id查询文件
	TFile selectById(Long id);

}
