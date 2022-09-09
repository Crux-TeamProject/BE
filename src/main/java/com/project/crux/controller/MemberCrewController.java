package com.project.crux.controller;

import com.project.crux.domain.response.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.MemberCrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberCrewController {

    private final MemberCrewService memberCrewService;

    //api 크루가입 신청
    @PostMapping("membercrews/{crewId}")
    public ResponseDto<String> registerSubmit(@PathVariable Long crewId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(memberCrewService.registerSubmit(crewId, userDetails));
    }

    //api 크루 가입 승인
    @PostMapping("membercrews/{crewId}/{memberId}")
    public ResponseDto<String> registerPermit(@PathVariable Long crewId, @PathVariable Long memberId) {
        return ResponseDto.success(memberCrewService.registerPermit(crewId, memberId));
    }
}
