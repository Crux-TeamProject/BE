package com.project.crux.crew.repository;

import com.project.crux.crew.domain.Crew;
import com.project.crux.crew.domain.LikeCrew;
import com.project.crux.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeCrewRepository extends JpaRepository<LikeCrew, Long> {
    Optional<LikeCrew> findByCrewAndMember(Crew crew, Member member);
    List<LikeCrew> findAllByMember(Member member);
}
