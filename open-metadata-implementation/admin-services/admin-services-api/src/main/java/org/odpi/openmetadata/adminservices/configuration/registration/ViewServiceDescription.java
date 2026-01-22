/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;


/**
 * ViewServiceDescription provides a list of registered view services.
 */
public enum ViewServiceDescription
{
    /**
     * Create new assets, software capabilities and link them to other elements.
     */
    ASSET_MAKER(201,
                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                     "Asset Maker",
                     "asset-maker",
                     "Create new assets, software capabilities and link them to other elements."),

    /**
     * Provides advanced metadata management features for experts in open metadata.
     */
    METADATA_EXPERT(202,
                ComponentDevelopmentStatus.STABLE,
                "Metadata Expert",
                "metadata-expert",
                "Provides advanced metadata management features for experts in open metadata."),

    /**
     * Supports the development and maintenance of digital products.
     */
    PRODUCT_MANAGER(203,
                    ComponentDevelopmentStatus.STABLE,
                    "Product Manager",
                    "product-manager",
                    "Supports the development and maintenance of digital products."),

    /**
     * Supports the development and maintenance of digital products.
     */
    PRODUCT_CATALOG(204,
                    ComponentDevelopmentStatus.STABLE,
                    "Product Catalog",
                    "product-catalog",
                    "Supports researching the available digital products and managing the subscriptions."),

    /**
     * Create glossary terms and organize them into folders. It supports the editing glossary and multiple states.
     */
    GLOSSARY_MANAGER(206,
                     ComponentDevelopmentStatus.STABLE,
                     "Glossary Manager",
                     "glossary-manager",
                     "Create glossary terms and organize them into folders. It supports the editing glossary and multiple states."),

    /**
     * Manage information about the logged on user as well as their preferences.
     */
    MY_PROFILE(207,
               ComponentDevelopmentStatus.STABLE,
               "My Profile",
               "my-profile",
               "Manage information about the logged on user as well as their preferences."),

    /**
     * Search and understand your assets.
     */
    ASSET_CATALOG (208,
                   ComponentDevelopmentStatus.STABLE,
                   "Asset Catalog",
                   "asset-catalog",
                   "Search and understand your assets."),

    /**
     * Build collections of asset and other metadata.
     */
    COLLECTION_MANAGER  (209,
                         ComponentDevelopmentStatus.STABLE,
                         "Collection Manager",
                         "collection-manager",
                         "Build collections of asset and other metadata."),

    /**
     * Manage Egeria's automation services.
     */
    AUTOMATED_CURATION  (210,
                         ComponentDevelopmentStatus.STABLE,
                         "Automated Curation",
                         "automated-curation",
                         "Manage Egeria's automation services."),

    /**
     * Work with notelogs, comments, informal tags, ratings/reviews and likes.
     */
    FEEDBACK_MANAGER  (211,
                         ComponentDevelopmentStatus.STABLE,
                         "Feedback Manager",
                         "feedback-manager",
                         "Work with note logs, comments, informal tags, ratings/reviews and likes."),

    /**
     * Maintain definitions of governance actions such as governance action processes and governance action types.
     */
    ACTION_AUTHOR(212,
                  ComponentDevelopmentStatus.STABLE,
                  "Action Author",
                  "action-author",
                  "Maintain definitions of governance actions such as governance action processes and governance action types."),

    /**
     * Set up and manage projects.
     */
    PROJECT_MANAGER  (213,
                         ComponentDevelopmentStatus.STABLE,
                         "Project Manager",
                         "project-manager",
                         "Set up and manage projects."),


    /**
     * Define and search for new data resources.
     */
    DATA_DISCOVERY  (214,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Data Discovery",
                      "data-discovery",
                      "Define and search for new data resources."),

    /**
     * Retrieve and refine the templates for use during cataloguing.
     */
    TEMPLATE_MANAGER  (215,
                     ComponentDevelopmentStatus.STABLE,
                     "Template Manager",
                     "template-manager",
                     "Retrieve and refine the templates for use during cataloguing."),

    /**
     * Work with code tables and associated reference data.
     */
    REFERENCE_DATA  (216,
                       ComponentDevelopmentStatus.IN_DEVELOPMENT,
                       "Reference Data",
                       "reference-data",
                       "Work with code tables and associated reference data."),

    /**
     * Maintain and query valid values for metadata.
     */
    VALID_METADATA (217,
                     ComponentDevelopmentStatus.STABLE,
                     "Valid Metadata",
                     "valid-metadata",
                     "Maintain and query valid values for metadata."),

    /**
     * Maintain classifications and relationships used to organize open metadata elements.
     */
    CLASSIFICATION_MANAGER (218,
                            ComponentDevelopmentStatus.STABLE,
                            "Classification Manager",
                            "classification-manager",
                            "Maintain classifications and relationships used to organize open metadata elements."),

    /**
     * Manage the metadata about the assets managed by a DevOps pipeline.
     */
    DEVOPS_PIPELINE (219,
                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                     "DevOps Pipeline",
                     "devops-pipeline",
                     "Maintain the metadata about the assets managed by a devops pipeline."),


    /**
     * Set up and review rules and security tags to protect data and systems.
     */
    SECURITY_OFFICER (220,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Security Officer",
                      "security-officer",
                      "Set up and review rules and security tags to protect data and systems."),


    /**
     * Manage governance of privacy.
     */
    PRIVACY_OFFICER (221,
                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                     "Privacy Officer",
                     "privacy-officer",
                     "Manage the governance of privacy."),

    /**
     * Manage governance of data.
     */
    DATA_OFFICER (222,
                  ComponentDevelopmentStatus.IN_DEVELOPMENT,
                  "Data Officer",
                  "data-officer",
                  "Manage the governance of data."),

    /**
     * Describe teams and organizational structure.
     */
    PEOPLE_ORGANIZER (223,
                  ComponentDevelopmentStatus.IN_DEVELOPMENT,
                  "People Organizer",
                  "people-organizer",
                  "Describe teams, roles and organizational structure."),


    /**
     * Retrieve configuration and status from platforms and servers.
     */
    RUNTIME_MANAGER (224,
                      ComponentDevelopmentStatus.STABLE,
                      "Runtime Manager",
                      "runtime-manager",
                      "Retrieve configuration and status from platforms and servers."),

    /**
     * Maintain governance definitions used in all governance domains.
     */
    GOVERNANCE_OFFICER (225,
                     ComponentDevelopmentStatus.STABLE,
                     "Governance Officer",
                     "governance-officer",
                     "Maintain governance definitions used to define any governance domain."),

    /**
     * Create schema definitions to describe the structure of data.
     */
    DATA_DESIGNER (226,
                   ComponentDevelopmentStatus.STABLE,
                   "Data Designer",
                   "data-designer",
                   "Create data specifications to describe data requirements."),


    /**
     * Provides generic search, query, and retrieval operations for open metadata.
     */
    METADATA_EXPLORER (227,
                   ComponentDevelopmentStatus.STABLE,
                   "Metadata Explorer",
                   "metadata-explorer",
                   "Provides generic search, query, and retrieval operations for open metadata."),


    /**
     * Manages the definitions of notifications.  This includes the definition of the trigger for the notification, the style of notification and the recipient.
     */
    NOTIFICATION_MANAGER (228,
                       ComponentDevelopmentStatus.IN_DEVELOPMENT,
                       "Notification Manager",
                       "notification-manager",
                       "Manages the definitions of notifications.  This includes the definition of the trigger for the notification, the style of notification and the recipient."),

    /**
     * Manages the definitions of information supply chains and solution components.
     */
    SOLUTION_ARCHITECT (229,
                          ComponentDevelopmentStatus.STABLE,
                          "Solution Architect",
                          "solution-architect",
                          "Manages the definitions of information supply chains and solution components."),

    /**
     * Retrieve elements based on type or the classifications/relationships attached to these metadata elements.
     */
    CLASSIFICATION_EXPLORER (230,
                            ComponentDevelopmentStatus.STABLE,
                            "Classification Explorer",
                            "classification-explorer",
                            "Retrieve elements based on type or the classifications/relationships attached to these metadata elements."),

    /**
     * Manages context events and other time-related behaviour.
     */
    TIME_KEEPER (231,
                             ComponentDevelopmentStatus.IN_DEVELOPMENT,
                             "Time Keeper",
                             "time-keeper",
                             "Manages context events and other time related-behaviour."),


    /**
     * Manages the definitions of user identities, actor profiles, contact details and actor roles.
     */
    ACTOR_MANAGER (232,
                 ComponentDevelopmentStatus.STABLE,
                 "Actor Manager",
                 "actor-manager",
                 "Manages the definitions of user identities, actor profiles, contact details and actor roles."),

    /**
     * Manages the definitions of communities, their leaders and membership.
     */
    COMMUNITY_MATTERS (233,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Community Matters",
                   "community-matters",
                   "Manages the definitions of communities, their leaders and membership."),

    /**
     * Manages the definition of subject areas and reporting on their contents.
     */
    SUBJECT_AREA (234,
                       ComponentDevelopmentStatus.IN_DEVELOPMENT,
                       "Subject Area",
                       "subject-area",
                       "Manages the definition of subject areas and reporting on their contents."),

    /**
     * Manages the creation and maintenance of connections, connector types and endpoints.
     */
    CONNECTION_MAKER (235,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Connection Maker",
                      "connection-maker",
                      "Manages the creation and maintenance of connections, connector types and endpoints."),


    /**
     * Manages the creation and maintenance of connections, connector types and endpoints.
     */
    SCHEMA_MAKER (236,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Schema Maker",
                      "schema-maker",
                      "Manages the creation and maintenance of schema elements."),


    /**
     * Manages the creation and maintenance of locations.
     */
    LOCATION_ARENA(237,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Location Arena",
                   "location-arena",
                   "Manages the creation and maintenance of locations."),


    /**
     * Manages the creation and maintenance of translations for open metadata elements.
     */
    MULTI_LANGUAGE(238,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Multi Language",
                   "multi-language",
                   "Manages the creation and maintenance of translations for open metadata elements."),

    /**
     * Manages the creation and maintenance of lineage relationships.
     */
    LINEAGE_LINKER(239,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Lineage Linker",
                   "lineage-linker",
                   "Manages the creation and maintenance of lineage relationships."),

    /**
     * Manages the creation and maintenance of external reference and external identifiers.
     */
    EXTERNAL_LINKS(240,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "External Links",
                   "external-links",
                   "Manages the creation and maintenance of external reference and external identifiers."),

    /**
     * Manages the creation and maintenance of locations.
     */
    DIGITAL_BUSINESS(241,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Digital Business",
                   "digital-business",
                   "Manages the definition and maintenance of the business context description used to identify where business value is being derived from."),

    /**
     * Manages the creation and maintenance of data pipelines, open metadata templates and reference data.
     */
    DATA_ENGINEER(242,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Data Engineer",
                   "data-engineer",
                   "Manages the creation and maintenance of data pipelines, open metadata templates and reference data."),

    ;


    private final int                        viewServiceCode;
    private final ComponentDevelopmentStatus viewServiceDevelopmentStatus;
    private final String                     viewServiceName;
    private final String                     viewServiceURLMarker;
    private final String                     viewServiceDescription;


    /**
     * Default Constructor
     *
     * @param viewServiceCode        ordinal for this UI view
     * @param viewServiceDevelopmentStatus development status
     * @param viewServiceURLMarker   string used in URLs
     * @param viewServiceName        symbolic name for this UI view
     * @param viewServiceDescription short description for this UI view
     */
    ViewServiceDescription(int                        viewServiceCode,
                           ComponentDevelopmentStatus viewServiceDevelopmentStatus,
                           String                     viewServiceName,
                           String                     viewServiceURLMarker,
                           String                     viewServiceDescription)
    {
        /*
         * Save the values supplied
         */
        this.viewServiceCode = viewServiceCode;
        this.viewServiceDevelopmentStatus = viewServiceDevelopmentStatus;
        this.viewServiceName = viewServiceName;
        this.viewServiceURLMarker = viewServiceURLMarker;
        this.viewServiceDescription = viewServiceDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getViewServiceCode() {
        return viewServiceCode;
    }


    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getViewServiceDevelopmentStatus()
    {
        return viewServiceDevelopmentStatus;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getViewServiceName() {
        return viewServiceName;
    }


    /**
     * Return the formal name for this enum instance.
     *
     * @return String default name
     */
    public String getViewServiceFullName() {
        return viewServiceName + " OMVS";
    }

    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getViewServiceURLMarker() {
        return viewServiceURLMarker;
    }

    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getViewServiceDescription() {
        return viewServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this UI view.
     *
     * @return String URL for the wiki page
     */
    public String getViewServiceWiki() 
    {
        return "https://egeria-project.org/services/omvs/" + viewServiceURLMarker + "/overview/";
    }


    /**
     * Return the description of the service that this view service is partnered with.
     *
     * @return  Full name of related service
     */
    public String getViewServicePartnerService()
    {
        return AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName();
    }
}
