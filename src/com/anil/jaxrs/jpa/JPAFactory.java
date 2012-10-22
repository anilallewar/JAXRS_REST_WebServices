package com.anil.jaxrs.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAFactory {

	private static Logger logger = LoggerFactory.getLogger(JPAFactory.class);

	private static final String unitOfWorkJAXRS = "jaxrsexample";

	private static EntityManagerFactory factory = null;
	
	/**
	 * The static block is used to load the configuration and set the
	 * factory. 
	 * 
	 * This static block will be called whenever this class is loaded by the ClassLoader
	 */
	static {
		if (factory == null) {
			logger.debug("The JPA factory is null");
			factory = Persistence.createEntityManagerFactory(unitOfWorkJAXRS);
			logger.debug("Factory initialized for unit of work: "
					+ unitOfWorkJAXRS);
		}
	}

	/**
	 * This static method is used to give access to a new EntityManager
	 * object(for the thread) to the current thread. The JPAFactory will be
	 * intialized at app startup time and will be used to provide the EntityManager 
	 * to each calling method.
	 * @return EntityManager
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		
		if (factory == null){
			throw new RuntimeException("The Entity Manager Factory is not initialized.");
		}
		return factory;
	}
}
