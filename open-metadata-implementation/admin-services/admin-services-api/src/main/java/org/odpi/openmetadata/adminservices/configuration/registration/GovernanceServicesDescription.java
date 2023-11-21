/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GovernanceServicesDescription provides a list of subsystems that support the various governance servers.
 */
public enum GovernanceServicesDescription implements Serializable
{
    /**
     * Store and query asset lineage.
     */
    OPEN_LINEAGE_SERVICES            (190,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Open Lineage Services",
                                      "open-lineage",
                                      "Store and query asset lineage",
                                      "https://egeria-project.org/services/open-lineage-services/"),

    /**
     * Run automated open metadata conformance suite services.
     */
    CONFORMANCE_SUITE_SERVICES       (191,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Conformance Suite Services",
                                      "conformance-suite",
                                      "Run automated open metadata conformance suite services.",
                                      "https://egeria-project.org/guides/cts/overview/"),

    /**
     * Integrate Data Engines that are not self-capable of integrating directly with the Data Engine OMAS.
     */
    DATA_ENGINE_PROXY_SERVICES       (192,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Data Engine Proxy Services",
                                      null,
                                      "Integrate Data Engines that are not self-capable of integrating directly with the Data Engine OMAS.",
                                      "https://egeria-project.org/services/data-engine-proxy-services/"),

    /**
     * Host one or more integration services that are exchanging metadata with third party technologies.
     */
    INTEGRATION_DAEMON_SERVICES      (193,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Integration Daemon Services",
                                      null,
                                      "Host one or more integration services that are exchanging metadata with third party technologies.",
                                      "https://egeria-project.org/services/integration-daemon-services/"),

    /**
     * Host one or more engine services that are actively managing governance of open metadata and the digital landscape.
     */
    ENGINE_HOST_SERVICES             (194,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Engine Host Services",
                                      null,
                                      "Host one or more engine services that are actively managing governance of open metadata and the digital landscape.",
                                      "https://egeria-project.org/services/engine-host-services/"),
    ;


    private static final long serialVersionUID = 1L;

    private final int                        serviceCode;
    private final ComponentDevelopmentStatus serviceDevelopmentStatus;
    private final String                     serviceName;
    private final String                     serviceURLMarker;
    private final String                     serviceDescription;
    private final String                     serviceWiki;


    /**
     * Return a list containing each of the governance service descriptions defined in this enum class.
     *
     * @return List of enums
     */
    public static List<GovernanceServicesDescription> getGovernanceServersDescriptionList()
    {
        List<GovernanceServicesDescription> serviceDescriptionList = new ArrayList<>();

        serviceDescriptionList.add(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES);
        serviceDescriptionList.add(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES);
        serviceDescriptionList.add(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES);
        serviceDescriptionList.add(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES);

        return serviceDescriptionList;
    }


    /**
     * Return a set of non null url markers (short names) of the governance services
     *
     * @return set of url markers
     */
    public static Set<String> getGovernanceServersURLMarkers()
    {
        Set<String> urlMarkerSet = new HashSet<>();
        for (GovernanceServicesDescription governanceServicesDescription:getGovernanceServersDescriptionList())
        {
            String urlMarker = governanceServicesDescription.getServiceURLMarker();
            if (urlMarker != null)
            {
                urlMarkerSet.add(urlMarker);
            }
        }

        return urlMarkerSet;
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
    GovernanceServicesDescription(int                        serviceCode,
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
     * Return the URL for the wiki page describing this governance service.
     *
     * @return String URL name for the wiki page
     */
    public String getServiceWiki()
    {
        return serviceWiki;
    }
}
