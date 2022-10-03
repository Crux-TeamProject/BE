package com.project.crux.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.crux.crew.domain.CrewMember;
import com.project.crux.crew.domain.LikeCrew;
import com.project.crux.gym.domain.LikeGym;
import com.project.crux.member.domain.request.MypageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    private Long kakaoId;
    @Column
    private String content;

    @Column
    private String imgUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<CrewMember> crewMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<LikeGym> likeGymList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<LikeCrew> likeCrewList;

    @Builder
    public Member(String email, String nickname, String password, String content,String imgUrl) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.imgUrl = imgUrl;
    }
    //카카오 서비스 생성자
    @Builder
    public Member(String nickname, String password, String email, Long kakaoId, String imgUrl) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.kakaoId = kakaoId;
        this.imgUrl = imgUrl;
    }


    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.getId());
    }

    public void update(MypageRequestDto mypageRequestDto) {
        this.content = mypageRequestDto.getContent();
        this.nickname = mypageRequestDto.getNickname();
        this.imgUrl = mypageRequestDto.getImgUrl();
    }
}
