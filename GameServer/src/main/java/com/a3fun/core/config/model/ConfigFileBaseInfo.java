package com.a3fun.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigFileBaseInfo {
    private String name;

    private long lastModified;

}
