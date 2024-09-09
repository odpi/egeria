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
     * Access assets through connectors.
     */
    ASSET_CONSUMER_OMAS              (201,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Asset Consumer",
                                      "Asset Consumer OMAS",
                                      "asset-consumer",
                                      "Access assets through connectors.",
                                      "https://egeria-project.org/services/omas/asset-consumer/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Publish asset lineage.
     */
    ASSET_LINEAGE_OMAS               (203,
                                      ComponentDevelopmentStatus.DEPRECATED,
                                      "Asset Lineage",
                                      "Asset Lineage OMAS",
                                      "asset-lineage",
                                      "Publish asset lineage",
                                      "https://egeria-project.org/services/omas/asset-lineage/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Manage metadata from a third party asset manager.
     */
    ASSET_MANAGER_OMAS               (204,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Asset Manager",
                                      "Asset Manager OMAS",
                                      "asset-manager",
                                      "Manage metadata from a third party asset manager",
                                      "https://egeria-project.org/services/omas/asset-manager/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Manage an asset.
     */
    ASSET_OWNER_OMAS                 (205,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Asset Owner",
                                      "Asset Owner OMAS",
                                      "asset-owner",
                                      "Manage an asset",
                                      "https://egeria-project.org/services/omas/asset-owner/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Provides metadata information for Analytics Modeling.
     */
    ANALYTICS_MODELING_OMAS			 (206,
                                         ComponentDevelopmentStatus.DEPRECATED,
                                         "Analytics Modeling",
									  "Analytics Modeling OMAS",
									  "analytics-modeling",
									  "Provides metadata information for Analytics Modeling.",
								      "",
                                         ServiceOperationalStatus.NOT_IMPLEMENTED,
                                         ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Define personal profile and collaborate
     */
    COMMUNITY_PROFILE_OMAS           (207,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Community Profile",
                                      "Community Profile OMAS",
                                      "community-profile",
                                      "Define personal profile and collaborate.",
                                      "https://egeria-project.org/services/omas/community-profile/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Manage information about the deployed IT infrastructure.
     */
    IT_INFRASTRUCTURE_OMAS           (208,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "IT Infrastructure",
                                      "IT Infrastructure OMAS",
                                      "it-infrastructure",
                                      "Manage information about the deployed IT infrastructure.",
                                      "https://egeria-project.org/services/omas/it-infrastructure/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Exchange process models and lineage with a data engine.
     */
    DATA_ENGINE_OMAS                 (209,
                                      ComponentDevelopmentStatus.DEPRECATED,
                                      "Data Engine",
                                      "Data Engine OMAS",
                                      "data-engine",
                                      "Exchange process models and lineage with a data engine.",
                                      "https://egeria-project.org/services/omas/data-engine/overview/",
                                      ServiceOperationalStatus.ENABLED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Capture changes to the data stores and data set managed by a data manager such as a database server, content manager or file system.
     */
    DATA_MANAGER_OMAS                (210,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Data Manager",
                                      "Data Manager OMAS",
                                      "data-manager",
                                      "Capture changes to the data stores and data set managed by a data manager such as a database server, content manager or file system.",
                                      "https://egeria-project.org/services/omas/data-manager/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Create and manage data science definitions and models.
     */
    DATA_SCIENCE_OMAS                (212,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Data Science",
                                      "Data Science OMAS",
                                      "data-science",
                                      "Create and manage data science definitions and models.",
                                      "https://egeria-project.org/services/omas/data-science/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Exchange design model content with tools and standard packages.
     */
    DESIGN_MODEL_OMAS                (213,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Design Model",
                                      "Design Model OMAS",
                                      "design-model",
                                      "Exchange design model content with tools and standard packages.",
                                      "https://egeria-project.org/services/omas/design-model/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Design of the digital services for an organization.
     */
    DIGITAL_ARCHITECTURE_OMAS        (215,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Digital Architecture",
                                      "Digital Architecture OMAS",
                                      "digital-architecture",
                                      "Design of the digital services for an organization",
                                      "https://egeria-project.org/services/omas/digital-architecture/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Manage a digital service through its lifecycle.
     */
    DIGITAL_SERVICE_OMAS            ( 216,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Digital Service",
                                      "Digital Service OMAS",
                                      "digital-service",
                                      "Manage a digital service through its lifecycle.",
                                      "https://egeria-project.org/services/omas/digital-service/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Support for automated open discovery services.
     */
    DISCOVERY_ENGINE_OMAS            (217,
                                      ComponentDevelopmentStatus.DEPRECATED,
                                      "Discovery Engine",
                                      "Discovery Engine OMAS",
                                      "discovery-engine",
                                      "Support for automated open discovery services.",
                                      "https://egeria-project.org/services/omas/discovery-engine/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Support glossary terms visualization.
     */
    GLOSSARY_VIEW_OMAS               (218,
                                      ComponentDevelopmentStatus.DEPRECATED,
                                      "Glossary View",
                                      "Glossary View OMAS",
                                      "glossary-view",
                                      "Support glossary terms visualization.",
                                      "https://egeria-project.org/services/omas/glossary-view/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Provide metadata services and watch dog notification to the governance action services.
     */
    GOVERNANCE_ENGINE_OMAS           (219,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Governance Engine",
                                      "Governance Engine OMAS",
                                      "governance-engine",
                                      "Provide metadata services and watch dog notification to the governance action services.",
                                      "https://egeria-project.org/services/omas/governance-engine/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Manage the governance program.
     */
    GOVERNANCE_PROGRAM_OMAS          (220,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Governance Program",
                                      "Governance Program OMAS",
                                      "governance-program",
                                      "Manage the governance program.",
                                      "https://egeria-project.org/services/omas/governance-program/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Manage governance related projects.
     */
    PROJECT_MANAGEMENT_OMAS          (221,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Project Management",
                                      "Project Management OMAS",
                                      "project-management",
                                      "Manage governance related projects.",
                                      "https://egeria-project.org/services/omas/project-management/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Manages exchange of metadata with a security service.
     */
    SECURITY_MANAGER_OMAS           (222,
                                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                     "Security Manager",
                                      "Security Manager OMAS",
                                      "security-manager",
                                      "Manages exchange of metadata with a security service.",
                                      "https://egeria-project.org/services/omas/security-manager/overview/",
                                     ServiceOperationalStatus.NOT_IMPLEMENTED,
                                     ServiceOperationalStatus.ENABLED),

    /**
     * Interact with software development tools.
     */
    SOFTWARE_DEVELOPER_OMAS          (224,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Software Developer",
                                      "Software Developer OMAS",
                                      "software-developer",
                                      "Interact with software development tools.",
                                      "https://egeria-project.org/services/omas/software-developer/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Manage exceptions and actions from open governance.
     */
    STEWARDSHIP_ACTION_OMAS          (225,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Stewardship Action",
                                      "Stewardship Action OMAS",
                                      "stewardship-action",
                                      "Manage exceptions and actions from open governance.",
                                      "https://egeria-project.org/services/omas/stewardship-action/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    /**
     * Document knowledge about a subject area in a glossary.
     */
    SUBJECT_AREA_OMAS                (226,
                                      ComponentDevelopmentStatus.DEPRECATED,
                                      "Subject Area",
                                      "Subject Area OMAS",
                                      "subject-area",
                                      "Document knowledge about a subject area in a glossary.",
                                      "https://egeria-project.org/services/omas/subject-area/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    /**
     * Supply the governance engine definitions to the engine hosts and the and integration group definitions to the integration daemons.
     */
    GOVERNANCE_SERVER_OMAS           (227,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Governance Server",
                                      "Governance Server OMAS",
                                      "governance-server",
                                      "Supply the governance engine definitions to the engine hosts and the and integration group definitions to the integration daemons.",
                                      "https://egeria-project.org/services/omas/governance-server/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    ;


    private final int                        accessServiceCode;
    private final ComponentDevelopmentStatus accessServiceDevelopmentStatus;
    private final String                     accessServiceName;
    private final String                     accessServiceFullName;
    private final String                     accessServiceURLMarker;
    private final String                     accessServiceDescription;
    private final String                     accessServiceWiki;
    private final ServiceOperationalStatus   accessServiceInTopicStatus;
    private final ServiceOperationalStatus   accessServiceOutTopicStatus;


    /**
     * Default Constructor
     *
     * @param accessServiceCode ordinal for this access service
     * @param accessServiceDevelopmentStatus development status
     * @param accessServiceName symbolic name for this access service
     * @param accessServiceFullName full name for this access service
     * @param accessServiceURLMarker string used in URLs
     * @param accessServiceDescription short description for this access service
     * @param accessServiceWiki wiki page for the access service for this access service
     * @param accessServiceInTopicStatus is the access service inTopic implemented, operational or disabled?
     * @param accessServiceOutTopicStatus is the access service outTopic implemented, operational or disabled?
     */
    AccessServiceDescription(int                        accessServiceCode,
                             ComponentDevelopmentStatus accessServiceDevelopmentStatus,
                             String                     accessServiceName,
                             String                     accessServiceFullName,
                             String                     accessServiceURLMarker,
                             String                     accessServiceDescription,
                             String                     accessServiceWiki,
                             ServiceOperationalStatus   accessServiceInTopicStatus,
                             ServiceOperationalStatus   accessServiceOutTopicStatus)
    {
        /*
         * Save the values supplied
         */
        this.accessServiceCode = accessServiceCode;
        this.accessServiceDevelopmentStatus = accessServiceDevelopmentStatus;
        this.accessServiceName = accessServiceName;
        this.accessServiceFullName = accessServiceFullName;
        this.accessServiceURLMarker = accessServiceURLMarker;
        this.accessServiceDescription = accessServiceDescription;
        this.accessServiceWiki = accessServiceWiki;
        this.accessServiceInTopicStatus = accessServiceInTopicStatus;
        this.accessServiceOutTopicStatus = accessServiceOutTopicStatus;
    }


    /**
     * Return the enum that corresponds with the supplied code.
     *
     * @param accessServiceCode requested code
     * @return enum
     */
    public static AccessServiceDescription getAccessServiceDefinition(int accessServiceCode)
    {
        for (AccessServiceDescription description : AccessServiceDescription.values())
        {
            if (accessServiceCode == description.getAccessServiceCode())
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
    public int getAccessServiceCode()
    {
        return accessServiceCode;
    }


    /**
     * Return the development status of the service.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getAccessServiceDevelopmentStatus()
    {
        return accessServiceDevelopmentStatus;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getAccessServiceName()
    {
        return accessServiceName;
    }


    /**
     * Return the formal name for this enum instance.
     *
     * @return String default name
     */
    public String getAccessServiceFullName()
    {
        return accessServiceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default URL marker
     */
    public String getAccessServiceURLMarker()
    {
        return accessServiceURLMarker;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getAccessServiceDescription()
    {
        return accessServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this access service.
     *
     * @return String URL for the wiki page
     */
    public String getAccessServiceWiki()
    {
        return accessServiceWiki;
    }


    /**
     * Return the status of the access services' in topic.
     *
     * @return not-implemented, enabled, disabled
     */
    public ServiceOperationalStatus getAccessServiceInTopicStatus()
    {
        return accessServiceInTopicStatus;
    }


    /**
     * Return the status of the access services' out topic.
     *
     * @return not-implemented, enabled, disabled
     */
    public ServiceOperationalStatus getAccessServiceOutTopicStatus()
    {
        return accessServiceOutTopicStatus;
    }
}
