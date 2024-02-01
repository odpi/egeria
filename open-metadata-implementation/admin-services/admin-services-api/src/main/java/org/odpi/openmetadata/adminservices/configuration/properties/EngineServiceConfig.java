/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineServiceConfig provides the properties to configure a single engine service in an engine hosting server.  The engine service
 * runs one or more engines of a specific type.  The configuration for each of these engines is extracted from the partner OMAS using
 * the engine name as the qualified name.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineServiceConfig extends OMAGServerClientConfig
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int                        engineServiceId                  = 0;
    private ComponentDevelopmentStatus engineServiceDevelopmentStatus   = null;
    private String                     engineServiceName                = null;
    private String                     engineServiceFullName            = null;
    private String                     engineServiceURLMarker           = null;
    private String                     engineServiceDescription         = null;
    private String                     engineServiceWiki                = null;
    private String                     engineServicePartnerOMAS         = null;
    private Map<String, Object>        engineServiceOptions             = null;
    private List<EngineConfig>         engines                          = null;
    private ServiceOperationalStatus   engineServiceOperationalStatus   = null;
    private String                     engineServiceAdminClass          = null;


    /**
     * Default constructor
     */
    public EngineServiceConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    @SuppressWarnings(value = "deprecation")
    public EngineServiceConfig(EngineServiceConfig template)
    {
        super(template);

        if (template != null)
        {
            engineServiceId                  = template.getEngineServiceId();
            engineServiceDevelopmentStatus   = template.getEngineServiceDevelopmentStatus();
            engineServiceAdminClass          = template.getEngineServiceAdminClass();
            engineServiceName                = template.getEngineServiceName();
            engineServiceFullName            = template.getEngineServiceFullName();
            engineServiceURLMarker           = template.getEngineServiceURLMarker();
            engineServiceDescription         = template.getEngineServiceDescription();
            engineServiceWiki                = template.getEngineServiceWiki();
            engineServicePartnerOMAS         = template.getEngineServicePartnerOMAS();
            engineServiceOperationalStatus   = template.getEngineServiceOperationalStatus();
            engineServiceOptions             = template.getEngineServiceOptions();
            engines                          = template.getEngines();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineServiceConfig(EngineServiceRegistrationEntry template)
    {
        super();

        if (template != null)
        {
            engineServiceId                  = template.getEngineServiceId();
            engineServiceDevelopmentStatus   = template.getEngineServiceDevelopmentStatus();
            engineServiceAdminClass          = template.getEngineServiceAdminClass();
            engineServiceName                = template.getEngineServiceName();
            engineServiceFullName            = template.getEngineServiceFullName();
            engineServiceURLMarker           = template.getEngineServiceURLMarker();
            engineServiceDescription         = template.getEngineServiceDescription();
            engineServiceWiki                = template.getEngineServiceWiki();
            engineServicePartnerOMAS         = template.getEngineServicePartnerOMAS();
            engineServiceOperationalStatus   = template.getEngineServiceOperationalStatus();
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
        return engineServiceFullName;
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
     * Return the options for this engine service. These are properties that are specific to the engine service.
     *
     * @return Map from String to String
     */
    public Map<String, Object> getEngineServiceOptions()
    {
        if (engineServiceOptions == null)
        {
            return null;
        }
        else if (engineServiceOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return engineServiceOptions;
        }
    }


    /**
     * Set up the options for this engine service.  These are properties that are specific to the engine service.
     *
     * @param engineServiceOptions Map from String to String
     */
    public void setEngineServiceOptions(Map<String, Object> engineServiceOptions)
    {
        this.engineServiceOptions = engineServiceOptions;
    }


    /**
     * Return the list of unique names (qualifiedName) for the governance engines that will run in this server.
     * This call is deprecated, use setEngineList() instead.
     *
     * @return list of qualified names and userIds
     */
    @Deprecated
    public List<EngineConfig> getEngines()
    {
        return engines;
    }


    /**
     * Set up the list of unique names (qualifiedName) for the governance engines that will run in this server.
     * This call is deprecated, use setEngineList() instead.
     *
     * @param engines list of qualified names and userIds
     */
    @Deprecated
    public void setEngines(List<EngineConfig> engines)
    {
        this.engines = engines;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineServiceConfig{" +
                       "engineServiceId=" + engineServiceId +
                       ", engineServiceDevelopmentStatus=" + engineServiceDevelopmentStatus +
                       ", engineServiceName='" + engineServiceName + '\'' +
                       ", engineServiceFullName='" + engineServiceFullName + '\'' +
                       ", engineServiceURLMarker='" + engineServiceURLMarker + '\'' +
                       ", engineServiceDescription='" + engineServiceDescription + '\'' +
                       ", engineServiceWiki='" + engineServiceWiki + '\'' +
                       ", engineServicePartnerOMAS='" + engineServicePartnerOMAS + '\'' +
                       ", engineServiceOptions=" + engineServiceOptions +
                       ", engines=" + engines +
                       ", engineServiceOperationalStatus=" + engineServiceOperationalStatus +
                       ", engineServiceAdminClass='" + engineServiceAdminClass + '\'' +
                       ", OMAGServerPlatformRootURL='" + getOMAGServerPlatformRootURL() + '\'' +
                       ", OMAGServerName='" + getOMAGServerName() + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        EngineServiceConfig that = (EngineServiceConfig) objectToCompare;
        return engineServiceId == that.engineServiceId &&
                       engineServiceDevelopmentStatus == that.engineServiceDevelopmentStatus &&
                       Objects.equals(engineServiceName, that.engineServiceName) &&
                       Objects.equals(engineServiceFullName, that.engineServiceFullName) &&
                       Objects.equals(engineServiceURLMarker, that.engineServiceURLMarker) &&
                       Objects.equals(engineServiceDescription, that.engineServiceDescription) &&
                       Objects.equals(engineServiceWiki, that.engineServiceWiki) &&
                       Objects.equals(engineServicePartnerOMAS, that.engineServicePartnerOMAS) &&
                       Objects.equals(engineServiceOptions, that.engineServiceOptions) &&
                       Objects.equals(engines, that.engines) &&
                       engineServiceOperationalStatus == that.engineServiceOperationalStatus &&
                       Objects.equals(engineServiceAdminClass, that.engineServiceAdminClass);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), engineServiceId, engineServiceDevelopmentStatus, engineServiceName, engineServiceFullName,
                            engineServiceURLMarker, engineServiceDescription, engineServiceWiki, engineServicePartnerOMAS, engineServiceOptions,
                            engines, engineServiceOperationalStatus, engineServiceAdminClass);
    }
}
