package com.a3fun.pudding.config.parser;

import com.a3fun.core.config.annotation.Config;
import com.a3fun.core.config.annotation.MetaMap;
import com.a3fun.core.config.model.AbstractMeta;
import com.a3fun.pudding.util.MetaUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
@Slf4j
@Config(name = "fish", metaClass = FishConfig.FishMeta.class)
public class FishConfig {
    @MetaMap
    private Map<String , FishMeta> metaMap;
    public FishMeta getById(String id) {
        return metaMap.get(id);
    }

    public Map<String, FishMeta> getIdMap() {
        return metaMap;
    }

    @Data
    public static class FishMeta extends AbstractMeta {
        private int rewardExp;
        private int rewardToken;
        private int rewardScore;
        private int[] fishLongRange;
        private double[] fishWeightRange;
        @Override
        public void init(JsonNode json) {
            super.init(json);
            if (StringUtils.isNotEmpty(json.path("fishLong").asText())){
                fishLongRange = MetaUtils.parseInts(json.path("fishLong").asText(), AbstractMeta.META_SEPARATOR_2);
            }
            if (StringUtils.isNotEmpty(json.path("fishWeight").asText())) {
                fishWeightRange = MetaUtils.parseDoubles(json.path("fishWeight").asText(), AbstractMeta.META_SEPARATOR_2);
            }

        }
        public String toString() {
            return "FishMeta{" +
                    "id='" + id + '\'' +
                    ", server='" + server + '\'' +
                    ", rewardExp=" + rewardExp +
                    ", rewardToken=" + rewardToken +
                    ", rewardScore=" + rewardScore +
                    ", fishLong=" + Arrays.toString(fishWeightRange) +
                    ", fishWeight=" + Arrays.toString(fishWeightRange) +
                    '}';
        }

    }



}
