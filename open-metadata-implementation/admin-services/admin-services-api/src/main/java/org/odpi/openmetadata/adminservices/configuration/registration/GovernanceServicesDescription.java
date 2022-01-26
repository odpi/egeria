/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

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
    OPEN_LINEAGE_SERVICES            (150,
                                      "Open Lineage Services",
                                      "open-lineage",
                                      "Store and query asset lineage",
                                      "https://odpi.github.io/egeria-docs/services/open-lineage-services/"),
    CONFORMANCE_SUITE_SERVICES       (151,
                                      "Conformance Suite Services",
                                      "conformance-suite",
                                      "Run automated open metadata conformance suite services",
                                      "https://odpi.github.io/egeria-docs/guides/cts/overview/"),
    DATA_ENGINE_PROXY_SERVICES       (152,
                                      "Data Engine Proxy Services",
                                      null,
                                      "Integrate Data Engines that are not self-capable of integrating directly with the Data Engine OMAS",
                                      "https://odpi.github.io/egeria-docs/services/data-engine-proxy-services/"),
    INTEGRATION_DAEMON_SERVICES      (153,
                                      "Integration Daemon Services",
                                      null,
                                      "Host one or more integration services that are exchanging metadata with third party technologies",
                                      "https://odpi.github.io/egeria-docs/services/integration-daemon-services/"),
    ENGINE_HOST_SERVICES             (154,
                                      "Engine Host Services",
                                      null,
                                      "Host one or more engine services that are actively managing governance of open metadata and the digital landscape",
                                      "https://odpi.github.io/egeria-docs/services/engine-host-services/"),
    ;


    private static final long serialVersionUID = 1L;

    private int    serviceCode;
    private String serviceName;
    private String serviceURLMarker;
    private String serviceDescription;
    private String serviceWiki;


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
     * @param serviceCode ordinal for this governance service
     * @param serviceName symbolic name for this governance service
     * @param serviceURLMarker string used in URLs
     * @param serviceDescription short description for this governance service
     * @param serviceWiki wiki page for the governance service for this governance service
     */
    GovernanceServicesDescription(int    serviceCode,
                                  String serviceName,
                                  String serviceURLMarker,
                                  String serviceDescription,
                                  String serviceWiki)
    {
        this.serviceCode = serviceCode;
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
