/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * ViewServiceRegistrationEntry is used by a view service to register its admin services interface.
 */
public class ViewServiceRegistrationEntry implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int                            viewServiceCode;
    private ComponentDevelopmentStatus     viewServiceDevelopmentStatus;
    private String                         viewServiceName;
    private String                         viewServiceFullName;
    private String                         viewServiceURLMarker;
    private String                         viewServiceDescription;
    private String                         viewServiceWiki;
    private ServiceOperationalStatus       viewServiceOperationalStatus;
    private String                         viewServiceAdminClassName;

    /**
     * Complete Constructor
     *
     * @param viewServiceCode ordinal for this view service
     * @param viewServiceDevelopmentStatus development status
     * @param viewServiceName symbolic name for this view service
     * @param viewServiceFullName full name for this view service
     * @param viewServiceURLMarker name of the part of the URL that is the name of the view service
     * @param viewServiceDescription short description for this view service
     * @param viewServiceWiki wiki page for the view service for this view service
     * @param viewServiceOperationalStatus default initial operational status for the view service
     * @param viewServiceAdminClassName  name of ViewServiceAdmin implementation class for the view service
     */
    public ViewServiceRegistrationEntry(int                            viewServiceCode,
                                        ComponentDevelopmentStatus     viewServiceDevelopmentStatus,
                                        String                         viewServiceName,
                                        String                         viewServiceFullName,
                                        String                         viewServiceURLMarker,
                                        String                         viewServiceDescription,
                                        String                         viewServiceWiki,
                                        ServiceOperationalStatus       viewServiceOperationalStatus,
                                        String                         viewServiceAdminClassName)
    {
        this.viewServiceCode = viewServiceCode;
        this.viewServiceDevelopmentStatus = viewServiceDevelopmentStatus;
        this.viewServiceName = viewServiceName;
        this.viewServiceFullName = viewServiceFullName;
        this.viewServiceURLMarker = viewServiceURLMarker;
        this.viewServiceDescription = viewServiceDescription;
        this.viewServiceWiki = viewServiceWiki;
        this.viewServiceOperationalStatus = viewServiceOperationalStatus;
        this.viewServiceAdminClassName = viewServiceAdminClassName;
    }


    /**
     * Enum Constructor
     *
     * @param viewServiceDescription enum for this view service
     * @param viewServiceOperationalStatus default initial operational status for the view service
     * @param viewServiceAdminClassName  name of ViewServiceAdmin implementation class for the view service
     */
    public ViewServiceRegistrationEntry(ViewServiceDescription   viewServiceDescription,
                                        ServiceOperationalStatus viewServiceOperationalStatus,
                                        String                   viewServiceAdminClassName)
    {
        this(viewServiceDescription.getViewServiceCode(),
             viewServiceDescription.getViewServiceDevelopmentStatus(),
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
    public ViewServiceRegistrationEntry()
    {
    }


    /**
     * Return the code for this view service
     *
     * @return int type code
     */
    public int getViewServiceCode()
    {
        return viewServiceCode;
    }


    /**
     * Set up the code for this view service
     *
     * @param viewServiceCode  int type code
     */
    public void setViewServiceCode(int viewServiceCode)
    {
        this.viewServiceCode = viewServiceCode;
    }


    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getViewServiceDevelopmentStatus()
    {
        return viewServiceDevelopmentStatus;
    }


    /**
     * Set up the development status of the component.
     *
     * @param viewServiceDevelopmentStatus  enum describing the status
     */
    public void setViewServiceDevelopmentStatus(ComponentDevelopmentStatus viewServiceDevelopmentStatus)
    {
        this.viewServiceDevelopmentStatus = viewServiceDevelopmentStatus;
    }


    /**
     * Return the default name for this view service.
     *
     * @return String default name
     */
    public String getViewServiceName()
    {
        return viewServiceName;
    }


    /**
     * Set up the default name for this view service.
     *
     * @param viewServiceName  String default name
     */
    public void setViewServiceName(String viewServiceName)
    {
        this.viewServiceName = viewServiceName;
    }


    /**
     * Return the full name for this view service.
     *
     * @return String default name
     */
    public String getViewServiceFullName()
    {
        return viewServiceFullName;
    }



    /**
     * Set up the full name for this view service.
     *
     * @param viewServiceFullName  String default name
     */
    public void setViewServiceFullName(String viewServiceFullName)
    {
        this.viewServiceFullName = viewServiceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getViewServiceURLMarker()
    {
        return viewServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param viewServiceURLMarker url fragment
     */
    public void setServiceURLMarker(String viewServiceURLMarker)
    {
        this.viewServiceURLMarker = viewServiceURLMarker;
    }


    /**
     * Return the default description for the type for this view service.
     *
     * @return String default description
     */
    public String getViewServiceDescription()
    {
        return viewServiceDescription;
    }


    /**
     * Set up the default description for the type for this view service.
     *
     * @param viewServiceDescription  String default description
     */
    public void setViewServiceDescription(String viewServiceDescription)
    {
        this.viewServiceDescription = viewServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this view service.
     *
     * @return String URL name for the wiki page
     */
    public String getViewServiceWiki()
    {
        return viewServiceWiki;
    }


    /**
     * Set up the URL for the wiki page describing this view service.
     *
     * @param viewServiceWiki  String URL name for the wiki page
     */
    public void setViewServiceWiki(String viewServiceWiki)
    {
        this.viewServiceWiki = viewServiceWiki;
    }


    /**
     * Return the initial operational status for this view service.
     *
     * @return ServiceOperationalStatus enum
     */
    public ServiceOperationalStatus getViewServiceOperationalStatus()
    {
        return viewServiceOperationalStatus;
    }


    /**
     * Set up the initial operational status for this view service.
     *
     * @param viewServiceOperationalStatus ServiceOperationalStatus enum
     */
    public void setViewServiceOperationalStatus(ServiceOperationalStatus viewServiceOperationalStatus)
    {
        this.viewServiceOperationalStatus = viewServiceOperationalStatus;
    }

    /**
     * Return the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @return class name
     */
    public String getViewServiceAdminClassName()
    {
        return viewServiceAdminClassName;
    }


    /**
     * Set up the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @param viewServiceAdminClassName  class name
     */
    public void setViewServiceAdminClassName(String viewServiceAdminClassName)
    {
        this.viewServiceAdminClassName = viewServiceAdminClassName;
    }

}
