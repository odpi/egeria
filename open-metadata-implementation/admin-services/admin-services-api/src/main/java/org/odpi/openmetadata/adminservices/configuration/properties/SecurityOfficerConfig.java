/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityOfficerConfig provides the properties for the deprecated security-officer-services.  This function
 * is replaced by the new services in the Engine Host OMAG Server and Integration Daemon OMAG Server.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityOfficerConfig extends AdminServicesConfigHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String      accessServiceRootURL;
    private String      accessServiceServerName;

    private String      securityOfficerServerInTopicName;
    private Connection  securityOfficerServerInTopic;
    private String      securityOfficerServerOutTopicName;
    private Connection  securityOfficerServerOutTopic;

    /**
     * Default constructor
     */
    public SecurityOfficerConfig() {
        super();
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
                " accessServiceRootURL='" + accessServiceRootURL + '\'' +
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
        return Objects.equals(accessServiceRootURL, that.accessServiceRootURL) &&
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
        return Objects.hash(accessServiceRootURL, accessServiceServerName, securityOfficerServerInTopicName,
                securityOfficerServerInTopic, securityOfficerServerOutTopicName, securityOfficerServerOutTopic);
    }
}