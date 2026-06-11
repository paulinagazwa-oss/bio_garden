package com.github.paulinagazwa.oss.bio.garden.exception;

public class PlantCompanionException extends RuntimeException {

	private PlantCompanionException(String message) {

		super(message);
	}

	public static PlantCompanionException selfCompanion() {

		return new PlantCompanionException("Invalid companion relationship: Plant cannot be its own companion");
	}

	public static PlantCompanionException notFound(Long id) {

		return new PlantCompanionException("Companion relationship not found with id: " + id);
	}

	public static PlantCompanionException alreadyExists(Long plantId, Long companionPlantId) {

		return new PlantCompanionException("Companion relationship already exists between plants " + plantId + " and " + companionPlantId);
	}

}
