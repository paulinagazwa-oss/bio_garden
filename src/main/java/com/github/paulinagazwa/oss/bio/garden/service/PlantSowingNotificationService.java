package com.github.paulinagazwa.oss.bio.garden.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;

public interface PlantSowingNotificationService {

	void sendSowingNotifications();

	default boolean isSowingStartToday(LocalDateTime sowFrom, LocalDate today) {
		if (sowFrom == null) return false;
		MonthDay sowDay = MonthDay.from(sowFrom);
		return sowDay.equals(MonthDay.from(today));
	}
}
