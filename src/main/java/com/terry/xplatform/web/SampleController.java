package com.terry.xplatform.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.terry.xplatform.config.xplatform.annotation.RequestDataSet;
import com.terry.xplatform.config.xplatform.annotation.RequestDataSetList;
import com.terry.xplatform.config.xplatform.annotation.RequestVariable;
import com.terry.xplatform.service.SampleService;
import com.terry.xplatform.vo.SampleVO;
import com.tobesoft.xplatform.data.DataSetList;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SampleController {

	@Autowired
	SampleService sampleService;

	@RequestMapping("egovSampleSelect")
	public void list(Model model
			, SampleVO sampleVO
			, @RequestVariable SampleVO requestVariableSampleVO
			, @RequestVariable Map<String, Object> requestVariableMap
			, @RequestVariable(name="firstIndex") int firstIndex
			, @RequestVariable(name="recordCountPerPage") String recordCountPerPage
			, HttpServletRequest httpServletRequest) {
	// public void list(Model model, SampleVO sampleVO, @RequestVariable SampleVO requestVariableSampleVO) {
		List<SampleVO> sampleList = sampleService.list(sampleVO);
		model.addAttribute("ds_output", sampleList);
	}

	/*
	@RequestMapping("/egovSampleSelect")
	public void list(HttpServletRequest httpServletRequest) {
		String firstIndex = httpServletRequest.getParameter("firstIndex");
		String recordCountPerPage = httpServletRequest.getParameter("recordCountPerPage");
	}
	*/

	@RequestMapping("/egovSampleModify")
	public void modify(@RequestDataSet(name="ds_input")ArrayList<SampleVO> dataSet
						, @RequestDataSet(name="ds_input")List<Map<String, Object>> dataSetMap
						, @RequestDataSet(name="ds_input")Set<SampleVO> dataHashSet
						, @RequestDataSetList DataSetList dataSetList) {
		logger.info("modify");
		sampleService.modify(dataSet);
	}

}
