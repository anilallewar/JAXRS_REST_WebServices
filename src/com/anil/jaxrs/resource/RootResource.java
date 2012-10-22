package com.anil.jaxrs.resource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.anil.jaxrs.producer.BaseProducer;

/**
 * This is the root request class that is used to service the endpoint and
 * distribute the request to appropriate sub-resources.
 * 
 * @author Anil Allewar
 * 
 */
@Path("rest")
public class RootResource {

	/*
	 * The @Context makes the HTTP context related objects available to the
	 * resource class
	 */
	@Context
	private transient SecurityContext secContext;

	@Context
	private transient HttpServletRequest request;
	
	@Inject
	private Provider<BaseProducer> producer;
	
	@Path("employee")
	public EmployeeResource getEmployeeResource() {
		this.request.setAttribute("test", "Memory");
		EmployeeResource returnResource = new EmployeeResource();
		returnResource.setContext(this.secContext);
		producer.get();
		return returnResource;
	}
}
