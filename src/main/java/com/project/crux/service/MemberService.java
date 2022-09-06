package com.project.crux.service;

import com.project.crux.domain.Member;
import com.project.crux.domain.request.LoginRequestDto;
import com.project.crux.domain.request.SignupRequestDto;
import com.project.crux.domain.response.LoginResponseDto;
import com.project.crux.domain.response.ResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.repository.MemberRepository;
import com.project.crux.security.jwt.TokenProvider;
import com.project.crux.security.jwt.UserDetailsImpl;
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
        if (memberRepository.findByNickname(signupRequestDto.getNickname()).isPresent()) {
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
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        return ResponseDto.success("사용 가능한 이메일 입니다.");
    }

    public ResponseDto<?> checkNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return ResponseDto.success("사용 가능한 닉네임 입니다.");

    }

    //일반 로그인
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = isPresentMember(loginRequestDto.getEmail());
        if (null == member) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
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

    @Transactional
    public ResponseDto<?> withdraw(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        memberRepository.delete(member);
        return ResponseDto.success("회원 탈퇴 완료");
    }
}
