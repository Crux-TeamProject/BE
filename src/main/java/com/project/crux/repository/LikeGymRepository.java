package com.project.crux.repository;


import com.project.crux.domain.LikeGym;
import com.project.crux.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeGymRepository extends JpaRepository<LikeGym, Long> {
    LikeGym findByMemberAndGymId(Member member, Long gymId);
    List<LikeGym> findAllByMember(Member member);
}
