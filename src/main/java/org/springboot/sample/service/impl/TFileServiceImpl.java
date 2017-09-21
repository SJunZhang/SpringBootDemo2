package org.springboot.sample.service.impl;

import org.springboot.sample.entity.TFile;
import org.springboot.sample.mapper.TFileMapper;
import org.springboot.sample.service.TFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tfileService")
public class TFileServiceImpl implements TFileService {
	@Autowired
	private TFileMapper tfileMapper;

	@Override
	public Long insertFile(TFile file) {
		return tfileMapper.insertFile(file);
	}

	@Override
	public TFile selectById(Long id) {
		return tfileMapper.selectById(id);
	}

}
