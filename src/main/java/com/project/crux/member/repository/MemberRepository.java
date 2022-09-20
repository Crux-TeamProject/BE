package com.project.crux.member.repository;

import com.project.crux.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickName);
    Optional<Member> findByKakaoId(Long kakaoId);



}
