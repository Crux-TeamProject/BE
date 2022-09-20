package com.project.crux.crew.repository;

import com.project.crux.crew.domain.CrewPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewPostRepository extends JpaRepository<CrewPost, Long> {
    @EntityGraph(attributePaths = {"crewMember"})
    Page<CrewPost> findAllByCrewMember_CrewId(Long crewId, Pageable pageable);
}
