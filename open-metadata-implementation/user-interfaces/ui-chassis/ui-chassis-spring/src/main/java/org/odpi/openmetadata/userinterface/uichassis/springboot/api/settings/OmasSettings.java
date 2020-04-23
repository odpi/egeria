/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import java.io.Serializable;

public class OmasSettings implements Serializable {
    String serverName;
    String baseUrl;

    public OmasSettings(String serverName, String baseUrl) {
        this.serverName = serverName;
        this.baseUrl = baseUrl;
    }

    public String getServerName() {
        return serverName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}