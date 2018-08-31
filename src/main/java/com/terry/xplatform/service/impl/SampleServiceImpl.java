package com.terry.xplatform.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.terry.xplatform.dao.SampleDAO;
import com.terry.xplatform.service.SampleService;
import com.terry.xplatform.vo.SampleDefaultVO;
import com.terry.xplatform.vo.SampleVO;
import com.tobesoft.xplatform.data.DataSet;

@Service
public class SampleServiceImpl implements SampleService {
	
	@Autowired
	private SqlSession sqlSession;
	
	SampleDAO sampleDAO;
	
	@PostConstruct
	public void init() {
		sampleDAO = sqlSession.getMapper(SampleDAO.class);
	}
	
	@Override
	public List<SampleVO> list(SampleDefaultVO sampleDefaultVO) throws DataAccessException {
		// TODO Auto-generated method stub
		List<SampleVO> result = sampleDAO.selectSampleList(sampleDefaultVO);
		return result;
	}

	@Override
	public void modify(List<SampleVO> dataSet) throws DataAccessException {
		// TODO Auto-generated method stub
		for(SampleVO sampleVO : dataSet) {
			if(sampleVO.isStoreDataChanges()) {
				int rowType = sampleVO.getRowType();
				switch(rowType) {
				case DataSet.ROW_TYPE_INSERTED :
					sampleDAO.insertSample(sampleVO);
					break;
				case DataSet.ROW_TYPE_UPDATED :
					sampleDAO.updateSample(sampleVO);
					break;
				case DataSet.ROW_TYPE_DELETED : 
					sampleDAO.deleteSample(sampleVO.getId());
					break;
				}
			} else {
				int rowType = sampleVO.getRowType();
				switch(rowType) {
				case DataSet.ROW_TYPE_NORMAL :						// ROW_TYPE_NORMAL로 받을 경우 현재 행이 insert 상태인지 update 상태인지를 알 수 없기 때문에 delete, insert 작업을 통해서 두 가지 상황을 모두 만족시키도록 한다
					sampleDAO.deleteSample(sampleVO.getId());
					sampleDAO.insertSample(sampleVO);
					break;
				case DataSet.ROW_TYPE_DELETED : 
					sampleDAO.deleteSample(sampleVO.getId());
					break;
				}
			}
			
		}
	}

	

}
