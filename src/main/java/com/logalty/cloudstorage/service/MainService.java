package com.logalty.cloudstorage.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainService {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public MainService() {		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml") ;		
	}
	
	public void init() {
		log.info("Quartz initialized...");
	}
	
	public static void main(String[] args) {
		MainService service = new MainService() ;
		service.init(); 
	}
	
	
}
