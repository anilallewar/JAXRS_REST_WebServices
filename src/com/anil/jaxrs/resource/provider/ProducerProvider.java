package com.anil.jaxrs.resource.provider;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import com.anil.jaxrs.producer.BaseProducer;
import com.anil.jaxrs.producer.InMemoryProducer;
import com.anil.jaxrs.producer.PersistenceProducer;

/**
 * This is registered to being injected by the jersey runtime using CDI provided
 * by JEE 6.
 * 
 * @author Anil Allewar
 * 
 */
public class ProducerProvider implements Provider<BaseProducer> {

	@Inject
	HttpServletRequest httpRequest;

	@Override
	public BaseProducer get() {
		BaseProducer baseProducer = null;
		String testValue = httpRequest.getAttribute("test").toString();
		if (testValue != null) {
			if (testValue.equals("Memory")) {
				baseProducer = new InMemoryProducer();
			} else {
				baseProducer = new PersistenceProducer();
			}
		}
		return baseProducer;
	}
}
