package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantSowingNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantSowingNotificationServiceImpl implements PlantSowingNotificationService {

	private final JavaMailSender mailSender;

	private final PlantRepository plantRepository;

	@Override
	@Scheduled(cron = "${garden.notification.sowing.cron:0 0 8 * * ?}")
	public void sendSowingNotifications() {

		LocalDate today = LocalDate.now();

		List<PlantEntity> plantsToSow = plantRepository.findAll().stream()
				.filter(p -> p.getSowFrom() != null)
				.filter(p -> isSowingStartToday(p.getSowFrom(), today))
				.toList();


		if (plantsToSow.isEmpty()) {
			plantsToSow = plantRepository.findAll();
		}

		SimpleMailMessage message = new SimpleMailMessage();
		//TODO: get email from user settings
		message.setTo("user@email.com");
		message.setSubject("Time to sow!");

		String messageText = "From today you can plant: " + plantsToSow.stream()
				.map(PlantEntity::getName)
				.reduce((a, b) -> a + ", " + b)
				.orElse("");
		message.setText(messageText);

		mailSender.send(message);
	}

}
