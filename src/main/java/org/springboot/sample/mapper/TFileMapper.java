package org.springboot.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springboot.sample.entity.TFile;

@Mapper
public interface TFileMapper {
	//插入文件
	Long insertFile(TFile file);
	
	//根据id查询文件
	TFile selectById(Long id);

}
