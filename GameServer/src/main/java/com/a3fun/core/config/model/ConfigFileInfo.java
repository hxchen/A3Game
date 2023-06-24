package com.a3fun.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigFileInfo {
    private ConfigFileBaseInfo baseInfo;
    private byte[] content;

}
