package com.project.crux.repository;

import com.project.crux.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickName);
    Optional<Member> findByKakaoId(Long kakaoId);



}
