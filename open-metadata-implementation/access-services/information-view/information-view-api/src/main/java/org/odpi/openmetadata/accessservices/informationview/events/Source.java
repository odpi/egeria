/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TableSource.class, name = "TableSource"),
        @JsonSubTypes.Type(value = DatabaseColumnSource.class, name = "DatabaseColumnSource"),
        @JsonSubTypes.Type(value = ReportColumnSource.class, name = "ReportColumnSource"),
        @JsonSubTypes.Type(value = ReportSection.class, name = "ReportSection"),
        @JsonSubTypes.Type(value = ReportSectionSource.class, name = "ReportSectionSource")})
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "@id")
@JsonIdentityReference
public abstract class Source {

    private String networkAddress;
    private String protocol;
    private String encryptionMethod;
    private String connectorProviderName;
    private String user;
    private Map<String, String> additionalProperties;

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

    public String getConnectorProviderName() {
        return connectorProviderName;
    }

    public void setConnectorProviderName(String connectorProviderName) {
        this.connectorProviderName = connectorProviderName;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
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

    public abstract String getQualifiedName();

    @Override
    public String toString() {
        return "Source{" +
                "networkAddress='" + networkAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", encryptionMethod='" + encryptionMethod + '\'' +
                ", connectorProviderName='" + connectorProviderName + '\'' +
                ", user='" + user + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
