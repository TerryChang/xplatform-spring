package com.terry.xplatform.config.xplatform.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

public class HttpRequestWrapper extends HttpServletRequestWrapper {

	private byte[] bodyData;
	
	public HttpRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// TODO Auto-generated constructor stub
		InputStream is = request.getInputStream();
		bodyData = StreamUtils.copyToByteArray(is);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		final ByteArrayInputStream bis = new ByteArrayInputStream(bodyData);
		return new ServletImpl(bis);
	}
}

class ServletImpl extends ServletInputStream {

	private InputStream is;

	public ServletImpl(InputStream bis) {
		is = bis;
	}

	@Override
	public int read() throws IOException {
		return is.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return is.read(b);
	}
}