/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import java.io.Serializable;

/**
 * Encapsulates server base url and the serverName form the configuration.
 * The purpose is to be consumed in order to compute the url for registry repository view
 */
public class UISettings implements Serializable {
    String serverName;
    String baseUrl;
    String pageSize;

    public UISettings(String serverName, String baseUrl, String pageSize) {
        this.serverName = serverName;
        this.baseUrl = baseUrl;
        this.pageSize = pageSize;
    }

    public String getServerName() {
        return serverName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPageSize() {
        return pageSize;
    }
}