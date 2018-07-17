package com.amir.controller;

import com.amir.dao.UserRepository;
import com.amir.dto.GameDTO;
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
    public GameDTO sendMessage(@Payload GameObject gamingObject) {
        System.out.println("object of game is ::" + gamingObject);
        UserDomain user = new UserDomain();
        try {
            user = userRepository.getOne(gamingObject.getGameUserId());
        } catch (Exception e) {
            log.error("exception::" + e.getMessage());
        }
        GameDTO assignPoint = assignPoint(user);
        return assignPoint;
    }

    GameDTO assignPoint(UserDomain userDomain) {
        Long lastPoint = userDomain.getPoint();
        long pointAssign = 100L;
        Long newPoint = lastPoint + pointAssign;
        userDomain.setPoint(newPoint);
        userRepository.saveAndFlush(userDomain);
        GameDTO dto = new GameDTO();
        dto.setGameUserId(userDomain.getId());
        dto.setPointAssigned(pointAssign);
        dto.setUserPoint(newPoint);

        return dto;
    }


}
