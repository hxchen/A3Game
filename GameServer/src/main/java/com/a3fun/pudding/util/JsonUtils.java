package com.a3fun.pudding.util;

import com.a3fun.core.common.IntEnum;
import com.a3fun.core.common.IntEnumDeserializer;
import com.a3fun.core.common.IntEnumKeySerializer;
import com.a3fun.core.common.IntEnumSerializer;
import com.a3fun.core.util.JavaUtils;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class JsonUtils {
    public static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
    }

    public static void intEnumForJson(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends IntEnum>> intEnumClasses = reflections.getSubTypesOf(IntEnum.class);
        if (intEnumClasses.isEmpty()) {
            return;
        }
        intEnumForJackson(intEnumClasses);
    }


    private static void intEnumForJackson(Set<Class<? extends IntEnum>> intEnumClasses) {
        ObjectMapper om = JsonUtils.objectMapper;
        SimpleModule mod = new SimpleModule();
        for (Class<? extends IntEnum> c : intEnumClasses) {
            intEnumForJackson(c, mod);
        }
        om.registerModule(mod);
    }


    public static void intEnumForJackson(Class<? extends IntEnum> c, SimpleModule mod) {
        mod.addSerializer(c, new IntEnumSerializer(c));
        mod.addKeySerializer(c, new IntEnumKeySerializer(c));
        mod.addDeserializer(c, new IntEnumDeserializer(c));
    }

    public static JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception e) {
            throw JavaUtils.sneakyThrow(e);
        }
    }

    public static <T> T readValue(String content, Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (Exception e) {
            throw JavaUtils.sneakyThrow(e);
        }
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw JavaUtils.sneakyThrow(e);
        }
    }

    public static <T> JSONObject parseMap(Map<String, T> map) {
        JSONObject object = new JSONObject();
        map.forEach((k, v) -> object.put(k,v));
        return object;
    }
}
