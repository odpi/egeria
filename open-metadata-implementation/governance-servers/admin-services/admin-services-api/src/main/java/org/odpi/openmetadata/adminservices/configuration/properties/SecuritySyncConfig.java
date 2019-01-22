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
     * Set up the default values for security sync
     *
     * @param template fixed properties about security sync
     */
    public SecuritySyncConfig(SecuritySyncConfig template) {
        super(template);

        if (template != null) {
            securitySyncId = template.securitySyncId;
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

    /**
     * Return the code number (ordinal) for this Security Sync
     *
     * @return the code numner for Security Sync component
     */
    public int getSecuritySyncId() {
        return securitySyncId;
    }

    /**
     * Set up the code number (ordinal) for the Security Sync
     *
     * @param securitySyncId int ordinal
     */
    public void setSecuritySyncId(int securitySyncId) {
        this.securitySyncId = securitySyncId;
    }

    /**
     *  Return the name of the Security Sync Connector
     *
     * @return the name of the security sync connector
     */
    public String getSecuritySyncName() {
        return securitySyncName;
    }

    /**
     * Set up the name of the Security Sync Connector
     *
     * @param securitySyncName connector name
     */
    public void setSecuritySyncName(String securitySyncName) {
        this.securitySyncName = securitySyncName;
    }

    /**
     * Return the short description of the Security Sync Component.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getSecuritySyncDescription() {
        return securitySyncDescription;
    }

    /**
     * Set up the short description of the Security Sync.
     *
     * @param securitySyncDescription String description
     */
    public void setSecuritySyncDescription(String securitySyncDescription) {
        this.securitySyncDescription = securitySyncDescription;
    }

    /**
     * Return the wiki page link for the Security Sync. The default value points to a page on the Egeria confluence wiki.
     *
     * @return String url
     */
    public String getSecuritySyncWiki() {
        return securitySyncWiki;
    }

    /**
     * Set up the wiki page link for the Security Sync. The default value points to a page on the Egeria confluence wiki.
     *
     * @param securitySyncWiki String url
     */
    public void setSecuritySyncWiki(String securitySyncWiki) {
        this.securitySyncWiki = securitySyncWiki;
    }

    /**
     * Return the URL for the Security Server used in the Governance Server Connector
     *
     * @return String URL
     */
    public String getSecurityServerURL() {
        return securityServerURL;
    }

    /**
     * Set up the URL for the Security Server used in the Governance Server Connector.
     *
     * @param securityServerURL String for Governance Server URL
     */
    public void setSecurityServerURL(String securityServerURL) {
        this.securityServerURL = securityServerURL;
    }

    /**
     * Return the server type in order to identify the Governance Connector.
     *
     * @return String Server type
     */
    public String getSecurityServerType() {
        return securityServerType;
    }

    /**
     * Set up the server type in order to identify the Governance Connector.
     *
     * @param securityServerType String
     */
    public void setSecurityServerType(String securityServerType) {
        this.securityServerType = securityServerType;
    }

    /**
     * Return the authorization needed in the Governance Services Connector
     *
     * @return String with basic authorization header
     */
    public String getSecurityServerAuthorization() {
        return securityServerAuthorization;
    }

    /**
     * Set up the authorization needed in the Governance Services Connector.
     *
     * @param securityServerAuthorization String with basic authorization header
     */
    public void setSecurityServerAuthorization(String securityServerAuthorization) {
        this.securityServerAuthorization = securityServerAuthorization;
    }

    /**
     * Return the Tag Service Named used in the Governance Services Connector to synchronize the governed classifications.
     *
     * @return String tag service name
     */
    public String getTagServiceName() {
        return tagServiceName;
    }

    /**
     * Set up the Tag Service Named used in the Governance Services Connector to synchronize the governed classifications.
     *
     * @param tagServiceName the name of the tag service
     */
    public void setTagServiceName(String tagServiceName) {
        this.tagServiceName = tagServiceName;
    }

    /**
     * Return the Input Topic Name for Security Sync
     *
     * @return String Input Topic name
     */
    public String getSecuritySyncInTopicName() {
        return securitySyncInTopicName;
    }

    /**
     * Set up the Security Sync In Topic Name
     *
     * @param securitySyncInTopicName String Security Sync Name
     */
    public void setSecuritySyncInTopicName(String securitySyncInTopicName) {
        this.securitySyncInTopicName = securitySyncInTopicName;
    }

    /**
     * Return the OCF Connection for the In Topic used to pass requests to this Security Sync.
     * For example, the output topic of Governance Engine OMAS can be provided
     * (e.g. "open-metadata.access-services.GovernanceEngine.outTopic")
     *
     * @return  Connection for In Topic
     */
    public Connection getSecuritySyncInTopic() {
        return securitySyncInTopic;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Security Sync.
     *
     * @param securitySyncInTopic  Connection for In Topic
     */
    public void setSecuritySyncInTopic(Connection securitySyncInTopic) {
        this.securitySyncInTopic = securitySyncInTopic;
    }


    /**
     * Return the Security Sync Out Topic Name
     *
     * @return String security out topic name
     */
    public String getSecuritySyncOutTopicName() {
        return securitySyncOutTopicName;
    }

    /**
     * Set up the Security Sync Out Name
     * @param securitySyncOutTopicName String Security Out Topic Name
     */
    public void setSecuritySyncOutTopicName(String securitySyncOutTopicName) {
        this.securitySyncOutTopicName = securitySyncOutTopicName;
    }

    /**
     * Return the OCF Connection for the topic used to pass requests to Security Sync.
     * The default values are constructed from the Security Sync Server Name.
     *
     * @return Connection for Out Topic
     */
    public Connection getSecuritySyncOutTopic() {
        return securitySyncOutTopic;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Security Sync.
     *
     * @param securitySyncOutTopic Connection for Out Topic
     */
    public void setSecuritySyncOutTopic(Connection securitySyncOutTopic) {
        this.securitySyncOutTopic = securitySyncOutTopic;
    }

    @Override
    public String toString() {
        return "SecuritySyncConfig{" +
                "securitySyncId=" + securitySyncId +
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
        return Objects.hash(getSecuritySyncId(), getSecuritySyncName(),
                getSecuritySyncDescription(), getSecuritySyncWiki(), getSecurityServerURL(), getSecurityServerAuthorization(), getTagServiceName(),
                getSecuritySyncInTopic(), getSecuritySyncOutTopic(), getSecuritySyncInTopicName(), getSecuritySyncOutTopicName());
    }
}