package com.anil.jaxrs.producer;

import javax.inject.Named;

import org.jvnet.hk2.annotations.Service;

@Service
@Named("InMemoryProducer")
public class InMemoryProducer implements BaseProducer {

	@Override
	public void version() {
		System.out.println(this.getClass().getName());
	}

}
