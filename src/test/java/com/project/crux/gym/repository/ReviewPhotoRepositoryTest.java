package com.project.crux.gym.repository;

import com.project.crux.gym.domain.Gym;
import com.project.crux.member.domain.Member;
import com.project.crux.gym.domain.Review;
import com.project.crux.gym.domain.ReviewPhoto;
import com.project.crux.gym.repository.GymRepository;
import com.project.crux.gym.repository.ReviewPhotoRepository;
import com.project.crux.gym.repository.ReviewRepository;
import com.project.crux.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewPhotoRepositoryTest {

    @Autowired
    ReviewPhotoRepository reviewPhotoRepository;
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
        gym = new Gym("이름","주소","전화번호",5.0);
        member = new Member("이메일","닉네임","비밀번호","자기소개", "이미지 주소");
        review = Review.builder()
                .score(4)
                .content("리뷰내용")
                .gym(gym)
                .member(member)
                .build();
    }

    @Test
    @DisplayName("리뷰로 리뷰 포토 DB 조회")
    void findAllByReview() {

        //given
        gymRepository.save(gym);
        memberRepository.save(member);
        reviewRepository.save(review);

        ReviewPhoto reviewPhoto = new ReviewPhoto();
        reviewPhoto.setReview(review);
        reviewPhoto.setImgUrl("이미지 주소.png");
        reviewPhotoRepository.save(reviewPhoto);


        //when
        List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReview(review);
        //then
        assertThat(reviewPhotoList.size()).isEqualTo(1);
        assertThat(reviewPhotoList.get(0).getReview()).isEqualTo(review);
        assertThat(reviewPhotoList.get(0).getImgUrl()).isEqualTo("이미지 주소.png");

    }
}