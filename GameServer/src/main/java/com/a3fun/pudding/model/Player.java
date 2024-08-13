package com.a3fun.pudding.model;


import lombok.Getter;
import lombok.Setter;

public class Player {
    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private Role role;
}
