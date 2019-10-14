/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import java.util.Objects;

/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoint an endpoint for each governance server.
 * If specified then the endpoint overrides the metadata server endpoint
 */
public class GovernanceServerEndpoint {
    private String serverName;
    private String serverRootURL;
    private String governanceServiceName;

    /**
     * the serverName for the governance server.
     * @return server name for the governance server
     */
    public String getServerName() {
        return serverName;
    }
    /**
     * Set up the serverName for the governance server.
     *
     * @param serverName governance server name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * The serverRootURL for the governance server. This is the root (the first part) of the URL for
     * the governance server
     * @return root server URL
     */
    public String getServerRootURL() {
        return serverRootURL;
    }  
    /**
     * Set up the serverRootURL for the governance server.
     *
     * @param serverRootURL governance server name
     */

    public void setServerRootURL(String serverRootURL) {
        this.serverRootURL = serverRootURL;
    }

    /**
     * Governance service name.
     * @return governance service name
     */
    public String getGovernanceServiceName() {
        return governanceServiceName;
    }
    /**
     * Set up the governance service Name for the governance server.
     *
     * @param governanceServiceName governance server name
     */
    public void setGovernanceServiceName(String governanceServiceName) {
        this.governanceServiceName = governanceServiceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GovernanceServerEndpoint)) return false;
        GovernanceServerEndpoint that = (GovernanceServerEndpoint) o;
        return Objects.equals(getServerName(), that.getServerName()) &&
                Objects.equals(getServerRootURL(), that.getServerRootURL()) &&
                Objects.equals(getGovernanceServiceName(), that.getGovernanceServiceName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerName(), getServerRootURL(), getGovernanceServiceName());
    }

    @Override
    public String toString() {
        return "GovernanceServerEndpoint{" +
                "serverName='" + serverName + '\'' +
                ", serverRootURL='" + serverRootURL + '\'' +
                ", governanceServiceName='" + governanceServiceName + '\'' +
                '}';
    }


}
