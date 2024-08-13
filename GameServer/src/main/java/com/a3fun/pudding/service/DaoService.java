package com.a3fun.pudding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DaoService {
    private long lastSaveTime;

    @Autowired
    ApplicationContext applicationContext;



}
