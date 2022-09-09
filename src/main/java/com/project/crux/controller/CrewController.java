package com.project.crux.controller;

import com.project.crux.domain.request.CrewRequestDto;
import com.project.crux.domain.response.CrewResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.CrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;

    @PostMapping("/crews")
    public ResponseEntity<?> createCrew(@RequestBody CrewRequestDto crewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CrewResponseDto crew = crewService.createCrew(crewRequestDto, userDetails);
        return ResponseEntity.ok(ResponseDto.success(crew));
    }

    @GetMapping("/crews")
    public ResponseEntity<?> findAllCrew(@RequestParam Long lastCrewId, @RequestParam int size) {
        List<CrewResponseDto> crewResponseDtoList = crewService.findAllCrew(lastCrewId, size);
        return ResponseEntity.ok(ResponseDto.success(crewResponseDtoList));
    }

    @GetMapping("/crews/popular")
    public ResponseEntity<?> findAllPopularCrew(@PageableDefault(sort = "countOfMemberCrewList", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CrewResponseDto> crewResponseDtoList = crewService.findAllPopularCrew(pageable);
        return ResponseDto.success(crewResponseDtoList);
    }

    //크루 상세 조회
    @GetMapping("/crews/{crewId}")
    public ResponseDto<CrewFindOneResponseDto> findCrew(@PathVariable Long crewId) {
        return ResponseDto.success(crewService.findCrew(crewId));
    }

    //크루 수정
    @PutMapping("/crews/{crewId}")
    public ResponseDto<CrewResponseDto> updateCrew(@PathVariable Long crewId, @RequestBody CrewRequestDto crewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewService.updateCrew(crewId, crewRequestDto, userDetails));
    }

    //크루 삭제
    @DeleteMapping("/crews/{crewId}")
    public ResponseDto<String> deleteCrew(@PathVariable Long crewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewService.deleteCrew(crewId, userDetails));
    }

    //크루 탈퇴
    @DeleteMapping("/memberCrews/{crewId}")
    public ResponseDto<String> withdrawCrew(@PathVariable Long crewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewService.withdrawCrew(crewId, userDetails));
    }

    //크루 추방
    @DeleteMapping("/memberCrews/{crewId}/{memberId}")
    public ResponseDto<String> dropMemberCrew(@PathVariable Long crewId, @PathVariable Long memberId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(crewService.dropMemberCrew(crewId, memberId, userDetails));
    }
}
