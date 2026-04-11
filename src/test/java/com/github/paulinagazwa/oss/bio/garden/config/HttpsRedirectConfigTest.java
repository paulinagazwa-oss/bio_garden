package com.github.paulinagazwa.oss.bio.garden.config;


import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class HttpsRedirectConfigTest {

	private HttpsRedirectConfig config;

	@BeforeEach
	void setUp() throws Exception {

		config = new HttpsRedirectConfig();
		setField(config, "httpsPort", 8443);
		setField(config, "httpPort", 8080);
	}

	@Test
	void shouldReturnTomcatFactory() {

		ServletWebServerFactory factory = config.servletContainer();
		assertNotNull(factory);
		assertInstanceOf(TomcatServletWebServerFactory.class, factory);
	}

	private void setField(Object target, String fieldName, int value) throws Exception {

		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	@Test
	void shouldAddConfidentialConstraintToContext() throws Exception {

		TomcatServletWebServerFactory factory =
				(TomcatServletWebServerFactory) config.servletContainer();

		Context context = mock(Context.class);

		Method method =
				TomcatServletWebServerFactory.class.getDeclaredMethod("postProcessContext",
						org.apache.catalina.Context.class);
		method.setAccessible(true);
		method.invoke(factory, context);

		ArgumentCaptor<SecurityConstraint> captor =
				ArgumentCaptor.forClass(SecurityConstraint.class);
		verify(context, times(1)).addConstraint(captor.capture());

		SecurityConstraint captured = captor.getValue();
		assertEquals("CONFIDENTIAL", captured.getUserConstraint());

		SecurityCollection[] collections = captured.findCollections();
		assertNotNull(collections);
		assertEquals(1, collections.length);
		assertEquals("/*", collections[0].findPatterns()[0]);
	}
}
