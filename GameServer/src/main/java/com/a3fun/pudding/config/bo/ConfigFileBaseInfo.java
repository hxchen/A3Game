package com.a3fun.pudding.config.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigFileBaseInfo {
    private String name;

    private long lastModified;

}
