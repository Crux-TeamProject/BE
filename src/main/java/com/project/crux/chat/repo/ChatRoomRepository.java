package com.project.crux.chat.repo;

import com.project.crux.chat.model.ChatRoom;
import com.project.crux.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByCrew(Crew crew);
}
