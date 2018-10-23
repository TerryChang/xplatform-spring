package com.terry.xplatform.config.xplatform.web;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class XplatformViewResolver implements ViewResolver, Ordered {

	private int order = Ordered.LOWEST_PRECEDENCE;

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		XplatformView xplatformView = new XplatformView();
		return xplatformView;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
