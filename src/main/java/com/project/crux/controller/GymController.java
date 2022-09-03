package com.project.crux.controller;

import com.project.crux.domain.response.ResponseDto;
import com.project.crux.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    //api 인기 클라이밍짐 조회
    @GetMapping("/gyms/popular")
    public ResponseDto<?> getPopularGyms(@RequestParam double lastGymScore, @RequestParam int size) {
        return gymService.getPopularGyms(lastGymScore, size);
    }
}
