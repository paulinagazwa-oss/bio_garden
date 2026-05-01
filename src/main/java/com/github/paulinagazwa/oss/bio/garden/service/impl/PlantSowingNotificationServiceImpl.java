package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.logging.LogMessages;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantSowingNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantSowingNotificationServiceImpl implements PlantSowingNotificationService {

	private final JavaMailSender mailSender;

	private final PlantRepository plantRepository;

	@Override
	@Scheduled(cron = "${garden.notification.sowing.cron:0 0 8 * * ?}")
	public void sendSowingNotifications() {

		LocalDate today = LocalDate.now();
		log.debug(LogMessages.SOWING_NOTIFICATION_CHECK_START, today);

		List<PlantEntity> plantsToSow = plantRepository.findAll().stream()
				.filter(p -> p.getSowFrom() != null)
				.filter(p -> isSowingStartToday(p.getSowFrom(), today))
				.toList();


		if (plantsToSow.isEmpty()) {
			log.debug(LogMessages.SOWING_NOTIFICATION_NONE_TO_SEND, today);
			return;
		}

		log.info(LogMessages.SOWING_NOTIFICATION_READY_COUNT, plantsToSow.size());

		String plantNames = plantsToSow.stream()
				.map(PlantEntity::getName)
				.collect(Collectors.joining(", "));

		SimpleMailMessage message = new SimpleMailMessage();
		//TODO: get email from user settings
		message.setTo("user@email.com");
		message.setSubject("Time to sow!");
		message.setText("From today you can plant: " + plantNames);

		mailSender.send(message);
		log.info(LogMessages.SOWING_NOTIFICATION_SENT, plantsToSow.size());
	}

}
