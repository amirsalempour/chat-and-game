package com.amir.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameDTO implements Serializable {
    private Long gameUserId;
    private Long userPoint;
    private Long pointAssigned;
}
