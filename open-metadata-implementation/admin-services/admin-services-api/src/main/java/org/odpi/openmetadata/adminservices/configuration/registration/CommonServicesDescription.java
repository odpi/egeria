/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

/**
 * CommonServicesDescription provides a list of fixed services that support the platform.
 */
public enum CommonServicesDescription
{
    /**
     * Manages the synchronization, retrieval and maintenance of metadata stored in open metadata repositories.
     */
    REPOSITORY_SERVICES              (180,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Metadata Repository Services (OMRS)",
                                      "repository-services",
                                      "Manages the synchronization, retrieval and maintenance of metadata stored in open metadata repositories.",
                                      "https://egeria-project.org/services/omrs",
                                      ServerTypeClassification.OMAG_SERVER.getServerTypeName(),
                                      "Open Metadata Repository Services (OMRS)",
                                      ServerTypeClassification.COHORT_MEMBER.getServerTypeName()),

    /**
     * Configuration of services for an Open Metadata and Governance server (OMAG Server).
     */
    ADMINISTRATION_SERVICES         (181,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Administration Services",
                                      "admin-services",
                                      "Platform service providing configuration of services for an Open Metadata and Governance server (OMAG Server).",
                                      "https://egeria-project.org/services/admin-services/overview",
                                     null,
                                     "Administration Services",
                                     null),

    /**
     * Common metadata services for the Open Connector Framework (OCF).
     */
    OCF_METADATA_MANAGEMENT          (182,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Connected Asset Services",
                                      "connected-asset",
                                      "Common metadata services for the Open Connector Framework (OCF).",
                                      "https://egeria-project.org/services/ocf-metadata-management",
                                      ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName(),
                                      null,
                                      null),

    /**
     * Authorization services for Open Metadata and Governance
     */
    OPEN_METADATA_SECURITY           (183,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Metadata Security Services",
                                      null, // no REST API
                                      "Authorization services for Open Metadata and Governance",
                                      "https://egeria-project.org/services/metadata-security-services",
                                      ServerTypeClassification.OMAG_SERVER.getServerTypeName(),
                                      null,
                                      null),

    /**
     * Support governance action services.
     */
    GAF_METADATA_MANAGEMENT          (184,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Governance Action Framework Services",
                                      "open-governance-service",
                                      "Support governance action services.",
                                      "https://egeria-project.org/services/gaf-metadata-management",
                                      ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName(),
                                      null,
                                      null),

    /**
     * Provides information about the registered services and connectors available in an OMAG Server Platform along with services to control and query information about the OMAG Servers running on the platform.
     */
    PLATFORM_SERVICES               (185,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Platform Operational Services",
                                      "platform-services",
                                      "A platform service that provides information about the registered services and connectors available in an OMAG Server Platform along with services to control and query information about the OMAG Servers running on the platform.",
                                      "https://egeria-project.org/services/platform-services/overview",
                                     null,
                                     null,
                                     null),

    /**
     * Provides operational support for the integration connectors.
     */
    OIF_METADATA_MANAGEMENT          (186,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Integration Service",
                                      "open-integration-service",
                                      "Provides operational support for the integration connectors.",
                                      "https://egeria-project.org/services/oif-metadata-management",
                                      ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName(),
                                      null,
                                      null),

    /**
     * Supports the startup and shutdown of OMAG Servers.
     */
    SERVER_OPERATIONS               (187,
                                     ComponentDevelopmentStatus.STABLE,
                                     "Server Operations",
                                     "server-operations",
                                     "Supports the start up and shutdown of OMAG Servers.",
                                     "https://egeria-project.org/services/server-operations/overview",
                                     ServerTypeClassification.OMAG_SERVER.getServerTypeName(),
                                     null,
                                     null),


    /**
     * Provides generic open metadata retrieval and management services for the Open Frameworks and Open Metadata Access Services (OMASs).
     */
    OMF_METADATA_MANAGEMENT     (188,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Open Metadata Store Services",
                                  "open-metadata-store",
                                  "Provides generic open metadata retrieval and management services for the Open Frameworks and Open Metadata Access Services (OMASs).",
                                  "https://egeria-project.org/services/oms-metadata-management",
                                  ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName(),
                                  null,
                                  null),
    ;


    private final int                        serviceCode;
    private final ComponentDevelopmentStatus serviceDevelopmentStatus;
    private final String                     serviceName;
    private final String                     serviceURLMarker;
    private final String                     serviceDescription;
    private final String                     serviceWiki;
    private final String                     serverType;
    private final String                     partnerServiceName;
    private final String                     partnerServerType;


    /**
     * Default Constructor
     *
     * @param serviceCode ordinal for this access service
     * @param serviceDevelopmentStatus development status
     * @param serviceName symbolic name for this access service
     * @param serviceURLMarker string used in URLs
     * @param serviceDescription short description for this access service
     * @param serviceWiki wiki page for the access service for this access service
     * @param serverType the server type where this service resides
     * @param partnerServiceName the name of a partner service called in a remote server
     * @param partnerServerType the type of server where the partner service resides
     */
    CommonServicesDescription(int                        serviceCode,
                              ComponentDevelopmentStatus serviceDevelopmentStatus,
                              String                     serviceName,
                              String                     serviceURLMarker,
                              String                     serviceDescription,
                              String                     serviceWiki,
                              String                     serverType,
                              String                     partnerServiceName,
                              String                     partnerServerType)
    {
        this.serviceCode = serviceCode;
        this.serviceDevelopmentStatus = serviceDevelopmentStatus;
        this.serviceName = serviceName;
        this.serviceURLMarker = serviceURLMarker;
        this.serviceDescription = serviceDescription;
        this.serviceWiki = serviceWiki;
        this.serverType = serverType;
        this.partnerServiceName = partnerServiceName;
        this.partnerServerType = partnerServerType;
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


    /**
     * Return the name of the type of server where this service resides.
     *
     * @return server type name
     */
    public String getServerType()
    {
        return serverType;
    }


    /**
     * Return the name of a service called in a remote server (if any).
     *
     * @return name of service
     */
    public String getPartnerServiceName()
    {
        return partnerServiceName;
    }


    /**
     * Return the type of server where the partner service resides.
     *
     * @return name of type of server
     */
    public String getPartnerServerType()
    {
        return partnerServerType;
    }
}
