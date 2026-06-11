package com.github.paulinagazwa.oss.bio.garden.repository;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, Long> {

}

