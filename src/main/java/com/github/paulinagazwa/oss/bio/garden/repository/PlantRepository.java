package com.github.paulinagazwa.oss.bio.garden.repository;

import com.github.paulinagazwa.oss.bio.garden.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}

