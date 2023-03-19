/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration;

import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serializable;

/**
 * IntegrationServiceDescription provides a list of registered integration services.
 */
public enum IntegrationServiceDescription implements Serializable
{
    CATALOG_INTEGRATOR_OMIS(600,
                            ComponentDevelopmentStatus.IN_DEVELOPMENT,
                            "Catalog Integrator",
                            "Catalog Integrator OMIS",
                            "catalog-integrator",
                            "Exchange metadata with third party data catalogs.",
                            "https://egeria-project.org/services/omis/catalog-integrator/overview/",
                            "Asset Manager OMAS",
                            PermittedSynchronization.BOTH_DIRECTIONS),

    API_INTEGRATOR_OMIS(601,
                        ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                        "API Integrator",
                        "API Integrator OMIS",
                        "api-integrator",
                        "Exchange metadata with third party API Gateways.",
                        "https://egeria-project.org/services/omis/api-integrator/overview/",
                        "Data Manager OMAS",
                        PermittedSynchronization.FROM_THIRD_PARTY),

    TOPIC_INTEGRATOR_OMIS(602,
                          ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                          "Topic Integrator",
                          "Topic Integrator OMIS",
                          "topic-integrator",
                          "Exchange metadata with third party event-based brokers.",
                          "https://egeria-project.org/services/omis/topic-integrator/overview/",
                          "Data Manager OMAS",
                          PermittedSynchronization.FROM_THIRD_PARTY),

    DISPLAY_INTEGRATOR_OMIS(603,
                            ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                            "Display Integrator",
                            "Display Integrator OMIS",
                            "display-integrator",
                            "Exchange metadata with applications that display data to users.",
                            "https://egeria-project.org/services/omis/display-integrator/overview/",
                            "Data Manager OMAS",
                            PermittedSynchronization.FROM_THIRD_PARTY),

    DATABASE_INTEGRATOR_OMIS     (604,
                                  ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                  "Database Integrator",
                                  "Database Integrator OMIS",
                                  "database-integrator",
                                  "Extract metadata such as schema, tables and columns from database managers.",
                                  "https://egeria-project.org/services/omis/database-integrator/overview/",
                                  "Data Manager OMAS",
                                  PermittedSynchronization.FROM_THIRD_PARTY),

    FILES_INTEGRATOR_OMIS(605,
                          ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                          "Files Integrator",
                          "Files Integrator OMIS",
                          "files-integrator",
                          "Extract metadata about files stored in a file system or file manager.",
                          "https://egeria-project.org/services/omis/files-integrator/overview/",
                          "Data Manager OMAS",
                          PermittedSynchronization.FROM_THIRD_PARTY),

    LINEAGE_INTEGRATOR_OMIS(606,
                            ComponentDevelopmentStatus.IN_DEVELOPMENT,
                            "Lineage Integrator",
                            "Lineage Integrator OMIS",
                            "lineage-integrator",
                            "Manage capture of lineage from a third party tool.",
                            "https://egeria-project.org/services/omis/lineage-integrator/overview/",
                            "Asset Manager OMAS",
                            PermittedSynchronization.FROM_THIRD_PARTY),

    ORGANIZATION_INTEGRATOR_OMIS     (607,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Organization Integrator",
                                      "Organization Integrator OMIS",
                                      "organization-integrator",
                                      "Load information about the teams and people in an organization and return collaboration activity.",
                                      "https://egeria-project.org/services/omis/organization-integrator/overview/",
                                      "Community Profile OMAS",
                                      PermittedSynchronization.FROM_THIRD_PARTY),

    SECURITY_INTEGRATOR_OMIS(608,
                             ComponentDevelopmentStatus.IN_DEVELOPMENT,
                             "Security Integrator",
                             "Security Integrator OMIS",
                             "security-integrator",
                             "Distribute security properties to security enforcement points.",
                             "https://egeria-project.org/services/omis/security-integrator/overview/",
                             "Security Manager OMAS",
                             PermittedSynchronization.TO_THIRD_PARTY),

    ANALYTICS_INTEGRATOR_OMIS(609,
                              ComponentDevelopmentStatus.IN_DEVELOPMENT,
                              "Analytics Integrator",
                              "Analytics Integrator OMIS",
                              "analytics-integrator",
                              "Exchange metadata with third party analytics tools.",
                              "https://egeria-project.org/services/omis/analytics-integrator/overview/",
                              "Data Science OMAS",
                              PermittedSynchronization.BOTH_DIRECTIONS),

    SEARCH_INTEGRATOR_OMIS(610,
                           ComponentDevelopmentStatus.IN_DEVELOPMENT,
                           "Search Integrator",
                           "Search Integrator OMIS",
                           "search-integrator",
                           "Store metadata with a third party technology that is focused on search efficiency.",
                           "https://egeria-project.org/services/omis/search-integrator/overview/",
                           "Asset Catalog OMAS",
                           PermittedSynchronization.BOTH_DIRECTIONS),

    INFRASTRUCTURE_INTEGRATOR_OMIS(611,
                                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                   "Infrastructure Integrator",
                                   "Infrastructure Integrator OMIS",
                                   "infrastructure-integrator",
                                   "Exchange information relating to IT infrastructure such as hosts, platforms, servers, server capabilities and services.",
                                   "https://egeria-project.org/services/omis/infrastructure-integrator/overview/",
                                   "IT infrastructure OMAS",
                                   PermittedSynchronization.BOTH_DIRECTIONS)
    ;

    private static final long     serialVersionUID    = 1L;

    private int                        integrationServiceCode;
    private ComponentDevelopmentStatus integrationServiceDevelopmentStatus   = null;
    private String                     integrationServiceName;
    private String                     integrationServiceFullName;
    private String                     integrationServiceURLMarker;
    private String                     integrationServiceDescription;
    private String                     integrationServiceWiki;
    private String                     integrationServicePartnerOMAS;
    private PermittedSynchronization   defaultPermittedSynchronization;


    /**
     * Default Constructor
     *
     * @param integrationServiceCode ordinal for this integration service
     * @param integrationServiceDevelopmentStatus development status
     * @param integrationServiceName symbolic name for this integration service
     * @param integrationServiceFullName full name for this integration service
     * @param integrationServiceURLMarker string used in URLs
     * @param integrationServiceDescription short description for this integration service
     * @param integrationServiceWiki wiki page for the integration service for this integration service
     * @param integrationServicePartnerOMAS name of the OMAS that is partnered with this integration service
     * @param defaultPermittedSynchronization synchronization pattern
     */
    IntegrationServiceDescription(int                        integrationServiceCode,
                                  ComponentDevelopmentStatus integrationServiceDevelopmentStatus,
                                  String                     integrationServiceName,
                                  String                     integrationServiceFullName,
                                  String                     integrationServiceURLMarker,
                                  String                     integrationServiceDescription,
                                  String                     integrationServiceWiki,
                                  String                     integrationServicePartnerOMAS,
                                  PermittedSynchronization   defaultPermittedSynchronization)
    {
        /*
         * Save the values supplied
         */
        this.integrationServiceCode              = integrationServiceCode;
        this.integrationServiceDevelopmentStatus = integrationServiceDevelopmentStatus;
        this.integrationServiceName              = integrationServiceName;
        this.integrationServiceFullName          = integrationServiceFullName;
        this.integrationServiceURLMarker         = integrationServiceURLMarker;
        this.integrationServiceDescription       = integrationServiceDescription;
        this.integrationServiceWiki              = integrationServiceWiki;
        this.integrationServicePartnerOMAS       = integrationServicePartnerOMAS;
        this.defaultPermittedSynchronization     = defaultPermittedSynchronization;
    }


    /**
     * Return the enum that corresponds with the supplied code.
     *
     * @param integrationServiceCode requested code
     * @return enum
     */
    public static IntegrationServiceDescription getIntegrationServiceDefinition(int integrationServiceCode)
    {
        for (IntegrationServiceDescription description : IntegrationServiceDescription.values())
        {
            if (integrationServiceCode == description.getIntegrationServiceCode())
            {
                return description;
            }
        }
        return null;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getIntegrationServiceCode()
    {
        return integrationServiceCode;
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
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getIntegrationServiceName()
    {
        return integrationServiceName;
    }


    /**
     * Return the formal name for this enum instance.
     *
     * @return String default name
     */
    public String getIntegrationServiceFullName()
    {
        return integrationServiceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     *
     * @return String default URL marker
     */
    public String getIntegrationServiceURLMarker()
    {
        return integrationServiceURLMarker;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getIntegrationServiceDescription()
    {
        return integrationServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this integration service.
     *
     * @return String URL name for the wiki page
     */
    public String getIntegrationServiceWiki()
    {
        return integrationServiceWiki;
    }


    /**
     * Return the full name of the Open Metadata Access Service (OMAS) that this integration service is partnered with.
     *
     * @return  Full name of OMAS
     */
    public String getIntegrationServicePartnerOMAS()
    {
        return integrationServicePartnerOMAS;
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
}
