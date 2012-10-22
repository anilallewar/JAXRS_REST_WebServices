package com.anil.jaxrs.model;

import javax.ws.rs.FormParam;

/**
 * This class is used to inject the parameters for search employee form into a
 * single bean.
 * 
 * @author Anil Allewar
 * 
 */
public class SearchEmployeeParamBean {
	@FormParam("name")
	private String name;

	@FormParam("designation")
	private String designation;

	@FormParam("department")
	private String department;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation
	 *            the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

}
