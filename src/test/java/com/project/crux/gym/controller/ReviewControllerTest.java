package com.project.crux.gym.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.crux.gym.domain.Review;
import com.project.crux.gym.domain.request.ReviewRequestDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.gym.domain.response.ReviewPhotoResponseDto;
import com.project.crux.gym.domain.response.ReviewResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.gym.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.reflect.Type;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    Review review;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

       review = Review.builder()
                .id(1L)
                .score(4)
                .content("리뷰내용")
                .build();
    }

    @Test
    @DisplayName("리뷰 작성 성공")
    void createReview() throws Exception {

        //given
        ReviewRequestDto requestDto  = new ReviewRequestDto(review.getScore(),review.getContent(), null);
        ReviewResponseDto responseDto = new ReviewResponseDto(review);
        responseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto());
        when(reviewService.createReview(any(ReviewRequestDto.class), eq(1L), eq(new UserDetailsImpl()))).thenReturn(responseDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/reviews/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type ResponseDto = new TypeToken<ResponseDto<ReviewResponseDto>>() {
        }.getType();

        ResponseDto<ReviewResponseDto> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto);
        assertThat(response.getData().getScore()).isEqualTo(4);
        assertThat(response.getData().getContent()).isEqualTo("리뷰내용");
        assertThat(response.getData().getReviewPhotoList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void updateReview() throws Exception {

        //given
        ReviewRequestDto requestDto  = new ReviewRequestDto(review.getScore(),review.getContent(), null);
        ReviewResponseDto responseDto = new ReviewResponseDto(review);
        responseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto());
        when(reviewService.updateReview(any(ReviewRequestDto.class), eq(1L), eq(new UserDetailsImpl()))).thenReturn(responseDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/reviews/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type ResponseDto = new TypeToken<ResponseDto<ReviewResponseDto>>() {
        }.getType();

        ResponseDto<ReviewResponseDto> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto);
        assertThat(response.getData().getScore()).isEqualTo(4);
        assertThat(response.getData().getContent()).isEqualTo("리뷰내용");
        assertThat(response.getData().getReviewPhotoList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deleteReview() throws Exception {

        //given
        when(reviewService.deleteReview(new UserDetailsImpl(),1L)).thenReturn("후기 삭제 완료");
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/reviews/" + 1L)
        );
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        ResponseDto<String> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        assertThat(response.getData()).isEqualTo("후기 삭제 완료");
    }

    @Test
    @DisplayName("리뷰 상세조회 성공")
    void getReview() throws Exception {

        //given
        ReviewResponseDto responseDto = new ReviewResponseDto(review);
        responseDto.getReviewPhotoList().add(new ReviewPhotoResponseDto());
        when(reviewService.getReview(1L)).thenReturn(responseDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/reviews/" + 1L)
        );
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type ResponseDto = new TypeToken<ResponseDto<ReviewResponseDto>>() {
        }.getType();

        ResponseDto<ReviewResponseDto> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto);
        assertThat(response.getData().getScore()).isEqualTo(4);
        assertThat(response.getData().getContent()).isEqualTo("리뷰내용");
        assertThat(response.getData().getReviewPhotoList().size()).isEqualTo(1);
    }
}