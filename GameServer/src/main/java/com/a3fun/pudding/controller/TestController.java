package com.a3fun.pudding.controller;

import com.a3fun.pudding.config.ConfigHelper;
import com.a3fun.pudding.config.parser.FishConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {
        FishConfig fishConfig = ConfigHelper.getServiceInstance().getConfig(FishConfig.class);
        fishConfig.getIdMap().forEach((k, v) -> {
            System.out.println(v);
        });
        return "test";
    }
}
