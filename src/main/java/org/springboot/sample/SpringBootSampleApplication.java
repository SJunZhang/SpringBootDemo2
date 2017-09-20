package org.springboot.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springboot.sample.Boot.ShutdownBootApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan

public class SpringBootSampleApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootSampleApplication.class);

	public static void main(String[] args) {
		ShutdownBootApp.run(SpringBootSampleApplication.class, args);
	}
}
