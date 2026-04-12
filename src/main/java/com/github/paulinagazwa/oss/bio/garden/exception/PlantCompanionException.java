package com.github.paulinagazwa.oss.bio.garden.exception;

public class PlantCompanionException extends IllegalArgumentException {

	public PlantCompanionException() {
		super("Invalid companion relationship: Plant cannot be its own companion");
	}

	public PlantCompanionException(Long id) {
		super ("Companion relationship not found with id: " + id);
	}

	public PlantCompanionException(Long plantId, Long companionPlantId) {
		super ("Companion relationship already exists between plants " + plantId + " and " + companionPlantId);
	}

}
