/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SecuritySyncConfig provides the properties for the security-sync-services.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecuritySyncConfig extends AdminServicesConfigHeader {

    private int securitySyncId = 0;
    private String securitySyncAdminClass;
    private String securitySyncName;
    private String securitySyncDescription;
    private String securitySyncWiki;

    private String securityServerURL;
    private String securityServerType;
    private String securityServerAuthorization;
    private String tagServiceName;

    private String securitySyncInTopicName;
    private Connection securitySyncInTopic;

    private String securitySyncOutTopicName;
    private Connection securitySyncOutTopic;

    /**
     * Default constructor
     */
    public SecuritySyncConfig() {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SecuritySyncConfig(SecuritySyncConfig template) {
        super(template);

        if (template != null) {
            securitySyncId = template.securitySyncId;
            securitySyncAdminClass = template.securitySyncAdminClass;
            securitySyncName = template.securitySyncName;
            securitySyncDescription = template.securitySyncDescription;
            securitySyncWiki = template.securitySyncWiki;

            securityServerURL = template.securityServerURL;
            securityServerAuthorization = template.securityServerAuthorization;
            tagServiceName = template.tagServiceName;

            securitySyncInTopic = template.securitySyncInTopic;

            securitySyncOutTopic = template.securitySyncOutTopic;
        }
    }

    public int getSecuritySyncId() {
        return securitySyncId;
    }

    public void setSecuritySyncId(int securitySyncId) {
        this.securitySyncId = securitySyncId;
    }

    public String getSecuritySyncAdminClass() {
        return securitySyncAdminClass;
    }

    public void setSecuritySyncAdminClass(String securitySyncAdminClass) {
        this.securitySyncAdminClass = securitySyncAdminClass;
    }

    public String getSecuritySyncName() {
        return securitySyncName;
    }

    public void setSecuritySyncName(String securitySyncName) {
        this.securitySyncName = securitySyncName;
    }

    public String getSecuritySyncDescription() {
        return securitySyncDescription;
    }

    public void setSecuritySyncDescription(String securitySyncDescription) {
        this.securitySyncDescription = securitySyncDescription;
    }

    public String getSecuritySyncWiki() {
        return securitySyncWiki;
    }

    public void setSecuritySyncWiki(String securitySyncWiki) {
        this.securitySyncWiki = securitySyncWiki;
    }

    public String getSecurityServerURL() {
        return securityServerURL;
    }

    public void setSecurityServerURL(String securityServerURL) {
        this.securityServerURL = securityServerURL;
    }

    public String getSecurityServerType() {
        return securityServerType;
    }

    public void setSecurityServerType(String securityServerType) {
        this.securityServerType = securityServerType;
    }

    public String getSecurityServerAuthorization() {
        return securityServerAuthorization;
    }

    public void setSecurityServerAuthorization(String securityServerAuthorization) {
        this.securityServerAuthorization = securityServerAuthorization;
    }

    public String getTagServiceName() {
        return tagServiceName;
    }

    public void setTagServiceName(String tagServiceName) {
        this.tagServiceName = tagServiceName;
    }

    public String getSecuritySyncInTopicName() {
        return securitySyncInTopicName;
    }

    public void setSecuritySyncInTopicName(String securitySyncInTopicName) {
        this.securitySyncInTopicName = securitySyncInTopicName;
    }

    public Connection getSecuritySyncInTopic() {
        return securitySyncInTopic;
    }

    public void setSecuritySyncInTopic(Connection securitySyncInTopic) {
        this.securitySyncInTopic = securitySyncInTopic;
    }

    public String getSecuritySyncOutTopicName() {
        return securitySyncOutTopicName;
    }

    public void setSecuritySyncOutTopicName(String securitySyncOutTopicName) {
        this.securitySyncOutTopicName = securitySyncOutTopicName;
    }

    public Connection getSecuritySyncOutTopic() {
        return securitySyncOutTopic;
    }

    public void setSecuritySyncOutTopic(Connection securitySyncOutTopic) {
        this.securitySyncOutTopic = securitySyncOutTopic;
    }

    @Override
    public String toString() {
        return "SecuritySyncConfig{" +
                "securitySyncId=" + securitySyncId +
                ", securitySyncAdminClass='" + securitySyncAdminClass + '\'' +
                ", securitySyncName='" + securitySyncName + '\'' +
                ", securitySyncDescription='" + securitySyncDescription + '\'' +
                ", securitySyncWiki ='" + securitySyncWiki + '\'' +
                ", securityServerURL='" + securityServerURL + '\'' +
                ", securityServerType='" + securityServerType + '\'' +
                ", securityServerAuthorization='" + securityServerAuthorization + '\'' +
                ", tagServiceName='" + tagServiceName + '\'' +
                ", securitySyncInTopicName='" + securitySyncInTopicName + '\'' +
                ", securitySyncInTopic=" + securitySyncInTopic +
                ", securitySyncOutTopicName='" + securitySyncOutTopicName + '\'' +
                ", securitySyncOutTopic=" + securitySyncOutTopic +
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
        if (this == objectToCompare) {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) {
            return false;
        }
        SecuritySyncConfig that = (SecuritySyncConfig) objectToCompare;
        return getSecuritySyncId() == that.getSecuritySyncId() &&
                Objects.equals(getSecuritySyncAdminClass(), that.getSecuritySyncAdminClass()) &&
                Objects.equals(getSecuritySyncName(), that.getSecuritySyncName()) &&
                Objects.equals(getSecuritySyncDescription(), that.getSecuritySyncDescription()) &&
                Objects.equals(getSecuritySyncWiki(), that.getSecuritySyncWiki()) &&
                Objects.equals(getSecurityServerURL(), that.getSecurityServerURL()) &&
                getSecurityServerAuthorization() == that.getSecurityServerAuthorization() &&
                Objects.equals(getTagServiceName(), that.getTagServiceName()) &&
                Objects.equals(getSecuritySyncInTopic(), that.getSecuritySyncInTopic()) &&
                Objects.equals(getSecuritySyncOutTopic(), that.getSecuritySyncOutTopic()) &&
                Objects.equals(getSecuritySyncInTopicName(), that.getSecuritySyncInTopicName()) &&
                Objects.equals(getSecuritySyncOutTopicName(), that.getSecuritySyncOutTopicName());
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSecuritySyncId(), getSecuritySyncAdminClass(), getSecuritySyncName(),
                getSecuritySyncDescription(), getSecuritySyncWiki(), getSecurityServerURL(), getSecurityServerAuthorization(), getTagServiceName(),
                getSecuritySyncInTopic(), getSecuritySyncOutTopic(), getSecuritySyncInTopicName(), getSecuritySyncOutTopicName());
    }
}
