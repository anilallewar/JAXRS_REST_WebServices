package com.anil.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.Assert;

import org.junit.Test;

import com.anil.jaxrs.model.Address;
import com.anil.jaxrs.model.Employee;
import com.anil.jaxrs.model.EmployeeCollection;

public class JAXBTest {
	private static final String EMPLOYEE_XML = "./employee-jaxb.xml";

	private ArrayList<Employee> employeeList = new ArrayList<Employee>();

	/**
	 * Method used to populate the sample data to be used for JAXB Testing
	 */
	private void populateEmployeeList() {
		// create employees
		Employee employee1 = new Employee();

		employee1.setEmployeeId(1);
		employee1.setName("Anil Allewar");
		employee1.setDesignation("Solution Architect");
		employee1.setDepartment("IT Services");

		Address address1 = new Address();
		address1.setAddress1("12, James lane");
		address1.setCity("Pune");
		address1.setCountry("India");
		address1.setState("Maharashtra");
		address1.setZipCode(411021);

		employee1.addAddress(address1);

		Address address2 = new Address();
		address2.setAddress1("30, Puneri Lane");
		address2.setCity("Pune");
		address2.setCountry("India");
		address2.setState("Maharashtra");
		address2.setZipCode(411041);

		employee1.addAddress(address2);

		employeeList.add(employee1);

		// create employees
		Employee employee2 = new Employee();

		employee2.setEmployeeId(2);
		employee2.setName("Amit Koparkar");
		employee2.setDesignation("QA Lead");
		employee2.setDepartment("IT Services");

		Address address3 = new Address();
		address3.setAddress1("331, Shaniwar Peth");
		address3.setCity("Pune");
		address3.setCountry("India");
		address3.setState("Maharashtra");
		address3.setZipCode(411023);

		employee2.addAddress(address3);

		employeeList.add(employee2);
	}

	/**
	 * Method used to marshal the java object into XML document
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void marshalData() throws JAXBException, IOException {

		// populate data
		this.populateEmployeeList();
		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(Employee.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(employeeList.get(1), System.out);

		Writer w = null;
		try {
			w = new FileWriter(EMPLOYEE_XML);
			m.marshal(employeeList.get(1), w);
		} finally {
			try {
				w.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Method used to marshal the java object into XML document
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public void marshalEmployeeListData() throws JAXBException, IOException {

		// populate data
		this.populateEmployeeList();
		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(EmployeeCollection.class);
		
		EmployeeCollection empCol = new EmployeeCollection();
		empCol.setEmployeeList(this.employeeList);
		
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(empCol, System.out);

	}
	
	/**
	 * Method used to un-marshal the XML file into Java object
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void unMarshalData() throws JAXBException {
		// create JAXB context and instantiate un-marshaller
		JAXBContext context = JAXBContext.newInstance(Employee.class);

		// get variables from our xml file, created before
		System.out.println();
		System.out.println("Output from our XML File: ");
		Unmarshaller um = context.createUnmarshaller();

		Employee employeeReturned = (Employee) um.unmarshal(new File(
				EMPLOYEE_XML));
		
		Assert.assertNotNull(employeeReturned);
		
		System.out.println(employeeReturned);
	}
}
