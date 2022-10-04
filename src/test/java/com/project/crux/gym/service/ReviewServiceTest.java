package com.project.crux.gym.service;

import com.project.crux.gym.domain.Gym;
import com.project.crux.member.domain.Member;
import com.project.crux.gym.domain.Review;
import com.project.crux.gym.domain.ReviewPhoto;
import com.project.crux.gym.domain.request.ReviewRequestDto;
import com.project.crux.gym.domain.response.ReviewResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.gym.repository.GymRepository;
import com.project.crux.gym.repository.ReviewPhotoRepository;
import com.project.crux.gym.repository.ReviewRepository;
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
    static Gym gym;
    static Review review;
    static Review review2;

    @BeforeAll
    static void initData() {
        member = new Member("이메일","닉네임","비밀번호","자기소개", "이미지 주소");
        gym = new Gym("이름","주소","전화번호",5.0);
        review = Review.builder()
                .id(1L)
                .score(4)
                .content("리뷰내용")
                .member(member)
                .gym(gym)
                .build();
        review2 = Review.builder()
                .id(2L)
                .score(5)
                .content("리뷰내용 2")
                .member(member)
                .gym(gym)
                .build();
        reviewphoto = new ReviewPhoto(1L, "이미지 주소.png", review);
    }

    @Nested
    @DisplayName("리뷰 작성")
    class CreateReviewTest {

        @Test
        @DisplayName("성공")
        void createReview() {

            //given
            Gym gym = new Gym();
            List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
            reviewPhotoList.add(reviewphoto);
            ReviewRequestDto requestDto = new ReviewRequestDto(4, "리뷰내용", reviewPhotoList);
            gym.getReviewList().add(review);
            when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));
            when(reviewPhotoRepository.save(reviewphoto)).thenReturn(reviewphoto);

            //when
            System.out.println(gym.getReviewList().size());
            ReviewResponseDto reviewResponseDto = reviewService.createReview(requestDto, 1L, new UserDetailsImpl());


            //then
            assertThat(reviewResponseDto.getScore()).isEqualTo(4);
            assertThat(reviewResponseDto.getContent()).isEqualTo("리뷰내용");
            assertThat(reviewResponseDto.getReviewPhotoList().get(0).getImgUrl()).isEqualTo("이미지 주소.png");
            assertThat(gym.getAvgScore()).isEqualTo(4);

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
            List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
            reviewPhotoList.add(reviewphoto);
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);
            gym.getReviewList().add(review);
            ReviewRequestDto requestDto = new ReviewRequestDto(5, "리뷰수정", reviewPhotoList);

            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));
            when(reviewPhotoRepository.save(reviewphoto)).thenReturn(reviewphoto);


            //when
            ReviewResponseDto reviewResponseDto = reviewService.updateReview(requestDto, 1L, userDetails);

            //then
            assertThat(reviewResponseDto.getScore()).isEqualTo(5);
            assertThat(reviewResponseDto.getContent()).isEqualTo("리뷰수정");
            assertThat(reviewResponseDto.getReviewPhotoList().get(0).getImgUrl()).isEqualTo("이미지 주소.png");

        }

        @Test
        @DisplayName("Review Id 조회 실패")
        void updateReview_failed_reviewId() {

            //given
            ReviewRequestDto requestDto = new ReviewRequestDto();

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.updateReview(requestDto, 1L, new UserDetailsImpl()));
            //then
            assertThat("해당 리뷰 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());

        }

        @Test
        @DisplayName("작성자만 수정가능 실패")
        void updateReview_failed_reviewId_Member() {

            //given
            ReviewRequestDto requestDto = new ReviewRequestDto();
            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.updateReview(requestDto, 1L, new UserDetailsImpl()));
            //then
            assertThat("리뷰 수정 권한이 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());

        }
    }

    @Nested
    @DisplayName("리뷰 삭제")
    class deleteReviewTest {

        @Test
        @DisplayName("성공")
        void deleteReview() {

            //given
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);
            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));

            //when
            String response = reviewService.deleteReview(userDetails,1L);

            //then
            assertThat(response).isEqualTo("후기 삭제 완료");
        }

        @Test
        @DisplayName("Review Id 조회 실패")
        void deleteReview_failed_reviewId() {

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.deleteReview(new UserDetailsImpl(),1L));

            //then
            assertThat("해당 리뷰 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());
        }

        @Test
        @DisplayName("작성자만 삭제 가능 실패")
        void deleteReview_failed_invalidMember() {

            //given
            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.deleteReview(new UserDetailsImpl(),1L));

            //then
            assertThat("리뷰 삭제 권한이 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());
        }

    }

    @Nested
    @DisplayName("리뷰 상세 조회")
    class getReviewTest {

        @Test
        @DisplayName("성공")
        void getReview() {

            //given
            List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
            reviewPhotoList.add(reviewphoto);
            when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review));
            when(reviewPhotoRepository.findAllByReview(review)).thenReturn(reviewPhotoList);

            //when
            ReviewResponseDto reviewResponseDto = reviewService.getReview(1L);

            //then
            assertThat(reviewResponseDto.getScore()).isEqualTo(4);
            assertThat(reviewResponseDto.getContent()).isEqualTo("리뷰내용");
            assertThat(reviewResponseDto.getReviewPhotoList().get(0).getImgUrl()).isEqualTo("이미지 주소.png");
        }

        @Test
        @DisplayName("Review Id 조회 실패")
        void getReview_failed() {

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> reviewService.getReview(1L));

            //then
            assertThat("해당 리뷰 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());
        }
    }

}