package com.ubikasoftwares.marriageinvitation.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Parameters {
    @SerializedName("invitation_id")
    private String invitationId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Parameters(String invitationId) {
        super();
        this.invitationId = invitationId;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
