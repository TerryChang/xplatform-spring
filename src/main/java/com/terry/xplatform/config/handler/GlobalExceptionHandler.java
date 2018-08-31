package com.terry.xplatform.config.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value={DataAccessException.class})
	public ModelAndView processDataAccessException(DataAccessException ex){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("ErrorCode", "-1");
		modelAndView.addObject("ErrorMsg", ex.getMessage());
		modelAndView.setViewName("errorView");
		return modelAndView;
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
