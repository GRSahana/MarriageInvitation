package com.ubikasoftwares.marriageinvitation.Model;

import java.util.HashMap;
import java.util.Map;

public class Param {

    private String userid;
    private String name;
    private String address;
    private String phone;
    private String cat;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Param(String userid, String name, String address, String phone, String cat){
        this.userid = userid;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.cat = cat;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}