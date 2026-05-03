package com.github.paulinagazwa.oss.bio.garden.service;

import java.time.LocalDate;
import java.time.MonthDay;

public interface PlantSowingNotificationService {

	void sendSowingNotifications();

	default boolean isSowingStartToday(MonthDay sowFrom, LocalDate today) {

		if (sowFrom == null) return false;
		return sowFrom.equals(MonthDay.from(today));
	}
}
