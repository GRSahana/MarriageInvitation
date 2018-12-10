package com.ubikasoftwares.marriageinvitation.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class InviteDetailsParam {

    @SerializedName("invitation_id")
    private String invitationId;
    private String userid;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    public InviteDetailsParam(String invitationId, String userid) {
        super();
        this.invitationId = invitationId;
        this.userid = userid;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
