package com.project.crux.repository;

import com.project.crux.domain.CrewPost;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewPostRepository extends JpaRepository<CrewPost, Long> {
    @EntityGraph(attributePaths = {"crewMember"})
    List<CrewPost> findAllByIdLessThanAndCrewMember_CrewId(Long lastId, Long crewId, PageRequest pageRequest);
}
