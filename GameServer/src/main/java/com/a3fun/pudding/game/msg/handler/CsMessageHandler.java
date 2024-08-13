package com.a3fun.pudding.game.msg.handler;

import com.a3fun.pudding.model.Player;

public interface CsMessageHandler<T> extends MessageHandler<T, Player> {
    void handle(T message, Player player);
}
