package com.a3fun.pudding.game.msg.handler.activity;

import com.a3fun.pudding.game.msg.handler.CsAbstractMessageHandler;
import com.a3fun.pudding.game.msg.handler.model.CsActivityList;
import com.a3fun.pudding.model.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsActivityListHandler extends CsAbstractMessageHandler<CsActivityList> {

    @Override
    public void handle(CsActivityList message, Role role) {
        log.info("handle CsActivityList message: {}", message);
    }
}
