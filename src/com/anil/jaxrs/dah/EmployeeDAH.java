package com.anil.jaxrs.dah;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.jaxrs.jpa.ThreadEntityManager;
import com.anil.jaxrs.model.Employee;

/**
 * This class provides the data access layer for handling the Employee entities.<br>
 * <br>
 * 
 * Since the calling method can call more than 1 data access method for the same
 * object(and in a single thread), the responsibility of closing the
 * EntityManager lies with the calling method.
 * 
 * @author Anil Allewar
 * 
 */
public class EmployeeDAH {

	// Class level logger
	private static Logger logger = LoggerFactory.getLogger(EmployeeDAH.class);

	// Fetch query by Id - This will eagerly fetch the addresses associated with
	// the employee
	private static final String QUERY_FETCH_EMPLOYEE_BY_ID = "SELECT employee FROM Employee as employee "
			+ "INNER JOIN FETCH employee.addresses WHERE employee.employeeId = :passedEmployeeId";

	// Fetch employees by search criteria - This will only fetch the employees
	// and not their addresses
	private static String QUERY_SEARCH_EMPLOYEE = "SELECT employee FROM Employee as employee WHERE ";

	/**
	 * This method is used to persist the passed Employee object in database
	 * 
	 * @param employee
	 *            The Employee object to persist
	 * @return The persisted Employee object
	 * @throws Throwable
	 */
	public Employee insertEmployee(Employee employee) throws Exception {
		if (employee == null) {
			throw new Exception("The Employee object passed is null");
		}

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			tx = em.getTransaction();
			tx.begin();
			em.persist(employee);
			tx.commit();
			/*
			 * Clear the persistence context, causing all managed entities to
			 * become detached. Changes made to entities that have not been
			 * flushed to the database will not be persisted.
			 * 
			 * The commit operation flushes the entity changes in the
			 * transaction to the database.
			 */
			em.clear();

		} catch (Exception exception) {
			logger.error("Exception while inserting Employee: "
					+ exception.getMessage());
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw exception;
		}

		return employee;
	}

	/**
	 * Returns the associated employee object for the passed primary key. If no
	 * data is found, then null is returned.
	 * 
	 * @param id
	 *            The primary key for the Employee object that needs to be
	 *            retrieved from DB
	 * @return Employee
	 * @throws Throwable
	 */
	public Employee getEmployeeById(long id) throws Exception {

		Employee employee = null;

		EntityManager em = null;

		try {
			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			Query query = em.createQuery(QUERY_FETCH_EMPLOYEE_BY_ID);
			query.setParameter("passedEmployeeId", new Long(id));

			employee = (Employee) query.getSingleResult();

		} catch (Exception exception) {
			logger.error("Exception while getting Employee by Id: "
					+ exception.getMessage());
			throw exception;
		}

		return employee;
	}

	/**
	 * This method is used to return a list of employee objects which match the
	 * passed search criteria.<br>
	 * <br>
	 * The search criteria is passed as a hashmap of key-value pairs where the
	 * key is the attribute name of the Employee class and the value is the
	 * value to apply for the search criteria.
	 * 
	 * @param params
	 *            hashmap of key-value search parameter pairs
	 * @return List of Employee objects
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> searchEmployee(HashMap<String, String> params)
			throws Exception {

		String queryString = QUERY_SEARCH_EMPLOYEE;
		boolean firstSearchCriteria = true;
		List<Employee> resultList = null;
		EntityManager em = null;

		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> searchPair = iterator.next();
			if (firstSearchCriteria) {
				queryString += searchPair.getKey() + " = '"
						+ searchPair.getValue() + "'";
				firstSearchCriteria = false;
			} else {
				queryString += " AND " + searchPair.getKey() + " = '"
						+ searchPair.getValue() + "'";
			}
		}

		try {
			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			Query query = em.createQuery(queryString);

			resultList = query.getResultList();

		} catch (Exception exception) {
			logger.error("Exception while searching for Employee: "
					+ exception.getMessage());
			throw exception;
		}

		return resultList;
	}

	/**
	 * This method is used to return a list of employee objects which match the
	 * passed department.
	 * 
	 * @param department
	 * @return List of Employee objects
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> searchEmployeeByDepartment(String department)
			throws Exception {

		String queryString = QUERY_SEARCH_EMPLOYEE + "department = '"
				+ department + "'";
		List<Employee> resultList = null;
		EntityManager em = null;

		try {
			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			Query query = em.createQuery(queryString);

			resultList = query.getResultList();

		} catch (Exception exception) {
			logger.error("Exception while searching for Employee: "
					+ exception.getMessage());
			throw exception;
		}

		return resultList;
	}

	/**
	 * This method is used to delete the Employee object corresponding to passed
	 * id
	 * 
	 * @param id
	 *            The primary key for the Employee to be deleted
	 * 
	 * @return boolean representing whether the delete operation was successful
	 *         or not
	 * @throws Exception
	 */
	public boolean deleteEmployee(Long id) throws Exception {
		boolean result = false;

		EntityManager em = null;
		EntityTransaction tx = null;
		Employee employee = null;

		try {

			// This method will use the same EntityManager stored in the current
			// ThreadLocal for DB access
			employee = this.getEmployeeById(id);

			// This will get the Entity Manager that is stored in ThreadLocal
			// variable
			em = ThreadEntityManager.getEntityManager();

			tx = em.getTransaction();
			tx.begin();
			em.remove(employee);
			tx.commit();
			em.clear();

			result = true;

		} catch (Exception exception) {
			logger.error("Exception while inserting Employee: "
					+ exception.getMessage());
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw exception;
		}

		return result;
	}
}
