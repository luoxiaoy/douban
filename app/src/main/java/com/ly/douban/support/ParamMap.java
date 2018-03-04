package com.ly.douban.support;

import java.io.Serializable;
import java.util.HashMap;

public class ParamMap extends HashMap<String, Object> implements Serializable {

    public ParamMap() {
        super();
    }

    public ParamMap addParam(String key, Object value) {
        put(key, value);
        return this;
    }

}
