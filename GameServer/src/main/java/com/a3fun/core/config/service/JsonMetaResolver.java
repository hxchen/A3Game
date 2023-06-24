package com.a3fun.core.config.service;

import com.a3fun.core.common.MetaCheckResolver;
import com.a3fun.core.config.annotation.*;
import com.a3fun.core.config.model.AbstractMeta;
import com.a3fun.core.config.model.ConfigFormat;
import com.a3fun.core.config.model.MetaServerType;
import com.a3fun.core.config.model.VoidMeta;
import com.a3fun.pudding.util.JavaUtils;
import com.a3fun.pudding.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置数据解析器
 */
@Slf4j
@Service
public class JsonMetaResolver implements ConfigResolver {

    @Override
    public ConfigFormat type() {
        return ConfigFormat.JSON;
    }

    @Override
    public Object resolve(Class<?> configClass, Config configAnno, byte[] content) {
        String configName = configAnno.name();
        Class<? extends AbstractMeta> metaClass = configAnno.metaClass();
        boolean isAutoGen = configAnno.autoGen();

        ObjectMapper om = JsonUtils.objectMapper;

        String data = new String(content, StandardCharsets.UTF_8);

        Object config = null;
        try {
            int metaCount = 0;
            JsonNode metaJsonList = om.readTree(data);
            if (isAutoGen) {
                config = configClass.newInstance();
                Class<?> baseClazz = config.getClass().getSuperclass();
                Method loadMethod = findMethod(baseClazz, "load");
                assert loadMethod != null;
                loadMethod.invoke(config, metaJsonList);
            }  else if (metaClass == VoidMeta.class) {
                metaJsonList = readKvJson(data, om);
                config = om.treeToValue(metaJsonList, configClass);
                tryInvokeConfigInit(config, metaJsonList);
                metaCount = metaJsonList.size();
            } else {
                List<AbstractMeta> metaList = new ArrayList<>();
                for (JsonNode jsonNode : metaJsonList) {
                    AbstractMeta entity = om.readValue(jsonNode.toString(), metaClass);
                    entity.init(jsonNode);
                    checkField(entity);
                    metaList.add(entity);
                }
                config = configClass.newInstance();
                tryInjectMetas(config, metaList);
                tryInjectMetaMapByField(config, metaList);
                tryInvokeConfigInit(config, metaList);
                metaCount = metaList.size();
            }

            log.info("Load config: {}, {} metas", configName, metaCount);
        } catch (Exception e) {
            throw JavaUtils.sneakyThrow(e);
        }

        return config;
    }
    private void checkField(AbstractMeta entity) throws Exception {
        MetaCheckResolver.check(entity);
    }
    private Method findMethod(Class<?> clazz, String name) {
        Class<?> tempClass = clazz;
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            for (Method m : tempClass.getDeclaredMethods()) {
                if (m.getName().equals(name)) {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    return m;
                }
            }
            tempClass = tempClass.getSuperclass();
        }
        return null;
    }
    private Method findMethod(Class<?> clazz, Class<? extends Annotation> annoType) {
        Class<?> tempClass = clazz;
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            for (Method m : tempClass.getDeclaredMethods()) {
                if (m.getAnnotation(annoType) != null) {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    return m;
                }
            }
            tempClass = tempClass.getSuperclass();
        }
        return null;
    }
    private Field findField(Class<?> clazz, Class<? extends Annotation> annoType) {
        Class<?> tempClass = clazz;
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            for (Field f : tempClass.getDeclaredFields()) {
                if (f.getAnnotation(annoType) != null) {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    return f;
                }
            }
            tempClass = tempClass.getSuperclass();
        }
        return null;
    }
    private JsonNode readKvJson(String data, ObjectMapper om) throws Exception {
        JsonNode metasJson = om.readTree(data);
        ObjectNode rstJson = om.createObjectNode();
        for (JsonNode jsonNode : metasJson) {
            JsonNode nameNode = jsonNode.get("name");
            if (nameNode != null) {
                rstJson.put(nameNode.asText(), jsonNode.path("value").asText());
            }
        }
        return rstJson;
    }
    private void tryInjectMetas(Object config, List<? extends AbstractMeta> metaList) throws Exception {
        Field metaListField = findField(config.getClass(), MetaList.class);
        if (metaListField != null) {
            metaListField.set(config, metaList);
        }

        // MetaMap
        Field metaMapField = findField(config.getClass(), MetaMap.class);
        if (metaMapField != null) {
            Map<String, AbstractMeta> metaMap = new HashMap<>();
            for (AbstractMeta m : metaList) {
                metaMap.put(m.getId(), m);
            }
            metaMapField.set(config, metaMap);
        }

    }

    private void tryInvokeConfigInit(Object config, List<? extends AbstractMeta> metaList) throws Exception {
        Method method = findMethod(config.getClass(), Init.class);
        if (method == null) {
            method = findMethod(config.getClass(), "init");
        }

        if (method == null) {
            return;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        if (params.length > 0) {
            params[0] = metaList;
        }
        method.invoke(config, params);
    }

    private void tryInvokeConfigInit(Object config, JsonNode kvJson) throws Exception {
        Method method = findMethod(config.getClass(), Init.class);
        if (method == null) {
            method = findMethod(config.getClass(), "init");
        }

        if (method == null) {
            return;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        if (params.length > 0) {
            params[0] = kvJson;
        }
        method.invoke(config, params);
    }

    /**
     *
     * @param config
     * @param metaList
     * @throws Exception
     */
    private void tryInjectMetaMapByField(Object config, List<? extends AbstractMeta> metaList) throws Exception {
        if (metaList == null) {
            return;
        }

        Field metaMapField = findField(config.getClass(), MetaMapByField.class);
        if (metaMapField != null) {
            Map<MetaServerType, List<AbstractMeta>> metaMap = new HashMap<>();
            for (AbstractMeta m : metaList) {
                MetaServerType metaServerType = m.getMetaServerType();
                if (metaServerType == null) {
                    metaMap.compute(MetaServerType.BASE, (k, v) -> v == null ? new ArrayList<>() : v).add(m);
                    metaMap.compute(MetaServerType.KVK, (k, v) -> v == null ? new ArrayList<>() : v).add(m);
                } else {
                    metaMap.compute(metaServerType, (k, v) -> v == null ? new ArrayList<>() : v).add(m);
                }
            }
            metaMapField.set(config, metaMap);
        }
    }
}
