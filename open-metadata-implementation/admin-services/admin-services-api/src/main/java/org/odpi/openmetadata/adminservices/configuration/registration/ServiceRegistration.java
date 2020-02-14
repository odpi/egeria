/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import java.io.Serializable;

/**
 * ServiceRegistration is used by a service to register its admin services interface.
 */
public class ServiceRegistration implements Serializable
{
    private static final long     serialVersionUID    = 1L;
    private static final String   defaultTopicRoot    = "omas.";
    private static final String   defaultInTopicLeaf  = ".inTopic";
    private static final String   defaultOutTopicLeaf = ".outTopic";


    private int                            serviceCode;
    private String                         serviceName;
    private String                         serviceFullName;
    private String                         serviceURLMarker;
    private String                         serviceDescription;
    private String                         serviceWiki;
    private ServiceOperationalStatus       serviceOperationalStatus;
    private String                         serviceAdminClassName;

    /**
     * Complete Constructor
     *
     * @param serviceCode ordinal for this access service
     * @param serviceName symbolic name for this access service
     * @param serviceURLMarker name of the part of the URL that is the name of the access service
     * @param serviceDescription short description for this access service
     * @param serviceWiki wiki page for the access service for this access service
     * @param serviceOperationalStatus default initial operational status for the access service
     * @param serviceAdminClassName  name of serviceAdmin implementation class for the access service
     */
    public ServiceRegistration(int                            serviceCode,
                                     String                         serviceName,
                                     String                         serviceFullName,
                                     String                         serviceURLMarker,
                                     String                         serviceDescription,
                                     String                         serviceWiki,
                                     ServiceOperationalStatus       serviceOperationalStatus,
                                     String                         serviceAdminClassName)
    {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.serviceFullName = serviceFullName;
        this.serviceURLMarker = serviceURLMarker;
        this.serviceDescription = serviceDescription;
        this.serviceWiki = serviceWiki;
        this.serviceOperationalStatus = serviceOperationalStatus;
        this.serviceAdminClassName = serviceAdminClassName;
    }


    /**
     * Access Service Enum Constructor
     *
     * @param accessServiceDescription enum for this access service
     * @param accessServiceOperationalStatus default initial operational status for the access service
     * @param accessServiceAdminClassName  name of AccessServiceAdmin implementation class for the access service
     */
    public ServiceRegistration(AccessServiceDescription       accessServiceDescription,
                               ServiceOperationalStatus       accessServiceOperationalStatus,
                               String                         accessServiceAdminClassName)
    {
        this(accessServiceDescription.getAccessServiceCode(),
             accessServiceDescription.getAccessServiceName(),
             accessServiceDescription.getAccessServiceFullName(),
             accessServiceDescription.getAccessServiceURLMarker(),
             accessServiceDescription.getAccessServiceDescription(),
             accessServiceDescription.getAccessServiceWiki(),
             accessServiceOperationalStatus,
             accessServiceAdminClassName);
    }

    /**
     * View Service Enum Constructor
     *
     * @param viewServiceDescription enum for this view service
     * @param viewServiceOperationalStatus default initial operational status for the view service
     * @param viewServiceAdminClassName  name of viewServiceAdmin implementation class for the view service
     */
    public ServiceRegistration(ViewServiceDescription      viewServiceDescription,
                               ServiceOperationalStatus viewServiceOperationalStatus,
                               String                         viewServiceAdminClassName)
    {
        this(viewServiceDescription.getViewServiceCode(),
                viewServiceDescription.getViewServiceName(),
                viewServiceDescription.getViewServiceFullName(),
                viewServiceDescription.getViewServiceURLMarker(),
                viewServiceDescription.getViewServiceDescription(),
                viewServiceDescription.getViewServiceWiki(),
                viewServiceOperationalStatus,
                viewServiceAdminClassName);
    }


    /**
     * Default constructor
     */
    public ServiceRegistration()
    {
    }

    /**
     * Return the code for this  service
     *
     * @return int type code
     */
    public int getServiceCode()
    {
        return serviceCode;
    }


    /**
     * Set up the code for this  service
     *
     * @param serviceCode  int type code
     */
    public void setserviceCode(int serviceCode)
    {
        this.serviceCode = serviceCode;
    }


    /**
     * Return the default name for this  service.
     *
     * @return String default name
     */
    public String getServiceName()
    {
        return serviceName;
    }



    /**
     * Set up the default name for this  service.
     *
     * @param serviceName  String default name
     */
    public void setserviceName(String serviceName)
    {
        this.serviceName = serviceName;
    }


    /**
     * Return the full name for this  service.
     *
     * @return String default name
     */
    public String getServiceFullName()
    {
        return serviceFullName;
    }



    /**
     * Set up the full name for this  service.
     *
     * @param serviceFullName  String default name
     */
    public void setserviceFullName(String serviceFullName)
    {
        this.serviceFullName = serviceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getServiceURLMarker()
    {
        return serviceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param serviceURLMarker url fragment
     */
    public void setServiceURLMarker(String serviceURLMarker)
    {
        this.serviceURLMarker = serviceURLMarker;
    }


    /**
     * Return the default description for the type for this  service.
     *
     * @return String default description
     */
    public String getServiceDescription()
    {
        return serviceDescription;
    }


    /**
     * Set up the default description for the type for this  service.
     *
     * @param serviceDescription  String default description
     */
    public void setserviceDescription(String serviceDescription)
    {
        this.serviceDescription = serviceDescription;
    }


    /**
     * Return the URL for the wiki page describing this  service.
     *
     * @return String URL name for the wiki page
     */
    public String getServiceWiki()
    {
        return serviceWiki;
    }


    /**
     * Set up the URL for the wiki page describing this  service.
     *
     * @param serviceWiki  String URL name for the wiki page
     */
    public void setserviceWiki(String serviceWiki)
    {
        this.serviceWiki = serviceWiki;
    }


    /**
     * Return the initial operational status for this  service.
     *
     * @return serviceOperationalStatus enum
     */
    public ServiceOperationalStatus getServiceOperationalStatus()
    {
        return serviceOperationalStatus;
    }


    /**
     * Set up the initial operational status for this  service.
     *
     * @param serviceOperationalStatus serviceOperationalStatus enum
     */
    public void setserviceOperationalStatus(ServiceOperationalStatus serviceOperationalStatus)
    {
        this.serviceOperationalStatus = serviceOperationalStatus;
    }

    /**
     * Return the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @return class name
     */
    public String getServiceAdminClassName()
    {
        return serviceAdminClassName;
    }


    /**
     * Set up the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @param serviceAdminClassName  class name
     */
    public void setserviceAdminClassName(String serviceAdminClassName)
    {
        this.serviceAdminClassName = serviceAdminClassName;
    }


    /**
     * Return the InTopic name for the  service.
     *
     * @return String topic name
     */
    public String getServiceInTopic()
    {
        return defaultTopicRoot + serviceURLMarker.replaceAll("-", "") + defaultInTopicLeaf;
    }


    /**
     * Return the OutTopic name for the  service.
     *
     * @return String topic name
     */
    public String getServiceOutTopic()
    {
        return defaultTopicRoot + serviceURLMarker.replaceAll("-", "") + defaultOutTopicLeaf;
    }

}
