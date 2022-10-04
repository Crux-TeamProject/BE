package com.project.crux.member.service;

import com.project.crux.crew.Status;
import com.project.crux.crew.domain.CrewMember;
import com.project.crux.crew.repository.CrewMemberRepository;
import com.project.crux.crew.repository.CrewRepository;
import com.project.crux.member.domain.Member;
import com.project.crux.member.domain.request.LoginRequestDto;
import com.project.crux.member.domain.request.SignupRequestDto;
import com.project.crux.member.domain.response.LoginResponseDto;
import com.project.crux.common.ResponseDto;
import com.project.crux.exception.CustomException;
import com.project.crux.exception.ErrorCode;
import com.project.crux.member.repository.MemberRepository;
import com.project.crux.security.jwt.TokenProvider;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private static final String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/test-12a64.appspot.com/o/images%2F%EC%82%AC%EC%9A%A9%EC%9E%90%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg?alt=media&token=979c173f-7c47-4292-9aa9-b6fb421e2bb8";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final CrewMemberRepository crewMemberRepository;
    private final CrewRepository crewRepository;

    //일반회원가입
    @Transactional
    public ResponseDto<?> signUpMember(SignupRequestDto signupRequestDto) {
        if (memberRepository.findByNickname(signupRequestDto.getNickname()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        if(memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        Member member = Member.builder()
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .content(signupRequestDto.getContent())
                .imgUrl(DEFAULT_IMAGE_URL)
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

    //닉네임 중복 확인
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
                        .imgUrl(member.getImgUrl())
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
        List<CrewMember> crewMembers = crewMemberRepository.findAllByMember(member).stream().filter(crewMember -> crewMember.getStatus().equals(Status.ADMIN)).collect(Collectors.toList());
        for (CrewMember crewMember : crewMembers) {
            crewRepository.delete(crewMember.getCrew());
        }
        memberRepository.delete(member);
        return ResponseDto.success("회원 탈퇴 완료");
    }
}
