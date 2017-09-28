package org.springboot.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springboot.sample.entity.TWeedfsFile;

@Mapper
public interface TWeedfsFileMapper {

	// 插入文件
	Long insertFile(TWeedfsFile file);

	// 根据id查询文件
	TWeedfsFile selectById(Long id);
}
