package com.terry.xplatform.config.xplatform.support;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

/**
 * HttpServletRequest 객체를 한번 감싸주어서 request로 전달해주는 Filter이다.
 * 일반적으로 HttpServletRequest 객체의 request body를 읽었을 경우 Stream으로 읽어나가기 때문에
 * 한번 읽게 되면 다시 읽을수가 없는 상황이 된다.
 * 그래서 HttpServletRequest 객체를 한번 감싸는 Wrapper 클래스를 만든뒤에 그 클래스에 원래 request의 InputStream을 배열로 보관한뒤
 * 이 Wrapper 클래스 객체를 전달함으로써 이걸 이용하는 클래스는 Wrapper 클래스 객체의 배열을 읽게 함으로써 여러번 읽을수 있게끔 해준다
 * 
 * 이 클래스를 Spring Bean으로 등록할땐 Servlet Context가 아닌 Root Context에 등록해야 한다
 * 그래야 web.xml에서 Spring의 DelegatingFilterProxy 클래스를 통해 이 Filter 클래스를 사용할 수 있다(Spring의 Bean Injection 기능 사용이 가능)
 * Spring Security에서 설정 xml이 Root Context에 등록되는 것과 같은 의미이다
 * 
 * @author Terry Chang
 *
 */
@Service
public class HttpServletRequestWrapperFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper(httpServletRequest);
		chain.doFilter(httpRequestWrapper, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
