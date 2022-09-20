package com.project.crux.crew.repository;


import com.project.crux.crew.Status;
import com.project.crux.crew.domain.Crew;
import com.project.crux.member.domain.Member;
import com.project.crux.crew.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    List<CrewMember> findAllByMember(Member member);

    Optional<CrewMember> findByCrewAndMember(Crew crew, Member member);

    List<CrewMember> findAllByCrewId(Long crewId);

    Optional<CrewMember> findByCrewAndStatus(Crew crew, Status admin);
}
