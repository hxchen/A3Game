package com.a3fun.core.common;

import java.util.List;

public interface IConfigChangeListener {
    void onchange(List<String> configNames);
}
