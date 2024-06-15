/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

/**
 * GovernanceServicesDescription provides a list of subsystems that support the various governance servers.
 */
public enum GovernanceServicesDescription
{
    /**
     * Store and query asset lineage.
     */
    LINEAGE_WAREHOUSE_SERVICES(190,
                               ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                               "Lineage Warehouse Services",
                               "lineage-warehouse",
                               "Store and query asset lineage",
                               "https://egeria-project.org/services/lineage-warehouse-services/",
                               ServerTypeClassification.LINEAGE_WAREHOUSE.getServerTypeName(),
                               AccessServiceDescription.ASSET_LINEAGE_OMAS.getAccessServiceFullName(),
                               ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName()),

    /**
     * Run automated open metadata conformance suite services.
     */
    CONFORMANCE_SUITE_SERVICES       (191,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Conformance Suite Services",
                                      "conformance-suite",
                                      "Run automated open metadata conformance suite services.",
                                      "https://egeria-project.org/guides/cts/overview/",
                                      ServerTypeClassification.CONFORMANCE_SERVER.getServerTypeName(),
                                      null,
                                      null),

    /**
     * Integrate Data Engines that are not self-capable of integrating directly with the Data Engine OMAS.
     */
    DATA_ENGINE_PROXY_SERVICES       (192,
                                      ComponentDevelopmentStatus.DEPRECATED,
                                      "Data Engine Proxy Services",
                                      "data-engine-proxy",
                                      "Integrate Data Engines that are not self-capable of integrating directly with the Data Engine OMAS.",
                                      "https://egeria-project.org/services/data-engine-proxy-services/",
                                      ServerTypeClassification.DATA_ENGINE_PROXY.getServerTypeName(),
                                      AccessServiceDescription.DATA_ENGINE_OMAS.getAccessServiceFullName(),
                                      ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName()),

    /**
     * Host one or more integration services that are exchanging metadata with third party technologies.
     */
    INTEGRATION_DAEMON_SERVICES      (193,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Integration Daemon Services",
                                      "integration-daemon",
                                      "Hosts integration connectors that are exchanging metadata with third party technologies.",
                                      "https://egeria-project.org/services/integration-daemon-services/",
                                      ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName(),
                                      AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                      ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName()),

    /**
     * Host one or more engine services that are actively managing governance of open metadata and the digital landscape.
     */
    ENGINE_HOST_SERVICES             (194,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Engine Host Services",
                                      "engine-host",
                                      "Host one or more engine services that are actively managing governance of open metadata and the digital landscape.",
                                      "https://egeria-project.org/services/engine-host-services/",
                                      ServerTypeClassification.ENGINE_HOST.getServerTypeName(),
                                      AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                      ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName()),
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
    GovernanceServicesDescription(int                        serviceCode,
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
     * Return the URL for the wiki page describing this governance service.
     *
     * @return String URL name for the wiki page
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
