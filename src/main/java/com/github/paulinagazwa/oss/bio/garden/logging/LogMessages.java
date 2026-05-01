package com.github.paulinagazwa.oss.bio.garden.logging;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LogMessages {

	public static final String COMPANION_CREATE_START =
			"Creating companion relationship: plant={}, companion={}, type={}";

	public static final String COMPANION_CREATE_SUCCESS =
			"Companion relationship created with id: {}";

	public static final String COMPANION_BIDIRECTIONAL_CREATED =
			"Created bidirectional companion relationship";

	public static final String COMPANION_GET_ALL_FOR_PLANT =
			"Getting all companions for plant: {}";

	public static final String COMPANION_GET_BY_TYPE =
			"Getting companions of type {} for plant: {}";

	public static final String COMPANION_DELETE_START =
			"Deleting companion relationship: {}";

	public static final String COMPANION_REVERSE_DELETE =
			"Deleted reverse companion relationship";

	public static final String COMPANION_DELETE_SUCCESS =
			"Companion relationship deleted";

	public static final String COMPANION_UPDATE_START =
			"Updating companion relationship: {}";

	public static final String COMPANION_UPDATE_SUCCESS =
			"Companion relationship updated";

	public static final String COMPANION_REVERSE_DELETE_ON_UPDATE =
			"Deleted reverse companion relationship due to bidirectional update";

	public static final String COMPANION_GET_ALL_RELATIONSHIPS =
			"Getting all relationships for plant: {}";

	public static final String PLANT_GET_ALL =
			"Getting all plants for page: page={}, size={}";

	public static final String PLANT_GET_ALL_WITH_COMPANIONS =
			"Getting all plants with companions for page: page={}, size={}";

	public static final String PLANT_GET_BY_ID =
			"Getting plant by id: {}";

	public static final String PLANT_CREATE_START =
			"Creating plant";

	public static final String PLANT_CREATE_SUCCESS =
			"Plant created with id: {}";

	public static final String PLANT_UPDATE_START =
			"Updating plant: {}";

	public static final String PLANT_UPDATE_SUCCESS =
			"Plant updated: {}";

	public static final String PLANT_DELETE_START =
			"Deleting plant: {}";

	public static final String PLANT_DELETE_SUCCESS =
			"Plant deleted: {}";

	public static final String WEATHER_GET_CURRENT_START =
			"Getting current weather for coordinates: latitude={}, longitude={}";

	public static final String WEATHER_GET_CURRENT_SUCCESS =
			"Current weather fetched successfully";

	public static final String WEATHER_GET_CURRENT_EMPTY =
			"Weather service returned empty response body";

	public static final String WEATHER_CLIENT_ERROR =
			"Weather service returned client error: {}";

	public static final String WEATHER_SERVER_ERROR =
			"Weather service returned server error: {}";

	public static final String WEATHER_CONNECTION_ERROR =
			"Failed to connect to weather service";

	public static final String SOWING_NOTIFICATION_CHECK_START =
			"Checking plants for sowing notifications on: {}";

	public static final String SOWING_NOTIFICATION_NONE_TO_SEND =
			"No plants require sowing notification on: {}";

	public static final String SOWING_NOTIFICATION_READY_COUNT =
			"Found {} plants ready for sowing notification";

	public static final String SOWING_NOTIFICATION_SENT =
			"Sowing notification sent for {} plants";

}
