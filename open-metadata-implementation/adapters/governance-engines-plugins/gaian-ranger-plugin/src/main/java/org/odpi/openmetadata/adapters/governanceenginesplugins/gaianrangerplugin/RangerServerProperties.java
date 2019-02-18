/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

public class RangerServerProperties {

    private String serverURL;
    private String serverAuthorization;

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getServerAuthorization() {
        return serverAuthorization;
    }

    public void setServerAuthorization(String serverAuthorization) {
        this.serverAuthorization = serverAuthorization;
    }
}
