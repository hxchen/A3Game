package com.a3fun.core.persistence;

public interface IPersistable<PID> {
    void setPersistentKey(PID id);
    PID getPersistentKey();
    @Override
    int hashCode();
    @Override
    boolean equals(Object obj);
    @Override
    String toString();
}
