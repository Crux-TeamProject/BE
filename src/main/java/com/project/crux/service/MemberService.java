package com.project.crux.service;

import com.project.crux.domain.Member;
import com.project.crux.domain.request.LoginRequestDto;
import com.project.crux.domain.request.SignupRequestDto;
import com.project.crux.domain.response.LoginResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.repository.MemberRepository;
import com.project.crux.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    //일반회원가입
    @Transactional
    public ResponseDto<?> signUpMember(SignupRequestDto signupRequestDto) {

        if (memberRepository.findByNickname(signupRequestDto.getNickname()).equals(signupRequestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATE_NICKNAME", "중복된 닉네임이 존재합니다.");
        }

        Member member = Member.builder()
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .content(signupRequestDto.getContent())
                .build();
        memberRepository.save(member);
        return ResponseDto.success("회원가입 축하합니다.");
    }

    //이메일 중복 확인
    public ResponseDto<?> checkEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            return ResponseDto.fail("DUPLICATE_EMAIL", "중복된 이메일이 존재합니다.");
        } else {
            return ResponseDto.success("사용 가능한 이메일 입니다.");
        }
    }

    public ResponseDto<?> checkNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            return ResponseDto.fail("DUPLICATE_NICKNAME", "중복된 닉네임이 존재합니다.");
        } else {
            return ResponseDto.success("사용 가능한 닉네임 입니다.");
        }
    }

    //일반 로그인
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = isPresentMember(loginRequestDto.getEmail());
        if (null == member) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "해당 유저 정보를 찾을 수 없습니다");
        }
        if (!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())) {
            return ResponseDto.fail("INVALID_PASSWORD", "비밀번호가 틀렸습니다");
        }

        String token = tokenProvider.generateToken(member);
        tokenToHeaders(token, response);

        return ResponseDto.success(
                LoginResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .build()
        );

    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(String token, HttpServletResponse response) {
        response.addHeader("Access_Token", "Bearer " + token);

    }


}
