/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AccessServiceConfig provides the configuration for a single Open Metadata Access Service (OMAS).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccessServiceConfig extends AdminServicesConfigHeader
{
    private int                        accessServiceId                = 0;
    private ComponentDevelopmentStatus accessServiceDevelopmentStatus = null;
    private String                     accessServiceAdminClass        = null;
    private String                     accessServiceName              = null;
    private String                     accessServiceFullName          = null;
    private String                     accessServiceURLMarker         = null;
    private String                     accessServiceDescription       = null;
    private String                     accessServiceWiki              = null;
    private ServiceOperationalStatus   accessServiceOperationalStatus = null;
    private Connection                 accessServiceInTopic           = null;
    private Connection                 accessServiceOutTopic          = null;
    private Map<String, Object>        accessServiceOptions           = null;


    /**
     * Default constructor for use with Jackson libraries
     */
    public AccessServiceConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AccessServiceConfig(AccessServiceConfig  template)
    {
        super(template);

        if (template != null)
        {
            accessServiceId = template.getAccessServiceId();
            accessServiceDevelopmentStatus = template.getAccessServiceDevelopmentStatus();
            accessServiceAdminClass = template.getAccessServiceAdminClass();
            accessServiceName = template.getAccessServiceName();
            accessServiceFullName = template.getAccessServiceFullName();
            accessServiceDescription = template.getAccessServiceDescription();
            accessServiceURLMarker = template.getAccessServiceURLMarker();
            accessServiceWiki = template.getAccessServiceWiki();
            accessServiceOperationalStatus = template.getAccessServiceOperationalStatus();
            accessServiceInTopic = template.getAccessServiceInTopic();
            accessServiceOutTopic = template.getAccessServiceOutTopic();
            accessServiceOptions = template.getAccessServiceOptions();
        }
    }



    /**
     * Set up the default values for an access service using an access service description.
     *
     * @param accessServiceRegistration fixed properties about the access service
     */
    public AccessServiceConfig(AccessServiceRegistrationEntry accessServiceRegistration)
    {
        this.accessServiceId = accessServiceRegistration.getAccessServiceCode();
        this.accessServiceDevelopmentStatus = accessServiceRegistration.getAccessServiceDevelopmentStatus();
        this.accessServiceName = accessServiceRegistration.getAccessServiceName();
        this.accessServiceFullName = accessServiceRegistration.getAccessServiceFullName();
        this.accessServiceURLMarker = accessServiceRegistration.getAccessServiceURLMarker();
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
     * Return the development status of the service.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getAccessServiceDevelopmentStatus()
    {
        return accessServiceDevelopmentStatus;
    }


    /**
     * Set up the development status of the service.
     *
     * @param accessServiceDevelopmentStatus  enum describing the status
     */
    public void setAccessServiceDevelopmentStatus(ComponentDevelopmentStatus accessServiceDevelopmentStatus)
    {
        this.accessServiceDevelopmentStatus = accessServiceDevelopmentStatus;
    }



    /**
     * Return the Java class name of the admin-services interface for this access service.
     *
     * @return String class name implementing the
     * AccessServiceAdmin interface.
     */
    public String getAccessServiceAdminClass()
    {
        return accessServiceAdminClass;
    }


    /**
     * Set up the Java class name of the admin services interface for this access service.
     *
     * @param accessServiceAdminClass String class name implementing the
     * AccessServiceAdmin interface.
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
     * Set up the full name of the access service.
     *
     * @param accessServiceFullName String name
     */
    public void setAccessServiceFullName(String accessServiceFullName)
    {
        this.accessServiceFullName = accessServiceFullName;
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
     * Return the full name of the access service.
     *
     * @return String name
     */
    public String getAccessServiceFullName()
    {
        if (accessServiceFullName == null)
        {
            if (accessServiceName == null)
            {
                return null;
            }

            return accessServiceName + " OMAS";
        }
        else
        {
            return accessServiceFullName;
        }
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getAccessServiceURLMarker()
    {
        return accessServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param accessServiceURLMarker url fragment
     */
    public void setAccessServiceURLMarker(String accessServiceURLMarker)
    {
        this.accessServiceURLMarker = accessServiceURLMarker;
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
     * Return the wiki page link for the access service.
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
    public ServiceOperationalStatus getAccessServiceOperationalStatus()
    {
        return accessServiceOperationalStatus;
    }


    /**
     * Set up the status of the access service.
     *
     * @param accessServiceOperationalStatus AccessServiceOperationalStatus enum
     */
    public void setAccessServiceOperationalStatus(ServiceOperationalStatus accessServiceOperationalStatus)
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


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "AccessServiceConfig{" +
                       "accessServiceId=" + accessServiceId +
                       ", accessServiceDevelopmentStatus=" + accessServiceDevelopmentStatus +
                       ", accessServiceAdminClass='" + accessServiceAdminClass + '\'' +
                       ", accessServiceName='" + accessServiceName + '\'' +
                       ", accessServiceFullName='" + accessServiceFullName + '\'' +
                       ", accessServiceURLMarker='" + accessServiceURLMarker + '\'' +
                       ", accessServiceDescription='" + accessServiceDescription + '\'' +
                       ", accessServiceWiki='" + accessServiceWiki + '\'' +
                       ", accessServiceOperationalStatus=" + accessServiceOperationalStatus +
                       ", accessServiceInTopic=" + accessServiceInTopic +
                       ", accessServiceOutTopic=" + accessServiceOutTopic +
                       ", accessServiceOptions=" + accessServiceOptions +
                       '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        AccessServiceConfig that = (AccessServiceConfig) objectToCompare;
        return accessServiceId == that.accessServiceId &&
                       accessServiceDevelopmentStatus == that.accessServiceDevelopmentStatus &&
                       Objects.equals(accessServiceAdminClass, that.accessServiceAdminClass) &&
                       Objects.equals(accessServiceName, that.accessServiceName) &&
                       Objects.equals(accessServiceFullName, that.accessServiceFullName) &&
                       Objects.equals(accessServiceURLMarker, that.accessServiceURLMarker) &&
                       Objects.equals(accessServiceDescription, that.accessServiceDescription) &&
                       Objects.equals(accessServiceWiki, that.accessServiceWiki) &&
                       accessServiceOperationalStatus == that.accessServiceOperationalStatus &&
                       Objects.equals(accessServiceInTopic, that.accessServiceInTopic) &&
                       Objects.equals(accessServiceOutTopic, that.accessServiceOutTopic) &&
                       Objects.equals(accessServiceOptions, that.accessServiceOptions);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(accessServiceId, accessServiceDevelopmentStatus, accessServiceAdminClass, accessServiceName, accessServiceFullName,
                            accessServiceURLMarker, accessServiceDescription, accessServiceWiki, accessServiceOperationalStatus, accessServiceInTopic,
                            accessServiceOutTopic, accessServiceOptions);
    }
}
