package com.project.crux.repository;

import com.project.crux.domain.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    Optional<Crew> findAllById(Long id);
    Page<Crew> findByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);
    List<Crew> findAllByNameContainsIgnoreCase(String query);
}
