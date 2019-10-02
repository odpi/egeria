package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ServerEndpointConfig provides the properties describing the endpoints of the services the UI calls.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerEndpointConfig extends UIAdminServicesConfigHeader {

    private String serverName;
    private String serverUrl;

    /**
     * Default constructor
     */
    public ServerEndpointConfig() {
        super();
    }

    /**
     * The server name
     * @return the name.
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * set the name of the server
     * @param serverName name of endpoint
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * get the server url
     * @return the url
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * set omag server url
     * @param serverUrl  omag server url
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }


    @Override
    public String toString() {
        return "org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ServerEndpointConfig{" +
                "serverName='" + serverName + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                '}';
    }
    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare) {
        if (this == objectToCompare) return true;
        if (!(objectToCompare instanceof ServerEndpointConfig)) return false;
        ServerEndpointConfig that = (ServerEndpointConfig) objectToCompare;
        return Objects.equals(getServerName(), that.getServerName()) &&
                Objects.equals(getServerUrl(), that.getServerUrl());
    }
    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getServerName(), getServerUrl());
    }

}