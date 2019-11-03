/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;


public class EndpointSource extends Source {
    private String networkAddress;
    private String protocol;
    private String encryptionMethod;
    private String connectorProviderName;
    private String user;

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
     * Return the name of the connector provider.
     *
     * @return sting name
     */
    public String getConnectorProviderName() {
        return connectorProviderName;
    }

    /**
     * Set up the name of the connector provider.
     *
     * @param connectorProviderName string name
     */
    public void setConnectorProviderName(String connectorProviderName) {
        this.connectorProviderName = connectorProviderName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    public void setEncryptionMethod(String encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }

    @Override
    public String toString() {
        return "{" +
                "networkAddress='" + networkAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", encryptionMethod='" + encryptionMethod + '\'' +
                ", connectorProviderName='" + connectorProviderName + '\'' +
                ", user='" + user + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
