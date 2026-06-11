package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantSowingNotificationServiceImplTest {

	private static final String USER_EMAIL = "user@example.com";
	private static final String USER_EMAIL_2 = "user2@example.com";

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private PlantRepository plantRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private PlantSowingNotificationServiceImpl service;

	@Test
	void sendSowingNotifications_sendsEmailWithPlantsWhoseSowingStartsToday() {

		LocalDate today = LocalDate.now();

		PlantEntity tomato = new PlantEntity();
		tomato.setName("Tomato");
		tomato.setSowFrom(MonthDay.from(today));

		PlantEntity carrot = new PlantEntity();
		carrot.setName("Carrot");
		carrot.setSowFrom(MonthDay.from(today.atTime(15, 30)));

		PlantEntity cucumber = new PlantEntity();
		cucumber.setName("Cucumber");
		cucumber.setSowFrom(MonthDay.from(today.plusDays(1)));

		when(plantRepository.findAll()).thenReturn(List.of(tomato, carrot, cucumber));
		when(userRepository.findByNotificationsEnabledTrue()).thenReturn(List.of(createUser(USER_EMAIL)));

		service.sendSowingNotifications();

		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender).send(captor.capture());

		SimpleMailMessage message = captor.getValue();
		assertThat(message.getTo()).containsExactly(USER_EMAIL);
		assertThat(message.getSubject()).isEqualTo("Time to sow!");
		assertThat(message.getText()).isEqualTo("From today you can plant: Tomato, Carrot");
	}

	@Test
	void sendSowingNotifications_sendsEmailToAllUsersWithNotificationsEnabled() {

		LocalDate today = LocalDate.now();

		PlantEntity tomato = new PlantEntity();
		tomato.setName("Tomato");
		tomato.setSowFrom(MonthDay.from(today));

		when(plantRepository.findAll()).thenReturn(List.of(tomato));
		when(userRepository.findByNotificationsEnabledTrue()).thenReturn(
				List.of(createUser(USER_EMAIL), createUser(USER_EMAIL_2))
		);

		service.sendSowingNotifications();

		verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
	}

	@Test
	void sendSowingNotifications_doesNotSendEmail_whenNoPlantsExist() {

		when(plantRepository.findAll()).thenReturn(List.of());

		service.sendSowingNotifications();

		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void sendSowingNotifications_doesNotSendEmail_whenNoPlantStartsSowingToday() {

		LocalDate today = LocalDate.now();

		PlantEntity tomato = new PlantEntity();
		tomato.setName("Tomato");
		tomato.setSowFrom(MonthDay.from(today.minusDays(1)));

		PlantEntity carrot = new PlantEntity();
		carrot.setName("Carrot");
		carrot.setSowFrom(MonthDay.from(today.plusDays(1)));

		when(plantRepository.findAll()).thenReturn(List.of(tomato, carrot));

		service.sendSowingNotifications();

		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void sendSowingNotifications_ignoresPlantsWithoutSowingStartDate() {

		LocalDate today = LocalDate.now();

		PlantEntity tomato = new PlantEntity();
		tomato.setName("Tomato");
		tomato.setSowFrom(MonthDay.from(today));

		PlantEntity pepper = new PlantEntity();
		pepper.setName("Pepper");
		pepper.setSowFrom(null);

		when(plantRepository.findAll()).thenReturn(List.of(tomato, pepper));
		when(userRepository.findByNotificationsEnabledTrue()).thenReturn(List.of(createUser(USER_EMAIL)));

		service.sendSowingNotifications();

		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender).send(captor.capture());

		assertThat(captor.getValue().getText()).isEqualTo("From today you can plant: Tomato");
	}

	@Test
	void sendSowingNotifications_skipsUsersWithoutEmail() {

		LocalDate today = LocalDate.now();

		PlantEntity tomato = new PlantEntity();
		tomato.setName("Tomato");
		tomato.setSowFrom(MonthDay.from(today));

		UserEntity userWithoutEmail = new UserEntity();
		userWithoutEmail.setEmail(null);

		when(plantRepository.findAll()).thenReturn(List.of(tomato));
		when(userRepository.findByNotificationsEnabledTrue()).thenReturn(List.of(userWithoutEmail));

		service.sendSowingNotifications();

		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	private UserEntity createUser(String email) {
		UserEntity user = new UserEntity();
		user.setEmail(email);
		return user;
	}
}
