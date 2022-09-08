package com.project.crux.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.crux.domain.request.MypageRequestDto;
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

    @Column
    private String content;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MemberCrew> memberCrewList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<LikeGym> likeGymList;

    @Builder
    public Member(String email, String nickname, String password, String content) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
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
        return id != null && Objects.equals(id, member.id);
    }

    public void update(MypageRequestDto mypageRequestDto) {
        this.content = mypageRequestDto.getContent();
        this.nickname = mypageRequestDto.getNickname();
    }
}
