/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AccessServiceConfig provides the configuration for a single Open Metadata Access Service (OMAS).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccessServiceConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int                            accessServiceId                = 0;
    private String                         accessServiceAdminClass        = null;
    private String                         accessServiceName              = null;
    private String                         accessServiceDescription       = null;
    private String                         accessServiceWiki              = null;
    private AccessServiceOperationalStatus accessServiceOperationalStatus = null;
    private Connection                     accessServiceInTopic           = null;
    private Connection                     accessServiceOutTopic          = null;
    private Map<String, Object>            accessServiceOptions           = null;

    /**
     * Default constructor for use with Jackson libraries
     */
    public AccessServiceConfig()
    {
    }


    /**
     * Set up the default values for an access service using an access service description.
     *
     * @param accessServiceRegistration fixed properties about the access service
     */
    public AccessServiceConfig(AccessServiceRegistration accessServiceRegistration)
    {
        this.accessServiceId = accessServiceRegistration.getAccessServiceCode();
        this.accessServiceName = accessServiceRegistration.getAccessServiceName();
        this.accessServiceAdminClass = accessServiceRegistration.getAccessServiceAdminClassName();
        this.accessServiceDescription = accessServiceRegistration.getAccessServiceDescription();
        this.accessServiceWiki = accessServiceRegistration.getAccessServiceWiki();
        this.accessServiceOperationalStatus = accessServiceRegistration.getAccessServiceOperationalStatus();
    }


    /**
     * Return the code number (ordinal) for this access service.
     *
     * @return int ordinal
     */
    public int getAccessServiceId()
    {
        return accessServiceId;
    }


    /**
     * Set up the code number (ordinal) for this access service.
     *
     * @param accessServiceId int ordinal
     */
    public void setAccessServiceId(int accessServiceId)
    {
        this.accessServiceId = accessServiceId;
    }


    /**
     * Return the Java class name of the admin-services interface for this access service.
     *
     * @return String class name implementing the
     * org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin interface.
     */
    public String getAccessServiceAdminClass()
    {
        return accessServiceAdminClass;
    }


    /**
     * Set up the Java class name of the admin services interface for this access service.
     *
     * @param accessServiceAdminClass String class name implementing the
     * org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin interface.
     */
    public void setAccessServiceAdminClass(String accessServiceAdminClass)
    {
        this.accessServiceAdminClass = accessServiceAdminClass;
    }


    /**
     * Return the name of the access service.
     *
     * @return String name
     */
    public String getAccessServiceName()
    {
        return accessServiceName;
    }


    /**
     * Set up the name of the access service.
     *
     * @param accessServiceName String name
     */
    public void setAccessServiceName(String accessServiceName)
    {
        this.accessServiceName = accessServiceName;
    }


    /**
     * Return the short description of the access service.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getAccessServiceDescription()
    {
        return accessServiceDescription;
    }


    /**
     * Set up the short description of the access service.
     *
     * @param accessServiceDescription String description
     */
    public void setAccessServiceDescription(String accessServiceDescription)
    {
        this.accessServiceDescription = accessServiceDescription;
    }


    /**
     * Return the wiki page link for the access service.  The default value points to a page on the Atlas
     * confluence wiki.
     *
     * @return String url
     */
    public String getAccessServiceWiki()
    {
        return accessServiceWiki;
    }


    /**
     * Set up the wiki page link for the access service.
     *
     * @param accessServiceWiki String url
     */
    public void setAccessServiceWiki(String accessServiceWiki)
    {
        this.accessServiceWiki = accessServiceWiki;
    }


    /**
     * Return the status of this access service.
     *
     * @return AccessServiceOperationalStatus enum
     */
    public AccessServiceOperationalStatus getAccessServiceOperationalStatus()
    {
        return accessServiceOperationalStatus;
    }


    /**
     * Set up the status of the access service.
     *
     * @param accessServiceOperationalStatus AccessServiceOperationalStatus enum
     */
    public void setAccessServiceOperationalStatus(AccessServiceOperationalStatus accessServiceOperationalStatus)
    {
        this.accessServiceOperationalStatus = accessServiceOperationalStatus;
    }


    /**
     * Return the OCF Connection for the topic used to pass requests to this access service.
     * The default values are constructed from the access service name.
     * If this value is set to null then the access service ignores incoming events.
     *
     * @return Connection for InTopic
     */
    public Connection getAccessServiceInTopic()
    {
        return accessServiceInTopic;
    }


    /**
     * Set up the OCF Connection for the topic used to pass requests to this access service.
     * The default values are constructed from the access service name.
     * If this value is set to null then the access service ignores incoming events.
     *
     * @param accessServiceInTopic Connection properties
     */
    public void setAccessServiceInTopic(Connection accessServiceInTopic)
    {
        this.accessServiceInTopic = accessServiceInTopic;
    }


    /**
     * Return the OCF Connection for the topic used by this access service to publish events.
     * The default values are constructed from the access service name.
     * If this value is set to null then events are not published by this OMAS.
     *
     * @return Connection for OutTopic
     */
    public Connection getAccessServiceOutTopic()
    {
        return accessServiceOutTopic;
    }


    /**
     * Set up the OCF Connection of the topic used by this access service to publish events.
     * The default values are constructed from the access service name.
     * If this value is set to null then events are not published by this OMAS.
     *
     * @param accessServiceOutTopic Connection properties
     */
    public void setAccessServiceOutTopic(Connection accessServiceOutTopic)
    {
        this.accessServiceOutTopic = accessServiceOutTopic;
    }


    /**
     * Return the options for this access service. These are properties that are specific to the access service.
     *
     * @return Map from String to String
     */
    public Map<String, Object> getAccessServiceOptions()
    {
        if (accessServiceOptions == null)
        {
            return null;
        }
        else if (accessServiceOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return accessServiceOptions;
        }
    }


    /**
     * Set up the options for this access service.  These are properties that are specific to the access service.
     *
     * @param accessServiceOptions Map from String to String
     */
    public void setAccessServiceOptions(Map<String, Object> accessServiceOptions)
    {
        this.accessServiceOptions = accessServiceOptions;
    }
}
