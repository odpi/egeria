/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OLSSimplifiedAccessServiceConfig {
    private String serverName;
    private String serverPlatformUrlRoot;
    private String user;
    private String password;

    public OLSSimplifiedAccessServiceConfig() {}

    public OLSSimplifiedAccessServiceConfig(String serverName, String serverPlatformUrlRoot, String user, String password) {
        this.serverName = serverName;
        this.serverPlatformUrlRoot = serverPlatformUrlRoot;
        this.user = user;
        this.password = password;
    }

    /**
     * Gets server name.
     *
     * @return the server name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets server name.
     *
     * @param serverName the server name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Gets server platform url root.
     *
     * @return the server platform url root
     */
    public String getServerPlatformUrlRoot() {
        return serverPlatformUrlRoot;
    }

    /**
     * Sets server platform url root.
     *
     * @param serverPlatformUrlRoot the server platform url root
     */
    public void setServerPlatformUrlRoot(String serverPlatformUrlRoot) {
        this.serverPlatformUrlRoot = serverPlatformUrlRoot;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "OLSSimplifiedAccessServiceConfig{" +
                "serverName=" + serverName +
                ", serverPlatformUrlRoot='" + serverPlatformUrlRoot + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OLSSimplifiedAccessServiceConfig that = (OLSSimplifiedAccessServiceConfig) o;
        return Objects.equals(serverName, that.serverName) &&
                Objects.equals(serverPlatformUrlRoot, that.serverPlatformUrlRoot) &&
                Objects.equals(user, that.user) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, serverPlatformUrlRoot, user, password);
    }

}
