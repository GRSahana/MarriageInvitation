package com.ubikasoftwares.marriageinvitation.Model;

import java.util.HashMap;
import java.util.Map;

public class AddInvitees {


        private String name;
        private Param param;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public AddInvitees(String name, Param param){
            this.name = name;
            this.param = param;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Param getParam() {
            return param;
        }

        public void setParam(Param param) {
            this.param = param;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

