package com.project.crux.controller;

import com.project.crux.domain.request.LoginRequestDto;
import com.project.crux.domain.request.SignupRequestDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.MemberService;
import com.project.crux.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //일반 회원가입
    @PostMapping(value = "/members/signup")
    public ResponseDto<?>signUpMember(@RequestBody @Valid SignupRequestDto signupRequestDto){
        return memberService.signUpMember(signupRequestDto);
    }
    //이메일 중복 확인
    @GetMapping("/members/email-check")
    public ResponseDto<?>checkEmail(@RequestParam String email){
        return memberService.checkEmail(email);
    }
    //닉네임 중복 확인
    @GetMapping("/members/nickname-check")
    public ResponseDto<?>checkNickname(@RequestParam String nickname){
        return memberService.checkNickname(nickname);
    }
    //일반 로그인
    @PostMapping("/members/login")
    public ResponseDto<?>login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                               HttpServletResponse response){
        return memberService.login(loginRequestDto, response);
    }
    //일반 회원 탈퇴
    @DeleteMapping("/members/withdraw")
    public ResponseDto<?>withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.withdraw(userDetails);
    }
}
