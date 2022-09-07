package com.project.crux.repository;

import com.project.crux.domain.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Page<Crew> findByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);
}
