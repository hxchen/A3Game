package com.a3fun.pudding.service;

import com.a3fun.core.world.World;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class WorldService {
    private World world;

    @Autowired
    ServiceContainer serviceContainer;

    public World getWorld() {
        return world;
    }
    public void init(){
        log.info("WorldService 初始化");
        this.world = new World();
        this.world.init(serviceContainer);
    }
}
