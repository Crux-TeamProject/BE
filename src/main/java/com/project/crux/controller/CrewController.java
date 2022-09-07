package com.project.crux.controller;

import com.project.crux.domain.request.CrewRequestDto;
import com.project.crux.domain.response.CrewResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import com.project.crux.service.CrewService;
import lombok.RequiredArgsConstructor;
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
}
