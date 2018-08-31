package com.terry.xplatform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.terry.xplatform.vo.SampleDefaultVO;
import com.terry.xplatform.vo.SampleVO;

@Mapper
public interface SampleDAO {

	public void insertSample(SampleVO sampleVO);
	public void updateSample(SampleVO sampleVO);
	public void deleteSample(@Param("id") int id);
	public SampleVO selectSample(@Param("id") int id);
	public List<SampleVO> selectSampleList(SampleDefaultVO sampleDefaultVO);
	public long selectSampleListTotCnt(SampleDefaultVO sampleDefaultVO);
	public Map<String, String> dataType();
	
}
