package com.project.crux.chat.service;

import com.project.crux.chat.model.response.ChatRoomResponseDto;
import com.project.crux.chat.repo.ChatRoomRepository;
import com.project.crux.crew.repository.CrewMemberRepository;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final CrewMemberRepository crewMemberRepository;

    public List<ChatRoomResponseDto> findAllRoom(UserDetailsImpl userDetails) {
        return crewMemberRepository.findAllByMember(userDetails.getMember()).stream()
                .map(cm -> new ChatRoomResponseDto(chatRoomRepository.findByCrew(cm.getCrew())))
                .collect(Collectors.toList());
    }

    public ChatRoomResponseDto findRoom(Long roomId) {
        return new ChatRoomResponseDto(chatRoomRepository.findById(roomId).get());
    }
}
