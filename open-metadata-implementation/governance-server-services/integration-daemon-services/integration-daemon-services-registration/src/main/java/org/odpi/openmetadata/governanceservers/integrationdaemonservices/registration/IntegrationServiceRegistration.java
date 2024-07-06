/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration;

import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

;
import java.util.Objects;

/**
 * IntegrationServiceRegistration is used to describe an integration service and register it with the integration daemon registry.
 */
public class IntegrationServiceRegistration
{
    private int                        integrationServiceId                  = 0;
    private ComponentDevelopmentStatus integrationServiceDevelopmentStatus   = null;
    private String                     integrationServiceName                = null;
    private String                     integrationServiceFullName            = null;
    private String                     integrationServiceURLMarker           = null;
    private String                     integrationServiceDescription         = null;
    private String                     integrationServiceWiki                = null;
    private String                     integrationServicePartnerOMAS         = null;
    private ServiceOperationalStatus   integrationServiceOperationalStatus   = null;
    private String                     integrationServiceContextManagerClass = null;
    private PermittedSynchronization   defaultPermittedSynchronization;


    /**
     * Default constructor for use with Jackson libraries
     */
    public IntegrationServiceRegistration()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationServiceRegistration(IntegrationServiceRegistration template)
    {
        if (template != null)
        {
            integrationServiceId                  = template.getIntegrationServiceId();
            integrationServiceDevelopmentStatus   = template.getIntegrationServiceDevelopmentStatus();
            integrationServiceName                = template.getIntegrationServiceName();
            integrationServiceFullName            = template.getIntegrationServiceFullName();
            integrationServiceURLMarker           = template.getIntegrationServiceURLMarker();
            integrationServiceDescription         = template.getIntegrationServiceDescription();
            integrationServiceWiki                = template.getIntegrationServiceWiki();
            integrationServicePartnerOMAS         = template.getIntegrationServicePartnerOMAS();
            integrationServiceContextManagerClass = template.getIntegrationServiceContextManagerClass();
            integrationServiceOperationalStatus   = template.getIntegrationServiceOperationalStatus();
            defaultPermittedSynchronization       = template.getDefaultPermittedSynchronization();
        }
    }


    /**
     * Return the code number (ordinal) for this integration service.
     *
     * @return int ordinal
     */
    public int getIntegrationServiceId()
    {
        return integrationServiceId;
    }


    /**
     * Set up the code number (ordinal) for this integration service.
     *
     * @param integrationServiceId int ordinal
     */
    public void setIntegrationServiceId(int integrationServiceId)
    {
        this.integrationServiceId = integrationServiceId;
    }


    /**
     * Return the development status of the service.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getIntegrationServiceDevelopmentStatus()
    {
        return integrationServiceDevelopmentStatus;
    }


    /**
     * Set up the development status of the service.
     *
     * @param  integrationServiceDevelopmentStatus enum describing the status
     */
    public void setEngineServiceDevelopmentStatus(ComponentDevelopmentStatus integrationServiceDevelopmentStatus)
    {
        this.integrationServiceDevelopmentStatus = integrationServiceDevelopmentStatus;
    }


    /**
     * Return the Java class name of the admin-services interface for this integration service.
     *
     * @return String class name implementing the
     * IntegrationServiceAdmin interface.
     */
    public String getIntegrationServiceContextManagerClass()
    {
        return integrationServiceContextManagerClass;
    }


    /**
     * Set up the Java class name of the admin services interface for this integration service.
     *
     * @param integrationServiceContextManagerClass String class name implementing the
     * IntegrationServiceAdmin interface.
     */
    public void setIntegrationServiceContextManagerClass(String integrationServiceContextManagerClass)
    {
        this.integrationServiceContextManagerClass = integrationServiceContextManagerClass;
    }


    /**
     * Return the name of the integration service.
     *
     * @return String name
     */
    public String getIntegrationServiceName()
    {
        return integrationServiceName;
    }


    /**
     * Set up the full name of the integration service.
     *
     * @param integrationServiceFullName String name
     */
    public void setIntegrationServiceFullName(String integrationServiceFullName)
    {
        this.integrationServiceFullName = integrationServiceFullName;
    }


    /**
     * Set up the name of the integration service.
     *
     * @param integrationServiceName String name
     */
    public void setIntegrationServiceName(String integrationServiceName)
    {
        this.integrationServiceName = integrationServiceName;
    }


    /**
     * Return the full name of the integration service.
     *
     * @return String name
     */
    public String getIntegrationServiceFullName()
    {
        if (integrationServiceFullName == null)
        {
            if (integrationServiceName == null)
            {
                return null;
            }

            return integrationServiceName + " OMIS";
        }
        else
        {
            return integrationServiceFullName;
        }
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getIntegrationServiceURLMarker()
    {
        return integrationServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param integrationServiceURLMarker url fragment
     */
    public void setIntegrationServiceURLMarker(String integrationServiceURLMarker)
    {
        this.integrationServiceURLMarker = integrationServiceURLMarker;
    }



    /**
     * Return the short description of the integration service.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getIntegrationServiceDescription()
    {
        return integrationServiceDescription;
    }


    /**
     * Set up the short description of the integration service.
     *
     * @param integrationServiceDescription String description
     */
    public void setIntegrationServiceDescription(String integrationServiceDescription)
    {
        this.integrationServiceDescription = integrationServiceDescription;
    }


    /**
     * Return the wiki page link for the integration service.
     *
     * @return String url
     */
    public String getIntegrationServiceWiki()
    {
        return integrationServiceWiki;
    }


    /**
     * Set up the wiki page link for the integration service.
     *
     * @param integrationServiceWiki String url
     */
    public void setIntegrationServiceWiki(String integrationServiceWiki)
    {
        this.integrationServiceWiki = integrationServiceWiki;
    }


    /**
     * Return the full name of the Open Metadata Access Service (OMAS) that this integration service is partnered with.
     *
     * @return Full name of OMAS
     */
    public String getIntegrationServicePartnerOMAS()
    {
        return integrationServicePartnerOMAS;
    }


    /**
     * Set up the full name of the Open Metadata Access Service (OMAS) that this integration service is partnered with.
     *
     * @param integrationServicePartnerOMAS Full name of OMAS
     */
    public void setIntegrationServicePartnerOMAS(String integrationServicePartnerOMAS)
    {
        this.integrationServicePartnerOMAS = integrationServicePartnerOMAS;
    }


    /**
     * Return the status of this integration service.
     *
     * @return ServiceOperationalStatus enum
     */
    public ServiceOperationalStatus getIntegrationServiceOperationalStatus()
    {
        return integrationServiceOperationalStatus;
    }


    /**
     * Set up the status of the integration service.
     *
     * @param integrationServiceOperationalStatus ServiceOperationalStatus enum
     */
    public void setIntegrationServiceOperationalStatus(ServiceOperationalStatus integrationServiceOperationalStatus)
    {
        this.integrationServiceOperationalStatus = integrationServiceOperationalStatus;
    }


    /**
     * Return the default value for permitted synchronization that should be set up for the integration connectors
     * as they are configured.
     *
     * @return enum default
     */
    public PermittedSynchronization getDefaultPermittedSynchronization()
    {
        return defaultPermittedSynchronization;
    }


    /**
     * Set up the default value for permitted synchronization that should be set up for the integration connectors
     * as they are configured.
     *
     * @param defaultPermittedSynchronization enum default
     */
    public void setDefaultPermittedSynchronization(PermittedSynchronization defaultPermittedSynchronization)
    {
        this.defaultPermittedSynchronization = defaultPermittedSynchronization;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationServiceRegistration{" +
                       "integrationServiceId=" + integrationServiceId +
                       ", integrationServiceDevelopmentStatus=" + integrationServiceDevelopmentStatus +
                       ", integrationServiceName='" + integrationServiceName + '\'' +
                       ", integrationServiceFullName='" + integrationServiceFullName + '\'' +
                       ", integrationServiceURLMarker='" + integrationServiceURLMarker + '\'' +
                       ", integrationServiceDescription='" + integrationServiceDescription + '\'' +
                       ", integrationServiceWiki='" + integrationServiceWiki + '\'' +
                       ", integrationServicePartnerOMAS='" + integrationServicePartnerOMAS + '\'' +
                       ", integrationServiceOperationalStatus=" + integrationServiceOperationalStatus +
                       ", integrationServiceContextManagerClass='" + integrationServiceContextManagerClass + '\'' +
                       ", defaultPermittedSynchronization=" + defaultPermittedSynchronization +
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
        IntegrationServiceRegistration that = (IntegrationServiceRegistration) objectToCompare;
        return integrationServiceId == that.integrationServiceId &&
                       integrationServiceDevelopmentStatus == that.integrationServiceDevelopmentStatus &&
                       Objects.equals(integrationServiceName, that.integrationServiceName) &&
                       Objects.equals(integrationServiceFullName, that.integrationServiceFullName) &&
                       Objects.equals(integrationServiceURLMarker, that.integrationServiceURLMarker) &&
                       Objects.equals(integrationServiceDescription, that.integrationServiceDescription) &&
                       Objects.equals(integrationServiceWiki, that.integrationServiceWiki) &&
                       Objects.equals(integrationServicePartnerOMAS, that.integrationServicePartnerOMAS) &&
                       integrationServiceOperationalStatus == that.integrationServiceOperationalStatus &&
                       Objects.equals(integrationServiceContextManagerClass, that.integrationServiceContextManagerClass) &&
                       defaultPermittedSynchronization == that.defaultPermittedSynchronization;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(integrationServiceId, integrationServiceDevelopmentStatus, integrationServiceName, integrationServiceFullName,
                            integrationServiceURLMarker, integrationServiceDescription, integrationServiceWiki, integrationServicePartnerOMAS,
                            integrationServiceOperationalStatus, integrationServiceContextManagerClass, defaultPermittedSynchronization);
    }
}
