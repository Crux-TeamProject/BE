package com.project.crux.gym.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.crux.gym.domain.Gym;
import com.project.crux.gym.domain.response.GymResponseDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.gym.controller.GymController;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.gym.service.GymService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        gymResponseDtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Gym gym = new Gym("???????????????", "??????", "????????????", i);
            GymResponseDto gymResponseDto = GymResponseDto.from(gym);
            gymResponseDtoList.add(gymResponseDto);
        }
    }


    @Test
    @DisplayName("????????????????????? - ?????? ??????")
    void getPopularGyms() throws Exception {

        //given
/*        Pageable pageable = PageRequest.of(0,5, Sort.by("avgScore").descending());
        when(gymService.getPopularGyms(pageable)).thenReturn(gymResponseDtoList);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", String.valueOf(0));
        info.add("size", String.valueOf(5));
        info.add("sort", "DESC");*/

        Long lastArticleId = 5L;
        int size = 5;
        when(gymService.getPopularGyms(lastArticleId, size)).thenReturn(gymResponseDtoList);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("lastArticleId", String.valueOf(lastArticleId));
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
    @DisplayName("??????????????? - ?????? ?????? ??????")
    void getSearchGyms() throws Exception {

        //given
/*        Pageable pageable = PageRequest.of(0,5, Sort.by("id").descending());
        when(gymService.getSearchGyms("????????????", pageable)).thenReturn(gymResponseDtoList);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", String.valueOf(0));
        info.add("size", String.valueOf(5));
        info.add("query", "????????????");
        info.add("sort", "DESC");*/


        //given
        Long lastArticleId = 400L;
        int size = 5;
        when(gymService.getSearchGyms("????????????", lastArticleId, size)).thenReturn(gymResponseDtoList);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("query","????????????");
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
    @Order(1)
    @DisplayName("??????????????? ?????? ?????? ??????")
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

        assertThat(response.getData().getName()).isEqualTo("???????????????");
        assertThat(response.getData().getLocation()).isEqualTo("??????");
        assertThat(response.getData().getPhone()).isEqualTo("????????????");
        assertThat(response.getData().getAvgScore()).isEqualTo(gymId-1);
    }


    @Test
    @DisplayName("??????????????? ???????????? ?????? ?????? ??????")
    void likeGym() throws Exception {

        //given
        Long gymId = 3L;
        when(gymService.likeGym(new UserDetailsImpl(), gymId)).thenReturn("?????? ?????? ?????? ??????");
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/gyms/" + gymId + "/like")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("data").value("?????? ?????? ?????? ??????"));
    }
}