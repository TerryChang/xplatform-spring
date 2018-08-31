package com.terry.xplatform.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.terry.xplatform.vo.SampleDefaultVO;
import com.terry.xplatform.vo.SampleVO;

public interface SampleService {

	public void modify(List<SampleVO> dataSet) throws DataAccessException;
	public List<SampleVO> list(SampleDefaultVO sampleDefaultVO) throws DataAccessException;
}
