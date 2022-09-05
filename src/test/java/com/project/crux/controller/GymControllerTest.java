package com.project.crux.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.crux.domain.Gym;
import com.project.crux.domain.response.GymResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.service.GymService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class GymControllerTest {

    @InjectMocks
    private GymController gymController;

    @Mock
    private GymService gymService;

    private MockMvc mockMvc;

    List<GymResponseDto> gymResponseDtoList;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(gymController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        gymResponseDtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Gym gym = new Gym("클라이밍짐", "주소", "전화번호", i);
            GymResponseDto gymResponseDto = new GymResponseDto(gym);
            gymResponseDtoList.add(gymResponseDto);
        }
    }


    @Test
    @DisplayName("인기클라이밍짐 - 조회 성공")
    void getPopularGyms() throws Exception {

        //given
        double lastAvgScore = 5;
        int size = 5;
        when(gymService.getPopularGyms(lastAvgScore, size)).thenReturn(gymResponseDtoList);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("lastAvgScore", String.valueOf(lastAvgScore));
        info.add("size", String.valueOf(size));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/gyms/popular")
                        .params(info)
        );
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        ResponseDto<List<GymResponseDto>> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        List<GymResponseDto> gymList = response.getData();
        assertThat(gymList.size()).isEqualTo(gymResponseDtoList.size());
    }

    @Test
    @DisplayName("클라이밍짐 - 검색 조회 성공")
    void getSearchGyms() throws Exception {

        //given
        Long lastArticleId = 400L;
        int size = 5;
        when(gymService.getSearchGyms("클라이밍", lastArticleId, size)).thenReturn(gymResponseDtoList);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("query","클라이밍");
        info.add("lastArticleId", String.valueOf(lastArticleId));
        info.add("size", String.valueOf(size));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/gyms/search")
                        .params(info)
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        ResponseDto<List<GymResponseDto>> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        List<GymResponseDto> gymList = response.getData();
        assertThat(gymList.size()).isEqualTo(gymResponseDtoList.size());
    }

    @Test
    void getGym() throws Exception {

        //given
        Long gymId = 3L;
        when(gymService.getGym(gymId)).thenReturn(gymResponseDtoList.get((int) (gymId-1)));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/gyms/"+gymId)
        );
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type ResponseDto = new TypeToken<ResponseDto<GymResponseDto>>() {
        }.getType();

        ResponseDto<GymResponseDto> response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ResponseDto);


        assertThat(response.getData().getName()).isEqualTo("클라이밍짐");
        assertThat(response.getData().getLocation()).isEqualTo("주소");
        assertThat(response.getData().getPhone()).isEqualTo("전화번호");
        assertThat(response.getData().getAvgScore()).isEqualTo(gymId-1);
    }
}