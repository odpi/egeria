/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Connection extends Referenceable{

    private String clearPassword;
    private Map<String, String> configurationProperties;
    private String description;
    private String displayName;
    private String encryptedPassword;
    private Map<String, String> securedProperties;
    private String userId;

    /**
     * Get clear password
     *
     * @return clear password
     */
    public String getClearPassword() {
        return clearPassword;
    }

    /**
     * Set clear password
     *
     * @param clearPassword
     */
    public void setClearPassword(String clearPassword) {
        this.clearPassword = clearPassword;
    }

    /**
     * Get configuration properties
     *
     * @return configuration properties
     */
    public Map<String, String> getConfigurationProperties() {
        return configurationProperties;
    }

    /**
     * Set configuration properties
     *
     * @param configurationProperties
     */
    public void setConfigurationProperties(Map<String, String> configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    /**
     * Get description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get display name
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set display name
     *
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get encrypted password
     *
     * @return encrypted password
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * Set encrypted password
     *
     * @param encryptedPassword
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * Get secured properties
     *
     * @return secured properties
     */
    public Map<String, String> getSecuredProperties() {
        return securedProperties;
    }

    /**
     * Get secured properties
     *
     * @param securedProperties
     */
    public void setSecuredProperties(Map<String, String> securedProperties) {
        this.securedProperties = securedProperties;
    }

    /**
     * Get user id
     *
     * @return user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Get user id
     *
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "clearPassword='" + clearPassword + '\'' +
                ", configurationProperties=" + configurationProperties +
                ", description=" + description +
                ", displayName=" + displayName +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", securedProperties=" + securedProperties +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection dataStore = (Connection) o;
        return Objects.equals(clearPassword, dataStore.clearPassword) &&
                Objects.equals(configurationProperties, dataStore.configurationProperties) &&
                Objects.equals(description, dataStore.description) &&
                Objects.equals(displayName, dataStore.displayName) &&
                Objects.equals(encryptedPassword, dataStore.encryptedPassword) &&
                Objects.equals(securedProperties, dataStore.securedProperties) &&
                Objects.equals(userId, dataStore.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clearPassword, configurationProperties, description, displayName, encryptedPassword,
                securedProperties, userId);
    }

}
