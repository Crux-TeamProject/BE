package com.project.crux.repository;

import com.project.crux.domain.Gym;
import com.project.crux.domain.Member;
import com.project.crux.domain.Review;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    GymRepository gymRepository;
    @Autowired
    MemberRepository memberRepository;



    @Test
    @DisplayName("ID로 리뷰 DB 조회")
    void findById() {

        //given
        Long reviewId = 1L;
        Gym gym = new Gym("이름", "주소", "전화번호",5.0);
        gymRepository.save(gym);
        Member member = new Member("이메일", "닉네임", "비밀번호", "자기소개");
        memberRepository.save(member);
        Review review = Review.builder()
                .score(4)
                .content("리뷰내용")
                .gym(gym)
                .member(member)
                .build();
        reviewRepository.save(review);

        //when
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        //then
        optionalReview.ifPresent(findReview -> {
            assertThat(findReview.getScore()).isEqualTo(4);
            assertThat(findReview.getContent()).isEqualTo("리뷰내용");
            assertThat(findReview.getGym()).isEqualTo(gym);
            assertThat(findReview.getMember()).isEqualTo(member);
        });
    }

    @Test
    @DisplayName("Gym으로 리뷰 DB 조회")
    void findByGym() {

        //given
        Gym gym = new Gym("이름","주소","전화번호",5.0);
        gymRepository.save(gym);
        Member member = new Member("이메일","닉네임","비밀번호","자기소개");
        memberRepository.save(member);
        Review review = Review.builder()
                .score(4)
                .content("리뷰 내용")
                .gym(gym)
                .member(member)
                .build();
        reviewRepository.save(review);

        //when
        List<Review> reviewList = reviewRepository.findByGym(gym);

        //then
        assertThat(reviewList.size()).isEqualTo(1);
        assertThat(reviewList.get(0).getGym()).isEqualTo(gym);
        assertThat(reviewList.get(0).getScore()).isEqualTo(4);
        assertThat(reviewList.get(0).getContent()).isEqualTo("리뷰 내용");
    }
}