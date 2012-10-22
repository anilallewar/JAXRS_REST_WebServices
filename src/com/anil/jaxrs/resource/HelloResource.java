package com.anil.jaxrs.resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.jaxrs.filter.annotation.Logging;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Path("/hello")
public class HelloResource {

	public static final String NOTIFICATION_RESPONSE = "Hello async world!";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HelloResource.class.getName());
	private static final int SLEEP_TIME_IN_MILLIS = 10000;
	private static final ExecutorService TASK_EXECUTOR = Executors
			.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat(
					"long-running-resource-executor-%d").build());
	private static int count = 0;

	// This method is called if TEXT_PLAIN response is requested
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Jersey";
	}

	/**
	 * This method is called if XML or JSON response is requested. Since the qs
	 * for XML is less than that of JSON; if client accepts both
	 * "application/xml" and "application/json" (equally), then server always
	 * sends "application/json", since "application/xml" has a lower quality
	 * factor.
	 * 
	 * @return
	 */
	@GET
	@Produces({ "application/xml; qs=0.9", MediaType.APPLICATION_JSON })
	@Logging
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// This method is called if HTML response is requested
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}

	/**
	 * Asyc method server support
	 * 
	 * @param asyncResponse
	 */
	@Path("async")
	@GET
	public void longGet(@Suspended final AsyncResponse asyncResponse) {
		TASK_EXECUTOR.submit(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(SLEEP_TIME_IN_MILLIS);
					LOGGER.debug("Received request for async processing: "
							+ HelloResource.count++);
				} catch (InterruptedException ex) {
					LOGGER.error("Response processing interrupted", ex);
				}
				asyncResponse.resume(NOTIFICATION_RESPONSE);
			}
		});
	}
}
