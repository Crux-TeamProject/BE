package com.project.crux.repository;

import com.project.crux.domain.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findByName(String place_name);
    Page<Gym> findByAvgScoreLessThan(double lastGymScore, PageRequest pageRequest);
    Page<Gym> findByIdLessThanAndNameContains(Long lastArticleId ,String name, PageRequest pageRequest);

}
