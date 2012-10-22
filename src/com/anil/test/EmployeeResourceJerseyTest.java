package com.anil.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.anil.jaxrs.model.Address;
import com.anil.jaxrs.model.Employee;

/**
 * Junit test case to test CRUD functionality with JAX-RS 2.0
 * 
 * @author Anil Allewar
 * 
 */
public class EmployeeResourceJerseyTest {

	private static final String EMPLOYEE_XML = "./employee-jaxb.xml";
	private static final String EMPLOYEE_ERROR_XML = "./employee-jaxb-error.xml";

	Client client = null;

	/**
	 * Instantiate client before each test
	 */
	@Before
	public void init() {
		this.client = ClientFactory.newClient();
	}

	/**
	 * Cleanup the client after each test. Note that the client should be closed
	 * after each interaction.
	 */
	@After
	public void cleanUp() {
		if (this.client != null) {
			this.client.close();
			this.client = null;
		}
	}

	/**
	 * This method is used to put request for an employee to the REST API to
	 * insert an employee<br>
	 * <br>
	 * 
	 * We use batch processing using the invocation command
	 * 
	 * @throws IOException
	 */
	@Test
	public void testInsertEmployeeRequest() throws IOException {

		StringBuilder inputXMLBuilder = new StringBuilder();
		StringBuilder inputErrorXMLBuilder = new StringBuilder();

		BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_XML));
		String input;
		while ((input = reader.readLine()) != null) {
			inputXMLBuilder.append(input);
		}
		reader.close();

		reader = new BufferedReader(new FileReader(EMPLOYEE_ERROR_XML));
		while ((input = reader.readLine()) != null) {
			inputErrorXMLBuilder.append(input);
		}
		reader.close();

		Invocation invGoodRequest = this.client
				.target(getBaseURI())
				.path("rest/employee")
				.request(MediaType.APPLICATION_XML)
				.buildPut(
						Entity.entity(inputXMLBuilder.toString(),
								MediaType.APPLICATION_XML));

		Invocation invBadRequest = this.client
				.target(getBaseURI())
				.path("rest/employee")
				.request(MediaType.APPLICATION_XML)
				.buildPut(
						Entity.entity(inputErrorXMLBuilder.toString(),
								MediaType.APPLICATION_XML));

		// Register the authentication to be used for logging in the web
		// application
		invGoodRequest.configuration().register(
				new HttpBasicAuthFilter("tomcat", "tomcat"));
		invBadRequest.configuration().register(
				new HttpBasicAuthFilter("tomcat", "tomcat"));

		Collection<Invocation> invCollection = Arrays.asList(invGoodRequest,
				invBadRequest);

		// Execute the invocation as a batch process
		for (Invocation currentInvocation : invCollection) {
			// By default invoke() returns a response but we also have an
			// overloaded method that takes the target class as parameter
			Response response = currentInvocation.invoke();

			if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
				System.out.println("The entity created from put is: "
						+ response.getEntity() + " and the location is:"
						+ response.getLocation());
			} else {
				System.out
						.println("The entity was not created and the possible error message is: "
								+ response.getEntity());
			}
		}
	}

	/**
	 * This method is used to put request for an employee object to the REST API
	 * to insert an employee
	 * 
	 * @throws IOException
	 */

	@Test
	public void testInsertEmployeeRequestWithObject() {
		// create employee
		Employee employeeToAdd = new Employee();

		employeeToAdd.setName("Anil Allewar");
		employeeToAdd.setDesignation("Solution Architect");
		employeeToAdd.setDepartment("IT Services");

		Address address1 = new Address();
		address1.setAddress1("12, James lane");
		address1.setCity("Pune");
		address1.setCountry("India");
		address1.setState("Maharashtra");
		address1.setZipCode(411021);

		employeeToAdd.addAddress(address1);

		Address address2 = new Address();
		address2.setAddress1("30, Puneri Lane");
		address2.setCity("Pune");
		address2.setCountry("India");
		address2.setState("Maharashtra");
		address2.setZipCode(411041);

		employeeToAdd.addAddress(address2);

		WebTarget webResource = this.client.target(getBaseURI()).path(
				"rest/employee");
		// Register the authentication to be used for logging in the web
		// application
		webResource.configuration().register(
				new HttpBasicAuthFilter("tomcat", "tomcat"));

		Response response = webResource.request(MediaType.APPLICATION_XML).put(
				Entity.entity(employeeToAdd, MediaType.APPLICATION_XML),
				Response.class);

		if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
			System.out.println("The entity created from put is: "
					+ response.getEntity() + " and the location is:"
					+ response.getLocation());
		} else {
			System.out
					.println("The entity was not created and the possible error message is: "
							+ response.getEntity());
		}
	}

	/**
	 * This method is used to test get/delete request for an employee to the
	 * REST API API
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDeleteEmployeeRequest() throws IOException {

		WebTarget webResource = this.client.target(getBaseURI()).path(
				"rest/employee/3");
		// Register the authentication to be used for logging in the web
		// application
		webResource.configuration().register(
				new HttpBasicAuthFilter("tomcat", "tomcat"));

		String getResponse = webResource.request(MediaType.APPLICATION_XML)
				.get(String.class);
		String deleteResponse = webResource.request(MediaType.APPLICATION_XML)
				.delete(String.class);

		System.out.println("The response received from get is: " + getResponse);
		System.out.println("The response received from delete is: "
				+ deleteResponse);
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
