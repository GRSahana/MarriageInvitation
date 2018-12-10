package com.ubikasoftwares.marriageinvitation.Model;

import java.util.HashMap;
import java.util.Map;

public class ViewInvitees {

    private String name;
    private Parameters param;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public ViewInvitees(String name, Parameters param) {
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

    public Parameters getParam() {
        return param;
    }

    public void setParam(Parameters param) {
        this.param = param;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}