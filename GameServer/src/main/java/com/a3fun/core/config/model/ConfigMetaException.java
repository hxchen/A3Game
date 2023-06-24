package com.a3fun.core.config.model;

public class ConfigMetaException extends RuntimeException{
    public ConfigMetaException() {
        super();
    }

    public ConfigMetaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigMetaException(String message) {
        super(message);
    }

    public ConfigMetaException(Throwable cause) {
        super(cause);
    }
}
