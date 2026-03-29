package com.github.paulinagazwa.oss.bio.garden;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GardenApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {

		assertThat(applicationContext).isNotNull();
	}

	@Test
	void applicationContextContainsMainClass() {

		assertThat(applicationContext.containsBean("gardenApplication")).isTrue();
	}

	@Test
	void applicationHasCorrectPackage() {

		assertThat(GardenApplication.class.getPackageName())
				.isEqualTo("com.github.paulinagazwa.oss.bio.garden");
	}

	@Test
	void applicationHasSpringBootApplicationAnnotation() {

		assertThat(GardenApplication.class
				.isAnnotationPresent(
						org.springframework.boot.autoconfigure.SpringBootApplication.class))
				.isTrue();
	}

	@Test
	void databaseConnectionIsAvailable() throws SQLException {

		assertThat(dataSource).isNotNull();
		assertThat(dataSource.getConnection()).isNotNull();
		assertThat(dataSource.getConnection().isValid(1)).isTrue();
	}

	@Test
	void databaseIsH2InMemory() throws SQLException {

		String url = dataSource.getConnection().getMetaData().getURL();
		assertThat(url).contains("h2:mem");
	}

}
