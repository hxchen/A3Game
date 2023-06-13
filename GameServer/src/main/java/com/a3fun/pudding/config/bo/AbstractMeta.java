package com.a3fun.pudding.config.bo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

public class AbstractMeta {
    public static final char META_SEPARATOR_1 = ',';

    public static final char META_SEPARATOR_2 = '|';

    public static final char META_SEPARATOR_3 = ';';

    public static final char META_SEPARATOR_4 = ':';

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Getter
    @Setter
    protected String id;

    public void init(JsonNode json) {

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractMeta other = (AbstractMeta) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
