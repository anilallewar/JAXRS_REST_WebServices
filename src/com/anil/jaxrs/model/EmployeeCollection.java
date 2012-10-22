package com.anil.jaxrs.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EmployeeList")
public class EmployeeCollection {

	private List<Employee> employeeList = null;

	/**
	 * @return the employeeList
	 */
	@XmlElement(name = "Employee")
	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	/**
	 * @param employeeList
	 *            the employeeList to set
	 */
	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
}
