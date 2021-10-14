/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@ConfigurationProperties(prefix = "egeria", ignoreUnknownFields = false, ignoreInvalidFields = false)
public class DemoUsers {

    private Map<String, User> demoUsers  = new HashMap<>();;

    public Map<String, User> getDemoUsers() {
        return demoUsers;
    }

//    public void setDemoUsers(Map<String, User> demoUsers) {
//        this.demoUsers = demoUsers;
//    }
}
