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
     * View Service for glossary authoring.
     */
    GLOSSARY_AUTHOR(800,
                    ComponentDevelopmentStatus.DEPRECATED,
                    "Glossary Author",
                    "Glossary Author OMVS",
                    "glossary-author",
                    "View Service for glossary authoring.",
                    "https://egeria-project.org/services/omvs/glossary-author/overview",
                    AccessServiceDescription.SUBJECT_AREA_OMAS.getAccessServiceFullName()),

    /**
     * Explore open metadata instances.
     */
    REPOSITORY_EXPLORER(801,
                        ComponentDevelopmentStatus.DEPRECATED,
                        "Repository Explorer",
                        "Repository Explorer OMVS",
                        "rex",
                        "Explore open metadata instances.",
                        "https://egeria-project.org/services/omvs/rex/overview",
                        CommonServicesDescription.REPOSITORY_SERVICES.getServiceName()),

    /**
     * Explore the open metadata types in a repository or cohort.
     */
    TYPE_EXPLORER(802,
                  ComponentDevelopmentStatus.DEPRECATED,
                  "Type Explorer",
                  "Type Explorer OMVS",
                  "tex",
                  "Explore the open metadata types in a repository or cohort.",
                  "https://egeria-project.org/services/omvs/tex/overview",
                  CommonServicesDescription.REPOSITORY_SERVICES.getServiceName()),

    /**
     * Explore and operate an open metadata ecosystem.
     */
    DINO(803,
         ComponentDevelopmentStatus.DEPRECATED,
         "Dynamic Infrastructure and Operations",
         "Dynamic Infrastructure and Operations OMVS",
         "dino",
         "Explore and operate an open metadata ecosystem.",
         "https://egeria-project.org/services/omvs/dino/overview",
         CommonServicesDescription.SERVER_OPERATIONS.getServiceName()),

    /**
     * Author server configuration.
     */
    SERVER_AUTHOR(804,
                  ComponentDevelopmentStatus.DEPRECATED,
                  "Server Author",
                  "Server Author OMVS",
                  "server-author",
                  "Author server configuration.",
                  "https://egeria-project.org/services/omvs/server-author/overview/",
                  CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName()),

    /**
     * View glossary terms and categories within a glossary.
     */
    GLOSSARY_BROWSER(805,
                     ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                     "Glossary Browser",
                     "Glossary Browser OMVS",
                     "glossary-browser",
                     "View glossary terms and categories within a glossary.",
                     "https://egeria-project.org/services/omvs/glossary-browser/overview/",
                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName()),

    /**
     * Create glossary terms and organize them into categories as part of a controlled workflow process. It supports the editing glossary and multiple states.
     */
    GLOSSARY_MANAGER(806,
                     ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                     "Glossary Manager",
                     "Glossary Manager OMVS",
                     "glossary-manager",
                     "Create glossary terms and organize them into categories as part of a controlled workflow process. It supports the editing glossary and multiple states.",
                     "https://egeria-project.org/services/omvs/glossary-manager/overview/",
                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName()),

    /**
     * Manage information about the logged on user as well as their preferences.
     */
    MY_PROFILE(807,
               ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
               "My Profile",
               "My Profile OMVS",
               "my-profile",
               "Manage information about the logged on user as well as their preferences.",
               "https://egeria-project.org/services/omvs/my-profile/overview/",
               AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName()),

    /**
     * Search and understand your assets.
     */
    ASSET_CATALOG (808,
                   ComponentDevelopmentStatus.IN_DEVELOPMENT,
                   "Asset Catalog",
                   "Asset Catalog OMVS",
                   "asset-catalog",
                   "Search and understand your assets.",
                   "https://egeria-project.org/services/omvs/asset-catalog/overview/",
                   AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName()),

    /**
     * Build collections of asset and other metadata.
     */
    COLLECTION_MANAGER  (809,
                         ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                         "Collection Manager",
                         "Collection Manager OMVS",
                         "collection-manager",
                         "Build collections of asset and other metadata.",
                         "https://egeria-project.org/services/omvs/collection-manager/overview/",
                         AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceFullName()),

    /**
     * Manage Egeria's automation services.
     */
    AUTOMATED_CURATION  (810,
                         ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                         "Automated Curation",
                         "Automated Curation OMVS",
                         "automated-curation",
                         "Manage Egeria's automation services.",
                         "https://egeria-project.org/services/omvs/automated-curation/overview/",
                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName()),

    /**
     * Work with notelogs, comments, informal tags, ratings/reviews and likes.
     */
    FEEDBACK_MANAGER  (811,
                         ComponentDevelopmentStatus.IN_DEVELOPMENT,
                         "Feedback Manager",
                         "Feedback Manager OMVS",
                         "feedback-manager",
                         "Work with note logs, comments, informal tags, ratings/reviews and likes.",
                         "https://egeria-project.org/services/omvs/feedback-manager/overview/",
                         CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceName()),

    /**
     * Maintain definitions of governance actions such as governance action processes and governance action types.
     */
    ACTION_AUTHOR(812,
                  ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                  "Action Author",
                  "Action Author OMVS",
                  "action-author",
                  "Maintain definitions of governance actions such as governance action processes and governance action types.",
                  "https://egeria-project.org/services/omvs/action-author/overview/",
                  AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceFullName()),

    /**
     * Set up and manage projects.
     */
    PROJECT_MANAGER  (813,
                         ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                         "Project Manager",
                         "Project Manager OMVS",
                         "project-manager",
                         "Set up and manage projects.",
                         "https://egeria-project.org/services/omvs/project-manager/overview/",
                         AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceFullName()),


    /**
     * Define and search for new data resources.
     */
    DATA_DISCOVERY  (814,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Data Discovery",
                      "Data Discovery OMVS",
                      "data-discovery",
                      "Define and search for new data resources.",
                      "https://egeria-project.org/services/omvs/data-discovery/overview/",
                      AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName()),

    /**
     * Retrieve and refine the templates for use during cataloguing.
     */
    TEMPLATE_MANAGER  (815,
                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                     "Template Manager",
                     "Template Manager OMVS",
                     "template-manager",
                     "Retrieve and refine the templates for use during cataloguing.",
                     "https://egeria-project.org/services/omvs/template-manager/overview/",
                     AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName()),

    /**
     * Work with code tables and associated reference data.
     */
    REFERENCE_DATA  (816,
                       ComponentDevelopmentStatus.IN_DEVELOPMENT,
                       "Reference Data",
                       "Reference Data OMVS",
                       "reference-data",
                       "Work with code tables and associated reference data.",
                       "https://egeria-project.org/services/omvs/reference-data/overview/",
                       AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName()),

    /**
     * Maintain and query valid values for metadata.
     */
    VALID_METADATA (817,
                     ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                     "Valid Metadata",
                     "Valid Metadata OMVS",
                     "valid-metadata",
                     "Maintain and query valid values for metadata.",
                     "https://egeria-project.org/services/omvs/valid-metadata/overview/",
                     AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName()),

    /**
     * Maintain classifications and relationships used to organize open metadata elements.
     */
    CLASSIFICATION_MANAGER (818,
                            ComponentDevelopmentStatus.IN_DEVELOPMENT,
                            "Classification Manager",
                            "Classification Manager OMVS",
                            "classification-manager",
                            "Maintain classifications and relationships used to organize open metadata elements.",
                            "https://egeria-project.org/services/omvs/valid-metadata/overview/",
                            AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName()),

    /**
     * Manage the metadata about the assets managed by a DevOps pipeline.
     */
    DEVOPS_PIPELINE (819,
                                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                      "DevOps Pipeline",
                                      "DevOps Pipeline OMVS",
                                      "devops-pipeline",
                                      "Maintain the metadata about the assets managed by a DevOps pipeline.",
                                      "https://egeria-project.org/services/omvs/devops-pipeline/overview/",
                                      AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName()),


    /**
     * Set up and review rules and security tags to protect data and systems.
     */
    SECURITY_OFFICER (820,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Security Officer",
                      "Security Officer OMVS",
                      "security-officer",
                      "Set up and review rules and security tags to protect data and systems.",
                      "https://egeria-project.org/services/omvs/security-officer/overview/",
                      AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName()),


    /**
     * Manage governance of privacy.
     */
    PRIVACY_OFFICER (821,
                     ComponentDevelopmentStatus.IN_DEVELOPMENT,
                     "Privacy Officer",
                     "Privacy Officer OMVS",
                     "privacy-officer",
                     "Manage the governance of privacy.",
                     "https://egeria-project.org/services/omvs/privacy-officer/overview/",
                      AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceFullName()),

    /**
     * Manage governance of data.
     */
    DATA_OFFICER (822,
                  ComponentDevelopmentStatus.IN_DEVELOPMENT,
                  "Data Officer",
                  "Data Officer OMVS",
                  "data-officer",
                  "Manage the governance of data.",
                  "https://egeria-project.org/services/omvs/data-officer/overview/",
                  AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceFullName()),

    /**
     * Describe teams and organizational structure.
     */
    PEOPLE_ORGANIZER (823,
                  ComponentDevelopmentStatus.IN_DEVELOPMENT,
                  "People Organizer",
                  "People Organizer OMVS",
                  "people-organizer",
                  "Describe teams and organizational structure.",
                  "https://egeria-project.org/services/omvs/people-organizer/overview/",
                  AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName()),


    /**
     * Retrieve configuration and status from platforms and servers.
     */
    RUNTIME_MANAGER (824,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Runtime Manager",
                      "Runtime Manager OMVS",
                      "runtime-manager",
                      "Retrieve configuration and status from platforms and servers.",
                      "https://egeria-project.org/services/omvs/runtime-manager/overview/",
                      AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName()),

    ;


    private final int                        viewServiceCode;
    private final ComponentDevelopmentStatus viewServiceDevelopmentStatus;
    private final String                     viewServiceName;
    private final String                     viewServiceFullName;
    private final String                     viewServiceURLMarker;
    private final String                     viewServiceDescription;
    private final String                     viewServiceWiki;
    private final String                     viewServicePartnerService;


    /**
     * Default Constructor
     *
     * @param viewServiceCode        ordinal for this UI view
     * @param viewServiceDevelopmentStatus development status
     * @param viewServiceURLMarker   string used in URLs
     * @param viewServiceName        symbolic name for this UI view
     * @param viewServiceDescription short description for this UI view
     * @param viewServiceWiki        wiki page for the UI view for this UI view
     */
    ViewServiceDescription(int                        viewServiceCode,
                           ComponentDevelopmentStatus viewServiceDevelopmentStatus,
                           String                     viewServiceName,
                           String                     viewServiceFullName,
                           String                     viewServiceURLMarker,
                           String                     viewServiceDescription,
                           String                     viewServiceWiki,
                           String                     viewServicePartnerService)
    {
        /*
         * Save the values supplied
         */
        this.viewServiceCode = viewServiceCode;
        this.viewServiceDevelopmentStatus = viewServiceDevelopmentStatus;
        this.viewServiceName = viewServiceName;
        this.viewServiceFullName = viewServiceFullName;
        this.viewServiceURLMarker = viewServiceURLMarker;
        this.viewServiceDescription = viewServiceDescription;
        this.viewServiceWiki = viewServiceWiki;
        this.viewServicePartnerService = viewServicePartnerService;
    }


    /**
     * Return the enum that corresponds with the supplied code.
     *
     * @param viewServiceCode requested code
     * @return enum
     */
    public static ViewServiceDescription getViewServiceDefinition(int viewServiceCode)
    {
        for (ViewServiceDescription description : ViewServiceDescription.values())
        {
            if (viewServiceCode == description.getViewServiceCode())
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
        return viewServiceFullName;
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
    public String getViewServiceWiki() {
        return viewServiceWiki;
    }


    /**
     * Return the description of the service that this view service is partnered with.
     *
     * @return  Full name of related service
     */
    public String getViewServicePartnerService()
    {
        return viewServicePartnerService;
    }
}
