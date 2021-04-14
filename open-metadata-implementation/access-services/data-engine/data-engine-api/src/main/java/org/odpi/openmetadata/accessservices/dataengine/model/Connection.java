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

    public String getClearPassword() {
        return clearPassword;
    }

    public void setClearPassword(String clearPassword) {
        this.clearPassword = clearPassword;
    }

    public Map<String, String> getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Map<String, String> configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Map<String, String> getSecuredProperties() {
        return securedProperties;
    }

    public void setSecuredProperties(Map<String, String> securedProperties) {
        this.securedProperties = securedProperties;
    }

    public String getUserId() {
        return userId;
    }

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
