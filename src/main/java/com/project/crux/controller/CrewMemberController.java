package com.project.crux.controller;

import com.project.crux.domain.request.CrewPhotoRequestDto;
import com.project.crux.domain.response.CrewPostResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.CrewMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseDto<String> registerPermit(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long crewId, @PathVariable Long memberId,
                                              @RequestParam Boolean permit) {
        return ResponseDto.success(crewMemberService.registerPermit(userDetails, crewId, memberId, permit));
    }

    //크루 탈퇴
    @DeleteMapping("/crew-members/{crewId}")
    public ResponseDto<String> withdrawCrew(@PathVariable Long crewId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.withdrawCrew(crewId, userDetails));
    }

    //크루 추방
    @DeleteMapping("/crew-members/{crewId}/{memberId}")
    public ResponseDto<String> dropMemberCrew(@PathVariable Long crewId, @PathVariable Long memberId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.dropMemberCrew(crewId, memberId, userDetails));
    }

    //크루 사진 등록
    @PostMapping("/crew-posts/{crewId}")
    public ResponseDto<CrewPostResponseDto> createCrewPost(@PathVariable Long crewId,
                                                             @RequestBody CrewPhotoRequestDto crewPhotoRequestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.createCrewPost(crewId, crewPhotoRequestDto, userDetails));
    }

    //크루 사진 조회
    @GetMapping("/crew-posts/{crewId}")
    public ResponseDto<List<CrewPostResponseDto>> findAllCrewPosts(@PathVariable Long crewId, @RequestParam Long lastPostId, @RequestParam int size) {
        return ResponseDto.success(crewMemberService.findAllCrewPosts(crewId, lastPostId, size));
    }

    //크루 사진 삭제
    @DeleteMapping("/crew-posts/{photoId}")
    public ResponseDto<String> deletePhoto(@PathVariable Long photoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.delete(photoId, userDetails));
    }
}
