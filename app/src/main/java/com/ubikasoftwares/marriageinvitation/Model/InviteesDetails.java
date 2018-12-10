package com.ubikasoftwares.marriageinvitation.Model;

public class InviteesDetails {

    private String name, phone, status,id;

    public InviteesDetails(String name, String phone, String status, String id){
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
