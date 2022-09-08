package com.project.crux.controller;

import com.project.crux.domain.response.ResponseDto;
import com.project.crux.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;
    //마이 페이지 조회
    @GetMapping("/members/{memberId}")
    public ResponseDto<?> viewMypage(@PathVariable Long memberId){
        return mypageService.viewMypage(memberId);
    }
}
