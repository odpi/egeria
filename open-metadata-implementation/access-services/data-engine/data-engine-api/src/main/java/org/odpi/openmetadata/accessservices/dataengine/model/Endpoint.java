/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Endpoint extends Referenceable{

    private String description;
    private String encryptionMethod;
    private String name;
    private String networkAddress;
    private String protocol;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    public void setEncryptionMethod(String encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Endpoint{" +
                "description='" + description + '\'' +
                ", encryptionMethod=" + encryptionMethod +
                ", name=" + name +
                ", networkAddress=" + networkAddress +
                ", protocol='" + protocol + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint dataStore = (Endpoint) o;
        return Objects.equals(description, dataStore.description) &&
                Objects.equals(encryptionMethod, dataStore.encryptionMethod) &&
                Objects.equals(name, dataStore.name) &&
                Objects.equals(networkAddress, dataStore.networkAddress) &&
                Objects.equals(protocol, dataStore.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, encryptionMethod, name, networkAddress, protocol);
    }

}
