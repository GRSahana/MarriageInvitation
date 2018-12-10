package com.ubikasoftwares.marriageinvitation.Model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Params {

    private String name;
    private Login param;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Params(String name, Login param){
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Login getParam() {
        return param;
    }

    public void setParam(Login param) {
        this.param = param;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
