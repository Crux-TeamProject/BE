package com.project.crux.chat.controller;

import com.project.crux.chat.model.ChatRoom;
import com.project.crux.chat.repo.ChatRoomRepository;
import com.project.crux.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    @ResponseBody
    public ResponseDto<List<ChatRoom>> room() {
        return ResponseDto.success(chatRoomRepository.findAllRoom());
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }

    //view
    //채팅방 목록 조회
    @GetMapping(value = "/rooms/view")
    public ModelAndView rooms(){
        ModelAndView mv = new ModelAndView("chat/rooms");
        mv.addObject("list", chatRoomRepository.findAllRoom());
        return mv;
    }

    //채팅방 조회
    @GetMapping("/room2")
    public void getRoom(String roomId, Model model){
        model.addAttribute("room", chatRoomRepository.findRoomById(roomId));
    }
}
