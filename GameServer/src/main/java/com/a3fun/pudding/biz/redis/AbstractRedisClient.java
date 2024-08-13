package com.a3fun.pudding.biz.redis;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ZParams;
import redis.clients.jedis.resps.Tuple;

@Slf4j
@Data
public abstract class AbstractRedisClient {

    protected String host;
    protected int port;
    protected int timeout = 30000;
    protected String password;
    protected int dbIndex;
    protected int maxTotal;
    protected int maxIdle;
    protected int mxWaitMillis;

    @PostConstruct
    public void init() {

    }

    protected abstract Jedis getResource();

    public abstract void close(Jedis resource);

    public <T> T doOperation(IRedisOperation<T> op, RedisDataType redisDataType) {
        Jedis jedis = getResource();
        try {
            jedis.select(redisDataType.getDbIndex());
            return op.execute(jedis);
        } catch (Exception e) {
            log.error("redis operator failed.", e);
        } finally {
            jedis.close();

        }
        return null;
    }

    public long zcard(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zcard(key), redisDataType);
    }

    public long incr(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.incr(key), redisDataType);
    }

    public String get(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.get(key), redisDataType);
    }

    public byte[] getBytes(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.get(key.getBytes()), redisDataType);
    }

    public List<String> mget(final List<String> keys, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.mget(keys.toArray(new String[0])), redisDataType);
    }

    public String set(final String key, final String value, RedisDataType redisDataType) {
        return doOperation(new IRedisOperation<String>() {
            @Override
            public String execute(Jedis jedis) {
                return jedis.set(key, value);
            }
        }, redisDataType);
    }

    public String set(final String key, final byte[] value, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.set(key.getBytes(), value), redisDataType);
    }

    public String set(final String key, final byte[] value, final long expireTime, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.psetex(key.getBytes(), expireTime, value), redisDataType);
    }

    public String set(final String key, final String value, final long expireTime, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.psetex(key, expireTime, value), redisDataType);
    }

    /**
     * set value + expire seconds
     * @param key
     * @param seconds
     * @param value
     * @param redisDataType
     * @return
     */
    public String setex(final String key, final int seconds, final String value, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.setex(key, seconds, value), redisDataType);
    }

    public long del(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.del(key), redisDataType);
    }

    public long hdel(final String key, final String mapKey, final RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.hdel(key, mapKey), redisDataType);
    }

    public long sadd(final String key, RedisDataType redisDataType, final String... members) {
        return doOperation(jedis -> jedis.sadd(key, members), redisDataType);
    }

    public Set<String> smembers(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.smembers(key), redisDataType);
    }

    public long srem(final String key, RedisDataType redisDataType, final String... members) {
        return doOperation(jedis -> jedis.srem(key, members), redisDataType);
    }

    public List<String> hmget(final String key, final List<String> fields, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.hmget(key, fields.toArray(new String[0])), redisDataType);
    }

    public String hget(final String key, String mapKey, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.hget(key, mapKey), redisDataType);
    }

    public Map<String, String> hgetAll(final String key, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.hgetAll(key), redisDataType);
    }

    public String hmset(final String key, final Map<String, String> map, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.hmset(key, map), redisDataType);
    }

    public Long hset(final String key, final String mapKey, final String value, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.hset(key, mapKey, value), redisDataType);
    }

    public long zadd(final String key, final double score, final String member, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zadd(key, score, member), redisDataType);
    }

    public long zadd(final String key, Map<String, Double> scoreMembers, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zadd(key, scoreMembers), redisDataType);
    }

    public long zdel(final String key, final String member, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zrem(key, member), redisDataType);
    }

    public double zincrby(final String key, final long score, final String member, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zincrby(key, score, member), redisDataType);
    }

    public List<String> zrange(final String key, final long start, final long end, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zrange(key, start, end), redisDataType);
    }

    public List<String> zrevrange(final String key, final long start, final long end, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zrevrange(key, start, end), redisDataType);
    }

    public Long zrevrank(final String key, final String member, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zrevrank(key, member), redisDataType);
    }

    public Double zrevvalue(final String key, final String member, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zscore(key, member), redisDataType);
    }

    public List<Tuple> zrevrangeWithScores(final String key, final long start, final long end, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zrevrangeWithScores(key, start, end), redisDataType);
    }

    public List<Tuple> zrangeByScore(final String key, final long start, final long end, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zrangeByScoreWithScores(key, start, end), redisDataType);
    }

    public Long zunionstore(final String dstkey, RedisDataType redisDataType, final String... sets) {
        return doOperation(jedis -> jedis.zunionstore(dstkey, sets), redisDataType);
    }

    public Long zunionstore(final String dstkey, RedisDataType redisDataType, final ZParams params, final String... sets) {
        return doOperation((IRedisOperation<Long>) jedis -> jedis.zunionstore(dstkey, params, sets), redisDataType);
    }

    public Long zcount(String dstkey, RedisDataType redisDataType, double min, double max) {
        return doOperation(jedis -> jedis.zcount(dstkey, min, max), redisDataType);
    }

    public long zRemRangeByRank(final String key, final long start, final long end, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.zremrangeByRank(key, start, end), redisDataType);
    }

    public Boolean exists(final String key, RedisDataType redisDataType){
        return doOperation(jedis -> jedis.exists(key), redisDataType);
    }

    public Object eval(final String script, final List<String> keys, final List<String> values, RedisDataType redisDataType) {
        return doOperation(jedis -> jedis.eval(script, keys, values), redisDataType);
    }

    public Long expire(final String key, RedisDataType redisDataType, final int seconds) {
        return doOperation(jedis -> jedis.expire(key, seconds), redisDataType);
    }

    public String ltrim(final String key, RedisDataType redisDataType, final long start , final long stop) {
        return doOperation(jedis -> jedis.ltrim(key, start , stop), redisDataType);
    }

    public Long rpush(final String key, RedisDataType redisDataType, String... params) {
        return doOperation(jedis -> jedis.rpush(key, params), redisDataType);
    }

    public List<String> lrange(final String key, RedisDataType redisDataType, final long start , final long stop) {
        return doOperation(jedis -> jedis.lrange(key, start , stop), redisDataType);
    }

    public List<String> mget(RedisDataType redisDataType, final String... keys) {
        return doOperation(jedis -> jedis.mget(keys), redisDataType);
    }

    public interface IRedisOperation<T> {

        T execute(Jedis jedis);

    }

}
