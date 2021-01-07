/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import java.io.Serializable;

/**
 * AccessServiceDescription provides a list of registered OMAS services.
 */
public enum AccessServiceDescription implements Serializable
{
    ASSET_CATALOG_OMAS               (1000,
                                      "Asset Catalog",
                                      "Asset Catalog OMAS",
                                      "asset-catalog",
                                      "Search and understand your assets",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-catalog/"),
    ASSET_CONSUMER_OMAS              (1001,
                                      "Asset Consumer",
                                      "Asset Consumer OMAS",
                                      "asset-consumer",
                                      "Access assets through connectors",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-consumer/"),
    ASSET_LINEAGE_OMAS               (1023,
                                      "Asset Lineage",
                                      "Asset Lineage OMAS",
                                      "asset-lineage",
                                      "Store asset lineage",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-lineage/"),
    ASSET_MANAGER_OMAS               (1028,
                                      "Asset Manager",
                                      "Asset Manager OMAS",
                                      "asset-manager",
                                      "Manage metadata from a third party asset manager",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager/"),
    ASSET_OWNER_OMAS                 (1002,
                                      "Asset Owner",
                                      "Asset Owner OMAS",
                                      "asset-owner",
                                      "Manage an asset",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-owner/"),
    ANALYTICS_MODELING_OMAS			  (1024,
										"Analytics Modeling",
										"Analytics Modeling OMAS",
										"analytics-modeling",
										"Provides metadata information for Analytics Modeling.",
										"https://egeria.odpi.org/open-metadata-implementation/access-services/analytics-modeling/"),
    COMMUNITY_PROFILE_OMAS           (1003,
                                      "Community Profile",
                                      "Community Profile OMAS",
                                      "community-profile",
                                      "Define personal profile and collaborate",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/community-profile/"),
    CONNECTED_ASSET_OMAS             (1004,
            /* Deprecated */          "Connected Asset",
            /* Deprecated */          "Connected Asset OMAS",
                                      "null",
                                      "Understand an asset",
                                      "https://egeria.odpi.org/open-metadata-implementation/common-services/ocf-metadata-management/"),
    IT_INFRASTRUCTURE_OMAS           (1010,
                                      "IT Infrastructure",
                                      "IT Infrastructure OMAS",
                                      "it-infrastructure",
                                      "Manage information about the deployed IT infrastructure",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/it-infrastructure/"),
    DATA_ENGINE_OMAS                 (1021,
                                      "Data Engine",
                                      "Data Engine OMAS",
                                      "data-engine",
                                      "Exchange process models and lineage with a data engine",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/data-engine/"),
    DATA_PLATFORM_OMAS               (1005,
            /* Deprecated */          "Data Platform",
            /* Deprecated */          "Data Platform OMAS",
                                      "data-platform",
                                      "Capture changes to the data stores and data set managed by a data platform (deprecated and replaced by Data Manager OMAS)",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/data-platform/"),
    DATA_MANAGER_OMAS                (1025,
                                      "Data Manager",
                                      "Data Manager OMAS",
                                      "data-manager",
                                      "Capture changes to the data stores and data set managed by a data manager such as a database server, content manager or file system.",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/data-manager/"),
    DATA_PRIVACY_OMAS                (1016,
                                      "Data Privacy",
                                      "Data Privacy OMAS",
                                      "data-privacy",
                                      "Manage governance of privacy",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/data-privacy/"),
    DATA_SCIENCE_OMAS                (1006,
                                      "Data Science",
                                      "Data Science OMAS",
                                      "data-science",
                                      "Create and manage data science definitions and models",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/data-science/"),
    DESIGN_MODEL_OMAS                (1012,
                                      "Design Model",
                                      "Design Model OMAS",
                                      "design-model",
                                      "Exchange design model content with tools and standard packages",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/design-model/"),
    DEVOPS_OMAS                      (1007,
                                      "DevOps",
                                      "DevOps OMAS",
                                      "devops",
                                      "Manage a DevOps pipeline",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/dev-ops/"),
    DIGITAL_ARCHITECTURE_OMAS        (1026,
                                      "Digital Architecture",
                                      "Digital Architecture OMAS",
                                      "digital-architecture",
                                      "Design of the digital services for an organization",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/digital-architecture/"),
    DIGITAL_SERVICE_OMAS            ( 1027,
                                      "DigitalService",
                                      "Digital Service OMAS",
                                      "digital-service",
                                      "Manage a Digital Service Lifecycle",
                                        "https://egeria.odpi.org/open-metadata-implementation/access-services/digital-service/"),
    DISCOVERY_ENGINE_OMAS            (1015,
                                      "Discovery Engine",
                                      "Discovery Engine OMAS",
                                      "discovery-engine",
                                      "Support for automated metadata discovery engines",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/discovery-engine/"),
    GLOSSARY_VIEW_OMAS               (1022,
                                      "Glossary View",
                                      "Glossary View OMAS",
                                      "glossary-view",
                                      "Support glossary terms visualization",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/glossary-view/"),
    GOVERNANCE_ENGINE_OMAS           (1008,
                                      "Governance Engine",
                                      "Governance Engine OMAS",
                                      "governance-engine",
                                      "Set up an operational governance engine",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/governance-engine/"),
    GOVERNANCE_PROGRAM_OMAS          (1009,
                                      "Governance Program",
                                      "Governance Program OMAS",
                                      "governance-program",
                                      "Manage the governance program",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/governance-program/"),
    INFORMATION_VIEW_OMAS            (1014,
                                      "Information View",
                                      "Information View OMAS",
                                      "information-view",
                                      "Support information virtualization and data set definitions",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/information-view/"),
    PROJECT_MANAGEMENT_OMAS          (1017,
                                      "Project Management",
                                      "Project Management OMAS",
                                      "project-management",
                                      "Manage data projects",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/project-management/"),
    SECURITY_OFFICER_OMAS            (1013,
                                      "Security Officer",
                                      "Security Officer OMAS",
                                      "security-officer",
                                      "Set up rules and security tags to protect data",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/security-officer/"),
    SOFTWARE_DEVELOPER_OMAS          (1018,
                                      "Software Developer",
                                      "Software Developer OMAS",
                                      "software-developer",
                                      "Interact with software development tools",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/software-developer/"),
    STEWARDSHIP_ACTION_OMAS          (1019,
                                      "Stewardship Action",
                                      "Stewardship Action OMAS",
                                      "stewardship-action",
                                      "Manage exceptions and actions from open governance",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/stewardship-action/"),
    SUBJECT_AREA_OMAS                (1020,
                                      "Subject Area",
                                      "Subject Area OMAS",
                                      "subject-area",
                                      "Document knowledge about a subject area",
                                      "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/");

    private static final long     serialVersionUID    = 1L;

    private int    accessServiceCode;
    private String accessServiceName;
    private String accessServiceFullName;
    private String accessServiceURLMarker;
    private String accessServiceDescription;
    private String accessServiceWiki;


    /**
     * Default Constructor
     *
     * @param accessServiceCode ordinal for this access service
     * @param accessServiceName symbolic name for this access service
     * @param accessServiceFullName full name for this access service
     * @param accessServiceURLMarker string used in URLs
     * @param accessServiceDescription short description for this access service
     * @param accessServiceWiki wiki page for the access service for this access service
     */
    AccessServiceDescription(int    accessServiceCode,
                             String accessServiceName,
                             String accessServiceFullName,
                             String accessServiceURLMarker,
                             String accessServiceDescription,
                             String accessServiceWiki)
    {
        /*
         * Save the values supplied
         */
        this.accessServiceCode = accessServiceCode;
        this.accessServiceName = accessServiceName;
        this.accessServiceFullName = accessServiceFullName;
        this.accessServiceURLMarker = accessServiceURLMarker;
        this.accessServiceDescription = accessServiceDescription;
        this.accessServiceWiki = accessServiceWiki;
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
     * @return String URL name for the wiki page
     */
    public String getAccessServiceWiki()
    {
        return accessServiceWiki;
    }
}
