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
                                      "https://odpi.github.io/egeria-docs/services/omas/asset-catalog/overview/"),
    ASSET_CONSUMER_OMAS              (1001,
                                      "Asset Consumer",
                                      "Asset Consumer OMAS",
                                      "asset-consumer",
                                      "Access assets through connectors",
                                      "https://odpi.github.io/egeria-docs/services/omas/asset-consumer/overview/"),
    ASSET_LINEAGE_OMAS               (1023,
                                      "Asset Lineage",
                                      "Asset Lineage OMAS",
                                      "asset-lineage",
                                      "Store asset lineage",
                                      "https://odpi.github.io/egeria-docs/services/omas/asset-lineage/overview/"),
    ASSET_MANAGER_OMAS               (1028,
                                      "Asset Manager",
                                      "Asset Manager OMAS",
                                      "asset-manager",
                                      "Manage metadata from a third party asset manager",
                                      "https://odpi.github.io/egeria-docs/services/omas/asset-manager/overview/"),
    ASSET_OWNER_OMAS                 (1002,
                                      "Asset Owner",
                                      "Asset Owner OMAS",
                                      "asset-owner",
                                      "Manage an asset",
                                      "https://odpi.github.io/egeria-docs/services/omas/asset-owner/overview/"),
    ANALYTICS_MODELING_OMAS			 (1024,
                                      "Analytics Modeling",
									  "Analytics Modeling OMAS",
									  "analytics-modeling",
									  "Provides metadata information for Analytics Modeling.",
								      "https://odpi.github.io/egeria-docs/services/omas/analytics-modeling/overview/"),
    COMMUNITY_PROFILE_OMAS           (1003,
                                      "Community Profile",
                                      "Community Profile OMAS",
                                      "community-profile",
                                      "Define personal profile and collaborate",
                                      "https://odpi.github.io/egeria-docs/services/omas/community-profile/overview/"),
    IT_INFRASTRUCTURE_OMAS           (1010,
                                      "IT Infrastructure",
                                      "IT Infrastructure OMAS",
                                      "it-infrastructure",
                                      "Manage information about the deployed IT infrastructure",
                                      "https://odpi.github.io/egeria-docs/services/omas/it-infrastructure/overview/"),
    DATA_ENGINE_OMAS                 (1021,
                                      "Data Engine",
                                      "Data Engine OMAS",
                                      "data-engine",
                                      "Exchange process models and lineage with a data engine",
                                      "https://odpi.github.io/egeria-docs/services/omas/data-engine/overview/"),
    DATA_MANAGER_OMAS                (1025,
                                      "Data Manager",
                                      "Data Manager OMAS",
                                      "data-manager",
                                      "Capture changes to the data stores and data set managed by a data manager such as a database server, content manager or file system.",
                                      "https://odpi.github.io/egeria-docs/services/omas/data-manager/overview/"),
    DATA_PRIVACY_OMAS                (1016,
                                      "Data Privacy",
                                      "Data Privacy OMAS",
                                      "data-privacy",
                                      "Manage governance of privacy",
                                      "https://odpi.github.io/egeria-docs/services/omas/data-privacy/overview/"),
    DATA_SCIENCE_OMAS                (1006,
                                      "Data Science",
                                      "Data Science OMAS",
                                      "data-science",
                                      "Create and manage data science definitions and models",
                                      "https://odpi.github.io/egeria-docs/services/omas/data-science/overview/"),
    DESIGN_MODEL_OMAS                (1012,
                                      "Design Model",
                                      "Design Model OMAS",
                                      "design-model",
                                      "Exchange design model content with tools and standard packages",
                                      "https://odpi.github.io/egeria-docs/services/omas/design-model/overview/"),
    DEVOPS_OMAS                      (1007,
                                      "DevOps",
                                      "DevOps OMAS",
                                      "devops",
                                      "Manage a DevOps pipeline",
                                      "https://odpi.github.io/egeria-docs/services/omas/dev-ops/overview/"),
    DIGITAL_ARCHITECTURE_OMAS        (1026,
                                      "Digital Architecture",
                                      "Digital Architecture OMAS",
                                      "digital-architecture",
                                      "Design of the digital services for an organization",
                                      "https://odpi.github.io/egeria-docs/services/omas/digital-architecture/overview/"),
    DIGITAL_SERVICE_OMAS            ( 1027,
                                      "DigitalService",
                                      "Digital Service OMAS",
                                      "digital-service",
                                      "Manage a digital service's lifecycle",
                                      "https://odpi.github.io/egeria-docs/services/omas/digital-service/overview/"),
    DISCOVERY_ENGINE_OMAS            (1015,
                                      "Discovery Engine",
                                      "Discovery Engine OMAS",
                                      "discovery-engine",
                                      "Support for automated metadata discovery engines",
                                      "https://odpi.github.io/egeria-docs/services/omas/discovery-engine/overview/"),
    GLOSSARY_VIEW_OMAS               (1022,
                                      "Glossary View",
                                      "Glossary View OMAS",
                                      "glossary-view",
                                      "Support glossary terms visualization",
                                      "https://odpi.github.io/egeria-docs/services/omas/glossary-view/overview/"),
    GOVERNANCE_ENGINE_OMAS           (1008,
                                      "Governance Engine",
                                      "Governance Engine OMAS",
                                      "governance-engine",
                                      "Set up an operational governance engine",
                                      "https://odpi.github.io/egeria-docs/services/omas/governance-engine/overview/"),
    GOVERNANCE_PROGRAM_OMAS          (1009,
                                      "Governance Program",
                                      "Governance Program OMAS",
                                      "governance-program",
                                      "Manage the governance program",
                                      "https://odpi.github.io/egeria-docs/services/omas/governance-program/overview/"),
    PROJECT_MANAGEMENT_OMAS          (1017,
                                      "Project Management",
                                      "Project Management OMAS",
                                      "project-management",
                                      "Manage governance related projects",
                                      "https://odpi.github.io/egeria-docs/services/omas/project-management/overview/"),
    SECURITY_MANAGER_OMAS           (1029,
                                      "Security Manager",
                                      "Security Manager OMAS",
                                      "security-manager",
                                      "Manages exchange of metadata with a security service",
                                      "https://odpi.github.io/egeria-docs/services/omas/security-manager/overview/"),
    SECURITY_OFFICER_OMAS            (1013,
                                      "Security Officer",
                                      "Security Officer OMAS",
                                      "security-officer",
                                      "Set up rules and security tags to protect data",
                                      "https://odpi.github.io/egeria-docs/services/omas/security-officer/overview/"),
    SOFTWARE_DEVELOPER_OMAS          (1018,
                                      "Software Developer",
                                      "Software Developer OMAS",
                                      "software-developer",
                                      "Interact with software development tools",
                                      "https://odpi.github.io/egeria-docs/services/omas/software-developer/overview/"),
    STEWARDSHIP_ACTION_OMAS          (1019,
                                      "Stewardship Action",
                                      "Stewardship Action OMAS",
                                      "stewardship-action",
                                      "Manage exceptions and actions from open governance",
                                      "https://odpi.github.io/egeria-docs/services/omas/stewardship-action/overview/"),
    SUBJECT_AREA_OMAS                (1020,
                                      "Subject Area",
                                      "Subject Area OMAS",
                                      "subject-area",
                                      "Document knowledge about a subject area",
                                      "https://odpi.github.io/egeria-docs/services/omas/subject-area/overview/");

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
