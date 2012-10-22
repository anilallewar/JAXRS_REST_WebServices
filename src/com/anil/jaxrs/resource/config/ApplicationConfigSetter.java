package com.anil.jaxrs.resource.config;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.core.Application;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.jaxrs.filter.LoggingFilter;
import com.anil.jaxrs.producer.InMemoryProducer;
import com.anil.jaxrs.producer.PersistenceProducer;
import com.anil.jaxrs.resource.HelloResource;
import com.anil.jaxrs.resource.RootResource;
import com.anil.jaxrs.resource.provider.ProducerProvider;

/**
 * @author Anil Allewar
 * 
 */
public class ApplicationConfigSetter extends Application {

	// Class level logger
	private static Logger logger = LoggerFactory
			.getLogger(ApplicationConfigSetter.class);

	@Inject
	public ApplicationConfigSetter(ServiceLocator serviceLocator) {
		logger.info("Registering injectibles for the jersey runtime");

		DynamicConfiguration dynamicConfig = Injections
				.getConfiguration(serviceLocator);

		// Add binding to insert a ProducerProvider for each request
		Injections.addBinding(
				Injections.newBinder(ProducerProvider.class)
						.to(ProducerProvider.class).in(RequestScoped.class),
				dynamicConfig);

		// commits changes
		dynamicConfig.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resourcesToDeployClassSet = new HashSet<Class<?>>();
		resourcesToDeployClassSet.add(RootResource.class);
		resourcesToDeployClassSet.add(HelloResource.class);
		resourcesToDeployClassSet.add(ProducerProvider.class);
		resourcesToDeployClassSet.add(LoggingFilter.class);
		return resourcesToDeployClassSet;
	}
}
