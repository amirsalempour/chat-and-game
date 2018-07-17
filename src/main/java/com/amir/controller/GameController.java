package com.amir.controller;

import com.amir.dao.UserRepository;
import com.amir.dto.GameDTO;
import com.amir.model.GameObject;
import com.amir.model.UserDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * Created by AmirSP on 7/16/2018.
 */
@Controller
@PropertySource("classpath:utility.properties")
public class GameController {
    private final UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(ChatController.class);

    private final Environment env;

    public GameController(UserRepository userRepository, Environment env) {
        this.userRepository = userRepository;
        this.env = env;
    }

    @MessageMapping("/chat.game")
    @SendTo("/topic/public")
    public GameDTO sendMessage(@Payload GameObject gamingObject) {
        System.out.println("object of game is ::" + gamingObject);
        GameDTO assignPoint = assignPoint(gamingObject.getGameUserId());
        return assignPoint;
    }

    GameDTO assignPoint(Long userId) {
        UserDomain user = new UserDomain();
        GameDTO dto = new GameDTO();
        try {
            Optional<UserDomain> userDomain = userRepository.findById(userId);
             user = userDomain.get();
//            user = userDomain;

            Long lastPoint = user.getPoint();
            Long pointAssign = Long.valueOf(env.getProperty("GAME_POINT"));
            Long newPoint = lastPoint + pointAssign;
            user.setPoint(newPoint);
            userRepository.saveAndFlush(user);
            dto.setGameUserId(user.getId());
            dto.setPointAssigned(pointAssign);
            dto.setUserPoint(newPoint);
        } catch (Exception e) {
            log.error("exception::" + e.getMessage());
        }
        return dto;
    }


}
