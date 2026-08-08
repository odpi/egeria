/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

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
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * Provides specialist governance services for Files and Directories
     */
    FILE_GOVERNANCE_ENGINE("f9cd9170-d783-4643-83de-bc2c60338bad",
                           "FileGovernance",
                           "Files and Directories Governance Engine",
                           "Provides specialist governance services for Files and Directories.",
                           "filegovernanceengine",
                           OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                           ContentPackDefinition.FILES_CONTENT_PACK),

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
     * Provides surveys on Microsoft SQL Servers.
     */
    MSSQL_SURVEY_ENGINE("d263ad17-3251-4010-bceb-9a1e6651334c",
                        "MSSQLSurvey",
                        "Microsoft SQL Server Survey Engine",
                        "Provides specialist surveys on Microsoft SQL Servers.",
                        "mssqlsurveyengine",
                        OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                        ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * Provides specialist governance services on Microsoft SQL Servers.
     */
    MSSQL_GOVERNANCE_ENGINE("820ab73f-34ea-4213-a221-469519ad48ae",
                            "MSSQLGovernance",
                            "Microsoft SQL Server Governance Engine",
                            "Provides specialist governance services to Microsoft SQL Servers.",
                            "mssqlgovernanceengine",
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            ContentPackDefinition.MSSQL_CONTENT_PACK),


    /**
     * Provides surveys on Oracle Database Servers.
     */
    ORACLE_SURVEY_ENGINE("6cb53b00-6cdb-4583-aa3e-bd2acbff65c5",
                         "OracleSurvey",
                         "Oracle Database Server Survey Engine",
                         "Provides specialist surveys on Oracle Database Servers.",
                         "oraclesurveyengine",
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * Provides specialist governance services on Oracle Database Servers.
     */
    ORACLE_GOVERNANCE_ENGINE("47289ba3-ef70-41f9-bfa4-376f8521071a",
                             "OracleGovernance",
                             "Oracle Database Server Governance Engine",
                             "Provides specialist governance services to Oracle Database Servers.",
                             "oraclegovernanceengine",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             ContentPackDefinition.ORACLE_CONTENT_PACK),


    /**
     * Provides surveys on Db2 for Linux, UNIX and Windows Servers.
     */
    DB2LUW_SURVEY_ENGINE("30db1c90-6217-43f2-b9ba-4c07671f7363",
                         "DB2LUWSurvey",
                         "Db2 for Linux, UNIX and Windows Server Survey Engine",
                         "Provides specialist surveys on Db2 for Linux, UNIX and Windows Servers.",
                         "db2luwsurveyengine",
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * Provides specialist governance services on Db2 for Linux, UNIX and Windows Servers.
     */
    DB2LUW_GOVERNANCE_ENGINE("72f8589d-c8e3-42da-b5f6-3ee7b56ee4fa",
                             "DB2LUWGovernance",
                             "Db2 for Linux, UNIX and Windows Server Governance Engine",
                             "Provides specialist governance services to Db2 for Linux, UNIX and Windows Servers.",
                             "db2luwgovernanceengine",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             ContentPackDefinition.DB2LUW_CONTENT_PACK),


    /**
     * Provides surveys on DuckDB databases.
     */
    DUCKDB_SURVEY_ENGINE("95cecd7a-c9f9-4d94-8ee2-fff85943ee4b",
                         "DuckDBSurvey",
                         "DuckDB Survey Engine",
                         "Provides specialist surveys on DuckDB databases.",
                         "duckdbsurveyengine",
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         ContentPackDefinition.DUCKDB_CONTENT_PACK),

    /**
     * Provides specialist governance services on DuckDB databases.
     */
    DUCKDB_GOVERNANCE_ENGINE("63c71fad-c7ea-4781-9ef6-0bcb2d169ace",
                             "DuckDBGovernance",
                             "DuckDB Governance Engine",
                             "Provides specialist governance services to DuckDB databases.",
                             "duckdbgovernanceengine",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             ContentPackDefinition.DUCKDB_CONTENT_PACK),


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
                         ContentPackDefinition.EGERIA_CONTENT_PACK),

    /**
     * Provides specialist governance services to Egeria's OMAG Server Platform and Servers.
     */
    EGERIA_GOVERNANCE_ENGINE("81d0292d-f074-41e3-93ec-dbecc45cc0c1",
                             "EgeriaGovernance",
                             "Egeria Governance Engine",
                             "Provides specialist governance services to Egeria's OMAG Server Platform and Servers.",
                             "egeriagovernanceengine",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Provides standard watchdog services to Egeria's OMAG Server Platform and Servers.
     */
    EGERIA_WATCHDOG_ENGINE("23ec5cb2-e0a2-412c-9c2e-deefbe3d6459",
                           "EgeriaWatchdog",
                           "Egeria Watchdog Engine",
                           "Provides standard watchdog services to Egeria's OMAG Server Platform and Servers.",
                           "egeriawatchdogengine",
                           OpenMetadataType.WATCHDOG_ACTION_ENGINE.typeName,
                           ContentPackDefinition.CORE_CONTENT_PACK),

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
