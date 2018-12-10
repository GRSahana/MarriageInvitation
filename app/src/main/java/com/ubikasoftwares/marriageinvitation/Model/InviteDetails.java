package com.ubikasoftwares.marriageinvitation.Model;

import java.util.HashMap;
import java.util.Map;

public class InviteDetails {

    private String name;
    private InviteDetailsParam param;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public InviteDetails(String name, InviteDetailsParam param) {
        super();
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InviteDetailsParam getParam() {
        return param;
    }

    public void setParam(InviteDetailsParam param) {
        this.param = param;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
