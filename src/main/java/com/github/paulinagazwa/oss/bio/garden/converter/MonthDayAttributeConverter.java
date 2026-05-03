package com.github.paulinagazwa.oss.bio.garden.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class MonthDayAttributeConverter implements AttributeConverter<MonthDay, String> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

	@Override
	public String convertToDatabaseColumn(MonthDay attribute) {
		return attribute == null ? null : attribute.format(FORMATTER);
	}

	@Override
	public MonthDay convertToEntityAttribute(String dbData) {
		return dbData == null || dbData.isBlank() ? null : MonthDay.parse("--" + dbData);
	}
}
