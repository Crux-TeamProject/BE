package com.project.crux.repository;


import com.project.crux.domain.Crew;
import com.project.crux.domain.Member;
import com.project.crux.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    List<CrewMember> findAllByMember(Member member);

    Optional<CrewMember> findByCrewAndMember(Crew crew, Member member);

    List<CrewMember> findAllByCrewId(Long crewId);
}
