package com.a3fun.core.persistence;

public interface IDao <Entity extends IPersistable<PID>, PID>{

    Entity create();

    void save(Entity entity);

    Entity findById(PID id);

    void delete(Entity entity);

}
