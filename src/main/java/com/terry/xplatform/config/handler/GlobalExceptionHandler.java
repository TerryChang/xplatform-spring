package com.terry.xplatform.config.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.terry.xplatform.config.xplatform.web.XplatformView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value={DataAccessException.class})
	public XplatformView processDataAccessException(DataAccessException ex){
		/*
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("ErrorCode", "-1");
		modelAndView.addObject("ErrorMsg", ex.getMessage());
		modelAndView.setViewName("errorView");
		return modelAndView;
		*/
		XplatformView xplatformView = new XplatformView("30", ex.getMessage());
		return xplatformView;
	}

	@ExceptionHandler(value={Exception.class})
	public ModelAndView processException(Exception ex){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("ErrorCode", "-1");
		modelAndView.addObject("ErrorMsg", ex.getMessage());
		modelAndView.setViewName("errorView");
		return modelAndView;
	}
}
