package com.github.paulinagazwa.oss.bio.garden.config;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsRedirectConfig {

	@Value("${server.port:8443}")
	private int httpsPort;

	@Value("${server.http.port:8080}")
	private int httpPort;

	@Bean
	public ServletWebServerFactory servletContainer() {

		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(org.apache.catalina.Context context) {

				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

	private Connector httpConnector() {

		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(httpPort);
		connector.setSecure(false);
		connector.setRedirectPort(httpsPort);
		return connector;
	}
}

