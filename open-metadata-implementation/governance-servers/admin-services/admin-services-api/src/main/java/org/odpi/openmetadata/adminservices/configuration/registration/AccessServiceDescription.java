/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AccessServiceDescription provides a list of registered OMAS services.
 */
public enum AccessServiceDescription implements Serializable
{
    ASSET_CATALOG_OMAS               (1000,   "Asset Catalog", "Search and understand your assets",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-catalog/"),
    ASSET_CONSUMER_OMAS              (1001,   "Asset Consumer", "Access assets through connectors",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-consumer/"),
    ASSET_OWNER_OMAS                 (1002,   "Asset Owner", "Manage an asset",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-owner/"),
    COMMUNITY_PROFILE_OMAS           (1003,   "Community Profile", "Define personal profile and collaborate",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/community-profile/"),
    CONNECTED_ASSET_OMAS             (1004,   "Connected Asset", "Understand an asset",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/connected-asset/"),
    DATA_INFRASTRUCTURE_OMAS         (1010,   "Data Infrastructure", "Manage information about the deployed IT infrastructure",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-infrastructure/"),
    DATA_PLATFORM_OMAS               (1005,   "Data Platform", "Capture changes to the data stores and data set managed by a data platform",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-platform/"),
    DATA_PRIVACY_OMAS                (1016,   "Data Privacy", "Manage governance of privacy",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-privacy/"),
    DATA_PROCESS_OMAS                (1012,   "Data Process", "Exchange process models and lineage with a data engine",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-process/"),
    DATA_PROTECTION_OMAS             (1013,   "Data Protection", "Set up rules to protect data",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-protection/"),
    DATA_SCIENCE_OMAS                (1006,   "Data Science", "Create and manage data science definitions and models",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/data-science/"),
    DEVOPS_OMAS                      (1007,   "DevOps", "Manage a DevOps pipeline",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/dev-ops/"),
    DIGITAL_ARCHITECTURE_OMAS        (1017,   "Digital Architecture", "Design of the digital services for an organization",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/digital-architecture/"),
    DISCOVERY_ENGINE_OMAS            (1015,   "Discovery Engine", "Support for automated metadata discovery engines",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/discovery-engine/"),
    GOVERNANCE_ENGINE_OMAS           (1008,   "Governance Engine", "Set up an operational governance engine",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/governance-engine/"),
    GOVERNANCE_PROGRAM_OMAS          (1009,   "Governance Program", "Manage the governance program",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/governance-program/"),
    INFORMATION_VIEW_OMAS            (1014,   "Information View", "Support information virtualization and data set definitions",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/information-view/"),
    PROJECT_MANAGEMENT_OMAS          (1017,   "Project Management", "Manage data projects",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/project-management/"),
    SOFTWARE_DEVELOPMENT_OMAS        (1018,   "Software Developer", "Develop software with best practices",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/software-developer/"),
    STEWARDSHIP_ACTION_OMAS          (1019,   "Stewardship Action", "Manage exceptions and actions from open governance",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/stewardship-action/"),
    SUBJECT_AREA_OMAS                (1020,   "Subject Area", "Document knowledge about a subject area",
                                              "https://odpi.github.io/egeria/open-metadata-implementation/access-services/subject-area/");

    private static final long     serialVersionUID    = 1L;

    private int                            accessServiceCode;
    private String                         accessServiceName;
    private String                         accessServiceDescription;
    private String                         accessServiceWiki;


    /**
     * Return a list containing each of the access service descriptions defined in this enum class.
     *
     * @return ArrayList of enums
     */
    public static ArrayList<AccessServiceDescription> getAccessServiceDescriptionList()
    {
        ArrayList<AccessServiceDescription>  accessServiceDescriptionList = new ArrayList<>();

        accessServiceDescriptionList.add(AccessServiceDescription.ASSET_CATALOG_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.ASSET_CONSUMER_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.ASSET_OWNER_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.COMMUNITY_PROFILE_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.CONNECTED_ASSET_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DATA_INFRASTRUCTURE_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DATA_PLATFORM_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DATA_PRIVACY_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DATA_PROCESS_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DATA_PROTECTION_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DATA_SCIENCE_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DEVOPS_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.DISCOVERY_ENGINE_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.GOVERNANCE_ENGINE_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.INFORMATION_VIEW_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.PROJECT_MANAGEMENT_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.SOFTWARE_DEVELOPMENT_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.STEWARDSHIP_ACTION_OMAS);
        accessServiceDescriptionList.add(AccessServiceDescription.SUBJECT_AREA_OMAS);

        return accessServiceDescriptionList;
    }


    /**
     * Default Constructor
     *
     * @param accessServiceCode ordinal for this access service
     * @param accessServiceName symbolic name for this access service
     * @param accessServiceDescription short description for this access service
     * @param accessServiceWiki wiki page for the access service for this access service
     */
    AccessServiceDescription(int                            accessServiceCode,
                             String                         accessServiceName,
                             String                         accessServiceDescription,
                             String                         accessServiceWiki)
    {
        /*
         * Save the values supplied
         */
        this.accessServiceCode = accessServiceCode;
        this.accessServiceName = accessServiceName;
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
