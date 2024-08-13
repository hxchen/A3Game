package com.a3fun.pudding.game.msg.handler;

import com.a3fun.pudding.model.Player;
import com.a3fun.pudding.model.Role;

public abstract class CsAbstractMessageHandler<T> implements CsMessageHandler<T> {

    @Override
    public void handle(T message, Player player) {
        // do something
        handle(message, player.getRole());
    }

    public abstract void handle(T message, Role role);
}
