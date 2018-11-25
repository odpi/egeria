/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import java.io.Serializable;

/**
 * AccessServiceRegistration is used by an access service to register its admin services interface
 */
public class AccessServiceRegistration implements Serializable
{
    private static final long     serialVersionUID    = 1L;
    private static final String   defaultTopicRoot    = "open-metadata.access-services.";
    private static final String   defaultInTopicLeaf  = ".inTopic";
    private static final String   defaultOutTopicLeaf = ".outTopic";


    private int                            accessServiceCode;
    private String                         accessServiceName;
    private String                         accessServiceDescription;
    private String                         accessServiceWiki;
    private AccessServiceOperationalStatus accessServiceOperationalStatus;
    private String                         accessServiceAdminClassName;

    /**
     * Complete Constructor
     *
     * @param accessServiceCode ordinal for this access service
     * @param accessServiceName symbolic name for this access service
     * @param accessServiceDescription short description for this access service
     * @param accessServiceWiki wiki page for the access service for this access service
     * @param accessServiceOperationalStatus default initial operational status for the access service
     * @param accessServiceAdminClassName  name of AccessServiceAdmin implementation class for the access service
     */
    public AccessServiceRegistration(int                            accessServiceCode,
                                     String                         accessServiceName,
                                     String                         accessServiceDescription,
                                     String                         accessServiceWiki,
                                     AccessServiceOperationalStatus accessServiceOperationalStatus,
                                     String                         accessServiceAdminClassName)
    {
        /*
         * Save the values supplied
         */
        this.accessServiceCode = accessServiceCode;
        this.accessServiceName = accessServiceName;
        this.accessServiceDescription = accessServiceDescription;
        this.accessServiceWiki = accessServiceWiki;
        this.accessServiceOperationalStatus = accessServiceOperationalStatus;
        this.accessServiceAdminClassName = accessServiceAdminClassName;
    }


    /**
     * Default constructor
     */
    public AccessServiceRegistration()
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
     * @return String URL name for the wiki page
     */
    public String getAccessServiceWiki()
    {
        return accessServiceWiki;
    }


    /**
     * Set up the URL for the wiki page describing this access service.
     *
     * @param accessServiceWiki  String URL name for the wiki page
     */
    public void setAccessServiceWiki(String accessServiceWiki)
    {
        this.accessServiceWiki = accessServiceWiki;
    }


    /**
     * Return the initial operational status for this access service.
     *
     * @return AccessServiceOperationalStatus enum
     */
    public AccessServiceOperationalStatus getAccessServiceOperationalStatus()
    {
        return accessServiceOperationalStatus;
    }


    /**
     * Set up the initial operational status for this access service.
     *
     * @param accessServiceOperationalStatus AccessServiceOperationalStatus enum
     */
    public void setAccessServiceOperationalStatus(AccessServiceOperationalStatus accessServiceOperationalStatus)
    {
        this.accessServiceOperationalStatus = accessServiceOperationalStatus;
    }

    /**
     * Return the class name of the adminservices class that should be called during initialization and
     * termination.
     *
     * @return class name
     */
    public String getAccessServiceAdminClassName()
    {
        return accessServiceAdminClassName;
    }


    /**
     * Set up the class name of the adminservices class that should be called during initialization and
     * termination.
     *
     * @param accessServiceAdminClassName  class name
     */
    public void setAccessServiceAdminClassName(String accessServiceAdminClassName)
    {
        this.accessServiceAdminClassName = accessServiceAdminClassName;
    }


    /**
     * Return the InTopic name for the access service.
     *
     * @return String topic name
     */
    public String getAccessServiceInTopic()
    {
        return defaultTopicRoot + accessServiceName + defaultInTopicLeaf;
    }


    /**
     * Return the OutTopic name for the access service.
     *
     * @return String topic name
     */
    public String getAccessServiceOutTopic()
    {
        return defaultTopicRoot + accessServiceName + defaultOutTopicLeaf;
    }

}
