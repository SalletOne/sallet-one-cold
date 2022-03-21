package com.sallet.cold.utils;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    public String jsonString() {
        return JsonUtils.serialize(this);
    }
}
