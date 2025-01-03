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
     * Monitors, validates and enriches metadata relating to assets as they are catalogued.
     */
    ASSET_ONBOARDING_ENGINE("aa15e19f-18a9-4623-9fad-c0895da7c034",
                            "AssetOnboarding",
                            "Asset Onboarding Engine",
                            "Monitors, validates and enriches metadata relating to assets as they are catalogued.",
                            "assetonboardingengine",
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * Liaises with stewards to make corrections to open metadata.
     */
    STEWARDSHIP_ENGINE("c79ada2b-15ae-4194-b47e-6171591cf5fd",
                       "Stewardship",
                       "Stewardship Engine",
                       "Liaises with stewards to make corrections to open metadata.",
                       "stewardshipengine",
                       OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                       ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * Provides surveys on Unity Catalog (UC) servers.
     */
    FILE_SURVEY_ENGINE("4168abb9-6c60-46fb-b9c0-b44180d19500",
                       "FileSurvey",
                       "Files and Directories Survey Engine",
                       "Provides specialist surveys on Files and Directories.",
                       "filesurveyengine",
                       OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Provides specialist governance services for Files and Directories
     */
    FILE_GOVERNANCE_ENGINE("f9cd9170-d783-4643-83de-bc2c60338bad",
                           "FileGovernance",
                           "Files and Directories Governance Engine",
                           "Provides specialist governance services for Files and Directories.",
                           "filegovernanceengine",
                           OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                           ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Provides surveys on Unity Catalog (UC) servers.
     */
    UNITY_CATALOG_SURVEY_ENGINE("ee6c7bfe-1623-480f-aea9-b6d677534322",
                                "UnityCatalogSurvey",
                                "Unity Catalog Survey Engine",
                                "Provides specialist surveys on Unity Catalog (UC) servers.",
                                "unitycatalogsurveyengine",
                                OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                                ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * Provides specialist governance services on Unity Catalog (UC) servers.
     */
    UNITY_CATALOG_GOVERNANCE_ENGINE("6e7a91ad-3fa1-4133-ba56-99d372d9a5fa",
                                    "UnityCatalogGovernance",
                                    "Unity Catalog Governance Engine",
                                    "Provides specialist governance services to Unity Catalog (UC) servers.",
                                    "unitycataloggovernanceengine",
                                    OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                    ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * Provides surveys on PostgreSQL Servers.
     */
    POSTGRES_SURVEY_ENGINE("064519ac-c6fb-4e76-b3dd-43c9bd31cd16",
                           "PostgreSQLSurvey",
                           "PostgreSQL Survey Engine",
                           "Provides specialist surveys on PostgreSQL servers.",
                           "postgresqlsurveyengine",
                           OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * Provides specialist governance services on PostgresSQL Servers.
     */
    POSTGRES_GOVERNANCE_ENGINE("ad16712d-9f3a-4b42-96ca-dc348847a3db",
                               "PostgreSQLGovernance",
                               "PostgreSQL Governance Engine",
                               "Provides specialist governance services to PostgreSQL servers.",
                               "postgresqlgovernanceengine",
                               OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                               ContentPackDefinition.POSTGRES_CONTENT_PACK),


    /**
     * Provides surveys on Apache Atlas Servers.
     */
    ATLAS_SURVEY_ENGINE("af1b89b3-c579-4ad3-b649-a007121566a0",
                        "ApacheAtlasSurvey",
                        "Apache Atlas Survey Engine",
                        "Provides specialist surveys on Apache Atlas servers.",
                        "apacheatlassurveyengine",
                        OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                        ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * Provides specialist governance services to Apache Atlas Servers.
     */
    ATLAS_GOVERNANCE_ENGINE("f4fc1197-9809-4ad8-8b4a-380dd8e09910",
                            "ApacheAtlasGovernance",
                            "Apache Atlas Governance Engine",
                            "Provides specialist governance services to Apache Atlas servers.",
                            "apacheatlasgovernanceengine",
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * Provides surveys on Egeria's OMAG Server Platform and Servers.
     */
    EGERIA_SURVEY_ENGINE("6fb9afe5-e346-487e-8938-fd5bc1761ceb",
                        "EgeriaSurvey",
                        "Egeria Survey Engine",
                        "Provides specialist surveys on Egeria's OMAG Server Platform and Servers.",
                         "egeriasurveyengine",
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                        ContentPackDefinition.NANNY_CONTENT_PACK),

    /**
     * Provides specialist governance services to Egeria's OMAG Server Platform and Servers.
     */
    EGERIA_GOVERNANCE_ENGINE("81d0292d-f074-41e3-93ec-dbecc45cc0c1",
                            "EgeriaGovernance",
                            "Egeria Governance Engine",
                            "Provides specialist governance services to Egeria's OMAG Server Platform and Servers.",
                             "egeriagovernanceengine",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            ContentPackDefinition.NANNY_CONTENT_PACK),

    /**
     * Provides surveys on Apache Kafka Servers.
     */
    KAFKA_SURVEY_ENGINE("de82d12d-f723-47c7-b5e6-c9a5998a0d6a",
                         "ApacheKafkaSurvey",
                         "Apache Kafka Survey Engine",
                         "Provides specialist surveys on Apache Kafka Servers.",
                        "apachekafkasurveyengine",
                        OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    /**
     * Provides specialist governance services to Apache Kafka Servers.
     */
    KAFKA_GOVERNANCE_ENGINE("0874bb8a-0003-4dda-b028-cbcecfff0e45",
                             "ApacheKafkaGovernance",
                             "Apache Kafka Governance Engine",
                             "Provides specialist governance services to Apache Kafka Servers.",
                            "apachekafkagovernanceengine",
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),
    ;


    private final String                guid;
    private final String                name;
    private final String                displayName;
    private final String                description;
    private final String                userId;
    private final String                type;
    private final ContentPackDefinition contentPackDefinition;



    GovernanceEngineDefinition(String                guid,
                               String                name,
                               String                displayName,
                               String                description,
                               String                userId,
                               String                type,
                               ContentPackDefinition contentPackDefinition)
    {
        this.guid                  = guid;
        this.name                  = name;
        this.displayName           = displayName;
        this.description           = description;
        this.userId                = userId;
        this.type                  = type;
        this.contentPackDefinition = contentPackDefinition;
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
     * Return the name of the userId that this engine is using.
     *
     * @return userId
     */
    public String getUserId()
    {
        return userId;
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
     * Get identifier of content pack where this template should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
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
