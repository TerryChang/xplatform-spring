package com.terry.xplatform.config.xplatform.web;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class XplatformViewResolver extends AbstractTemplateViewResolver {

    @Override
    protected Class getViewClass() {
		// TODO Auto-generated method stub
		return XplatformView.class;
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		// TODO Auto-generated method stub
		XplatformView view = (XplatformView)super.buildView(viewName);
		return view;
    }
    
    

}
