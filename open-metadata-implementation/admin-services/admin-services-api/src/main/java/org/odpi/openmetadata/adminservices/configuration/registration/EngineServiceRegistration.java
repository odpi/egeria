/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import java.io.Serializable;
import java.util.Objects;

/**
 * EngineServiceRegistration is used by an engine service to register its admin services interface.
 * The registration is dynamic because engine services from third parties can be written and run in the
 * OMAGServerPlatform.
 */
public class EngineServiceRegistration implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int                      engineServiceId                = 0;
    private String                   engineServiceName              = null;
    private String                   engineServiceFullName          = null;
    private String                   engineServiceURLMarker         = null;
    private String                   engineServiceDescription       = null;
    private String                   engineServiceWiki              = null;
    private String                   engineServicePartnerOMAS       = null;
    private ServiceOperationalStatus engineServiceOperationalStatus = null;
    private String                   engineServiceAdminClass        = null;


    /**
     * Complete Constructor
     *
     * @param engineServiceCode ordinal for this engine service
     * @param engineServiceName symbolic name for this engine service
     * @param engineServiceFullName full name for this engine service
     * @param engineServiceURLMarker name of the part of the URL that is the name of the engine service
     * @param engineServiceDescription short description for this engine service
     * @param engineServiceWiki wiki page for the engine service for this engine service
     * @param engineServicePartnerOMAS full name of the OMAS that this engine service is partnered with
     * @param engineServiceOperationalStatus default initial operational status for the engine service
     * @param engineServiceAdminClassName  name of EngineServiceAdmin implementation class for the engine service
     */
    public EngineServiceRegistration(int                      engineServiceCode,
                                     String                   engineServiceName,
                                     String                   engineServiceFullName,
                                     String                   engineServiceURLMarker,
                                     String                   engineServiceDescription,
                                     String                   engineServiceWiki,
                                     String                   engineServicePartnerOMAS,
                                     ServiceOperationalStatus engineServiceOperationalStatus,
                                     String                   engineServiceAdminClassName)
    {
        this.engineServiceId = engineServiceCode;
        this.engineServiceName = engineServiceName;
        this.engineServiceFullName = engineServiceFullName;
        this.engineServiceURLMarker = engineServiceURLMarker;
        this.engineServiceDescription = engineServiceDescription;
        this.engineServiceWiki = engineServiceWiki;
        this.engineServicePartnerOMAS = engineServicePartnerOMAS;
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
    public EngineServiceRegistration(EngineServiceDescription engineServiceDescription,
                                     ServiceOperationalStatus engineServiceOperationalStatus,
                                     String                   engineServiceAdminClassName)
    {
        this(engineServiceDescription.getEngineServiceCode(),
             engineServiceDescription.getEngineServiceName(),
             engineServiceDescription.getEngineServiceFullName(),
             engineServiceDescription.getEngineServiceURLMarker(),
             engineServiceDescription.getEngineServiceDescription(),
             engineServiceDescription.getEngineServiceWiki(),
             engineServiceDescription.getEngineServicePartnerOMAS(),
             engineServiceOperationalStatus,
             engineServiceAdminClassName);
    }
    
    
    /**
     * Default constructor for use with Jackson libraries
     */
    public EngineServiceRegistration()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineServiceRegistration(EngineServiceRegistration template)
    {
        if (template != null)
        {
            engineServiceId                 = template.getEngineServiceId();
            engineServiceName               = template.getEngineServiceName();
            engineServiceFullName           = template.getEngineServiceFullName();
            engineServiceURLMarker          = template.getEngineServiceURLMarker();
            engineServiceDescription        = template.getEngineServiceDescription();
            engineServiceWiki               = template.getEngineServiceWiki();
            engineServicePartnerOMAS        = template.getEngineServicePartnerOMAS();
            engineServiceOperationalStatus  = template.getEngineServiceOperationalStatus();
            engineServiceAdminClass         = template.getEngineServiceAdminClass();
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
     * Return the wiki page link for the engine service.  The default value points to a page on the Atlas
     * confluence wiki.
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
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineServiceRegistration{" +
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
        EngineServiceRegistration that = (EngineServiceRegistration) objectToCompare;
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
