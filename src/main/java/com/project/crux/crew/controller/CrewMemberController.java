package com.project.crux.crew.controller;

import com.project.crux.crew.domain.request.CrewPhotoRequestDto;
import com.project.crux.crew.domain.response.CrewMemberResponseDto;
import com.project.crux.crew.domain.response.CrewPostResponseDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.crew.service.CrewMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

    //api 크루가입 신청 ,취소
    @PostMapping("/crews/{crewId}/members")
    public ResponseDto<String> registerSubmit(@PathVariable Long crewId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.registerSubmit(crewId, userDetails));
    }

    //api 크루 가입 승인
    @PostMapping("/crews/{crewId}/members/{memberId}")
    public ResponseDto<String> registerPermit(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long crewId, @PathVariable Long memberId,
                                              @RequestParam Boolean permit) {
        return ResponseDto.success(crewMemberService.registerPermit(userDetails, crewId, memberId, permit));
    }

    //크루 가입 신청 목록 조회
    @GetMapping("/crews/{crewId}/members")
    public ResponseDto<List<CrewMemberResponseDto>> findSummitCrewMembers(@PathVariable Long crewId,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.findSummitCrewMembers(crewId, userDetails));
    }

    //크루 탈퇴
    @DeleteMapping("/crews/{crewId}/members")
    public ResponseDto<String> withdrawCrew(@PathVariable Long crewId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.withdrawCrew(crewId, userDetails));
    }

    //크루 추방
    @DeleteMapping("/crews/{crewId}/members/{memberId}")
    public ResponseDto<String> dropMemberCrew(@PathVariable Long crewId, @PathVariable Long memberId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.dropMemberCrew(crewId, memberId, userDetails));
    }

    //크루 사진 등록
    @PostMapping("/crews/{crewId}/posts")
    public ResponseDto<CrewPostResponseDto> createCrewPost(@PathVariable Long crewId,
                                                           @RequestBody CrewPhotoRequestDto crewPhotoRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.createCrewPost(crewId, crewPhotoRequestDto, userDetails));
    }

    //크루 사진 조회
    @GetMapping("/crews/{crewId}/posts")
    public ResponseDto<List<CrewPostResponseDto>> findAllCrewPosts(@PathVariable Long crewId,
                                                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseDto.success(crewMemberService.findAllCrewPosts(crewId, pageable));
    }

    //모든 크루 사진 조회
    @GetMapping("/crews/posts")
    public ResponseDto<List<CrewPostResponseDto>> findAllCrewsPosts(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseDto.success(crewMemberService.findAllCrewsPosts(pageable));
    }

    //크루 사진 삭제
    @DeleteMapping("/crews/posts/{postId}")
    public ResponseDto<String> deletePhoto(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.delete(postId, userDetails));
    }

    //크루 좋아요 추가
    @PostMapping("/crews/{crewId}/like")
    public ResponseDto<String> like(@PathVariable Long crewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.like(crewId, userDetails));
    }

    //크루 좋아요 삭제
    @DeleteMapping("/crews/{crewId}/like")
    public ResponseDto<String> unlike(@PathVariable Long crewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewMemberService.unLike(crewId, userDetails));
    }
}
