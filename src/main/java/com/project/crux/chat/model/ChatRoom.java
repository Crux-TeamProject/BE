package com.project.crux.chat.model;

import com.project.crux.crew.domain.Crew;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    private Crew crew;

    public ChatRoom(Crew crew) {
        this.crew = crew;
    }
}
