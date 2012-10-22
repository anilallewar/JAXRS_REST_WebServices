package com.anil.test;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientException;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import junit.framework.Assert;

import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.junit.Test;

public class HelloWorldTest {

	/**
	 * Test the hello service that returns different types of formats
	 */
	@Test
	public void testHelloService() {
		Client client = ClientFactory.newClient();
		WebTarget webResource = client.target(getBaseURI()).path("hello");
		// Register the authentication to be used for logging in the web
		// application
		webResource.configuration().register(
				new HttpBasicAuthFilter("tomcat", "tomcat"));

		System.out.println("Getting text data.........");
		// Get plain text
		System.out.println(webResource.request(MediaType.TEXT_PLAIN).get(
				String.class));

		System.out.println("Getting XML data.........");
		// Get XML
		System.out.println(webResource.request(MediaType.APPLICATION_XML).get(
				String.class));

		System.out
				.println("Getting JSON data that is returned as an XML.........");
		// Get XML
		System.out.println(webResource.request(MediaType.APPLICATION_JSON).get(
				String.class));

		System.out.println("Getting HTML data.........");
		// The HTML
		System.out.println(webResource.request(MediaType.TEXT_HTML).get(
				String.class));

	}

	/**
	 * Test method for making async invocations from the client
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testAsyncClientRequests() throws InterruptedException {

		Client client = ClientFactory.newClient();
		WebTarget webResource = client.target(getBaseURI()).path("hello/async");
		// Register the authentication to be used for logging in the web
		// application
		webResource.configuration().register(
				new HttpBasicAuthFilter("tomcat", "tomcat"));

		final int REQUESTS = 10;
		final CountDownLatch latch = new CountDownLatch(REQUESTS);
		final long tic = System.currentTimeMillis();
		for (int i = 0; i < REQUESTS; i++) {
			webResource.request(MediaType.TEXT_PLAIN).async()
					.get(new InvocationCallback<Response>() {

						@Override
						public void completed(Response response) {
							try {
								final String result = response
										.readEntity(String.class);
								Assert.assertEquals("Hello Jersey", result);
							} finally {
								latch.countDown();
							}
						}

						@Override
						public void failed(ClientException exception) {
							System.out.println("Exception while invocation: "
									+ exception.getMessage());
							latch.countDown();
						}
					});
		}
		latch.await(10, TimeUnit.SECONDS);
		final long toc = System.currentTimeMillis();
		System.out.println(HelloWorldTest.class.getName() + " => Async method call executed in: "
				+ (toc - tic));
	}

	/**
	 * Get the base URI
	 * 
	 * @return
	 */
	private URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/JAXRS_REST_WebServices").build();
	}
}
