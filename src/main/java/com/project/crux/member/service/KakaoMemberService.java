package com.project.crux.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.crux.member.domain.Member;
import com.project.crux.member.domain.response.LoginResponseDto;
import com.project.crux.infrastructure.kakao.request.KakaoMemberInfoDto;
import com.project.crux.member.repository.MemberRepository;
import com.project.crux.security.jwt.TokenProvider;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoMemberService {
    private static final String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/test-12a64.appspot.com/o/images%2F%EC%82%AC%EC%9A%A9%EC%9E%90%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg?alt=media&token=979c173f-7c47-4292-9aa9-b6fb421e2bb8";


    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    private final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public void kakaoLogin(String code, HttpServletResponse response) throws IOException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo(accessToken);

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member kakaoMember = registerKakaoMemberIfNeeded(kakaoMemberInfo);

        // 4. 강제 로그인 처리
        forceLogin(kakaoMember, response);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "6a2435f02f897dc1c87f7cca3eb2bfbb");              // rest api 키
        body.add("redirect_uri", "https://youmadeit.shop/kakaologin");  // 플랫폼
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();

        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        String email = (jsonNode.get("kakao_account")
                .get("email") != null) ? jsonNode.get("kakao_account")
                .get("email").asText() : null;

        return new KakaoMemberInfoDto(id, nickname, email);
    }

    private Member registerKakaoMemberIfNeeded(KakaoMemberInfoDto kakaoMemberInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoMemberInfo.getId();
        Member kakaoMember = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoMember == null) {
            // 회원가입
            // username: kakao nickname
            String username = UUID.randomUUID().toString();
            String nickname = kakaoMemberInfo.getNickname();

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            // email: kakao email
            String email = kakaoMemberInfo.getEmail();

            kakaoMember = new Member(nickname,encodedPassword,email,kakaoId, DEFAULT_IMAGE_URL);
            memberRepository.save(kakaoMember);
        }
        return kakaoMember;
    }

    private void forceLogin(Member kakaoMember, HttpServletResponse response) throws IOException {

        UserDetails userDetails = new UserDetailsImpl(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails1 = ((UserDetailsImpl) authentication.getPrincipal());

        final String token = tokenProvider.generateToken(userDetails1.getMember());

        response.addHeader("Authorization", "Bearer" + " " + token);

        // 헤더에 유저정보 같이 넣었지만 한글이 안나옴 다른곳에 넣어야함

        response.setContentType("application/json; charset=utf-8");
        Member member = userDetails1.getMember();
        LoginResponseDto loginResponseDto = new LoginResponseDto(member.getId(), member.getEmail(), member.getNickname(), member.getImgUrl());
        String result = mapper.writeValueAsString(loginResponseDto);
        response.getWriter().write(result);

        System.out.println("로그인 내려주는 값" + result);
    }
}