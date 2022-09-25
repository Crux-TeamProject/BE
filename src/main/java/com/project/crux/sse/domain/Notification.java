package com.project.crux.sse.domain;

import com.project.crux.sse.NotificationType;
import com.project.crux.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NotificationContent notificationContent;

    @Column(nullable = false)
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    public Notification(Member receiver, NotificationType notificationType, NotificationContent content) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.notificationContent = content;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
