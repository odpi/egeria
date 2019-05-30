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

    private int        securityOfficerServerId = 0;
    private String     securityOfficerServerName;
    private String     securityOfficerServerDescription;
    private String     securityOfficerServerWiki;

    private String     securityOfficerOMASURL;
    private String     securityOfficerOMASServerName;
    private String     securityOfficerOMASUsername;
    private Connection securityOfficerConnection;

    private String     securityOfficerServerInTopicName;
    private Connection securityOfficerServerInTopic;
    private String     securityOfficerServerOutTopicName;
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
    public int getSecurityOfficerServerId() {
        return securityOfficerServerId;
    }

    /**
     * Set up the code number (ordinal) for the Security Officer Server
     *
     * @param securityOfficerServerId int ordinal
     */
    public void setSecurityOfficerServerId(int securityOfficerServerId) {
        this.securityOfficerServerId = securityOfficerServerId;
    }

    /**
     *  Return the name of the Security Officer Server
     *
     * @return the name of the security Officer Server
     */
    public String getSecurityOfficerServerName() {
        return securityOfficerServerName;
    }

    /**
     * Set up the name of the Security Officer Server Name
     *
     * @param securityOfficerServerName server name
     */
    public void setSecurityOfficerServerName(String securityOfficerServerName) {
        this.securityOfficerServerName = securityOfficerServerName;
    }

    /**
     * Return the short description of the Security Officer Server Component.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getSecurityOfficerServerDescription() {
        return securityOfficerServerDescription;
    }

    /**
     * Set up the short description of the Security Officer Server Component.  The default value is in English but this can be changed.
     *
     * @param  securityOfficerServerDescription String securityOfficerServerDescription
     */
    public void setSecurityOfficerServerDescription(String securityOfficerServerDescription) {
        this.securityOfficerServerDescription = securityOfficerServerDescription;
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
    public String getSecurityOfficerOMASURL() {
        return securityOfficerOMASURL;
    }

    /**
     * Set up the URL for the Security Officer OMAS connected
     * @param securityOfficerOMASURL ULR for the Security Officer OMAS instance
     */
    public void setSecurityOfficerOMASURL(String securityOfficerOMASURL) {
        this.securityOfficerOMASURL = securityOfficerOMASURL;
    }

    /**
     * Return the name of the server where Security Officer OMAS is running
     * @return the server name
     */
    public String getSecurityOfficerOMASServerName() {
        return securityOfficerOMASServerName;
    }

    /**
     * Set up the name of the server where the instance of the Security Officer OMAS is running
     * @param securityOfficerOMASServerName name of the server for Security Officer OMAS
     */
    public void setSecurityOfficerOMASServerName(String securityOfficerOMASServerName) {
        this.securityOfficerOMASServerName = securityOfficerOMASServerName;
    }

    /**
     *  Returns the username used to connect to the instance of Security Officer OMAS
     * @return username
     */
    public String getSecurityOfficerOMASUsername() {
        return securityOfficerOMASUsername;
    }

    /**
     * Set up the username for connected instance of Security Officer OMAS
     * @param securityOfficerOMASUsername username of the Security Officer OMAS
     */
    public void setSecurityOfficerOMASUsername(String securityOfficerOMASUsername) {
        this.securityOfficerOMASUsername = securityOfficerOMASUsername;
    }


    /**
     * Return the OCF Connection for the topic used to pass requests to Security Officer Connector.
     * The default values are constructed from the Security Officer Server Name.
     *
     * @return Connection for Securituy Officer OMAS
     */
    public Connection getSecurityOfficerConnection() {
        return securityOfficerConnection;
    }

    /**
     * Set up the OCF Connection used to pass requests to this Security Officer Connector.
     *
     * @param securityOfficerConnection Connection for the Security Officer Connector
     */
    public void setSecurityOfficerConnection(Connection securityOfficerConnection) {
        this.securityOfficerConnection = securityOfficerConnection;
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
                "securityOfficerServerId=" + securityOfficerServerId +
                ", securityOfficerServerName='" + securityOfficerServerName + '\'' +
                ", securityOfficerServerDescription='" + securityOfficerServerDescription + '\'' +
                ", securityOfficerServerWiki='" + securityOfficerServerWiki + '\'' +
                ", securityOfficerOMASURL='" + securityOfficerOMASURL + '\'' +
                ", securityOfficerConnection='" + securityOfficerConnection + '\'' +
                ", securityOfficerOMASUsername='" + securityOfficerOMASUsername + '\'' +
                ", securityOfficerOMASServerName='" + securityOfficerOMASServerName + '\'' +
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
        return securityOfficerServerId == that.securityOfficerServerId &&
                Objects.equals(securityOfficerServerName, that.securityOfficerServerName) &&
                Objects.equals(securityOfficerServerDescription, that.securityOfficerServerDescription) &&
                Objects.equals(securityOfficerServerWiki, that.securityOfficerServerWiki) &&
                Objects.equals(securityOfficerOMASURL, that.securityOfficerOMASURL) &&
                Objects.equals(securityOfficerOMASUsername, that.securityOfficerOMASUsername) &&
                Objects.equals(securityOfficerConnection, that.securityOfficerConnection) &&
                Objects.equals(securityOfficerOMASServerName, that.securityOfficerOMASServerName) &&
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
        return Objects.hash(securityOfficerServerId, securityOfficerServerName, securityOfficerServerDescription, securityOfficerServerWiki,
                securityOfficerOMASURL, securityOfficerOMASUsername, securityOfficerOMASServerName, securityOfficerConnection,
                securityOfficerServerInTopicName, securityOfficerServerInTopic, securityOfficerServerOutTopicName, securityOfficerServerOutTopic);
    }
}