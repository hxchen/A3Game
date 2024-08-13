package com.a3fun.pudding.game.msg.handler;

public interface MessageHandler <T, S>{
    void handle(T message, S session);
}
