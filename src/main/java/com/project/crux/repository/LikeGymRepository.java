package com.project.crux.repository;


import com.project.crux.domain.Gym;
import com.project.crux.domain.LikeGym;
import com.project.crux.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeGymRepository extends JpaRepository<LikeGym, Long> {
    Optional<LikeGym> findByMemberAndGym(Member member, Gym gym);
    List<LikeGym> findAllByMember(Member member);
}
