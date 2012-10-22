/**
 * 
 */
package com.anil.jaxrs.filter;

import java.io.IOException;

import javax.ws.rs.BindingPriority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.jaxrs.filter.annotation.Logging;

/**
 * @author RDXSZ0001
 * 
 */
@Provider
@Logging
@BindingPriority(1)
public class LoggingFilter implements ContainerRequestFilter,
		ContainerResponseFilter {

	// Class level logger
	private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container
	 * .ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
	 */
	@Override
	public void filter(ContainerRequestContext reqContext,
			ContainerResponseContext responseContext) throws IOException {
		logger.info("POST request filter for server and the response entity class is: "
				+ responseContext.getEntity().getClass().getCanonicalName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container
	 * .ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext reqContext) throws IOException {
		logger.info("PRE request filter for server: "
				+ reqContext.getRequest().toString());
	}

}
