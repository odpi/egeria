/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;


/**
 * IntegrationServiceDescription provides a list of registered integration services.
 */
public enum IntegrationServiceDescription
{
    /**
     * Catalog Integrator OMIS - Exchange metadata with third party data catalogs.
     */
    CATALOG_INTEGRATOR_OMIS(600,
                            ComponentDevelopmentStatus.STABLE,
                            "Catalog Integrator",
                            "Catalog Integrator OMIS",
                            "catalog-integrator",
                            "Exchange metadata with third party data catalogs.",
                            "https://egeria-project.org/services/omis/catalog-integrator/overview/",
                            DeployedImplementationType.CATALOG_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                            AccessServiceDescription.ASSET_MANAGER_OMAS,
                            PermittedSynchronization.BOTH_DIRECTIONS),


    /**
     * API Integrator OMIS - Exchange metadata with third party API Gateways.
     */
    API_INTEGRATOR_OMIS(601,
                        ComponentDevelopmentStatus.STABLE,
                        "API Integrator",
                        "API Integrator OMIS",
                        "api-integrator",
                        "Exchange metadata with third party API Gateways.",
                        "https://egeria-project.org/services/omis/api-integrator/overview/",
                        DeployedImplementationType.API_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                        AccessServiceDescription.DATA_MANAGER_OMAS,
                        PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Topic Integrator OMIS - Exchange metadata with third party event-based brokers.
     */
    TOPIC_INTEGRATOR_OMIS(602,
                          ComponentDevelopmentStatus.STABLE,
                          "Topic Integrator",
                          "Topic Integrator OMIS",
                          "topic-integrator",
                          "Exchange metadata with third party event-based brokers.",
                          "https://egeria-project.org/services/omis/topic-integrator/overview/",
                          DeployedImplementationType.TOPIC_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                          AccessServiceDescription.DATA_MANAGER_OMAS,
                          PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Display Integrator OMIS - Exchange metadata with applications that display data to users.
     */
    DISPLAY_INTEGRATOR_OMIS(603,
                            ComponentDevelopmentStatus.STABLE,
                            "Display Integrator",
                            "Display Integrator OMIS",
                            "display-integrator",
                            "Exchange metadata with applications that display data to users.",
                            "https://egeria-project.org/services/omis/display-integrator/overview/",
                            DeployedImplementationType.DISPLAY_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                            AccessServiceDescription.DATA_MANAGER_OMAS,
                            PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Database Integrator OMIS - Extract metadata such as schema, tables and columns from database managers.
     */
    DATABASE_INTEGRATOR_OMIS     (604,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Database Integrator",
                                  "Database Integrator OMIS",
                                  "database-integrator",
                                  "Extract metadata such as schema, tables and columns from database managers.",
                                  "https://egeria-project.org/services/omis/database-integrator/overview/",
                                  DeployedImplementationType.DATABASE_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                                  AccessServiceDescription.DATA_MANAGER_OMAS,
                                  PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Files Integrator OMIS - Extract metadata about files stored in a file system or file manager.
     */
    FILES_INTEGRATOR_OMIS(605,
                          ComponentDevelopmentStatus.STABLE,
                          "Files Integrator",
                          "Files Integrator OMIS",
                          "files-integrator",
                          "Extract metadata about files stored in a file system or file manager.",
                          "https://egeria-project.org/services/omis/files-integrator/overview/",
                          DeployedImplementationType.FILES_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                          AccessServiceDescription.DATA_MANAGER_OMAS,
                          PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Lineage Integrator OMIS - Manage exchange of lineage with a third party tool.
     */
    LINEAGE_INTEGRATOR_OMIS(606,
                            ComponentDevelopmentStatus.STABLE,
                            "Lineage Integrator",
                            "Lineage Integrator OMIS",
                            "lineage-integrator",
                            "Manage exchange of lineage with a third party tool.",
                            "https://egeria-project.org/services/omis/lineage-integrator/overview/",
                            DeployedImplementationType.LINEAGE_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                            AccessServiceDescription.ASSET_MANAGER_OMAS,
                            PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Organization Integrator OMIS - Load information about the teams and people in an organization and return collaboration activity.
     */
    ORGANIZATION_INTEGRATOR_OMIS     (607,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Organization Integrator",
                                      "Organization Integrator OMIS",
                                      "organization-integrator",
                                      "Load information about the teams and people in an organization and return collaboration activity.",
                                      "https://egeria-project.org/services/omis/organization-integrator/overview/",
                                      DeployedImplementationType.ORGANIZATION_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                                      AccessServiceDescription.COMMUNITY_PROFILE_OMAS,
                                      PermittedSynchronization.FROM_THIRD_PARTY),

    /**
     * Security Integrator OMIS - Distribute security properties to security enforcement points.
     */
    SECURITY_INTEGRATOR_OMIS(608,
                             ComponentDevelopmentStatus.IN_DEVELOPMENT,
                             "Security Integrator",
                             "Security Integrator OMIS",
                             "security-integrator",
                             "Distribute security properties to security enforcement points.",
                             "https://egeria-project.org/services/omis/security-integrator/overview/",
                             DeployedImplementationType.SECURITY_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                             AccessServiceDescription.SECURITY_MANAGER_OMAS,
                             PermittedSynchronization.TO_THIRD_PARTY),

    /**
     * Analytics Integrator OMIS - Exchange metadata with third party analytics tools.
     */
    ANALYTICS_INTEGRATOR_OMIS(609,
                              ComponentDevelopmentStatus.IN_DEVELOPMENT,
                              "Analytics Integrator",
                              "Analytics Integrator OMIS",
                              "analytics-integrator",
                              "Exchange metadata with third party analytics tools.",
                              "https://egeria-project.org/services/omis/analytics-integrator/overview/",
                              DeployedImplementationType.ANALYTICS_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                              AccessServiceDescription.DATA_SCIENCE_OMAS,
                              PermittedSynchronization.BOTH_DIRECTIONS),


    /**
     * Search Integrator OMIS - Store metadata with a third party technology that is focused on search efficiency.
     */
    SEARCH_INTEGRATOR_OMIS(610,
                           ComponentDevelopmentStatus.DEPRECATED,
                           "Search Integrator",
                           "Search Integrator OMIS",
                           "search-integrator",
                           "Store metadata with a third party technology that is focused on search efficiency.",
                           "https://egeria-project.org/services/omis/search-integrator/overview/",
                           DeployedImplementationType.SEARCH_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                           AccessServiceDescription.ASSET_CONSUMER_OMAS,
                           PermittedSynchronization.BOTH_DIRECTIONS),

    /**
     * Infrastructure Integrator OMIS - Exchange information relating to IT infrastructure such as hosts, platforms, servers, server capabilities and services.
     */
    INFRASTRUCTURE_INTEGRATOR_OMIS(611,
                                   ComponentDevelopmentStatus.STABLE,
                                   "Infrastructure Integrator",
                                   "Infrastructure Integrator OMIS",
                                   "infrastructure-integrator",
                                   "Exchange information relating to IT infrastructure such as hosts, platforms, servers, server capabilities and services.",
                                   "https://egeria-project.org/services/omis/infrastructure-integrator/overview/",
                                   DeployedImplementationType.INFRASTRUCTURE_INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                                   AccessServiceDescription.IT_INFRASTRUCTURE_OMAS,
                                   PermittedSynchronization.BOTH_DIRECTIONS)
    ;


    private final int                        integrationServiceCode;
    private final ComponentDevelopmentStatus integrationServiceDevelopmentStatus;
    private final String                     integrationServiceName;
    private final String                     integrationServiceFullName;
    private final String                     integrationServiceURLMarker;
    private final String                     integrationServiceDescription;
    private final String                     integrationServiceWiki;
    private final String                     connectorDeployedImplementationType;
    private final AccessServiceDescription   integrationServicePartnerOMAS;
    private final PermittedSynchronization   defaultPermittedSynchronization;


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
     * @param connectorDeployedImplementationType the deployedImplementationType that represents the specific type of
     *                                            integration connector supported by this integration service
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
                                  String                     connectorDeployedImplementationType,
                                  AccessServiceDescription   integrationServicePartnerOMAS,
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
        this.connectorDeployedImplementationType = connectorDeployedImplementationType;
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
     * @return  Description of OMAS
     */
    public AccessServiceDescription getIntegrationServicePartnerOMAS()
    {
        return integrationServicePartnerOMAS;
    }


    /**
     * Return the deployedImplementationType for the specific type of integration connector supported by this service.
     *
     * @return deployedImplementationType
     */
    public String getConnectorDeployedImplementationType()
    {
        return connectorDeployedImplementationType;
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
