package com.project.crux.repository;

import com.project.crux.domain.Gym;
import com.project.crux.domain.Member;
import com.project.crux.domain.Review;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    GymRepository gymRepository;
    @Autowired
    MemberRepository memberRepository;

    static Review review;
    static Gym gym;
    static Member member;

    @BeforeAll
    static void initData() {
        gym = new Gym("이름","주소","전화번호");
        member = new Member("이메일","닉네임","비밀번호","자기소개");
        review = Review.builder()
                .score(4)
                .content("리뷰내용")
                .gym(gym)
                .member(member)
                .build();
    }

    @Test
    @DisplayName("ID로 리뷰 DB 조회")
    void findById() {

        //given
        Long reviewId = 1L;
        gymRepository.save(gym);
        memberRepository.save(member);
        reviewRepository.save(review);

        //when
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        //then
        optionalReview.ifPresent(review -> {
            assertThat(review.getScore()).isEqualTo(4);
            assertThat(review.getContent()).isEqualTo("리뷰내용");
            assertThat(review.getGym()).isEqualTo(gym);
            assertThat(review.getMember()).isEqualTo(member);
        });
    }
}