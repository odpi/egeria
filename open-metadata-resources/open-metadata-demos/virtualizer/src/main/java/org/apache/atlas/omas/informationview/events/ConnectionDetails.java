/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.omas.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *ConnectionDetails stores the information about the real datasource
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionDetails {

    private String url;
    private String networkAddress;
    private String protocol;
    private String connectorProviderName;
    private String connectorProviderQualifiedName;
    private String gaianNodeName;
    private String connectionQualifiedName;
    private String endpointQualifiedName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConnectorProviderName() {
        return connectorProviderName;
    }

    public void setConnectorProviderName(String connectorProviderName) {
        this.connectorProviderName = connectorProviderName;
    }

    public String getGaianNodeName() {
        return gaianNodeName;
    }

    public void setGaianNodeName(String gaianNodeName) {
        this.gaianNodeName = gaianNodeName;
    }

    public String getConnectorProviderQualifiedName() {
        return connectorProviderQualifiedName;
    }

    public void setConnectorProviderQualifiedName(String connectorProviderQualifiedName) {
        this.connectorProviderQualifiedName = connectorProviderQualifiedName;
    }

    public String getConnectionQualifiedName() {
        return connectionQualifiedName;
    }

    public void setConnectionQualifiedName(String connectionQualifiedName) {
        this.connectionQualifiedName = connectionQualifiedName;
    }

    public String getEndpointQualifiedName() {
        return endpointQualifiedName;
    }

    public void setEndpointQualifiedName(String endpointQualifiedName) {
        this.endpointQualifiedName = endpointQualifiedName;
    }
}
