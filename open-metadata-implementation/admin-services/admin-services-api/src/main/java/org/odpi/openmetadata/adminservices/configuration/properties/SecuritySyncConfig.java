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
    private int securitySyncServiceCode = 0;
    private String securitySyncServiceName;
    private String securitySyncServerDescription;
    private String securitySyncServiceWiki;

    private String accessServiceRootURL;
    private String accessServiceServerName;

    private String securityServerURL;
    private String securitySyncServerType;
    private String securitySyncServerAuthorization;
    private String securitySyncTagServiceName;
    private Connection securitySyncServerConnection;

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
            securitySyncServiceCode = template.securitySyncServiceCode;
            securitySyncServiceName = template.securitySyncServiceName;
            securitySyncServerDescription = template.securitySyncServerDescription;
            securitySyncServiceWiki = template.securitySyncServiceWiki;

            accessServiceRootURL = template.accessServiceRootURL;
            accessServiceServerName = template.accessServiceServerName;

            securityServerURL = template.securityServerURL;
            securitySyncServerType = template.securitySyncServerType;
            securitySyncServerAuthorization = template.securitySyncServerAuthorization;
            securitySyncTagServiceName = template.securitySyncTagServiceName;
            securitySyncServerConnection = template.securitySyncServerConnection;

            securitySyncInTopic = template.securitySyncInTopic;
            securitySyncInTopicName = template.securitySyncInTopicName;
            securitySyncOutTopic = template.securitySyncOutTopic;
            securitySyncOutTopicName = template.securitySyncOutTopicName;
        }
    }


    /**
     * Return the code number (ordinal) for this Security Sync
     *
     * @return the code number for Security Sync component
     */
    public int getSecuritySyncServiceCode() {
        return securitySyncServiceCode;
    }


    /**
     * Set up the code number (ordinal) for the Security Sync
     *
     * @param securitySyncServiceCode int ordinal
     */
    public void setSecuritySyncServiceCode(int securitySyncServiceCode) {
        this.securitySyncServiceCode = securitySyncServiceCode;
    }


    /**
     * Return the name of the Security Sync Connector
     *
     * @return the name of the security sync connector
     */
    public String getSecuritySyncServiceName() {
        return securitySyncServiceName;
    }


    /**
     * Set up the name of the Security Sync Connector
     *
     * @param securitySyncServiceName connector name
     */
    public void setSecuritySyncServiceName(String securitySyncServiceName) {
        this.securitySyncServiceName = securitySyncServiceName;
    }


    /**
     * Return the short description of the Security Sync Component.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getSecuritySyncServerDescription() {
        return securitySyncServerDescription;
    }


    /**
     * Set up the short description of the Security Sync.
     *
     * @param securitySyncServerDescription String description
     */
    public void setSecuritySyncServerDescription(String securitySyncServerDescription) {
        this.securitySyncServerDescription = securitySyncServerDescription;
    }


    /**
     * Return the wiki page link for the Security Sync. The default value points to a page on the Egeria confluence wiki.
     *
     * @return String url
     */
    public String getSecuritySyncServiceWiki() {
        return securitySyncServiceWiki;
    }


    /**
     * Set up the wiki page link for the Security Sync. The default value points to a page on the Egeria confluence wiki.
     *
     * @param securitySyncServiceWiki String url
     */
    public void setSecuritySyncServiceWiki(String securitySyncServiceWiki) {
        this.securitySyncServiceWiki = securitySyncServiceWiki;
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
     * Return the URL for the Governance Engine Server used in the Governance Server Connector
     *
     * @return String URL
     */
    public String getAccessServiceRootURL() {
        return accessServiceRootURL;
    }


    /**
     * Set up the URL for the Governance Engine  Server used in the Governance Server Connector.
     *
     * @param accessServiceRootURL String for Governance Server URL
     */
    public void setAccessServiceRootURL(String accessServiceRootURL) {
        this.accessServiceRootURL = accessServiceRootURL;
    }


    /**
     * Return the name of the server where Governance Engine OMAS is running
     *
     * @return
     */
    public String getAccessServiceServerName() {
        return accessServiceServerName;
    }

    /**
     * Set up the name of the server where Governance Engine OMAS is running
     *
     * @param accessServiceServerName
     */
    public void setAccessServiceServerName(String accessServiceServerName) {
        this.accessServiceServerName = accessServiceServerName;
    }

    /**
     * Return the server type in order to identify the Governance Connector.
     *
     * @return String Server type
     */
    public String getSecuritySyncServerType() {
        return securitySyncServerType;
    }


    /**
     * Set up the server type in order to identify the Governance Connector.
     *
     * @param securitySyncServerType String
     */
    public void setSecuritySyncServerType(String securitySyncServerType) {
        this.securitySyncServerType = securitySyncServerType;
    }


    /**
     * Return the authorization needed in the Governance Services Connector
     *
     * @return String with basic authorization header
     */
    public String getSecuritySyncServerAuthorization() {
        return securitySyncServerAuthorization;
    }


    /**
     * Set up the authorization needed in the Governance Services Connector.
     *
     * @param securitySyncServerAuthorization String with basic authorization header
     */
    public void setSecuritySyncServerAuthorization(String securitySyncServerAuthorization) {
        this.securitySyncServerAuthorization = securitySyncServerAuthorization;
    }

    /**
     * Return the Tag Service Named used in the Governance Services Connector to synchronize the governed classifications.
     *
     * @return String tag service name
     */
    public String getSecuritySyncTagServiceName() {
        return securitySyncTagServiceName;
    }


    /**
     * Set up the Tag Service Named used in the Governance Services Connector to synchronize the governed classifications.
     *
     * @param securitySyncTagServiceName the name of the tag service
     */
    public void setSecuritySyncTagServiceName(String securitySyncTagServiceName) {
        this.securitySyncTagServiceName = securitySyncTagServiceName;
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
     * @return Connection for In Topic
     */
    public Connection getSecuritySyncInTopic() {
        return securitySyncInTopic;
    }


    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Security Sync.
     *
     * @param securitySyncInTopic Connection for In Topic
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
     *
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


    /**
     * Return the OCF Connection for the Security Server used to push the Security Tags.
     *
     * @return Connection for Security Server
     */
    public Connection getSecuritySyncServerConnection() {
        return securitySyncServerConnection;
    }

    /**
     * Set up the OCF Connection for the Security Server used to pass requests to this Security Sync.
     *
     * @param securitySyncServerConnection Connection for Security Server
     */
    public void setSecuritySyncServerConnection(Connection securitySyncServerConnection) {
        this.securitySyncServerConnection = securitySyncServerConnection;
    }


    @Override
    public String toString() {
        return "SecuritySyncConfig{" +
                "securitySyncServiceCode=" + securitySyncServiceCode +
                ", securitySyncServiceName='" + securitySyncServiceName + '\'' +
                ", securitySyncServerDescription='" + securitySyncServerDescription + '\'' +
                ", securitySyncServiceWiki ='" + securitySyncServiceWiki + '\'' +
                ", securityServerURL='" + securityServerURL + '\'' +
                ", accessServiceRootURL='" + accessServiceRootURL + '\'' +
                ", accessServiceServerName='" + accessServiceServerName + '\'' +
                ", securitySyncServerType='" + securitySyncServerType + '\'' +
                ", securitySyncServerAuthorization='" + securitySyncServerAuthorization + '\'' +
                ", securitySyncTagServiceName='" + securitySyncTagServiceName + '\'' +
                ", securitySyncInTopicName='" + securitySyncInTopicName + '\'' +
                ", securitySyncInTopic=" + securitySyncInTopic +
                ", securitySyncOutTopicName='" + securitySyncOutTopicName + '\'' +
                ", securitySyncOutTopic=" + securitySyncOutTopic +
                ", securitySyncServerConnection=" + securitySyncServerConnection +
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
        return getSecuritySyncServiceCode() == that.getSecuritySyncServiceCode() &&
                Objects.equals(getSecuritySyncServiceName(), that.getSecuritySyncServiceName()) &&
                Objects.equals(getSecuritySyncServerDescription(), that.getSecuritySyncServerDescription()) &&
                Objects.equals(getSecuritySyncServiceWiki(), that.getSecuritySyncServiceWiki()) &&
                Objects.equals(getSecurityServerURL(), that.getSecurityServerURL()) &&
                Objects.equals(getAccessServiceRootURL(), that.getAccessServiceRootURL()) &&
                Objects.equals(getAccessServiceServerName(), that.getAccessServiceServerName()) &&
                Objects.equals(getSecuritySyncServerAuthorization(), that.getSecuritySyncServerAuthorization()) &&
                Objects.equals(getSecuritySyncTagServiceName(), that.getSecuritySyncTagServiceName()) &&
                Objects.equals(getSecuritySyncInTopic(), that.getSecuritySyncInTopic()) &&
                Objects.equals(getSecuritySyncOutTopic(), that.getSecuritySyncOutTopic()) &&
                Objects.equals(getSecuritySyncInTopicName(), that.getSecuritySyncInTopicName()) &&
                Objects.equals(getSecuritySyncServerConnection(), that.getSecuritySyncServerConnection()) &&
                Objects.equals(getSecuritySyncOutTopicName(), that.getSecuritySyncOutTopicName());
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                getSecuritySyncServiceCode(),
                getSecuritySyncServiceName(),
                getSecuritySyncServerDescription(),
                getSecuritySyncServiceWiki(),
                getSecurityServerURL(),
                getAccessServiceRootURL(),
                getAccessServiceServerName(),
                getSecuritySyncServerAuthorization(),
                getSecuritySyncTagServiceName(),
                getSecuritySyncInTopic(),
                getSecuritySyncOutTopic(),
                getSecuritySyncInTopicName(),
                getSecuritySyncOutTopicName(),
                getSecuritySyncServerConnection());
    }
}