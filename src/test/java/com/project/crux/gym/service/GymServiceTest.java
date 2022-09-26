package com.project.crux.gym.service;

import com.project.crux.gym.domain.Gym;
import com.project.crux.gym.domain.LikeGym;
import com.project.crux.gym.domain.Review;
import com.project.crux.gym.domain.ReviewPhoto;
import com.project.crux.gym.domain.response.GymResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.gym.repository.GymRepository;
import com.project.crux.gym.repository.LikeGymRepository;
import com.project.crux.gym.repository.ReviewPhotoRepository;
import com.project.crux.gym.repository.ReviewRepository;
import com.project.crux.gym.service.GymService;
import com.project.crux.member.domain.Member;
import com.project.crux.security.jwt.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymServiceTest {

    @InjectMocks
    private GymService gymService;

    @Mock
    private GymRepository gymRepository;
    @Mock
    private LikeGymRepository likeGymRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewPhotoRepository reviewPhotoRepository;

    static Page<Gym> gymPage;

    @BeforeAll
    static void initData() {
        List<Gym> gyms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Gym gym = new Gym("클라이밍짐", "주소", "전화번호", i);
            gyms.add(gym);
        }
        gymPage = new PageImpl<>(gyms);
    }

  /*  @Nested
    @DisplayName("인기클라이밍짐")
    class PopularGymsTest {

        @Test
        @DisplayName("조회 성공")
        void getPopularGyms() {

            //given
            Pageable pageable = null;
            when(gymRepository.findAll(pageable)).thenReturn(gymPage);

            //when
            final List<GymResponseDto> gymResponseDtos = gymService.getPopularGyms(pageable);

            //then
            assertThat(gymResponseDtos.size()).isEqualTo(5);
        }

    }

    @Nested
    @DisplayName("클라이밍짐 검색")
    class SearchGymsTest {

        @Test
        @DisplayName("조회 성공")
        void getSearchGyms() {
            //given
            String query = "클라이밍";
            PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
            when(gymRepository.findByNameContains(query, pageRequest)).thenReturn(gymPage);

            //when
            final List<GymResponseDto> gymResponseDtos = gymService.getSearchGyms(query,pageRequest);

            //then
            assertThat(gymResponseDtos.size()).isEqualTo(5);
        }

    }*/

    @Nested
    @DisplayName("클라이밍짐 상세")
    class GymTest {

        @Test
        @DisplayName("조회 성공")
        void getGym() {

            //given
            Long gymId = 3L;
            Gym gym = new Gym("클라이밍짐", "주소", "전화번호",3);
            List<Review> reviewList = new ArrayList<>();
            List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
            Review review = new Review(5,"content",new Member());
            reviewList.add(review);
            Authentication authentication = new UsernamePasswordAuthenticationToken(null,"",null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
            when(reviewRepository.findByGym(gym)).thenReturn(reviewList);
            when(reviewPhotoRepository.findAllByReview(reviewList.get(0))).thenReturn(reviewPhotoList);

            //when
            final GymResponseDto gymResponseDto = gymService.getGym(gymId);

            //then
            assertThat(gymResponseDto.getName()).isEqualTo("클라이밍짐");
            assertThat(gymResponseDto.getLocation()).isEqualTo("주소");
            assertThat(gymResponseDto.getPhone()).isEqualTo("전화번호");
            assertThat(gymResponseDto.getAvgScore()).isEqualTo(3);
            assertThat(gymResponseDto.getReviews().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("조회 실패")
        void getGym_failed() {

            //given
            Long gymId = -3L;

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> gymService.getGym(gymId));
            //then
            assertThat("해당 클라이밍짐 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());
        }
    }

    @Nested
    @DisplayName("클라이밍짐 즐겨찾기")
    class LikeGymTest {

        @Test
        @DisplayName("추가 성공")
        void likeGym_delete() {

            //given
            Member member = new Member();
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);

            Gym gym = new Gym();
            gymRepository.save(gym);

            when(gymRepository.findById(gym.getId())).thenReturn(Optional.of(gym));
            when(likeGymRepository.findByMemberAndGym(member, gym)).thenReturn(Optional.empty());

            //when
            String success = gymService.likeGym(userDetails,gym.getId());

            //then
            assertThat(success).isEqualTo("즐겨 찾기 추가 완료");
        }

        @Test
        @DisplayName("삭제 성공")
        void likeGym_add() {

            //given
            Member member = new Member();
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);

            Gym gym = new Gym();
            gymRepository.save(gym);

            LikeGym likGym = new LikeGym(member, gym);

            when(gymRepository.findById(gym.getId())).thenReturn(Optional.of(gym));
            when(likeGymRepository.findByMemberAndGym(member, gym)).thenReturn(Optional.of(likGym));

            //when
            String success = gymService.likeGym(userDetails,gym.getId());

            //then
            assertThat(success).isEqualTo("즐겨 찾기 삭제 완료");
        }

        @Test
        @DisplayName("실패")
        void likeGym_failed() {

            //given
            Long gymId = -3L;

            //when
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> gymService.likeGym(new UserDetailsImpl(),gymId));
            //then
            assertThat("해당 클라이밍짐 정보를 찾을 수 없습니다").isEqualTo(exception.getErrorCode().getErrorMessage());

        }
    }
}