package com.a3fun.pudding.biz.redis;

import com.a3fun.core.common.IntEnum;
import com.a3fun.pudding.util.EnumUtils;
import lombok.Data;
import lombok.Getter;

public enum RedisDataType implements IntEnum {

    USER_CENTER(0, "user_center", WhichDB.GAME_SERVER, 0),
    SINGLE_BASE(2, "single_base", WhichDB.GAME_SERVER, 1),

    ;
    @Getter
    private final int id;
    @Getter
    private final String name;
    @Getter
    private final int whichDb;
    @Getter
    private final int dbIndex;

    RedisDataType(int id, String name, int whichDb, int dbIndex) {
        this.id = id;
        this.name = name;
        this.whichDb = whichDb;
        this.dbIndex = dbIndex;
    }

    private static final RedisDataType[] INDEXES = EnumUtils.toArray(values());

    @Override
    public int getId() {
        return id;
    }

    public static RedisDataType valueOf(int id) {
        if (id < 0 || id >= INDEXES.length) {
            return null;
        }
        return INDEXES[id];
    }
}
