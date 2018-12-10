package com.ubikasoftwares.marriageinvitation.Model;

import java.util.HashMap;
import java.util.Map;

public class loginParam {

        private String username;
        private String pass;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }


}
