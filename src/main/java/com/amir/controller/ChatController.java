package com.amir.controller;

import com.amir.dao.UserRepository;
import com.amir.model.ChatMessage;
import com.amir.model.UserDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * Created by AmirSP on 7/12/2018.
 */
@Controller
public class ChatController {
    private final UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(ChatController.class);

    public ChatController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        UserDomain user = new UserDomain();
        user.setDate(new Date());
        user.setUserName(chatMessage.getSender());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("exception is::" + e.getMessage());
            System.out.println("exception occoured" + e.getMessage());
        }
        Long identifier = user.getId();
        System.out.println("your id is:" + identifier);

        headerAccessor.getSessionAttributes().put("identificationNumber", identifier);
        chatMessage.setIdentificationNumber(identifier);
        return chatMessage;
    }


}
