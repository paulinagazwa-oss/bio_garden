package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.model.WeatherResponse;
import com.github.paulinagazwa.oss.bio.garden.security.GardenUserDetails;
import com.github.paulinagazwa.oss.bio.garden.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/weather")
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	// TODO add to API documentation that this endpoint returns current weather for configured location,
	//  user should be able to set location via API or set it in profile
	@GetMapping("/current")
	public ResponseEntity<WeatherResponse> getCurrentWeather(@AuthenticationPrincipal GardenUserDetails userDetails) {

		UserEntity user = userDetails.getUser();

		return weatherService.getCurrentWeather(user.getLatitude(), user.getLongitude())
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

}
