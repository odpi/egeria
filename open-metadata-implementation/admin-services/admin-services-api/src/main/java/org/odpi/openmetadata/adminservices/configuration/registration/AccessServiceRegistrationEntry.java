/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

/**
 * AccessServiceRegistrationEntry is used by an access service to register its admin services interface.
 * The registration is dynamic because access services from third parties can be written and run in the
 * OMAGServerPlatform.
 */
public class AccessServiceRegistrationEntry
{
    private static final String   defaultTopicRoot    = "omas.";
    private static final String   defaultOutTopicLeaf = ".outTopic";


    private int                        accessServiceCode;
    private ComponentDevelopmentStatus accessServiceDevelopmentStatus = null;
    private String                     accessServiceName;
    private String                     accessServiceURLMarker;
    private String                     accessServiceDescription;
    private String                     accessServiceWiki;
    private ServiceOperationalStatus   accessServiceOperationalStatus;
    private String                     accessServiceAdminClassName;

    private ServiceOperationalStatus   accessServiceOutTopicStatus = ServiceOperationalStatus.NOT_IMPLEMENTED;


    /**
     * Complete Constructor
     *
     * @param accessServiceCode ordinal for this access service
     * @param accessServiceDevelopmentStatus development status
     * @param accessServiceName symbolic name for this access service
     * @param accessServiceURLMarker name of the part of the URL that is the name of the access service
     * @param accessServiceDescription short description for this access service
     * @param accessServiceWiki wiki page for the access service for this access service
     * @param accessServiceOutTopicStatus is the access service outTopic implemented, operational or disabled?
     * @param accessServiceOperationalStatus default initial operational status for the access service
     * @param accessServiceAdminClassName  name of AccessServiceAdmin implementation class for the access service
     */
    public AccessServiceRegistrationEntry(int                        accessServiceCode,
                                          ComponentDevelopmentStatus accessServiceDevelopmentStatus,
                                          String                     accessServiceName,
                                          String                     accessServiceURLMarker,
                                          String                     accessServiceDescription,
                                          String                     accessServiceWiki,
                                          ServiceOperationalStatus   accessServiceOutTopicStatus,
                                          ServiceOperationalStatus   accessServiceOperationalStatus,
                                          String                     accessServiceAdminClassName)
    {
        this.accessServiceCode = accessServiceCode;
        this.accessServiceDevelopmentStatus = accessServiceDevelopmentStatus;
        this.accessServiceName = accessServiceName;
        this.accessServiceURLMarker = accessServiceURLMarker;
        this.accessServiceDescription = accessServiceDescription;
        this.accessServiceWiki = accessServiceWiki;
        this.accessServiceOutTopicStatus = accessServiceOutTopicStatus;
        this.accessServiceOperationalStatus = accessServiceOperationalStatus;
        this.accessServiceAdminClassName = accessServiceAdminClassName;
    }


    /**
     * Enum Constructor
     *
     * @param accessServiceDescription enum for this access service
     * @param accessServiceOperationalStatus default initial operational status for the access service
     * @param accessServiceAdminClassName  name of AccessServiceAdmin implementation class for the access service
     */
    public AccessServiceRegistrationEntry(AccessServiceDescription accessServiceDescription,
                                          ServiceOperationalStatus accessServiceOperationalStatus,
                                          String                   accessServiceAdminClassName)
    {
        this(accessServiceDescription.getServiceCode(),
             accessServiceDescription.getServiceDevelopmentStatus(),
             accessServiceDescription.getServiceName(),
             accessServiceDescription.getServiceURLMarker(),
             accessServiceDescription.getServiceDescription(),
             accessServiceDescription.getServiceWiki(),
             accessServiceDescription.getOutTopicStatus(),
             accessServiceOperationalStatus,
             accessServiceAdminClassName);
    }


    /**
     * Default constructor
     */
    public AccessServiceRegistrationEntry()
    {
    }


    /**
     * Return the code for this access service
     *
     * @return int type code
     */
    public int getAccessServiceCode()
    {
        return accessServiceCode;
    }


    /**
     * Set up the code for this access service
     *
     * @param accessServiceCode  int type code
     */
    public void setAccessServiceCode(int accessServiceCode)
    {
        this.accessServiceCode = accessServiceCode;
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
     * Return the default name for this access service.
     *
     * @return String default name
     */
    public String getAccessServiceName()
    {
        return accessServiceName;
    }


    /**
     * Set up the default name for this access service.
     *
     * @param accessServiceName  String default name
     */
    public void setAccessServiceName(String accessServiceName)
    {
        this.accessServiceName = accessServiceName;
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
    public void setServiceURLMarker(String accessServiceURLMarker)
    {
        this.accessServiceURLMarker = accessServiceURLMarker;
    }


    /**
     * Return the default description for the type for this access service.
     *
     * @return String default description
     */
    public String getAccessServiceDescription()
    {
        return accessServiceDescription;
    }


    /**
     * Set up the default description for the type for this access service.
     *
     * @param accessServiceDescription  String default description
     */
    public void setAccessServiceDescription(String accessServiceDescription)
    {
        this.accessServiceDescription = accessServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this access service.
     *
     * @return String URL for the wiki page
     */
    public String getAccessServiceWiki()
    {
        return accessServiceWiki;
    }


    /**
     * Set up the URL for the wiki page describing this access service.
     *
     * @param accessServiceWiki  String URL for the wiki page
     */
    public void setAccessServiceWiki(String accessServiceWiki)
    {
        this.accessServiceWiki = accessServiceWiki;
    }


    /**
     * Return the initial operational status for this access service.
     *
     * @return ServiceOperationalStatus enum
     */
    public ServiceOperationalStatus getAccessServiceOperationalStatus()
    {
        return accessServiceOperationalStatus;
    }


    /**
     * Set up the initial operational status for this access service.
     *
     * @param accessServiceOperationalStatus ServiceOperationalStatus enum
     */
    public void setAccessServiceOperationalStatus(ServiceOperationalStatus accessServiceOperationalStatus)
    {
        this.accessServiceOperationalStatus = accessServiceOperationalStatus;
    }


    /**
     * Return the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @return class name
     */
    public String getAccessServiceAdminClassName()
    {
        return accessServiceAdminClassName;
    }


    /**
     * Set up the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @param accessServiceAdminClassName  class name
     */
    public void setAccessServiceAdminClassName(String accessServiceAdminClassName)
    {
        this.accessServiceAdminClassName = accessServiceAdminClassName;
    }



    /**
     * Return the OutTopic name for the access service.  Null means that the topic is either not implemented
     * or not enabled.
     *
     * @return String topic name
     */
    public String getAccessServiceOutTopic()
    {
        if (accessServiceOutTopicStatus == ServiceOperationalStatus.ENABLED)
        {
            return defaultTopicRoot + accessServiceURLMarker.replaceAll("-", "") + defaultOutTopicLeaf;
        }

        return null;
    }
}
