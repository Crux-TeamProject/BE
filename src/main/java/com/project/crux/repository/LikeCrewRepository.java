package com.project.crux.repository;

import com.project.crux.domain.Crew;
import com.project.crux.domain.LikeCrew;
import com.project.crux.domain.LikeGym;
import com.project.crux.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeCrewRepository extends JpaRepository<LikeCrew, Long> {
    Optional<LikeCrew> findByCrewAndMember(Crew crew, Member member);
    List<LikeCrew> findAllByMember(Member member);
}
