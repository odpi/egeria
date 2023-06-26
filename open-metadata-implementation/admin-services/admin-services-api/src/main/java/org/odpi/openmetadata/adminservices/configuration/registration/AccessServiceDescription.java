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
    ASSET_CATALOG_OMAS               (200,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Asset Catalog",
                                      "Asset Catalog OMAS",
                                      "asset-catalog",
                                      "Search and understand your assets",
                                      "https://egeria-project.org/services/omas/asset-catalog/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    ASSET_CONSUMER_OMAS              (201,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Asset Consumer",
                                      "Asset Consumer OMAS",
                                      "asset-consumer",
                                      "Access assets through connectors",
                                      "https://egeria-project.org/services/omas/asset-consumer/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    ASSET_LINEAGE_OMAS               (203,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Asset Lineage",
                                      "Asset Lineage OMAS",
                                      "asset-lineage",
                                      "Store asset lineage",
                                      "https://egeria-project.org/services/omas/asset-lineage/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    ASSET_MANAGER_OMAS               (204,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Asset Manager",
                                      "Asset Manager OMAS",
                                      "asset-manager",
                                      "Manage metadata from a third party asset manager",
                                      "https://egeria-project.org/services/omas/asset-manager/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),

    ASSET_OWNER_OMAS                 (205,
                                      ComponentDevelopmentStatus.STABLE,
                                      "Asset Owner",
                                      "Asset Owner OMAS",
                                      "asset-owner",
                                      "Manage an asset",
                                      "https://egeria-project.org/services/omas/asset-owner/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    ANALYTICS_MODELING_OMAS			 (206,
                                         ComponentDevelopmentStatus.DEPRECATED,
                                         "Analytics Modeling",
									  "Analytics Modeling OMAS",
									  "analytics-modeling",
									  "Provides metadata information for Analytics Modeling.",
								      "",
                                         ServiceOperationalStatus.NOT_IMPLEMENTED,
                                         ServiceOperationalStatus.NOT_IMPLEMENTED),

    COMMUNITY_PROFILE_OMAS           (207,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Community Profile",
                                      "Community Profile OMAS",
                                      "community-profile",
                                      "Define personal profile and collaborate",
                                      "https://egeria-project.org/services/omas/community-profile/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    IT_INFRASTRUCTURE_OMAS           (208,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "IT Infrastructure",
                                      "IT Infrastructure OMAS",
                                      "it-infrastructure",
                                      "Manage information about the deployed IT infrastructure",
                                      "https://egeria-project.org/services/omas/it-infrastructure/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    DATA_ENGINE_OMAS                 (209,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Data Engine",
                                      "Data Engine OMAS",
                                      "data-engine",
                                      "Exchange process models and lineage with a data engine",
                                      "https://egeria-project.org/services/omas/data-engine/overview/",
                                      ServiceOperationalStatus.ENABLED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    DATA_MANAGER_OMAS                (210,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Data Manager",
                                      "Data Manager OMAS",
                                      "data-manager",
                                      "Capture changes to the data stores and data set managed by a data manager such as a database server, content manager or file system.",
                                      "https://egeria-project.org/services/omas/data-manager/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    DATA_PRIVACY_OMAS                (211,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Data Privacy",
                                      "Data Privacy OMAS",
                                      "data-privacy",
                                      "Manage governance of privacy",
                                      "https://egeria-project.org/services/omas/data-privacy/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    DATA_SCIENCE_OMAS                (212,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Data Science",
                                      "Data Science OMAS",
                                      "data-science",
                                      "Create and manage data science definitions and models",
                                      "https://egeria-project.org/services/omas/data-science/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    DESIGN_MODEL_OMAS                (213,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Design Model",
                                      "Design Model OMAS",
                                      "design-model",
                                      "Exchange design model content with tools and standard packages",
                                      "https://egeria-project.org/services/omas/design-model/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    DEVOPS_OMAS                      (214,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "DevOps",
                                      "DevOps OMAS",
                                      "devops",
                                      "Manage a DevOps pipeline",
                                      "https://egeria-project.org/services/omas/dev-ops/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED
    ),
    DIGITAL_ARCHITECTURE_OMAS        (215,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Digital Architecture",
                                      "Digital Architecture OMAS",
                                      "digital-architecture",
                                      "Design of the digital services for an organization",
                                      "https://egeria-project.org/services/omas/digital-architecture/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    DIGITAL_SERVICE_OMAS            ( 216,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Digital Service",
                                      "Digital Service OMAS",
                                      "digital-service",
                                      "Manage a digital service through its lifecycle",
                                      "https://egeria-project.org/services/omas/digital-service/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    DISCOVERY_ENGINE_OMAS            (217,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Discovery Engine",
                                      "Discovery Engine OMAS",
                                      "discovery-engine",
                                      "Support for automated metadata discovery engines",
                                      "https://egeria-project.org/services/omas/discovery-engine/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    GLOSSARY_VIEW_OMAS               (218,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Glossary View",
                                      "Glossary View OMAS",
                                      "glossary-view",
                                      "Support glossary terms visualization",
                                      "https://egeria-project.org/services/omas/glossary-view/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),

    GOVERNANCE_ENGINE_OMAS           (219,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Governance Engine",
                                      "Governance Engine OMAS",
                                      "governance-engine",
                                      "Set up an operational governance engine",
                                      "https://egeria-project.org/services/omas/governance-engine/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    GOVERNANCE_PROGRAM_OMAS          (220,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Governance Program",
                                      "Governance Program OMAS",
                                      "governance-program",
                                      "Manage the governance program",
                                      "https://egeria-project.org/services/omas/governance-program/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    PROJECT_MANAGEMENT_OMAS          (221,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Project Management",
                                      "Project Management OMAS",
                                      "project-management",
                                      "Manage governance related projects",
                                      "https://egeria-project.org/services/omas/project-management/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    SECURITY_MANAGER_OMAS           (222,
                                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                     "Security Manager",
                                      "Security Manager OMAS",
                                      "security-manager",
                                      "Manages exchange of metadata with a security service",
                                      "https://egeria-project.org/services/omas/security-manager/overview/",
                                     ServiceOperationalStatus.NOT_IMPLEMENTED,
                                     ServiceOperationalStatus.ENABLED),
    SECURITY_OFFICER_OMAS            (223,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Security Officer",
                                      "Security Officer OMAS",
                                      "security-officer",
                                      "Set up rules and security tags to protect data",
                                      "https://egeria-project.org/services/omas/security-officer/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    SOFTWARE_DEVELOPER_OMAS          (224,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Software Developer",
                                      "Software Developer OMAS",
                                      "software-developer",
                                      "Interact with software development tools",
                                      "https://egeria-project.org/services/omas/software-developer/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED),
    STEWARDSHIP_ACTION_OMAS          (225,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "Stewardship Action",
                                      "Stewardship Action OMAS",
                                      "stewardship-action",
                                      "Manage exceptions and actions from open governance",
                                      "https://egeria-project.org/services/omas/stewardship-action/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.ENABLED),
    SUBJECT_AREA_OMAS                (226,
                                      ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                      "Subject Area",
                                      "Subject Area OMAS",
                                      "subject-area",
                                      "Document knowledge about a subject area",
                                      "https://egeria-project.org/services/omas/subject-area/overview/",
                                      ServiceOperationalStatus.NOT_IMPLEMENTED,
                                      ServiceOperationalStatus.NOT_IMPLEMENTED);

    private static final long     serialVersionUID    = 1L;

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
