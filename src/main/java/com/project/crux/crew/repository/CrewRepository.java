package com.project.crux.crew.repository;

import com.project.crux.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    @Query(value = "select c from Crew c where c.name like %:query% or c.keywords like %:query%")
    List<Crew> findAllBySearchQuery(@Param("query") String query);
}
