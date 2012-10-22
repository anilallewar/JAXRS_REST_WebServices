package com.anil.jaxrs.producer;

import javax.inject.Named;

import org.jvnet.hk2.annotations.Service;

@Service 
@Named
public class PersistenceProducer implements BaseProducer {

	@Override
	public void version() {
		System.out.println(this.getClass().getName());
	}

}
