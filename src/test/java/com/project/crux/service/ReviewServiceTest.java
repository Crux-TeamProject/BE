package com.project.crux.service;

import com.project.crux.domain.Gym;
import com.project.crux.domain.Member;
import com.project.crux.domain.Review;
import com.project.crux.domain.ReviewPhoto;
import com.project.crux.domain.request.ReviewRequestDto;
import com.project.crux.domain.response.ReviewResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.repository.GymRepository;
import com.project.crux.repository.ReviewPhotoRepository;
import com.project.crux.repository.ReviewRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GymRepository gymRepository;
    @Mock
    private ReviewPhotoRepository reviewPhotoRepository;

    static ReviewPhoto reviewphoto;
    static Member member;
    static Review review;

    @BeforeAll
    static void initData() {
        member = new Member("이메일","닉네임","비밀번호","자기소개");
        review = Review.builder()
                .id(1L)
                .score(4)
                .content("리뷰내용")
                .member(member)
                .build();
        reviewphoto = new ReviewPhoto(1L,"이미지 주소.png", review);
    }

    @Nested
    @DisplayName("리뷰 작성")
    class CreateReviewTest {

        @Test
        @DisplayName("성공")
        void createReview() {

            //given
            Gym gym = new Gym();
            gymRepository.save(gym);
            List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
            reviewPhotoList.add(reviewphoto);
            ReviewRequestDto requestDto = new ReviewRequestDto(4, "리뷰내용", reviewPhotoList);

            when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
            when(reviewPhotoRepository.save(reviewphoto)).thenReturn(reviewphoto);

            //when
            ReviewResponseDto reviewResponseDto = reviewService.createReview(requestDto, 1L, new UserDetailsImpl());

            //then
            assertThat(reviewResponseDto.getScore()).isEqualTo(4);
            assertThat(reviewResponseDto.getContent()).isEqualTo("리뷰내용");
            assertThat(reviewResponseDto.getReviewPhotoList().get(0).getImgUrl()).isEqualTo("이미지 주소.png");

        }


        @Test
        @DisplayName("Gym Id 조회 실패")
        void createReview_failed() {

            //given
            Long gymId = -3L;
            ReviewRequestDto requestDto = new ReviewRequestDto();

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    ()-> reviewService.createReview(requestDto,gymId, new UserDetailsImpl()));
            //then
            assertThat("해당 클라이밍짐 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());

        }

    }

    @Nested
    @DisplayName("리뷰 수정")
    class UpdateReviewTest {

        @Test
        @DisplayName("성공")
        void updateReview() {

            //given
            Gym gym = new Gym();
            gymRepository.save(gym);
            List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
            reviewPhotoList.add(reviewphoto);
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);
            ReviewRequestDto requestDto = new ReviewRequestDto(5, "리뷰수정", reviewPhotoList);

            when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));
            when(reviewPhotoRepository.save(reviewphoto)).thenReturn(reviewphoto);


            //when
            ReviewResponseDto reviewResponseDto = reviewService.updateReview(requestDto, 1L,1L, userDetails);

            //then
            assertThat(reviewResponseDto.getScore()).isEqualTo(5);
            assertThat(reviewResponseDto.getContent()).isEqualTo("리뷰수정");
            assertThat(reviewResponseDto.getReviewPhotoList().get(0).getImgUrl()).isEqualTo("이미지 주소.png");

        }

        @Test
        @DisplayName("Review Id 조회 실패")
        void updateReview_failed_reviewId() {

            //given
            Gym gym = new Gym();
            ReviewRequestDto requestDto = new ReviewRequestDto();
            when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.updateReview(requestDto, 1L,1L ,new UserDetailsImpl()));
            //then
            assertThat("해당 리뷰 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());

        }

        @Test
        @DisplayName("작성자만 수정가능 실패")
        void updateReview_failed_reviewId_Member() {

            //given
            Gym gym = new Gym();
            ReviewRequestDto requestDto = new ReviewRequestDto();
            when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.updateReview(requestDto, 1L,1L ,new UserDetailsImpl()));
            //then
            assertThat("리뷰 수정 권한이 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());

        }
    }
}