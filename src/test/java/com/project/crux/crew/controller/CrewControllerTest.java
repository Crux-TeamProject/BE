package com.project.crux.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.crux.crew.controller.CrewController;
import com.project.crux.crew.domain.request.CrewRequestDto;
import com.project.crux.crew.domain.response.CrewResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.crew.service.CrewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CrewControllerTest {
    static final String CREW_NAME = "문어크루";
    static final String CREW_CONTENT = "문어크루 입니다.";
    static final String CREW_IMG_URL = "https://img.freepik.com/premium-vector/cute-octopus-cartoon-hand-drawn-style_42349-125.jpg?w=826";

    @InjectMocks
    private CrewController crewController;

    @Mock
    private CrewService crewService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(crewController).build();
    }

    @DisplayName("크루 생성 성공")
    @Test
    void createCrewSuccess() throws Exception {
        //given
        CrewRequestDto request = crewRequestDto();
        CrewResponseDto response = crewResponseDto();

        when(crewService.createCrew(any(CrewRequestDto.class), any(UserDetailsImpl.class)))
                .thenReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/crews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(CREW_NAME))
                .andExpect(jsonPath("$.data.content").value(CREW_CONTENT))
                .andExpect(jsonPath("$.data.imgUrl").value(CREW_IMG_URL))
                .andExpect(jsonPath("$.data.crewNum").value(1));
    }

/*    @DisplayName("전체 크루 조회 성공")
    @Test
    void findAllCrew() throws Exception {
        //given
        Long lastCrewId = 4L;
        int size = 2;
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("lastCrewId", String.valueOf(lastCrewId));
        request.add("size", String.valueOf(size));

        when(crewService.findAllCrew(lastCrewId, size))
                .thenReturn(new ArrayList<>());

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/crews")
                        .params(request)
        );

        //then
        resultActions.andExpect(status().isOk());
    }*/

    private CrewRequestDto crewRequestDto() {
        return CrewRequestDto.builder()
                .name(CREW_NAME)
                .content(CREW_CONTENT)
                .imgUrl(CREW_IMG_URL)
                .build();
    }

    private CrewResponseDto crewResponseDto() {
        return CrewResponseDto.builder()
                .id(1L)
                .name(CREW_NAME)
                .content(CREW_CONTENT)
                .imgUrl(CREW_IMG_URL)
                .crewNum(1)
                .build();
    }
}