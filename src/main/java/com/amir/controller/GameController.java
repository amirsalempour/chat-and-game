package com.amir.controller;

import com.amir.dao.UserRepository;
import com.amir.model.GameObject;
import com.amir.model.UserDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by AmirSP on 7/16/2018.
 */
@Controller
public class GameController {
    private final UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(ChatController.class);

    public GameController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat.game")
    @SendTo("/topic/public")
    public GameObject sendMessage(@Payload GameObject gamingObject) {
        System.out.println("object of game is ::" + gamingObject);
        UserDomain user = new UserDomain();
        user = userRepository.getOne(gamingObject.getGameUserId());
        return gamingObject;
    }

//    static void assignPoint(UserDomain userDomain , GameObject gameObject){
//        if (gameObject.getColor().trim().startsWith())
//
//    }


}
