package com.a3fun.core.world;


import com.a3fun.core.tick.AbstractTicker;
import com.a3fun.pudding.Application;
import com.a3fun.pudding.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class World {
    private Logger LOGGER = LoggerFactory.getLogger(World.class);

    List<AbstractTicker<?>> tickers;

    ServiceContainer serviceContainer;

    public void init(ServiceContainer serviceContainer){
        this.serviceContainer = serviceContainer;
        tickers = new ArrayList<>();
        Map<String, AbstractTicker> beansOfType = Application.getBeanOfType(AbstractTicker.class);
        LOGGER.info(Thread.currentThread().getName() + "Ticker 初始化大小：" + beansOfType.size());
        for (AbstractTicker ticker : beansOfType.values()) {
            tickers.add(ticker);
        }
    }



    public void tick(long now){

        for (AbstractTicker<?> ticker : tickers) {
            LOGGER.info(Thread.currentThread().getName() + ticker + "执行tick");
            ticker.tick(now);
        }

        serviceContainer.getVipService().tick(System.currentTimeMillis());
    }

}
