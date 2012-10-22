package com.anil.jaxrs.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to implement the EntityManager-per-request pattern for a web application
 * or any multi-threaded application.
 * 
 * @author anila
 *
 */
public class ThreadEntityManager {

	private static Logger logger = LoggerFactory.getLogger(ThreadEntityManager.class);
	private static final ThreadLocal<EntityManager> currentThreadEntityManager=new ThreadLocal<EntityManager>();
	/*
	 * You should also have a ThreadLocal Transaction if you require your transaction to span
	 * across multiple operations.
	 */
	private static final EntityManagerFactory factory = JPAFactory.getEntityManagerFactory();
	
	/**
	 * This method returns the EntityManager associated with the current thread. If no 
	 * EnityManager is associated with the current thread, then a new EntityManager is created and 
	 * returned.<br><br>
	 * 
	 * Subsequent calls to this method will return the EntityManager associated with the thread.
	 * @return EntityManager
	 */
	public static EntityManager getEntityManager(){
		try{
			EntityManager em = currentThreadEntityManager.get();
			logger.debug("Got the EntityManager from the ThreadLocal class:" + em);
			if (em==null){
				if (logger.isDebugEnabled()){
					logger.debug("Creating a new entity manager as the ThreadLocal does not have any EntityManager");
				}
				
				currentThreadEntityManager.set(factory.createEntityManager());
				
				if (logger.isDebugEnabled()){
					logger.debug("Entity manager created: " + currentThreadEntityManager.get().hashCode());
				}
			}
		}catch (Exception e) {
			logger.error("getEntityManager(): Got exception while creating the EntityManager:", e);
			throw new RuntimeException("getEntityManager(): Got exception while creating the EntityManager:" + e.getMessage());
		}
		return currentThreadEntityManager.get();
	}
	
	/**
	 * This method is used to close the EntityManager associated with the thread.<br><br>
	 * 
	 * When you want to close the EntityManager after all persistence access is over, call this method before 
	 * the thread exists.<br><br>
	 * 
	 * For example, you might have Entity classes that get the entity manager and use it. It is <b>NOT</b>
	 * their responsibilty to close the EntityManager unless you are implementing the <b>EntityManager-per-operation</b>
	 * anti-pattern which is very performance-retarding and SHOULD NOT be used.<br><br>
	 * 
	 * You could implement a filter to close the EntityManager after the control returns to the filter. Ensure that you 
	 * only filter those URLs for which you are going to have persistence operations so that the performance impact is
	 * minimal. 
	 */
	public static void closeEntityManager(){
		try{
			EntityManager em = currentThreadEntityManager.get();
			currentThreadEntityManager.set(null);
			if (em!=null && em.isOpen()){
				if (logger.isDebugEnabled()){
					logger.debug("Closing the EntityManager associated with current thread");
				}
				em.close();
			}
		}catch (Exception e) {
			throw new RuntimeException("closeEntityManager(): Got exception while closing the EntityManager" + e.getMessage());
		}
		
	}
}
