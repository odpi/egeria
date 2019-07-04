/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityOfficerConfig provides the properties for the security-Officer-services.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityOfficerConfig extends AdminServicesConfigHeader {

    private int securityOfficerServiceCode = 0;
    private String securityOfficerServiceName;
    private String securityOfficerServiceDescription;
    private String securityOfficerServerWiki;

    private String accessServiceRootURL;
    private String accessServiceServerName;

    private String securityOfficerServerInTopicName;
    private Connection securityOfficerServerInTopic;
    private String securityOfficerServerOutTopicName;
    private Connection securityOfficerServerOutTopic;

    /**
     * Default constructor
     */
    public SecurityOfficerConfig() {
        super();
    }

    /**
     * Return the code number (ordinal) for this Security Officer Server
     *
     * @return the code number for Security Officer Server component
     */
    public int getSecurityOfficerServiceCode() {
        return securityOfficerServiceCode;
    }

    /**
     * Set up the code number (ordinal) for the Security Officer Server
     *
     * @param securityOfficerServiceCode int ordinal
     */
    public void setSecurityOfficerServiceCode(int securityOfficerServiceCode) {
        this.securityOfficerServiceCode = securityOfficerServiceCode;
    }

    /**
     *  Return the name of the Security Officer Server
     *
     * @return the name of the security Officer Server
     */
    public String getSecurityOfficerServiceName() {
        return securityOfficerServiceName;
    }

    /**
     * Set up the name of the Security Officer Server Name
     *
     * @param securityOfficerServiceName server name
     */
    public void setSecurityOfficerServiceName(String securityOfficerServiceName) {
        this.securityOfficerServiceName = securityOfficerServiceName;
    }

    /**
     * Return the short description of the Security Officer Server Component.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getSecurityOfficerServiceDescription() {
        return securityOfficerServiceDescription;
    }

    /**
     * Set up the short description of the Security Officer Server Component.  The default value is in English but this can be changed.
     *
     * @param  securityOfficerServiceDescription String securityOfficerServiceDescription
     */
    public void setSecurityOfficerServiceDescription(String securityOfficerServiceDescription) {
        this.securityOfficerServiceDescription = securityOfficerServiceDescription;
    }

    /**
     * Return the wiki page link for the Security Officer. The default value points to a page on the Egeria confluence wiki.
     *
     * @return String url
     */
    public String getSecurityOfficerServerWiki() {
        return securityOfficerServerWiki;
    }

    /**
     * Set up the wiki page link for the Security Officer Server. The default value points to a page on the Egeria confluence wiki.
     *
     * @param securityOfficerServerWiki String url
     */
    public void setSecurityOfficerServerWiki(String securityOfficerServerWiki) {
        this.securityOfficerServerWiki = securityOfficerServerWiki;
    }

    /**
     * Return the URL for the Security Officer OMAS connected
     *
     * @return String URL
     */
    public String getAccessServiceRootURL() {
        return accessServiceRootURL;
    }

    /**
     * Set up the URL for the Security Officer OMAS connected
     * @param accessServiceRootURL ULR for the Security Officer OMAS instance
     */
    public void setAccessServiceRootURL(String accessServiceRootURL) {
        this.accessServiceRootURL = accessServiceRootURL;
    }

    /**
     * Return the name of the server where Security Officer OMAS is running
     * @return the server name
     */
    public String getAccessServiceServerName() {
        return accessServiceServerName;
    }

    /**
     * Set up the name of the server where the instance of the Security Officer OMAS is running
     * @param accessServiceServerName name of the server for Security Officer OMAS
     */
    public void setAccessServiceServerName(String accessServiceServerName) {
        this.accessServiceServerName = accessServiceServerName;
    }

    /**
     * Return the Input Topic Name for Security Officer Server
     *
     * @return String Input Topic name
     */
    public String getSecurityOfficerServerInTopicName() {
        return securityOfficerServerInTopicName;
    }

    /**
     * Set up the Security Officer Server In Topic Name
     *
     * @param securityOfficerServerInTopicName String Security Officer Name
     */
    public void setSecurityOfficerServerInTopicName(String securityOfficerServerInTopicName) {
        this.securityOfficerServerInTopicName = securityOfficerServerInTopicName;
    }

    /**
     * Return the OCF Connection for the In Topic used to pass requests to this Security Officer.
     * For example, the output topic of Security Officer OMAS can be provided
     * (e.g. "open-metadata.access-services.SecurityOfficer.outTopic")
     *
     * @return  Connection for In Topic
     */
    public Connection getSecurityOfficerServerInTopic() {
        return securityOfficerServerInTopic;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Security Officer.
     *
     * @param securityOfficerServerInTopic  Connection for In Topic
     */
    public void setSecurityOfficerServerInTopic(Connection securityOfficerServerInTopic) {
        this.securityOfficerServerInTopic = securityOfficerServerInTopic;
    }

    /**
     * Return the Security Officer Out Topic Name
     *
     * @return String security out topic name
     */
    public String getSecurityOfficerServerOutTopicName() {
        return securityOfficerServerOutTopicName;
    }

    /**
     * Set up the Security Officer Server Out Name
     * @param securityOfficerServerOutTopicName String Security Out Topic Name
     */
    public void setSecurityOfficerServerOutTopicName(String securityOfficerServerOutTopicName) {
        this.securityOfficerServerOutTopicName = securityOfficerServerOutTopicName;
    }

    /**
     * Return the OCF Connection for the topic used to pass requests to Security Officer.
     * The default values are constructed from the Security Officer Server Name.
     *
     * @return Connection for Out Topic
     */
    public Connection getSecurityOfficerServerOutTopic() {
        return securityOfficerServerOutTopic;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Security Officer.
     *
     * @param securityOfficerServerOutTopic Connection for Out Topic
     */
    public void setSecurityOfficerServerOutTopic(Connection securityOfficerServerOutTopic) {
        this.securityOfficerServerOutTopic = securityOfficerServerOutTopic;
    }

    @Override
    public String toString() {
        return "SecurityOfficerConfig{" +
                "securityOfficerServiceCode=" + securityOfficerServiceCode +
                ", securityOfficerServiceName='" + securityOfficerServiceName + '\'' +
                ", securityOfficerServiceDescription='" + securityOfficerServiceDescription + '\'' +
                ", securityOfficerServerWiki='" + securityOfficerServerWiki + '\'' +
                ", accessServiceRootURL='" + accessServiceRootURL + '\'' +
                ", accessServiceServerName='" + accessServiceServerName + '\'' +
                ", securityOfficerServerInTopicName='" + securityOfficerServerInTopicName + '\'' +
                ", securityOfficerServerInTopic=" + securityOfficerServerInTopic +
                ", securityOfficerServerOutTopicName='" + securityOfficerServerOutTopicName + '\'' +
                ", securityOfficerServerOutTopic=" + securityOfficerServerOutTopic +
                '}';
    }

    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param o object to compare
     * @return boolean result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityOfficerConfig that = (SecurityOfficerConfig) o;
        return securityOfficerServiceCode == that.securityOfficerServiceCode &&
                Objects.equals(securityOfficerServiceName, that.securityOfficerServiceName) &&
                Objects.equals(securityOfficerServiceDescription, that.securityOfficerServiceDescription) &&
                Objects.equals(securityOfficerServerWiki, that.securityOfficerServerWiki) &&
                Objects.equals(accessServiceRootURL, that.accessServiceRootURL) &&
                Objects.equals(accessServiceServerName, that.accessServiceServerName) &&
                Objects.equals(securityOfficerServerInTopicName, that.securityOfficerServerInTopicName) &&
                Objects.equals(securityOfficerServerInTopic, that.securityOfficerServerInTopic) &&
                Objects.equals(securityOfficerServerOutTopicName, that.securityOfficerServerOutTopicName) &&
                Objects.equals(securityOfficerServerOutTopic, that.securityOfficerServerOutTopic);
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(securityOfficerServiceCode, securityOfficerServiceName, securityOfficerServiceDescription,
                securityOfficerServerWiki, accessServiceRootURL, accessServiceServerName, securityOfficerServerInTopicName,
                securityOfficerServerInTopic, securityOfficerServerOutTopicName, securityOfficerServerOutTopic);
    }
}