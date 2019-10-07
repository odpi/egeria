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
                                      "asset-catalog",
                                      "Search and understand your assets",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-catalog/"),
    ASSET_CONSUMER_OMAS              (1001,
                                      "Asset Consumer",
                                      "asset-consumer",
                                      "Access assets through connectors",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-consumer/"),
    ASSET_LINEAGE_OMAS               (1023,
                                      "Asset Lineage",
                                      "asset-lineage",
                                      "Store asset lineage",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-lineage/"),
    ASSET_OWNER_OMAS                 (1002,
                                      "Asset Owner",
                                      "asset-owner",
                                      "Manage an asset",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-owner/"),
    COMMUNITY_PROFILE_OMAS           (1003,
                                      "Community Profile",
                                      "community-profile",
                                      "Define personal profile and collaborate",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/community-profile/"),
    CONNECTED_ASSET_OMAS             (1004,
    /* Deprecated */                  "Connected Asset",
                                      "null",
                                      "Understand an asset",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/connected-asset/"),
    IT_INFRASTRUCTURE_OMAS           (1010,
                                      "IT Infrastructure",
                                      "it-infrastructure",
                                      "Manage information about the deployed IT infrastructure",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/it-infrastructure/"),
    DATA_ENGINE_OMAS                 (1021,
                                      "Data Engine",
                                      "data-engine",
                                      "Exchange process models and lineage with a data engine",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-engine/"),
    DATA_PLATFORM_OMAS               (1005,
                                      "Data Platform",
                                      "data-platform",
                                      "Capture changes to the data stores and data set managed by a data platform",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-platform/"),
    DATA_PRIVACY_OMAS                (1016,
                                      "Data Privacy",
                                      "data-privacy",
                                      "Manage governance of privacy",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-privacy/"),
    DATA_SCIENCE_OMAS                (1006,
                                      "Data Science",
                                      "data-science",
                                      "Create and manage data science definitions and models",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-science/"),
    DESIGN_MODEL_OMAS                (1012,
                                      "Design Model",
                                      "design-model",
                                      "Exchange design model content with tools and standard packages",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/design-model/"),
    DEVOPS_OMAS                      (1007,
                                      "DevOps",
                                      "devops",
                                      "Manage a DevOps pipeline",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/dev-ops/"),
    DIGITAL_ARCHITECTURE_OMAS        (1013,
                                      "Digital Architecture",
                                      "digital-architecture",
                                      "Design of the digital services for an organization",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/digital-architecture/"),
    DISCOVERY_ENGINE_OMAS            (1015,
                                      "Discovery Engine",
                                      "discovery-engine",
                                      "Support for automated metadata discovery engines",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/discovery-engine/"),
    GLOSSARY_VIEW_OMAS               (1022,
                                      "Glossary View",
                                      "glossary-view",
                                      "Support glossary terms visualization",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/glossary-view/"),
    GOVERNANCE_ENGINE_OMAS           (1008,
                                      "Governance Engine",
                                      "governance-engine",
                                      "Set up an operational governance engine",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/governance-engine/"),
    GOVERNANCE_PROGRAM_OMAS          (1009,
                                      "Governance Program",
                                      "governance-program",
                                      "Manage the governance program",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/governance-program/"),
    INFORMATION_VIEW_OMAS            (1014,
                                      "Information View",
                                      "information-view",
                                      "Support information virtualization and data set definitions",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/information-view/"),
    PROJECT_MANAGEMENT_OMAS          (1017,
                                      "Project Management",
                                      "project-management",
                                      "Manage data projects",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/project-management/"),
    SECURITY_OFFICER_OMAS            (1013,
                                      "Security Officer",
                                      "security-officer",
                                      "Set up rules to protect data",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/security-officer/"),
    SOFTWARE_DEVELOPER_OMAS          (1018,
                                      "Software Developer",
                                      "software-developer",
                                      "Interact with software development tools",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/software-developer/"),
    STEWARDSHIP_ACTION_OMAS          (1019,
                                      "Stewardship Action",
                                      "stewardship-action",
                                      "Manage exceptions and actions from open governance",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/stewardship-action/"),
    SUBJECT_AREA_OMAS                (1020,
                                      "Subject Area",
                                      "subject-area",
                                      "Document knowledge about a subject area",
                                      "https://odpi.github.io/egeria/open-metadata-implementation/access-services/subject-area/");

    private static final long     serialVersionUID    = 1L;

    private int    accessServiceCode;
    private String accessServiceName;
    private String accessServiceURLMarker;
    private String accessServiceDescription;
    private String accessServiceWiki;


    /**
     * Default Constructor
     *
     * @param accessServiceCode ordinal for this access service
     * @param accessServiceURLMarker string used in URLs
     * @param accessServiceName symbolic name for this access service
     * @param accessServiceDescription short description for this access service
     * @param accessServiceWiki wiki page for the access service for this access service
     */
    AccessServiceDescription(int                            accessServiceCode,
                             String                         accessServiceName,
                             String                         accessServiceURLMarker,
                             String                         accessServiceDescription,
                             String                         accessServiceWiki)
    {
        /*
         * Save the values supplied
         */
        this.accessServiceCode = accessServiceCode;
        this.accessServiceName = accessServiceName;
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
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
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
