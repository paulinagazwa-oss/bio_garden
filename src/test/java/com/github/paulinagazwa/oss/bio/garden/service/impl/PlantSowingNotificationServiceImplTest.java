package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantSowingNotificationServiceImplTest {

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private PlantRepository plantRepository;

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

		service.sendSowingNotifications();

		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender).send(captor.capture());

		SimpleMailMessage message = captor.getValue();
		assertThat(message.getTo()).containsExactly("user@email.com");
		assertThat(message.getSubject()).isEqualTo("Time to sow!");
		assertThat(message.getText()).isEqualTo("From today you can plant: Tomato, Carrot");
	}

	@Test
	void sendSowingNotifications_doesNotSendEmail_whenNoPlantsExist() {

		when(plantRepository.findAll()).thenReturn(List.of());

		service.sendSowingNotifications();

		verify(mailSender, org.mockito.Mockito.never()).send(any(SimpleMailMessage.class));
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

		verify(mailSender, org.mockito.Mockito.never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void sendSowingNotifications_ignoresPlantsWithoutSowingStartDate() {

		LocalDate today = LocalDate.now();

		PlantEntity tomato = new PlantEntity();
		tomato.setName("Tomato");
		tomato.setSowFrom(MonthDay.from(today));

		PlantEntity unnamed = new PlantEntity();
		unnamed.setName("Pepper");
		unnamed.setSowFrom(null);

		when(plantRepository.findAll()).thenReturn(List.of(tomato, unnamed));

		service.sendSowingNotifications();

		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender).send(captor.capture());

		SimpleMailMessage message = captor.getValue();
		assertThat(message.getText()).isEqualTo("From today you can plant: Tomato");
	}

}
