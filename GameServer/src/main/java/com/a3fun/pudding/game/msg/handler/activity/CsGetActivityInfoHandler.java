package com.a3fun.pudding.game.msg.handler.activity;

import com.a3fun.pudding.game.msg.handler.CsAbstractMessageHandler;
import com.a3fun.pudding.game.msg.handler.model.CsGetActivityInfo;
import com.a3fun.pudding.model.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsGetActivityInfoHandler extends CsAbstractMessageHandler<CsGetActivityInfo> {
    @Override
    public void handle(CsGetActivityInfo message, Role role) {
        log.info("handle CsGetActivityInfo message: {}", message);
    }
}
