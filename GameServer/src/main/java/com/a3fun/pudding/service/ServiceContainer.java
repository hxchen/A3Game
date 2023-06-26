package com.a3fun.pudding.service;

import com.a3fun.pudding.service.impl.VIPService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class ServiceContainer {

    @Autowired
    VIPService vipService;

}
