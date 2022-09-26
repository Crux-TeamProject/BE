package com.project.crux.chat.controller;

import com.project.crux.chat.model.response.ChatRoomResponseDto;
import com.project.crux.chat.service.ChatRoomService;
import com.project.crux.common.ResponseDto;
import com.project.crux.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    @ResponseBody
    public ResponseDto<List<ChatRoomResponseDto>> rooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(chatRoomService.findAllRoom(userDetails));
    }

    //view
    //채팅방 목록 조회
    @GetMapping(value = "/rooms/view")
    public ModelAndView getRooms(@AuthenticationPrincipal UserDetailsImpl userDetails){
        ModelAndView mv = new ModelAndView("chat/rooms");
        mv.addObject("list", chatRoomService.findAllRoom(userDetails));
        return mv;
    }

    //채팅방 조회
    @GetMapping("/room2")
    public void getRoom(Long roomId, Model model){
        model.addAttribute("room", chatRoomService.findRoom(roomId));
    }
}
