package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.entity.Plant;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlantController {

	@Autowired
	private PlantService plantService;

	@GetMapping ("/plants")
	public List<Plant> getAllPlants() {

		return plantService.findAllPlants();
	}
}
