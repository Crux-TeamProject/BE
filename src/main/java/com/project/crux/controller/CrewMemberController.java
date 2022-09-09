package com.project.crux.controller;

import com.project.crux.domain.response.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.CrewMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

    //api 크루가입 신청
    @PostMapping("crew-members/{crewId}")
    public ResponseDto<String> registerSubmit(@PathVariable Long crewId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.registerSubmit(crewId, userDetails));
    }

    //api 크루 가입 승인
    @PostMapping("crew-members/{crewId}/{memberId}")
    public ResponseDto<String> registerPermit(@PathVariable Long crewId, @PathVariable Long memberId) {
        return ResponseDto.success(crewMemberService.registerPermit(crewId, memberId));
    }

    //크루 탈퇴
    @DeleteMapping("/crew-members/{crewId}")
    public ResponseDto<String> withdrawCrew(@PathVariable Long crewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.withdrawCrew(crewId, userDetails));
    }

    //크루 추방
    @DeleteMapping("/crew-members/{crewId}/{memberId}")
    public ResponseDto<String> dropMemberCrew(@PathVariable Long crewId, @PathVariable Long memberId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.dropMemberCrew(crewId, memberId, userDetails));
    }
}
