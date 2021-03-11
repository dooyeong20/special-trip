package com.project.mega.triplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TriplusApplication {
	public static void main(String[] args) {
		SpringApplication.run(TriplusApplication.class, args);
	}
	@Bean
	public ServletWebServerFactory serverFactory(){
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(8080);
		return connector;
	}
}
