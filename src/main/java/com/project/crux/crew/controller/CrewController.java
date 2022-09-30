package com.project.crux.crew.controller;

import com.project.crux.crew.domain.request.CrewRequestDto;
import com.project.crux.crew.domain.response.CrewFindOneResponseDto;
import com.project.crux.crew.domain.response.CrewResponseDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.crew.service.CrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;

    //크루 생성
    @PostMapping("/crews")
    public ResponseDto<CrewResponseDto> createCrew(@RequestBody CrewRequestDto crewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CrewResponseDto crew = crewService.createCrew(crewRequestDto, userDetails);
        return ResponseDto.success(crew);
    }

    //크루 전체 조회
    @GetMapping("/crews")
    public ResponseDto<Page<CrewResponseDto>> findAllCrew(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CrewResponseDto> crewResponseDtoList = crewService.findAllCrew(pageable);
        return ResponseDto.success(crewResponseDtoList);
    }

    //인기 크루 조회
    @GetMapping("/crews/popular")
    public ResponseDto<Page<CrewResponseDto>> findAllPopularCrew(@PageableDefault(sort = {"countOfLike", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CrewResponseDto> crewResponseDtoList = crewService.findAllCrew(pageable);
        return ResponseDto.success(crewResponseDtoList);
    }

    //크루 상세 조회
    @GetMapping("/crews/{crewId}")
    public ResponseDto<CrewFindOneResponseDto> findCrew(@PathVariable Long crewId) {
        return ResponseDto.success(crewService.findCrew(crewId));
    }

    //크루 검색
    @GetMapping("/crews/search")
    public ResponseDto<List<CrewResponseDto>> searchCrew(@RequestParam String query) {
        return ResponseDto.success(crewService.searchCrew(query));
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
}
