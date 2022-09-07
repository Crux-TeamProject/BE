package com.project.crux.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}
