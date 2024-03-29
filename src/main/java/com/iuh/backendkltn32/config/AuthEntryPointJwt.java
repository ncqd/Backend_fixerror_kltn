package com.iuh.backendkltn32.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authRuntimeException) throws IOException, ServletException {

		logger.error("Unauthorized error: {}", authRuntimeException.getMessage());
		
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error Unauthorized");
		
	}
	
	

}
