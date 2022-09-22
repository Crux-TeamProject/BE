package com.project.crux.sse.repository;

import com.project.crux.sse.domain.Notification;
import com.project.crux.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findAllByReceiverOrderByIdDesc(Member receiver);
    Long countByIsReadFalseAndReceiver(Member receiver);

    void deleteAllByReceiver(Member receiver);
}
