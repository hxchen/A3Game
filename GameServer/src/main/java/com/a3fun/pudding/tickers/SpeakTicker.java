package com.a3fun.pudding.tickers;

import com.a3fun.core.tick.AbstractTicker;
import com.a3fun.pudding.model.Speak;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class SpeakTicker extends AbstractTicker<Speak> {

    @Override
    protected void tick(Speak speak, long now) {
        log.info("Speak ticker: " + speak);
    }

    @Override
    protected Collection<Speak> findAll() {
        List<Speak> list = new ArrayList<>();
        list.add(new Speak(1L, "Hello"));
        list.add(new Speak(2L, "World"));
        return list;
    }
}
