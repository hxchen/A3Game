package com.a3fun.core.persistence;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity implements IPersistable<Long>, Serializable {
    @Data
    protected int db = -1;
    @Data
    protected int  currentServerId = -1;

    protected int hashCode(Object... objects) {
        int result = 1;
        for (Object object : objects) {
            result = 31 * result + (object == null ? 0 : object.hashCode());
        }
        return result;
    }

    protected boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }



}
