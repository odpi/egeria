/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import java.util.Objects;

/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoint an endpoint for each governance server.
 * If specified then the endpoint overrides the metadata server endpoint
 */
public class GovernanceServerEndpoint {
    private String serverName;
    private String serverURL;
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
     * The serverURL for the governance server.
     * @return server URL
     */
    public String getServerURL() {
        return serverURL;
    }  
    /**
     * Set up the serverURL for the governance server.
     *
     * @param serverURL governance server name
     */

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    /**
     * governance service name
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
                Objects.equals(getServerURL(), that.getServerURL()) &&
                Objects.equals(getGovernanceServiceName(), that.getGovernanceServiceName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerName(), getServerURL(), getGovernanceServiceName());
    }

    @Override
    public String toString() {
        return "GovernanceServerEndpoint{" +
                "serverName='" + serverName + '\'' +
                ", serverURL='" + serverURL + '\'' +
                ", governanceServiceName='" + governanceServiceName + '\'' +
                '}';
    }


}
