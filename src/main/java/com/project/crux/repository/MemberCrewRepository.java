package com.project.crux.repository;


import com.project.crux.domain.LikeGym;
import com.project.crux.domain.Member;
import com.project.crux.domain.MemberCrew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCrewRepository extends JpaRepository<MemberCrew, Long> {
    List<MemberCrew> findAllByMember(Member member);
}
