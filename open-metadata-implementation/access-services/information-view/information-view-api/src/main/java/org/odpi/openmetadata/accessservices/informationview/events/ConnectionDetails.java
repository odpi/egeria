/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionDetails {

    private String networkAddress;
    private String protocol;
    private String user;
    private String connectorProviderName;
    private String connectorProviderQualifiedName;
    private String connectionQualifiedName;
    private String endpointQualifiedName;


    /**
     * Return the address of the connection
     *
     * @return address of the connection
     */
    public String getNetworkAddress() {
        return networkAddress;
    }

    /**
     * set up the address of the connection
     *
     * @param networkAddress - address of the connection
     */
    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }

    /**
     * Return the protocol of the connection
     *
     * @return protocol of the connection
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * set up the protocol of the connection
     *
     * @param protocol - protocol of the connection
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Return the connector provider name of the connection
     *
     * @return connector provider name of the connection
     */
    public String getConnectorProviderName() {
        return connectorProviderName;
    }

    /**
     * set up the connector provider name of the connection
     *
     * @param connectorProviderName - connector provider name of the connection
     */
    public void setConnectorProviderName(String connectorProviderName) {
        this.connectorProviderName = connectorProviderName;
    }


    /**
     * Return the connector provider qualified name for the connection
     *
     * @return connector provider qualified name
     */
    public String getConnectorProviderQualifiedName() {
        return connectorProviderQualifiedName;
    }

    /**
     * set up the connector provider qualified name of the connection
     *
     * @param connectorProviderQualifiedName - qualified name of the connector provider
     */
    public void setConnectorProviderQualifiedName(String connectorProviderQualifiedName) {
        this.connectorProviderQualifiedName = connectorProviderQualifiedName;
    }
    /**
     * Return the qualified name of the connection
     *
     * @return qualified name of the connection
     */
    public String getConnectionQualifiedName() {
        return connectionQualifiedName;
    }

    /**
     * set up the connection qualified name
     *
     * @param connectionQualifiedName - qualified name of the connection
     */
    public void setConnectionQualifiedName(String connectionQualifiedName) {
        this.connectionQualifiedName = connectionQualifiedName;
    }

    /**
     * Return the qualified name of the endpoint
     *
     * @return qualified name of the endpoint
     */
    public String getEndpointQualifiedName() {
        return endpointQualifiedName;
    }

    /**
     * set up the endpoint qualified name
     *
     * @param endpointQualifiedName - qualified name of the endpoint
     */
    public void setEndpointQualifiedName(String endpointQualifiedName) {
        this.endpointQualifiedName = endpointQualifiedName;
    }

    /**
     *
     * @return user to use ro connect
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user to connect
     */
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ConnectionDetails{" +
                ", networkAddress='" + networkAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", connectorProviderName='" + connectorProviderName + '\'' +
                ", connectorProviderQualifiedName='" + connectorProviderQualifiedName + '\'' +
                ", connectionQualifiedName='" + connectionQualifiedName + '\'' +
                ", endpointQualifiedName='" + endpointQualifiedName + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
