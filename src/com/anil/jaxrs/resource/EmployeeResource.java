package com.anil.jaxrs.resource;

import java.net.URI;
import java.util.HashMap;

import javax.inject.Provider;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.jaxrs.manager.EmployeeManager;
import com.anil.jaxrs.model.Employee;
import com.anil.jaxrs.model.SearchEmployeeParamBean;
import com.anil.jaxrs.producer.BaseProducer;

/**
 * This class acts as the root resource for accessing employee related services
 * 
 * @author Anil Allewar
 * 
 */
public class EmployeeResource {

	// Class level logger
	private static Logger logger = LoggerFactory
			.getLogger(EmployeeResource.class);
	// Variable to hold the SecurityContext corresponding to the authentication
	// principal
	private SecurityContext secContext;

	/**
	 * Method used to set the HTTP context objects
	 * 
	 * @param context
	 */
	public void setContext(SecurityContext context) {
		this.secContext = context;
		logger.debug("The resource was called by the user: "
				+ this.secContext.getUserPrincipal().getName());
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response insertEmployee(final String content,
			@Context UriInfo uriInfo) {

		Response response = null;
		try {
			EmployeeManager manager = new EmployeeManager();

			Employee addedEmployee = manager.insertEmployee(content);

			URI createdEmployeeURI = uriInfo.getAbsolutePathBuilder()
					.path(String.valueOf(addedEmployee.getEmployeeId()))
					.build();

			response = Response.created(createdEmployeeURI)
					.entity(addedEmployee).build();

		} catch (Exception exception) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(exception.getMessage()).type(MediaType.TEXT_PLAIN)
					.build();
		}

		return response;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response insertEmployee(final Employee employee,
			@Context UriInfo uriInfo) {

		Response response = null;
		try {
			EmployeeManager manager = new EmployeeManager();

			Employee addedEmployee = manager.insertEmployee(employee);

			URI createdEmployeeURI = uriInfo.getAbsolutePathBuilder()
					.path(String.valueOf(addedEmployee.getEmployeeId()))
					.build();

			response = Response.created(createdEmployeeURI)
					.entity(addedEmployee).build();

		} catch (Exception exception) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(exception.getMessage()).type(MediaType.TEXT_PLAIN)
					.build();
		}

		return response;
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getEmployeeById(@PathParam("id") Long id,
			@Context UriInfo uriInfo,
			@Context Provider<BaseProducer> producerProvider) {
		EmployeeManager manager = new EmployeeManager();
		return manager.getEmployeeById(id);
	}

	/**
	 * This method is used to search employees based on search criteria and uses
	 * a BeanParam object to get the form parameters and package then into a
	 * bean.
	 * 
	 * @param searchParamBean
	 * @param uriInfo
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_XML)
	public String searchEmployee(
			@BeanParam SearchEmployeeParamBean searchParamBean,
			@Context UriInfo uriInfo) {
		HashMap<String, String> params = new HashMap<String, String>();

		if (searchParamBean.getName() != null
				&& !searchParamBean.getName().trim().isEmpty()) {
			params.put("name", searchParamBean.getName());
		}

		if (searchParamBean.getDesignation() != null
				&& !searchParamBean.getDesignation().trim().isEmpty()) {
			params.put("designation", searchParamBean.getDesignation());
		}

		if (searchParamBean.getDepartment() != null
				&& !searchParamBean.getDepartment().trim().isEmpty()) {
			params.put("department", searchParamBean.getDepartment());
		}

		EmployeeManager manager = new EmployeeManager();
		return manager.getEmployeeBySearchCriteria(params);
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String searchEmployeeByDepartment(
			@DefaultValue("") @QueryParam("department") String department,
			@Context UriInfo uriInfo) {
		EmployeeManager manager = new EmployeeManager();
		return manager.getEmployeeByDepartment(department);
	}

	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_XML)
	public String deleteEmployee(@PathParam("id") Long id,
			@Context UriInfo uriInfo) {
		EmployeeManager manager = new EmployeeManager();
		return manager.deleteEmployee(id);
	}

}
