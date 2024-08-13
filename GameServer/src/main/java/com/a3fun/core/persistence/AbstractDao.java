package com.a3fun.core.persistence;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDao<Entity extends AbstractEntity> implements IDao<Entity, Long> {

}
