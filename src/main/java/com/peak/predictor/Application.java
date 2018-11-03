package com.peak.predictor;

import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.EnableAutoConfiguration; 
import org.springframework.context.annotation.ComponentScan; 
import org.springframework.context.annotation.Configuration;
 
@EnableAutoConfiguration 
@Configuration 
@ComponentScan 
public class Application { 
	//private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		//log.info("App is going to start.");
        SpringApplication.run(Application.class, args);
    }
} 