package com.project.crux.gym.repository;

import com.project.crux.gym.domain.Gym;
import com.project.crux.gym.domain.LikeGym;
import com.project.crux.member.domain.Member;
import com.project.crux.gym.repository.GymRepository;
import com.project.crux.gym.repository.LikeGymRepository;
import com.project.crux.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikeGymRepositoryTest {

    @Autowired
    LikeGymRepository likeGymRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    GymRepository gymRepository;

    @Test
    @DisplayName("클라이밍짐 즐겨찾기 조회 성공")
    void findByMemberAndGymId() {

        //given
        Member member = new Member("이메일주소","닉네임","비밀번호","자기소개","이미지 주소");
        memberRepository.save(member);
        Gym gym = new Gym("클라이밍짐", "주소", "전화번호", 3);
        gymRepository.save(gym);
        likeGymRepository.save(new LikeGym(member, gym));

        //when
         Optional<LikeGym> likeGym = likeGymRepository.findByMemberAndGym(member,gym);

        //then
        assertThat(likeGym.get().getMember().getEmail()).isEqualTo("이메일주소");
        assertThat(likeGym.get().getMember().getNickname()).isEqualTo("닉네임");
        assertThat(likeGym.get().getMember().getPassword()).isEqualTo("비밀번호");
        assertThat(likeGym.get().getMember().getContent()).isEqualTo("자기소개");
        assertThat(likeGym.get().getGym().getId()).isEqualTo(gym.getId());
    }
}