package com.a3fun.core.tick;


import java.util.Collection;

public abstract class AbstractTicker<T> implements ITicker{
    private long lastTime;
    private long interval;
    @Override
    public void tick(long now) {
        if (!check(now)){
            return;
        }
        Collection<T> all = findAll();
        if (all.isEmpty()){
            return;
        }
        for (T t : all){
            tick(t, now);
        }
    }
    private boolean check(long now){
        if (lastTime == 0){
            lastTime = System.currentTimeMillis();
            return true;
        }
        if (lastTime + interval < now){
            lastTime = now;
            return true;
        }
        return false;
    }
    protected abstract void tick(T t, long now);
    protected abstract Collection<T> findAll();
}
