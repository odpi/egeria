/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.util.Objects;

/**
 * EngineServiceRegistrationEntry is used by an engine service to register its admin services interface.
 * The registration is dynamic because engine services from third parties can be written and run in the
 * OMAGServerPlatform.
 */
public class EngineServiceRegistrationEntry
{
    private int                        engineServiceId                = 0;
    private ComponentDevelopmentStatus engineServiceDevelopmentStatus = null;
    private String                     engineServiceName              = null;
    private String                     engineServiceFullName          = null;
    private String                     engineServiceURLMarker         = null;
    private String                     engineServiceDescription       = null;
    private String                     engineServiceWiki              = null;
    private String                     engineServicePartnerOMAS       = null;
    private ServiceOperationalStatus   engineServiceOperationalStatus = null;
    private String                     engineServiceAdminClass        = null;
    private String                     hostedGovernanceEngineType     = null;
    private String                     hostedGovernanceServiceType    = null;


    /**
     * Complete Constructor
     *
     * @param engineServiceCode ordinal for this engine service
     * @param engineServiceDevelopmentStatus development status
     * @param engineServiceName symbolic name for this engine service
     * @param engineServiceFullName full name for this engine service
     * @param engineServiceURLMarker name of the part of the URL that is the name of the engine service
     * @param engineServiceDescription short description for this engine service
     * @param engineServiceWiki wiki page for the engine service for this engine service
     * @param engineServicePartnerOMAS full name of the OMAS that this engine service is partnered with
     * @param hostedGovernanceEngineType type of governance engine hosted by this service
     * @param hostedGovernanceServiceType type of governance service hosted by this service
     * @param engineServiceOperationalStatus default initial operational status for the engine service
     * @param engineServiceAdminClassName  name of EngineServiceAdmin implementation class for the engine service
     */
    public EngineServiceRegistrationEntry(int                        engineServiceCode,
                                          ComponentDevelopmentStatus engineServiceDevelopmentStatus,
                                          String                     engineServiceName,
                                          String                     engineServiceFullName,
                                          String                     engineServiceURLMarker,
                                          String                     engineServiceDescription,
                                          String                     engineServiceWiki,
                                          String                     engineServicePartnerOMAS,
                                          String                     hostedGovernanceEngineType,
                                          String                     hostedGovernanceServiceType,
                                          ServiceOperationalStatus   engineServiceOperationalStatus,
                                          String                     engineServiceAdminClassName)
    {
        this.engineServiceId = engineServiceCode;
        this.engineServiceDevelopmentStatus = engineServiceDevelopmentStatus;
        this.engineServiceName = engineServiceName;
        this.engineServiceFullName = engineServiceFullName;
        this.engineServiceURLMarker = engineServiceURLMarker;
        this.engineServiceDescription = engineServiceDescription;
        this.engineServiceWiki = engineServiceWiki;
        this.engineServicePartnerOMAS = engineServicePartnerOMAS;
        this.hostedGovernanceEngineType = hostedGovernanceEngineType;
        this.hostedGovernanceServiceType = hostedGovernanceServiceType;
        this.engineServiceOperationalStatus = engineServiceOperationalStatus;
        this.engineServiceAdminClass = engineServiceAdminClassName;
    }


    /**
     * Enum Constructor
     *
     * @param engineServiceDescription enum for this engine service
     * @param engineServiceOperationalStatus default initial operational status for the engine service
     * @param engineServiceAdminClassName  name of EngineServiceAdmin implementation class for the engine service
     */
    public EngineServiceRegistrationEntry(EngineServiceDescription engineServiceDescription,
                                          ServiceOperationalStatus engineServiceOperationalStatus,
                                          String                   engineServiceAdminClassName)
    {
        this(engineServiceDescription.getEngineServiceCode(),
             engineServiceDescription.getEngineServiceDevelopmentStatus(),
             engineServiceDescription.getEngineServiceName(),
             engineServiceDescription.getEngineServiceFullName(),
             engineServiceDescription.getEngineServiceURLMarker(),
             engineServiceDescription.getEngineServiceDescription(),
             engineServiceDescription.getEngineServiceWiki(),
             engineServiceDescription.getEngineServicePartnerService(),
             engineServiceDescription.getHostedGovernanceEngineType(),
             engineServiceDescription.getHostedGovernanceServiceType(),
             engineServiceOperationalStatus,
             engineServiceAdminClassName);
    }
    
    
    /**
     * Default constructor for use with Jackson libraries
     */
    public EngineServiceRegistrationEntry()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineServiceRegistrationEntry(EngineServiceRegistrationEntry template)
    {
        if (template != null)
        {
            engineServiceId                 = template.getEngineServiceId();
            engineServiceDevelopmentStatus  = template.getEngineServiceDevelopmentStatus();
            engineServiceName               = template.getEngineServiceName();
            engineServiceFullName           = template.getEngineServiceFullName();
            engineServiceURLMarker          = template.getEngineServiceURLMarker();
            engineServiceDescription        = template.getEngineServiceDescription();
            engineServiceWiki               = template.getEngineServiceWiki();
            engineServicePartnerOMAS        = template.getEngineServicePartnerOMAS();
            engineServiceOperationalStatus  = template.getEngineServiceOperationalStatus();
            engineServiceAdminClass         = template.getEngineServiceAdminClass();
            hostedGovernanceEngineType      = template.getHostedGovernanceEngineType();
            hostedGovernanceServiceType     = template.getHostedGovernanceServiceType();
        }
    }


    /**
     * Return the code number (ordinal) for this engine service.
     *
     * @return int ordinal
     */
    public int getEngineServiceId()
    {
        return engineServiceId;
    }


    /**
     * Set up the code number (ordinal) for this engine service.
     *
     * @param engineServiceId int ordinal
     */
    public void setEngineServiceId(int engineServiceId)
    {
        this.engineServiceId = engineServiceId;
    }


    /**
     * Return the development status of the service.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getEngineServiceDevelopmentStatus()
    {
        return engineServiceDevelopmentStatus;
    }


    /**
     * Set up the development status of the service.
     *
     * @param  engineServiceDevelopmentStatus enum describing the status
     */
    public void setEngineServiceDevelopmentStatus(ComponentDevelopmentStatus engineServiceDevelopmentStatus)
    {
        this.engineServiceDevelopmentStatus = engineServiceDevelopmentStatus;
    }


    /**
     * Return the Java class name of the admin-services interface for this engine service.
     *
     * @return String class name implementing the
     * EngineServiceAdmin interface.
     */
    public String getEngineServiceAdminClass()
    {
        return engineServiceAdminClass;
    }


    /**
     * Set up the Java class name of the admin services interface for this engine service.
     *
     * @param engineServiceAdminClass String class name implementing the
     * EngineServiceAdmin interface.
     */
    public void setEngineServiceAdminClass(String engineServiceAdminClass)
    {
        this.engineServiceAdminClass = engineServiceAdminClass;
    }


    /**
     * Return the name of the engine service.
     *
     * @return String name
     */
    public String getEngineServiceName()
    {
        return engineServiceName;
    }


    /**
     * Set up the full name of the engine service.
     *
     * @param engineServiceFullName String name
     */
    public void setEngineServiceFullName(String engineServiceFullName)
    {
        this.engineServiceFullName = engineServiceFullName;
    }


    /**
     * Set up the name of the engine service.
     *
     * @param engineServiceName String name
     */
    public void setEngineServiceName(String engineServiceName)
    {
        this.engineServiceName = engineServiceName;
    }


    /**
     * Return the full name of the engine service.
     *
     * @return String name
     */
    public String getEngineServiceFullName()
    {
        if (engineServiceFullName == null)
        {
            if (engineServiceName == null)
            {
                return null;
            }

            return engineServiceName + " OMES";
        }
        else
        {
            return engineServiceFullName;
        }
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getEngineServiceURLMarker()
    {
        return engineServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param engineServiceURLMarker url fragment
     */
    public void setEngineServiceURLMarker(String engineServiceURLMarker)
    {
        this.engineServiceURLMarker = engineServiceURLMarker;
    }



    /**
     * Return the short description of the engine service.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getEngineServiceDescription()
    {
        return engineServiceDescription;
    }


    /**
     * Set up the short description of the engine service.
     *
     * @param engineServiceDescription String description
     */
    public void setEngineServiceDescription(String engineServiceDescription)
    {
        this.engineServiceDescription = engineServiceDescription;
    }


    /**
     * Return the wiki page link for the engine service.
     *
     * @return String url
     */
    public String getEngineServiceWiki()
    {
        return engineServiceWiki;
    }


    /**
     * Set up the wiki page link for the engine service.
     *
     * @param engineServiceWiki String url
     */
    public void setEngineServiceWiki(String engineServiceWiki)
    {
        this.engineServiceWiki = engineServiceWiki;
    }


    /**
     * Return the full name of the Open Metadata Access Service (OMAS) that this engine service is partnered with.
     *
     * @return Full name of OMAS
     */
    public String getEngineServicePartnerOMAS()
    {
        return engineServicePartnerOMAS;
    }


    /**
     * Set up the full name of the Open Metadata Access Service (OMAS) that this engine service is partnered with.
     *
     * @param engineServicePartnerOMAS Full name of OMAS
     */
    public void setEngineServicePartnerOMAS(String engineServicePartnerOMAS)
    {
        this.engineServicePartnerOMAS = engineServicePartnerOMAS;
    }


    /**
     * Return the status of this engine service.
     *
     * @return ServiceOperationalStatus enum
     */
    public ServiceOperationalStatus getEngineServiceOperationalStatus()
    {
        return engineServiceOperationalStatus;
    }


    /**
     * Set up the status of the engine service.
     *
     * @param engineServiceOperationalStatus ServiceOperationalStatus enum
     */
    public void setEngineServiceOperationalStatus(ServiceOperationalStatus engineServiceOperationalStatus)
    {
        this.engineServiceOperationalStatus = engineServiceOperationalStatus;
    }


    /**
     * Return the open metadata type of governance engine that this engine service supports.
     *
     * @return engine open metadata type name
     */
    public String getHostedGovernanceEngineType()
    {
        return hostedGovernanceEngineType;
    }


    /**
     * Set up the open metadata type of governance engine that this engine service supports.
     *
     * @param hostedGovernanceEngineType open metadata type name
     */
    public void setHostedGovernanceEngineType(String hostedGovernanceEngineType)
    {
        this.hostedGovernanceEngineType = hostedGovernanceEngineType;
    }


    /**
     * Return the open metadata type of governance service that this engine service supports.
     *
     * @return governance service connector open metadata type name
     */
    public String getHostedGovernanceServiceType()
    {
        return hostedGovernanceServiceType;
    }


    /**
     * Set up the open metadata type of governance service that this engine service supports.
     *
     * @param hostedGovernanceServiceType open metadata type name
     */
    public void setHostedGovernanceServiceType(String hostedGovernanceServiceType)
    {
        this.hostedGovernanceServiceType = hostedGovernanceServiceType;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineServiceRegistrationEntry{" +
                       "engineServiceId=" + engineServiceId +
                       ", engineServiceName='" + engineServiceName + '\'' +
                       ", engineServiceFullName='" + engineServiceFullName + '\'' +
                       ", engineServiceURLMarker='" + engineServiceURLMarker + '\'' +
                       ", engineServiceDescription='" + engineServiceDescription + '\'' +
                       ", engineServiceWiki='" + engineServiceWiki + '\'' +
                       ", engineServicePartnerOMAS='" + engineServicePartnerOMAS + '\'' +
                       ", engineServiceOperationalStatus=" + engineServiceOperationalStatus +
                       ", engineServiceAdminClass='" + engineServiceAdminClass + '\'' +
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
        EngineServiceRegistrationEntry that = (EngineServiceRegistrationEntry) objectToCompare;
        return getEngineServiceId() == that.getEngineServiceId() &&
                       Objects.equals(getEngineServiceName(), that.getEngineServiceName()) &&
                       Objects.equals(getEngineServiceFullName(), that.getEngineServiceFullName()) &&
                       Objects.equals(getEngineServiceURLMarker(), that.getEngineServiceURLMarker()) &&
                       Objects.equals(getEngineServiceDescription(), that.getEngineServiceDescription()) &&
                       Objects.equals(getEngineServiceWiki(), that.getEngineServiceWiki()) &&
                       Objects.equals(getEngineServicePartnerOMAS(), that.getEngineServicePartnerOMAS()) &&
                       getEngineServiceOperationalStatus() == that.getEngineServiceOperationalStatus() &&
                       Objects.equals(getEngineServiceAdminClass(), that.getEngineServiceAdminClass());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getEngineServiceId(), getEngineServiceName(), getEngineServiceFullName(), getEngineServiceURLMarker(),
                            getEngineServiceDescription(), getEngineServiceWiki(), getEngineServicePartnerOMAS(), getEngineServiceOperationalStatus(),
                            getEngineServiceAdminClass());
    }
}
