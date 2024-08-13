package com.a3fun.pudding.biz.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
@Slf4j
public class WebJedisClient extends AbstractRedisClient {
    private JedisPool jedisPool = null;

    @Override
    public void init() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(maxTotal);
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMaxWaitMillis(mxWaitMillis);
            poolConfig.setTestOnBorrow(true);
            if (StringUtils.isEmpty(password)) {
                jedisPool = new JedisPool(poolConfig, host, port, timeout, null, dbIndex);
            } else {
                jedisPool = new JedisPool(poolConfig, host, port, timeout, password, dbIndex);
            }
            log.info("jedis pool init success. info: [host: {}, port: {}, timeout:{}]", host, port, timeout);
            jedisPool.getResource().close();
        } catch (Exception e) {
            log.error("jedis init failed.", e);
        }
    }

    @Override
    protected Jedis getResource() {
        return jedisPool.getResource();
    }

    @Override
    public void close(Jedis resource) {
        resource.close();
    }

}
