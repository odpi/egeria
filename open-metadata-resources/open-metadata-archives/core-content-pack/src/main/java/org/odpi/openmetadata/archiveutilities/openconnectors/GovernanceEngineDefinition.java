/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Describes the standard governance engines shipped with Egeria
 */
public enum GovernanceEngineDefinition
{
    /**
     * Copies, moves or deletes a file on request.
     */
    FILE_PROVISIONING_ENGINE("2e1bd9b5-d2dd-44e9-927a-0a19bb1890bb",
                             "FileProvisioning",
                             "File Provisioning Engine",
                             "Copies, moves or deletes a file on request.",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),


    /**
     * Monitors, validates and enriches metadata relating to assets as they are catalogued.
     */
    ASSET_ONBOARDING_ENGINE("aa15e19f-18a9-4623-9fad-c0895da7c034",
                            "AssetOnboarding",
                            "Asset Onboarding Engine",
                            "Monitors, validates and enriches metadata relating to assets as they are catalogued.",
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),

    /**
     * Liaises with stewards to make corrections to open metadata.
     */
    STEWARDSHIP_ENGINE("c79ada2b-15ae-4194-b47e-6171591cf5fd",
                       "Stewardship",
                       "Stewardship Engine",
                       "Liaises with stewards to make corrections to open metadata.",
                       OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),

    /**
     * Extracts information about a digital resource and attaches it to its asset description.
     */
    ASSET_SURVEY_ENGINE("606c46da-6619-4a07-83ae-0351ccb73f1a",
                        "AssetSurveys",
                        "Miscellaneous Asset Survey Engine",
                        "Extracts information about a digital resource and attaches it to its asset description.",
                        OpenMetadataType.SURVEY_ACTION_ENGINE.typeName),

    /**
     * Provides surveys on Unity Catalog (UC) servers.
     */
    FILE_SURVEY_ENGINE("4168abb9-6c60-46fb-b9c0-b44180d19500",
                       "FileSurveys",
                       "Files and Directories Survey Engine",
                       "Provides specialist surveys on Files and Directories.",
                       OpenMetadataType.SURVEY_ACTION_ENGINE.typeName),

    /**
     * Provides specialist governance services for Files and Directories
     */
    FILE_GOVERNANCE_ENGINE("f9cd9170-d783-4643-83de-bc2c60338bad",
                           "FileGovernanceServices",
                           "Files and Directories Governance Engine",
                           "Provides specialist governance services for Files and Directories.",
                           OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),

    /**
     * Provides surveys on Unity Catalog (UC) servers.
     */
    UNITY_CATALOG_SURVEY_ENGINE("ee6c7bfe-1623-480f-aea9-b6d677534322",
                                "UnityCatalogSurveys",
                                "Unity Catalog Survey Engine",
                                "Provides specialist surveys on Unity Catalog (UC) servers.",
                                OpenMetadataType.SURVEY_ACTION_ENGINE.typeName),

    /**
     * Provides specialist governance services on Unity Catalog (UC) servers.
     */
    UNITY_CATALOG_GOVERNANCE_ENGINE("6e7a91ad-3fa1-4133-ba56-99d372d9a5fa",
                                    "UnityCatalogGovernanceServices",
                                    "Unity Catalog Governance Engine",
                                    "Provides specialist governance services to Unity Catalog (UC) servers.",
                                    OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),

    /**
     * Provides surveys on PostgreSQL Servers.
     */
    POSTGRES_SURVEY_ENGINE("064519ac-c6fb-4e76-b3dd-43c9bd31cd16",
                           "PostgreSQLSurveys",
                           "PostgreSQL Survey Engine",
                           "Provides specialist surveys on PostgreSQL servers.",
                           OpenMetadataType.SURVEY_ACTION_ENGINE.typeName),

    /**
     * Provides specialist governance services on PostgresSQL Servers.
     */
    POSTGRES_GOVERNANCE_ENGINE("ad16712d-9f3a-4b42-96ca-dc348847a3db",
                               "PostgreSQLGovernanceServices",
                               "PostgreSQL Governance Engine",
                               "Provides specialist governance services to PostgreSQL servers.",
                               OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName),

    ;



    private final String guid;
    private final String name;
    private final String displayName;
    private final String description;
    private final String type;

    GovernanceEngineDefinition(String guid, String name, String displayName, String description, String type)
    {
        this.guid        = guid;
        this.name        = name;
        this.displayName = displayName;
        this.description = description;
        this.type        = type;
    }


    /**
     * Return the unique identifier of the governance engine.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the governance engine.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the display name of the governance engine.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Return the description of the governance engine.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the open metadata type name for this engine.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineDefinition{" + "name='" + name + '\'' + "}";
    }
}
