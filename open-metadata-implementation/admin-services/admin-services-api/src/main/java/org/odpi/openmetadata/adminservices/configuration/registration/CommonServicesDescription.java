/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CommonServicesDescription provides a list of fixed services that support the platform.
 */
public enum CommonServicesDescription implements Serializable
{
    REPOSITORY_SERVICES              (180,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Metadata Repository Services (OMRS)",
                                      "repository-services",
                                      "Manages the synchronization, retrieval and maintenance of metadata stored in open metadata repositories",
                                      "https://egeria-project.org/services/omrs"),

    ADMINISTRATION_SERVICES         (181,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Administration Services",
                                      "admin-services",
                                      "Management of services active in an Open Metadata and governance server (OMAG Server)",
                                      "https://egeria-project.org/services/admin-services/overview"),

    OCF_METADATA_MANAGEMENT          (182,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Connected Asset Services",
                                      "connected-asset",
                                      "Common metadata services for the Open Connector Framework (OCF)",
                                      "https://egeria-project.org/services/ocf-metadata-management"),

    OPEN_METADATA_SECURITY           (183,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Metadata Security Services",
                                      null, // no REST API
                                      "Authorization services for Open Metadata and Governance",
                                      "https://egeria-project.org/services/metadata-security-services"),

    GAF_METADATA_MANAGEMENT          (184,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Metadata Store Services",
                                      "open-metadata-store",
                                      "Provides generic open metadata retrieval and management services for Open Metadata Access Services (OMASs).",
                                      "https://egeria-project.org/services/gaf-metadata-management"),

    PLATFORM_SERVICES               (185,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Platform Services",
                                      "platform-services",
                                      "Provides information about the registered services and connectors available in an OMAG Server Platform along with services to control and query information about the OMAG Servers running on the platform.",
                                      "https://egeria-project.org/services/platform-services/overview"),

    OIF_METADATA_MANAGEMENT          (186,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Integration Service",
                                      "open-integration-service",
                                      "Provides operational support for the integration connectors.",
                                      "https://egeria-project.org/services/oif-metadata-management"),

    ;


    private static final long     serialVersionUID    = 1L;

    private final int                        serviceCode;
    private final ComponentDevelopmentStatus serviceDevelopmentStatus;
    private final String                     serviceName;
    private final String                     serviceURLMarker;
    private final String                     serviceDescription;
    private final String                     serviceWiki;


    /**
     * Return a list containing each of the access service descriptions defined in this enum class.
     *
     * @return List of enums
     */
    public static List<CommonServicesDescription> getGovernanceServersDescriptionList()
    {
        List<CommonServicesDescription> serviceDescriptionList = new ArrayList<>();

        serviceDescriptionList.add(CommonServicesDescription.REPOSITORY_SERVICES);
        serviceDescriptionList.add(CommonServicesDescription.ADMINISTRATION_SERVICES);
        serviceDescriptionList.add(CommonServicesDescription.OCF_METADATA_MANAGEMENT);
        serviceDescriptionList.add(CommonServicesDescription.GAF_METADATA_MANAGEMENT);
        serviceDescriptionList.add(CommonServicesDescription.OPEN_METADATA_SECURITY);
        serviceDescriptionList.add(CommonServicesDescription.PLATFORM_SERVICES);

        return serviceDescriptionList;
    }


    /**
     * Default Constructor
     *
     * @param serviceCode ordinal for this access service
     * @param serviceDevelopmentStatus development status
     * @param serviceName symbolic name for this access service
     * @param serviceURLMarker string used in URLs
     * @param serviceDescription short description for this access service
     * @param serviceWiki wiki page for the access service for this access service
     */
    CommonServicesDescription(int                        serviceCode,
                              ComponentDevelopmentStatus serviceDevelopmentStatus,
                              String                     serviceName,
                              String                     serviceURLMarker,
                              String                     serviceDescription,
                              String                     serviceWiki)
    {
        this.serviceCode = serviceCode;
        this.serviceDevelopmentStatus = serviceDevelopmentStatus;
        this.serviceName = serviceName;
        this.serviceURLMarker = serviceURLMarker;
        this.serviceDescription = serviceDescription;
        this.serviceWiki = serviceWiki;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getServiceCode()
    {
        return serviceCode;
    }



    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getServiceDevelopmentStatus()
    {
        return serviceDevelopmentStatus;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getServiceName()
    {
        return serviceName;
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
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getServiceDescription()
    {
        return serviceDescription;
    }


    /**
     * Return the URL for the wiki page describing this access service.
     *
     * @return String URL for the wiki page
     */
    public String getServiceWiki()
    {
        return serviceWiki;
    }
}
