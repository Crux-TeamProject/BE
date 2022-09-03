package com.project.crux.repository;

import com.project.crux.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findByName(String place_name);
}
