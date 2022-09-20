package com.project.crux.member.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MypageRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "유저 프로필 이미지는 필수 입니다.")
    private String imgUrl;

    @NotBlank(message =  "자기소개는 필수 입력 값입니다.")
    @Length(max = 150)
    private String content;
}
