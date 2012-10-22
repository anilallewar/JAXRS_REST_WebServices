package com.anil.jaxrs.manager;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.jaxrs.dah.EmployeeDAH;
import com.anil.jaxrs.jpa.ThreadEntityManager;
import com.anil.jaxrs.model.Address;
import com.anil.jaxrs.model.Employee;
import com.anil.jaxrs.model.EmployeeCollection;

public class EmployeeManager {

	// Class level logger
	private static Logger logger = LoggerFactory
			.getLogger(EmployeeManager.class);

	/**
	 * This inner class will be used to create the error response XML
	 * 
	 */
	@SuppressWarnings("serial")
	private static class EmployeeException extends RuntimeException {

		public EmployeeException(final String msg, final Exception root) {
			super(msg, root);
		}

		public String createResponse() {
			final Document errDoc = DocumentHelper.createDocument();
			final Element err = errDoc.addElement("Employee");
			err.addAttribute("error", getMessage());
			return errDoc.asXML();
		}
	}

	/**
	 * Method used to persist a new Employee in database. <br>
	 * <br>
	 * 
	 * The employee is persisted as follows<br>
	 * <br>
	 * 
	 * <ol>
	 * <li>Unmarshal the passed InputStream to get the Employee object</li>
	 * <li>Next call the EmployeeDAH method to persist the object in database</li>
	 * <li>Returns either the XML containing the marshalled object or error XML.
	 * </li>
	 * </ol>
	 * 
	 * @param in
	 *            String containing the Employee XML data to be inserted
	 * @return Employee - the newly created employee
	 * @throws Exception
	 */
	public Employee insertEmployee(String in) throws Exception {

		try {

			// create JAXB context and instantiate un-marshaller
			JAXBContext context = JAXBContext.newInstance(Employee.class);
			Unmarshaller um = context.createUnmarshaller();

			StringReader reader = new StringReader(in);

			Employee employeeToAdd = (Employee) um.unmarshal(reader);

			employeeToAdd = this.addEmployeeToDB(employeeToAdd);

			return employeeToAdd;

		} catch (JAXBException jaxException) {
			logger.error("insertEmployee(InputStream in) - JAXBException :"
					+ jaxException.getMessage(), jaxException);
			throw jaxException;
		} catch (Exception exception) {
			logger.error("insertEmployee(InputStream in) - Exception :"
					+ exception.getMessage(), exception);
			throw exception;
		}
	}

	/**
	 * Method used to persist a new Employee in database. <br>
	 * <br>
	 * 
	 * @param employee
	 *            Employee object to be created that is passed as XML input by
	 *            client and unmarshalled before passing to the resource method.
	 * @return Employee - the newly created employee
	 * @throws Exception
	 */
	public Employee insertEmployee(Employee employee) throws Exception {

		try {

			Employee employeeToAdd = this.addEmployeeToDB(employee);

			return employeeToAdd;

		} catch (Exception exception) {
			logger.error("insertEmployee(Employee employee) - Exception :"
					+ exception.getMessage(), exception);
			throw exception;
		}
	}

	/**
	 * This method does the actual operation of adding the employee to database.
	 * 
	 * The method uses an EntityManager-per-thread strategy where the
	 * EntityManager is created and managed by the manager method that is called
	 * by each HTTP thread. <br>
	 * <br>
	 * 
	 * Subsequent calls to <code>ThreadEntityManager.getEntityManager()</code>
	 * will get the EntityManager from the ThreadLocal variable associated with
	 * the current thread.
	 * 
	 * @param employeeToAdd
	 * @return
	 * @throws Exception
	 */
	private Employee addEmployeeToDB(Employee employeeToAdd) throws Exception {
		EntityManager em = null;
		Address address = null;
		try {
			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			/**
			 * We need to set a 2-way relationship between Address and Employee
			 * so that Hibernate can associate the Address entity with the
			 * correct employee and persist the data in database.
			 */
			Iterator<Address> iterator = employeeToAdd.getAddresses()
					.iterator();

			while (iterator.hasNext()) {
				address = iterator.next();
				address.setAssociatedEmployee(employeeToAdd);
			}

			EmployeeDAH employeeDAH = new EmployeeDAH();

			employeeToAdd = employeeDAH.insertEmployee(employeeToAdd);

			return employeeToAdd;
		} catch (Exception exception) {
			logger.error("insertEmployee(InputStream in) - Exception :"
					+ exception.getMessage(), exception);
			throw exception;
		} finally {
			if (em != null) {
				// Close the EntityManager
				ThreadEntityManager.closeEntityManager();
			}
		}
	}

	/**
	 * This method is used to retrive the Employee object corresponding to the
	 * passed primary key and return the XML representing the Employee
	 * 
	 * @param id
	 * @return XML containing the marshalled object or error XML
	 * @throws NotFoundException
	 */
	public String getEmployeeById(Long id) throws NotFoundException {
		String response = null;
		EntityManager em = null;

		try {

			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			JAXBContext context = JAXBContext.newInstance(Employee.class);

			EmployeeDAH employeeDAH = new EmployeeDAH();

			Employee employeeRetrieved = employeeDAH.getEmployeeById(id);

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();

			m.marshal(employeeRetrieved, sw);

			response = sw.toString();

		} catch (JAXBException jaxException) {
			logger.error("getEmployeeById(Long id) - JAXBException :"
					+ jaxException.getMessage(), jaxException);
			throw new ServerErrorException(Response.Status.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(
					"getEmployeeById(Long id) - Exception :" + e.getMessage(),
					e);
			throw new NotFoundException();
		} finally {
			if (em != null) {
				// Close the EntityManager
				ThreadEntityManager.closeEntityManager();
			}
		}

		return response;
	}

	/**
	 * This method is used to retrive the Employee object corresponding to the
	 * passed search criteria and return the XML representing the Employees
	 * matching the search criteria<br>
	 * <br>
	 * 
	 * When JAXB tries to get the addresses for an employee, it calls the getter
	 * method causing Hibernate to load the address entities attached to the
	 * employee.<br>
	 * <br>
	 * 
	 * There is only 2 ways to address this issue if you do NOT want to load the
	 * Address with employee
	 * <ol>
	 * <li>Create the Response XML manually using a DOM parser so that you don't
	 * call the <code>getAddresses()</code> method</li>
	 * <li>Detach the Employee entity and close the EntityManager before calling
	 * the JAXB marshaller. This will ensure that Hibernate does NOT recognize
	 * the current entity and try to get its associations.</li>
	 * </ol>
	 * 
	 * @param params
	 * @return XML containing the marshalled object or error XML
	 */
	public String getEmployeeBySearchCriteria(HashMap<String, String> params) {
		String response = null;
		EntityManager em = null;

		try {

			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			JAXBContext context = JAXBContext
					.newInstance(EmployeeCollection.class);

			EmployeeDAH employeeDAH = new EmployeeDAH();

			List<Employee> employeeList = employeeDAH.searchEmployee(params);

			EmployeeCollection empCol = new EmployeeCollection();
			empCol.setEmployeeList(employeeList);

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();

			m.marshal(empCol, sw);

			response = sw.toString();

		} catch (JAXBException jaxException) {
			final EmployeeException employeeExc = new EmployeeException(
					"JAXBException :" + jaxException.getMessage(), jaxException);
			response = employeeExc.createResponse();
			logger.error(
					"getEmployeeBySearchCriteria(HashMap<String, String> params) - JAXBException :"
							+ jaxException.getMessage(), jaxException);
		} catch (Exception e) {
			final EmployeeException employeeExc = new EmployeeException(
					"Exception :" + e.getMessage(), e);
			response = employeeExc.createResponse();
			logger.error(
					"getEmployeeBySearchCriteria(HashMap<String, String> params) - Exception :"
							+ e.getMessage(), e);
		} finally {
			if (em != null) {
				// Close the EntityManager
				ThreadEntityManager.closeEntityManager();
			}
		}

		return response;
	}

	/**
	 * This method is used to retrive the Employee object corresponding to the
	 * passed department and return the XML representing the Employees matching
	 * the search criteria. <br>
	 * <br>
	 * 
	 * When JAXB tries to get the addresses for an employee, it calls the getter
	 * method causing Hibernate to load the address entities attached to the
	 * employee.<br>
	 * <br>
	 * 
	 * There is only 2 ways to address this issue if you do NOT want to load the
	 * Address with employee
	 * <ol>
	 * <li>Create the Response XML manually using a DOM parser so that you don't
	 * call the <code>getAddresses()</code> method</li>
	 * <li>Detach the Employee entity and close the EntityManager before calling
	 * the JAXB marshaller. This will ensure that Hibernate does NOT recognize
	 * the current entity and try to get its associations.</li>
	 * </ol>
	 * 
	 * @param department
	 * @return XML containing the marshalled object or error XML
	 */
	public String getEmployeeByDepartment(String department) {
		String response = null;
		EntityManager em = null;

		try {

			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			JAXBContext context = JAXBContext
					.newInstance(EmployeeCollection.class);

			EmployeeDAH employeeDAH = new EmployeeDAH();

			List<Employee> employeeList = employeeDAH
					.searchEmployeeByDepartment(department);

			EmployeeCollection empCol = new EmployeeCollection();
			empCol.setEmployeeList(employeeList);

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();

			m.marshal(empCol, sw);

			response = sw.toString();

		} catch (JAXBException jaxException) {
			final EmployeeException employeeExc = new EmployeeException(
					"JAXBException :" + jaxException.getMessage(), jaxException);
			response = employeeExc.createResponse();
			logger.error(
					"getEmployeeByDepartment(String department) - JAXBException :"
							+ jaxException.getMessage(), jaxException);
		} catch (Exception e) {
			final EmployeeException employeeExc = new EmployeeException(
					"Exception :" + e.getMessage(), e);
			response = employeeExc.createResponse();
			logger.error(
					"getEmployeeByDepartment(String department) - Exception :"
							+ e.getMessage(), e);
		} finally {
			if (em != null) {
				// Close the EntityManager
				ThreadEntityManager.closeEntityManager();
			}
		}

		return response;
	}

	/**
	 * This method is used to delete the Employee object corresponding to passed
	 * id
	 * 
	 * @param id
	 *            The primary key for the Employee to be deleted
	 * @return XML denoting status of delete operation
	 */
	public String deleteEmployee(Long id) {
		String response = "<Employee><status>Deleted Successfully!</status></Employee>";
		EntityManager em = null;

		try {

			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			EmployeeDAH employeeDAH = new EmployeeDAH();

			boolean returnValue = employeeDAH.deleteEmployee(id);

			if (!returnValue) {
				// Throw new exception in case the return value is false
				// This will be caught by the catch block and result in error
				// XML to be sent out
				throw new Exception("Exception while deleting Employee");
			}
		} catch (JAXBException jaxException) {
			final EmployeeException employeeExc = new EmployeeException(
					"JAXBException :" + jaxException.getMessage(), jaxException);
			response = employeeExc.createResponse();
			logger.error("deleteEmployee(Long id) - JAXBException :"
					+ jaxException.getMessage(), jaxException);
		} catch (Exception e) {
			final EmployeeException employeeExc = new EmployeeException(
					"Exception :" + e.getMessage(), e);
			response = employeeExc.createResponse();
			logger.error(
					"deleteEmployee(Long id) - Exception :" + e.getMessage(), e);
		} finally {
			if (em != null) {
				// Close the EntityManager
				ThreadEntityManager.closeEntityManager();
			}
		}

		return response;
	}
}
