package com.project.crux.member.domain.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
@Setter
public class SignupRequestDto {
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수 입력 값입니다")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{4,12}", message = "비밀번호는 4~12자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message =  "자기소개는 필수 입력 값입니다.")
    @Length(max = 150)
    private String content;
}
