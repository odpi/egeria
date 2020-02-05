/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.registration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewServiceDescription provides a list of registered OMAS services.
 */
public enum CommonUIServicesDescription implements Serializable
{

    ADMIN_OPERATIONAL_SERVICES       (3100,
                                      "UI Server Operational Services",
                                      "admin-services",
                                      "Management of services active in an Open Metadata and governance UI server (UI Server)",
                                      "https://egeria.odpi.org/open-metadata-implementation/admin-services"),

    OPEN_METADATA_SECURITY           (3101,
                                      "Open Metadata Security Services",
                                      null,
                                      "Authorization services for Open Metadata and Governance",
                                      "https://egeria.odpi.org/open-metadata-implementation/common-services/metadata-security/");


    private static final long     serialVersionUID    = 1L;

    private int    serviceCode;
    private String serviceName;
    private String serviceURLMarker;
    private String serviceDescription;
    private String serviceWiki;


    /**
     * Return a list containing each of the view service descriptions defined in this enum class.
     *
     * @return List of enums
     */
    public static List<CommonUIServicesDescription> getGovernanceServersDescriptionList()
    {
        List<CommonUIServicesDescription> serviceDescriptionList = new ArrayList<>();

        serviceDescriptionList.add(CommonUIServicesDescription.ADMIN_OPERATIONAL_SERVICES);
        serviceDescriptionList.add(CommonUIServicesDescription.OPEN_METADATA_SECURITY);

        return serviceDescriptionList;
    }


    /**
     * Default Constructor
     *
     * @param serviceCode ordinal for this view service
     * @param serviceName symbolic name for this view service
     * @param serviceURLMarker string used in URLs
     * @param serviceDescription short description for this view service
     * @param serviceWiki wiki page for the view service for this view service
     */
    CommonUIServicesDescription(int    serviceCode,
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
     * Return the URL for the wiki page describing this view service.
     *
     * @return String URL name for the wiki page
     */
    public String getServiceWiki()
    {
        return serviceWiki;
    }
}
