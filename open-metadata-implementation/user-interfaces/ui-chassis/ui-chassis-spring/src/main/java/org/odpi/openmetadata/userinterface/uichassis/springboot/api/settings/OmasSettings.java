/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import java.io.Serializable;

/**
 * Encapsulates server base url and the serverName form the configuration.
 * The purpose is to be consumed in order to compute the url for registry repository view
 */
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