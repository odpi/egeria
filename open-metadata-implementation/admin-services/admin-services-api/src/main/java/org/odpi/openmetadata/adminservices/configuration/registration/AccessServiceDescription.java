/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serializable;

/**
 * AccessServiceDescription provides a list of registered OMAS services.
 */
public enum AccessServiceDescription implements Serializable
{
    /**
     * Common metadata services for the Open Connector Framework (OCF).
     */
    OCF_METADATA_MANAGEMENT          (182,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Connected Asset Services",
                                      "connected-asset",
                                      "Common metadata services for the Open Connector Framework (OCF).",
                                      "https://egeria-project.org/services/ocf-metadata-management",
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
                                 ServiceOperationalStatus.ENABLED),


    ;


    private final int                        serviceCode;
    private final ComponentDevelopmentStatus serviceDevelopmentStatus;
    private final String                     serviceName;
    private final String                     serviceURLMarker;
    private final String                     serviceDescription;
    private final String                     serviceWiki;
    private final String                     serverType;
    private final ServiceOperationalStatus   outTopicStatus;


    /**
     * Default Constructor
     *
     * @param serviceCode ordinal for this service
     * @param serviceDevelopmentStatus development status
     * @param serviceName symbolic name for this service
     * @param serviceURLMarker string used in URLs
     * @param serviceDescription short description for this service
     * @param serviceWiki wiki page for the access service for this service
     * @param outTopicStatus is the  service outTopic implemented, operational or disabled?
     */
    AccessServiceDescription(int                        serviceCode,
                             ComponentDevelopmentStatus serviceDevelopmentStatus,
                             String                     serviceName,
                             String                     serviceURLMarker,
                             String                     serviceDescription,
                             String                     serviceWiki,
                             ServiceOperationalStatus   outTopicStatus)
    {
        /*
         * Save the values supplied
         */
        this.serviceCode = serviceCode;
        this.serviceDevelopmentStatus = serviceDevelopmentStatus;
        this.serviceName = serviceName;
        this.serviceURLMarker = serviceURLMarker;
        this.serviceDescription = serviceDescription;
        this.serviceWiki = serviceWiki;
        this.serverType = ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName();
        this.outTopicStatus = outTopicStatus;
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
     * Return the status of the services' out topic.
     *
     * @return not-implemented, enabled, disabled
     */
    public ServiceOperationalStatus getOutTopicStatus()
    {
        return outTopicStatus;
    }
}
