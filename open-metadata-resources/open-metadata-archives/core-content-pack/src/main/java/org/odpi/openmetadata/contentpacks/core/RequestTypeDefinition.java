/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.adapters.connectors.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.MSSQLDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.OracleDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint.ProductSolutionComponent;
import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetRequestParameter;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.GovernanceActionTypeDefinition;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.FolderRequestParameter;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines the request types for the governance engines that identify which governance service to call
 * for a specific request type.
 */
public enum RequestTypeDefinition
{
    /**
     * watch-for-new-files-in-folder
     */
    WATCH_FOR_NEW_FILES("watch-for-new-files-in-folder",
                        "watch-nested-in-folder",
                        null,
                        null,
                        GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.NEW_FILES_WATCHDOG,
                        "69bead73-b5b7-4791-9293-c660990ec7bf",
                        GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                        DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                        "6ba4d520-a3fd-4eb7-9fc1-b1beddfd721e",
                        "Watch for new Files in Directory (folder)",
                        "Monitors the creation of open metadata elements that represent files and initiates appropriate governance actions.",
                        null,
                        null,
                        true,
                        ContentCollectionDefinition.WATCHDOG_ACTIONS,
                        ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * copy-file
     */
    COPY_FILE("copy-file",
              null,
              null,
              null,
              GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
              GovernanceServiceDefinition.FILE_PROVISIONER,
              "4f7c739b-69d3-4310-9bb2-507625dc2899",
              GovernanceDomain.UNCLASSIFIED.getOrdinal(),
              DeployedImplementationType.FILE.getQualifiedName(),
              "a709e480-ad2e-479e-84fb-dbfb6b2a62dd",
              "Copy File",
              "Copy a file from one directory to another and maintain the open metadata elements describing the files and the lineage representing the data flow between them.",
              DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
              null,
              true,
              ContentCollectionDefinition.FILE_ACTIONS,
              ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * move-file
     */
    MOVE_FILE("move-file",
              null,
              null,
              null,
              GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
              GovernanceServiceDefinition.FILE_PROVISIONER,
              "dc3ad63e-6663-4087-bcf3-6e48c68ed5b6",
              GovernanceDomain.UNCLASSIFIED.getOrdinal(),
              DeployedImplementationType.FILE.getQualifiedName(),
              "76534d17-d674-4412-ab08-8f0069b9b053",
              "Move File",
              "Move a file from one directory to another and maintain the open metadata elements describing the two locations of the file and the lineage representing the data flow between them.",
              DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
              null,
              true,
              ContentCollectionDefinition.FILE_ACTIONS,
              ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * delete-file
     */
    DELETE_FILE("delete-file",
                null,
                null,
                null,
                GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                GovernanceServiceDefinition.FILE_PROVISIONER,
                "c658530b-5f99-4212-a321-92bad0cd9b60",
                GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                DeployedImplementationType.FILE.getQualifiedName(),
                "039ffc59-e7b5-4a8f-9b3e-49a1b9d17ec2",
                "Delete File",
                "Deletes a file and deletes the associated ope metadata element.",
                DeployedImplementationType.FILE,
                null,
                true,
                ContentCollectionDefinition.FILE_ACTIONS,
                ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * baudot-subscription-manager
     */
    BAUDOT_SUBSCRIPTION_MANAGER(GovernanceActionTypeDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGovernanceRequestType(),
                                null,
                                null,
                                null,
                                GovernanceEngineDefinition.EGERIA_WATCHDOG_ENGINE,
                                GovernanceServiceDefinition.BAUDOT_SUBSCRIPTION_MANAGER,
                                GovernanceActionTypeDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGovernanceActionTypeGUID(),
                                GovernanceDomain.DATA_SHARING.getOrdinal(),
                                null,
                                ProductSolutionComponent.SUBSCRIPTION_MANAGER.getGUID(),
                                ProductSolutionComponent.SUBSCRIPTION_MANAGER.getDisplayName(),
                                ProductSolutionComponent.SUBSCRIPTION_MANAGER.getDescription(),
                                ContentCollectionDefinition.WATCHDOG_ACTIONS,
                                ContentPackDefinition.PRODUCTS_CONTENT_PACK),

    /**
     * award-karma-points
     */
    AWARD_KARMA_POINTS(GovernanceActionTypeDefinition.AWARD_KARMA_POINTS.getGovernanceRequestType(),
                       null,
                       null,
                       null,
                       GovernanceEngineDefinition.EGERIA_WATCHDOG_ENGINE,
                       GovernanceServiceDefinition.KARMA_POINT_AWARDS,
                       GovernanceActionTypeDefinition.AWARD_KARMA_POINTS.getGovernanceActionTypeGUID(),
                       GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                       null,
                       "5ca5b9fb-2a38-43b6-9206-1465574129ce",
                       "Award Karma Points",
                       "Monitors contributions to the open metadata ecosystem and awards karma points to the users responsible.",
                       ContentCollectionDefinition.ANALYTICAL_ACTIONS,
                       ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),

    /**
     * build-zone-membership-profile
     */
    BUILD_ZONE_MEMBERSHIP_PROFILE(GovernanceActionTypeDefinition.BUILD_ZONE_MEMBERSHIP_PROFILE.getGovernanceRequestType(),
                                  null,
                                  null,
                                  null,
                                  GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                                  GovernanceServiceDefinition.BUILD_ZONE_MEMBERSHIP_PROFILE,
                                  GovernanceActionTypeDefinition.BUILD_ZONE_MEMBERSHIP_PROFILE.getGovernanceActionTypeGUID(),
                                  GovernanceDomain.SECURITY.getOrdinal(),
                                  null,
                                  "3e1d7e33-f871-4a4f-b50d-25e59516f7bd",
                                  "Build Zone Membership Profile",
                                  "Counts the elements of each type in each Governance Zone.",
                                  ContentCollectionDefinition.ANALYTICAL_ACTIONS,
                                  ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),


    /**
     * provision-tabular-data-set
     */
    PROVISION_TABULAR(GovernanceActionTypeDefinition.PROVISION_TABULAR.getGovernanceRequestType(),
                      null,
                      null,
                      null,
                      GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                      GovernanceServiceDefinition.TABULAR_DATA_PROVISIONER,
                      GovernanceActionTypeDefinition.PROVISION_TABULAR.getGovernanceActionTypeGUID(),
                      GovernanceDomain.DATA_SHARING.getOrdinal(),
                      DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                      "15060bce-3034-4cd6-9288-15287c5a354e",
                      "Provision Tabular Data Set",
                      "Copies data from one tabular data set to another.",
                      DeployedImplementationType.TABULAR_DATA_SET,
                      null,
                      true,
                      ContentCollectionDefinition.PRODUCT_CATALOG,
                      ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * provision-subscription
     */
    PROVISION_SUBSCRIPTION(GovernanceActionTypeDefinition.PROVISION_SUBSCRIPTION.getGovernanceRequestType(),
                           null,
                           null,
                           null,
                           GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                           GovernanceServiceDefinition.SUBSCRIPTION_PROVISIONER,
                           GovernanceActionTypeDefinition.PROVISION_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                           GovernanceDomain.DATA_SHARING.getOrdinal(),
                           DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                           ProductSolutionComponent.PROVISIONING_PIPELINE.getGUID(),
                           ProductSolutionComponent.PROVISIONING_PIPELINE.getDisplayName(),
                           ProductSolutionComponent.PROVISIONING_PIPELINE.getDescription(),
                           DeployedImplementationType.TABULAR_DATA_SET,
                           null,
                           true,
                           ContentCollectionDefinition.PRODUCT_CATALOG,
                           ContentPackDefinition.PRODUCTS_CONTENT_PACK),

    /**
     * create-digital-subscription
     */
    CREATE_SUBSCRIPTION(GovernanceActionTypeDefinition.CREATE_SUBSCRIPTION.getGovernanceRequestType(),
                        null,
                        null,
                        null,
                        GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CREATE_SUBSCRIPTION,
                        GovernanceActionTypeDefinition.CREATE_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                        GovernanceDomain.DATA_SHARING.getOrdinal(),
                        DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                        ProductSolutionComponent.NEW_PRODUCT_SUBSCRIPTION.getGUID(),
                        ProductSolutionComponent.NEW_PRODUCT_SUBSCRIPTION.getDisplayName(),
                        ProductSolutionComponent.NEW_PRODUCT_SUBSCRIPTION.getDescription(),
                        null,
                        null,
                        true,
                        ContentCollectionDefinition.PRODUCT_CATALOG,
                        ContentPackDefinition.PRODUCTS_CONTENT_PACK),

    /**
     * cancel-digital-subscription
     */
    CANCEL_SUBSCRIPTION(GovernanceActionTypeDefinition.CANCEL_SUBSCRIPTION.getGovernanceRequestType(),
                        null,
                        null,
                        null,
                        GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CANCEL_SUBSCRIPTION,
                        GovernanceActionTypeDefinition.CANCEL_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                        GovernanceDomain.DATA_SHARING.getOrdinal(),
                        DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                        ProductSolutionComponent.CANCEL_PRODUCT_SUBSCRIPTION.getGUID(),
                        ProductSolutionComponent.CANCEL_PRODUCT_SUBSCRIPTION.getDisplayName(),
                        ProductSolutionComponent.CANCEL_PRODUCT_SUBSCRIPTION.getDescription(),
                        DeployedImplementationType.TABULAR_DATA_SET,
                        null,
                        true,
                        ContentCollectionDefinition.PRODUCT_CATALOG,
                        ContentPackDefinition.PRODUCTS_CONTENT_PACK),


    /**
     * seek-origin-of-asset
     */
    SEEK_ORIGIN("seek-origin-of-asset",
                null,
                null,
                null,
                GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                GovernanceServiceDefinition.ORIGIN_SEEKER,
                "98a63f4c-01fc-4c38-9897-d59fb7c888ee",
                GovernanceDomain.DATA.getOrdinal(),
                DeployedImplementationType.DATA_ASSET.getQualifiedName(),
                "0e982f14-b5f9-4c0d-9bdf-647a2a637efa",
                "Seek Origin of Data",
                "Using the lineage relationships, trace back to the source of data for a requested asset.  If a single DigitalResourceOrigin classification is encountered then add it to the asset.  If null, or multiple DigitalResourceOrigin classifications are encountered, raise an error.",
                ContentCollectionDefinition.STEWARDSHIP,
                ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * set-zone-membership
     */
    ZONE_MEMBER("set-zone-membership",
                null,
                null,
                null,
                GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                GovernanceServiceDefinition.ZONE_PUBLISHER,
                "05df4044-bc0a-40cd-b729-66aef891e7f0",
                GovernanceDomain.SECURITY.getOrdinal(),
                DeployedImplementationType.ASSET.getQualifiedName(),
                "a6db6967-0e78-4898-9602-0748932f3cfa",
                "Set Zone Membership",
                "Set up the zone membership on the requested element.",
                ContentCollectionDefinition.STEWARDSHIP,
                ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * set-retention-period
     */
    RETENTION_PERIOD("set-retention-period",
                     null,
                     null,
                     null,
                     GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                     GovernanceServiceDefinition.RETENTION_CLASSIFIER,
                     "633cca67-7be8-49bf-9c38-f82e4ceea44c",
                     GovernanceDomain.DATA.getOrdinal(),
                     DeployedImplementationType.DATA_ASSET.getQualifiedName(),
                     "edd1702a-c7b5-47ab-8cd2-3750849fe238",
                     "Set Retention Period",
                     "Set up the retention period for the supplied asset.",
                     ContentCollectionDefinition.STEWARDSHIP,
                     ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * verify-asset
     */
    VERIFY_ASSET("verify-asset",
                 null,
                 null,
                 null,
                 GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                 GovernanceServiceDefinition.VERIFY_ASSET,
                 "a7983409-8eee-4239-a252-a3c5515def59",
                 GovernanceDomain.DATA.getOrdinal(),
                 DeployedImplementationType.ASSET.getQualifiedName(),
                 "3f76aeab-bb47-4b5b-b77e-803cc73e7e13",
                 "Verify Asset",
                 "Verify that an asset has the correct classifications set up - which includes zone membership, retention and origin.",
                 ContentCollectionDefinition.STEWARDSHIP,
                 ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * evaluate-annotations
     */
    EVALUATE_ANNOTATIONS("evaluate-annotations",
                         null,
                         null,
                         null,
                         GovernanceEngineDefinition.STEWARDSHIP_ENGINE,
                         GovernanceServiceDefinition.EVALUATE_ANNOTATIONS,
                         "be193d1c-1a60-4f03-8204-22817f2d40c4",
                         GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                         DeployedImplementationType.ASSET.getQualifiedName(),
                         "255cb632-9bbc-4e1c-9e41-898673305341",
                         "Evaluate Annotations",
                         "Check a survey report for 'Request for Action' annotations and raise a ToDo request to the designated steward to resolve it.",
                         ContentCollectionDefinition.STEWARDSHIP,
                         ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * print-survey-report
     */
    PRINT_SURVEY_REPORT("print-survey-report",
                         null,
                         null,
                         null,
                         GovernanceEngineDefinition.STEWARDSHIP_ENGINE,
                         GovernanceServiceDefinition.PRINT_SURVEY_REPORT,
                         "8b81d9c1-3320-43b1-90a7-57772855460b",
                        GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                        DeployedImplementationType.ASSET.getQualifiedName(),
                        "f01af176-3086-4533-956a-8d11bcd314c8",
                        "Print Survey Report",
                        "Print out a survey report as a markdown document.",
                         DeployedImplementationType.FILE,
                        null,
                        true,
                         ContentCollectionDefinition.COMMUNICATION_ACTIONS,
                         ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * write-to-audit-log
     */
    WRITE_AUDIT_LOG("write-to-audit-log",
                    null,
                    null,
                    null,
                    GovernanceEngineDefinition.STEWARDSHIP_ENGINE,
                    GovernanceServiceDefinition.WRITE_AUDIT_LOG,
                    "faa9ef71-3f49-4ab8-8241-066ef7b517e8",
                    GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                    null,
                    "8fa24644-43ce-4a3f-b31f-d1d52db323f7",
                    "Write to Audit Log",
                    "Write a specific message to the audit log.",
                    DeployedImplementationType.AUDIT_LOG_DESTINATION_CONNECTOR,
                    null,
                    false,
                    ContentCollectionDefinition.COMMUNICATION_ACTIONS,
                    ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * get-day-of-week
     */
    GET_DAY_OF_WEEK("get-day-of-week",
                    null,
                    null,
                    null,
                    GovernanceEngineDefinition.STEWARDSHIP_ENGINE,
                    GovernanceServiceDefinition.DAY_OF_WEEK,
                    "a3c16a82-a754-434f-930d-f412e62643a6",
                    GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                    null,
                    "0f7e7dd9-eab2-4f4f-b5f9-51699b44ad69",
                    "Get Day of the Week",
                    "Determine the day of the week and output it as a guard.",
                    null,
                    null,
                    false,
                    ContentCollectionDefinition.COMMUNICATION_ACTIONS,
                    ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * qualified-name-dedup
     */
    QNAME_DEDUP("qualified-name-dedup",
                null,
                null,
                null,
                GovernanceEngineDefinition.STEWARDSHIP_ENGINE,
                GovernanceServiceDefinition.QUALIFIED_NAME_DEDUP,
                "066e9a5f-b725-4047-abd8-ce5353803ba1",
                GovernanceDomain.UNCLASSIFIED.getOrdinal(),
                null,
                "2cfe0c4a-7f63-4081-a8d5-ab6cc0159936",
                "Qualified Name Deduplication",
                "Link elements as duplicates if they have the same qualified name.",
                ContentCollectionDefinition.STEWARDSHIP,
                ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * survey-csv-file
     */
    SURVEY_CSV_FILE("survey-csv-file",
                    null,
                    null,
                    null,
                    GovernanceEngineDefinition.FILE_SURVEY_ENGINE,
                    GovernanceServiceDefinition.CSV_FILE_SURVEY,
                    "fcd7ddce-b61e-49eb-b993-293907dadf72",
                    GovernanceDomain.DATA.getOrdinal(),
                    DeployedImplementationType.CSV_FILE.getQualifiedName(),
                    "a4e69580-123d-43ce-a7d5-408fc0bc191e",
                    "Survey CSV File",
                    "Create a survey report about a requested CSV file, including extracting information about the file's characteristics and the columns inside..",
                    DeployedImplementationType.CSV_FILE,
                    null,
                    true,
                    ContentCollectionDefinition.SURVEY_ACTIONS,
                    ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * survey-data-file
     */
    SURVEY_DATA_FILE("survey-data-file",
                     null,
                     null,
                     null,
                     GovernanceEngineDefinition.FILE_SURVEY_ENGINE,
                     GovernanceServiceDefinition.DATA_FILE_SURVEY,
                     "3a15cfe4-e130-4b8c-b4fb-eedd39e1a2ae",
                     GovernanceDomain.DATA.getOrdinal(),
                     DeployedImplementationType.DATA_FILE.getQualifiedName(),
                     "67c8554a-8353-4a0a-b98e-596104cde47b",
                     "Survey Data File",
                     "Create a survey report about a particular file that describes the characteristics of the file.  It does not look inside the file.",
                     DeployedImplementationType.FILE,
                     null,
                     true,
                     ContentCollectionDefinition.SURVEY_ACTIONS,
                     ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * survey-folder
     */
    SURVEY_FOLDER("survey-folder",
                  null,
                  getFolderSurveyRequestParameters(),
                  null,
                  GovernanceEngineDefinition.FILE_SURVEY_ENGINE,
                  GovernanceServiceDefinition.FOLDER_SURVEY,
                  "381c60e6-733b-42db-a025-8e6eb29294fc",
                  GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                  DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                  "96a97dc7-10b0-4c7a-8e4f-1f4f2b0b1bf7",
                  "Survey File System Directory",
                  "Create a survey report that summarizes the files in a directory (folder) on a file system.  Nested directories are ignored.",
                  DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
                  null,
                  true,
                  ContentCollectionDefinition.SURVEY_ACTIONS,
                  ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * survey-folder-and-files
     */
    SURVEY_FOLDER_AND_FILES("survey-folder-and-files",
                            null,
                            getFolderAndFilesSurveyRequestParameters(),
                            null,
                            GovernanceEngineDefinition.FILE_SURVEY_ENGINE,
                            GovernanceServiceDefinition.FOLDER_SURVEY,
                            "633e7711-0c65-47b5-894f-c9dba5472412",
                            GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                            DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                            "7d8e6ed1-c2b8-49af-9e49-8e5c5290c064",
                            "Survey File System Directory and its Files",
                            "Create a survey report that characterises the files in a requested directory (folder) on a file system along with a summary of the directory itself.  Nested directories are ignored.",
                            DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
                            null,
                            true,
                            ContentCollectionDefinition.SURVEY_ACTIONS,
                            ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * survey-all-folders
     */
    SURVEY_ALL_FOLDERS("survey-all-folders",
                       null,
                       getAllFoldersSurveyRequestParameters(),
                       null,
                       GovernanceEngineDefinition.FILE_SURVEY_ENGINE,
                       GovernanceServiceDefinition.FOLDER_SURVEY,
                       "a6f2f6e8-d912-4101-982f-79c62190f1ba",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                       "b6c523a0-7318-4dd9-8b69-0e1e4aa08d1f",
                       "Survey Nested Files System Directories (Folders)",
                       "Starting from a particular directory, navigate through the hierarchy of nested directories in a file system and create a survey report that includes a summary of the files found in each directory.",
                       DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
                       null,
                       true,
                       ContentCollectionDefinition.SURVEY_ACTIONS,
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * survey-all-folders-and-files
     */
    SURVEY_ALL_FOLDERS_AND_FILES("survey-all-folders-and-files",
                                 null,
                                 getAllFolderAndFilesSurveyRequestParameters(),
                                 null,
                                 GovernanceEngineDefinition.FILE_SURVEY_ENGINE,
                                 GovernanceServiceDefinition.FOLDER_SURVEY,
                                 "cc642671-898a-4c83-9d29-b1a1758672d2",
                                 GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                                 DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                                 "9c38a6ca-4e0c-4d53-b517-6cdb78e46b35",
                                 "Survey Nested Files System Directories (Folders) and Files",
                                 "Starting from a particular directory, navigate through the hierarchy of nested directories in a file system and create a survey report that includes a description of each file encountered and a summary of the files found in each directory.",
                                 DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
                                 null,
                                 true,
                                 ContentCollectionDefinition.SURVEY_ACTIONS,
                                 ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * survey-apache-atlas-server
     */
    SURVEY_ATLAS_SERVER("survey-apache-atlas-server",
                        null,
                        null,
                        null,
                        GovernanceEngineDefinition.ATLAS_SURVEY_ENGINE,
                        GovernanceServiceDefinition.APACHE_ATLAS_SURVEY,
                        "18d36065-3e39-43bc-be31-4b6c22354480",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                        "892eb723-0bca-4f1e-b999-95604555f6f1",
                        "Survey an Apache Atlas Server",
                        "Connect to a requested Apache Atlas server and create a survey report that describes the types and numbers of associated metadata instances stored.",
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER,
                        null,
                        true,
                        ContentCollectionDefinition.SURVEY_ACTIONS,
                        ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * survey-kafka-server
     */
    SURVEY_KAFKA_SERVER("survey-kafka-server",
                        null,
                        null,
                        null,
                        GovernanceEngineDefinition.KAFKA_SURVEY_ENGINE,
                        GovernanceServiceDefinition.KAFKA_SERVER_SURVEY,
                        "71c73133-6817-42a1-9cc6-b610cee34a8b",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                        "4b8e9b5d-d095-43ac-a1d0-61155ea6f1b1",
                        "Survey Apache Kafka Server",
                        "Connects to an Apache Kafka server (broker) and create a survey report that lists the topics being managed.",
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER,
                        null,
                        true,
                        ContentCollectionDefinition.SURVEY_ACTIONS,
                        ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    /**
     * survey-unity-catalog-server
     */
    SURVEY_UC_SERVER("survey-unity-catalog-server",
                     null,
                     null,
                     null,
                     GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE,
                     GovernanceServiceDefinition.UC_SERVER_SURVEY,
                     "c9fca16e-854d-43bc-b97e-33691afafac3",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                     "ca21df08-926e-4645-8c9f-dcd3d6db6cea",
                     "Survey Unity Catalog Server",
                     "Connects to a Unity Catalog Server and create a survey report that summarizes all the catalogs, schemas and data resources listed.",
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                     null,
                     true,
                     ContentCollectionDefinition.SURVEY_ACTIONS,
                     ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * survey-unity-catalog-catalog
     */
    SURVEY_UC_CATALOG("survey-unity-catalog-catalog",
                      null,
                      null,
                      null,
                      GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE,
                      GovernanceServiceDefinition.UC_CATALOG_SURVEY,
                      "d00bc9af-0d2f-4640-a24b-35d77110883e",
                      GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                      UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getQualifiedName(),
                      "2107c858-e067-457c-a8e6-0a198ea27d21",
                      "Survey Unity Catalog Catalog",
                      "Connects to a Unity Catalog Server and creates a survey report that summarizes the schemas and data resources found in a requested catalog.",
                      UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                      null,
                      true,
                      ContentCollectionDefinition.SURVEY_ACTIONS,
                      ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * survey-unity-catalog-schema
     */
    SURVEY_UC_SCHEMA("survey-unity-catalog-schema",
                     null,
                     null,
                     null,
                     GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE,
                     GovernanceServiceDefinition.UC_SCHEMA_SURVEY,
                     "a53211fc-89e6-4405-9768-606d519649ee",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getQualifiedName(),
                     "a7469ade-dbc4-4958-b5cf-83350e1c719f",
                     "Survey Unity Catalog Schema",
                     "Connects to a Unity Catalog Server and creates a survey report that summarizes the data resources found in a requested catalog/schema.",
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                     null,
                     true,
                     ContentCollectionDefinition.SURVEY_ACTIONS,
                     ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * survey-unity-catalog-volume
     */
    SURVEY_UC_VOLUME("survey-unity-catalog-volume",
                     null,
                     null,
                     null,
                     GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE,
                     GovernanceServiceDefinition.UC_VOLUME_SURVEY,
                     "b62df48b-1390-4cb2-afff-2aa136d8467d",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getQualifiedName(),
                     "608c4836-c88a-4d4c-a774-2d5c37ac7cae",
                     "Survey Unity Catalog Volume",
                     "Connects to a Unity Catalog Server and creates a survey report that summarizes the data files found in a requested catalog/schema/volume.",
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                     null,
                     true,
                     ContentCollectionDefinition.SURVEY_ACTIONS,
                     ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * provision-unity-catalog-resource
     */
    PROVISION_UC("provision-unity-catalog-resource",
                 null,
                 null,
                 null,
                 GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                 GovernanceServiceDefinition.PROVISION_UC,
                 "b67bce2e-fc02-43b1-a45b-e3c7be02da66",
                 GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                 UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getQualifiedName(),
                 "4aba9c28-95fe-4289-9dde-67a3836ab1bf",
                 "Provision Unity Catalog Resource",
                 "Creates a description of a Unity Catalog resource (such as a volume, table or function) for a particular Unity Catalog Server that will be picked up and added to the Unity Catalog Server by the Unity Catalog integration connector on the next refresh.",
                 UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                 null,
                 true,
                 ContentCollectionDefinition.PROVISION_ACTIONS,
                 ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * survey-postgres-server
     */
    SURVEY_POSTGRES_SERVER("survey-postgres-server",
                           null,
                           null,
                           null,
                           GovernanceEngineDefinition.POSTGRES_SURVEY_ENGINE,
                           GovernanceServiceDefinition.POSTGRES_SERVER_SURVEY,
                           "fcad7603-bd05-4d07-b6e8-a4fb29fd57fc",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                           "88346a5c-c376-431c-9833-e90f81113cb5",
                           "Survey a PostgreSQL Server",
                           "Create a survey report of the databases found in a requested PostgreSQL Server.",
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                           null,
                           true,
                           ContentCollectionDefinition.SURVEY_ACTIONS,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * survey-postgres-database
     */
    SURVEY_POSTGRES_DATABASE("survey-postgres-database",
                             null,
                             null,
                             null,
                             GovernanceEngineDefinition.POSTGRES_SURVEY_ENGINE,
                             GovernanceServiceDefinition.POSTGRES_DATABASE_SURVEY,
                             "8a7e16eb-15e3-4e16-ba7e-1e8d6653677b",
                             GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                             "0cc37077-e688-4572-b6d3-ed0a24375bc1",
                             "Survey PostgreSQL Database",
                             "Create a survey report of the schemas, tables and columns found in a requested PostgreSQL Database.",
                             PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                             null,
                             true,
                             ContentCollectionDefinition.SURVEY_ACTIONS,
                             ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * survey-mssql-server
     */
    SURVEY_MSSQL_SERVER("survey-mssql-server",
                        null,
                        null,
                        null,
                        GovernanceEngineDefinition.MSSQL_SURVEY_ENGINE,
                        GovernanceServiceDefinition.MSSQL_SERVER_SURVEY,
                        "009ade35-33d5-4ff7-b4c9-ef3ba09f7c9f",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        MSSQLDeployedImplementationType.MSSQL_SERVER.getQualifiedName(),
                        "4efca160-7272-4899-864e-6d082ee442be",
                        "Survey a Microsoft SQL Server",
                        "Create a survey report of the databases found in a requested Microsoft SQL Server.",
                        MSSQLDeployedImplementationType.MSSQL_SERVER,
                        null,
                        true,
                        ContentCollectionDefinition.SURVEY_ACTIONS,
                        ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * survey-mssql-database
     */
    SURVEY_MSSQL_DATABASE("survey-mssql-database",
                          null,
                          null,
                          null,
                          GovernanceEngineDefinition.MSSQL_SURVEY_ENGINE,
                          GovernanceServiceDefinition.MSSQL_DATABASE_SURVEY,
                          "f915fa3c-24ad-4d12-a5a3-cd3c9a447ec4",
                          GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                          MSSQLDeployedImplementationType.MSSQL_DATABASE.getQualifiedName(),
                          "5c1768c9-b55e-401f-a406-ad01467c8fc0",
                          "Survey Microsoft SQL Server Database",
                          "Create a survey report of the schemas, tables and columns found in a requested Microsoft SQL Server Database.",
                          MSSQLDeployedImplementationType.MSSQL_SERVER,
                          null,
                          true,
                          ContentCollectionDefinition.SURVEY_ACTIONS,
                          ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * survey-oracle-server
     */
    SURVEY_ORACLE_SERVER("survey-oracle-server",
                         null,
                         null,
                         null,
                         GovernanceEngineDefinition.ORACLE_SURVEY_ENGINE,
                         GovernanceServiceDefinition.ORACLE_SERVER_SURVEY,
                         "60ceade6-cadc-45af-b6b4-42ec5fe41334",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         OracleDeployedImplementationType.ORACLE_SERVER.getQualifiedName(),
                         "c08e01cb-93e3-49cc-8fd0-672c72746f02",
                         "Survey an Oracle Database Server",
                         "Create a survey report of the pluggable databases found in a requested Oracle Database Server.",
                         OracleDeployedImplementationType.ORACLE_SERVER,
                         null,
                         true,
                         ContentCollectionDefinition.SURVEY_ACTIONS,
                         ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * survey-oracle-database
     */
    SURVEY_ORACLE_DATABASE("survey-oracle-database",
                           null,
                           null,
                           null,
                           GovernanceEngineDefinition.ORACLE_SURVEY_ENGINE,
                           GovernanceServiceDefinition.ORACLE_DATABASE_SURVEY,
                           "a6e8ffe6-c149-476d-b9e2-c7591298e81d",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           OracleDeployedImplementationType.ORACLE_DATABASE.getQualifiedName(),
                           "723cdcdd-2951-42f0-b6d6-335a9f02e9b6",
                           "Survey Oracle Pluggable Database",
                           "Create a survey report of the schemas, tables and columns found in a requested Oracle pluggable database.",
                           OracleDeployedImplementationType.ORACLE_SERVER,
                           null,
                           true,
                           ContentCollectionDefinition.SURVEY_ACTIONS,
                           ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * survey-db2luw-server
     */
    SURVEY_DB2LUW_SERVER("survey-db2luw-server",
                         null,
                         null,
                         null,
                         GovernanceEngineDefinition.DB2LUW_SURVEY_ENGINE,
                         GovernanceServiceDefinition.DB2LUW_SERVER_SURVEY,
                         "b72fb853-f48c-486b-b5e7-ebb0761825fc",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         DB2LUWDeployedImplementationType.DB2LUW_SERVER.getQualifiedName(),
                         "4035c4a8-f6af-4e65-8a25-57a6aaa6d740",
                         "Survey a Db2 for Linux, UNIX and Windows Server",
                         "Create a survey report of the databases found in a requested Db2 for Linux, UNIX and Windows Server.",
                         DB2LUWDeployedImplementationType.DB2LUW_SERVER,
                         null,
                         true,
                         ContentCollectionDefinition.SURVEY_ACTIONS,
                         ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * survey-db2luw-database
     */
    SURVEY_DB2LUW_DATABASE("survey-db2luw-database",
                           null,
                           null,
                           null,
                           GovernanceEngineDefinition.DB2LUW_SURVEY_ENGINE,
                           GovernanceServiceDefinition.DB2LUW_DATABASE_SURVEY,
                           "1398cb7b-fb3a-460d-be47-9028726e9a9b",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getQualifiedName(),
                           "4c44867f-0aca-4e23-aaca-8d6d50e3baf6",
                           "Survey Db2 for Linux, UNIX and Windows Database",
                           "Create a survey report of the schemas, tables and columns found in a requested Db2 for Linux, UNIX and Windows database.",
                           DB2LUWDeployedImplementationType.DB2LUW_SERVER,
                           null,
                           true,
                           ContentCollectionDefinition.SURVEY_ACTIONS,
                           ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * create-file-folder
     */
    CREATE_FILE_FOLDER("create-file-folder",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.CREATE_ASSET,
                       "52a82692-7e49-40a8-9b3d-469e87e0220b",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                       "44ff46f1-50f8-443c-836e-78458123340f",
                       "Create File Folder in Open Metadata",
                       "Create a FileFolder asset in open metadata to represent a file system directory.  This is typically used to request a survey or the cataloguing of the files/nested directories within the directory.",
                       ContentCollectionDefinition.CREATE_ACTIONS,
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * create-data-folder
     */
    CREATE_DATA_FOLDER("create-data-folder",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.DATA_FOLDER_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.CREATE_ASSET,
                       "246a8a18-7b10-402a-95d8-acf7115046ff",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                       "f1729715-1e31-471c-93d1-fc9b406a6cec",
                       "Create Data Folder in Open Metadata",
                       "Create a DataFolder asset in open metadata to represent a file system directory.  This is typically used to request a survey or the cataloguing of the files/nested directories within the directory.",
                       ContentCollectionDefinition.CREATE_ACTIONS,
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * delete-file-folder
     */
    DELETE_FILE_FOLDER("delete-file-folder",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.DELETE_ASSET,
                       "ac62ef3c-674e-48d7-b9a2-636cbaee4c6b",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                       "a822725e-e721-4f78-88a0-115b48d9b787",
                       "Delete File Folder from Open Metadata",
                       "Delete the requested FileFolder asset using the template placeholder properties used to create it.",
                       ContentCollectionDefinition.DELETE_ACTIONS,
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * delete-data-folder
     */
    DELETE_DATA_FOLDER("delete-data-folder",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.DATA_FOLDER_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.DELETE_ASSET,
                       "6710231c-bfd6-4701-8aed-52e2a818c3e0",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                       "05e01ab6-e897-4c36-97b8-1c647ed4b85c",
                       "Delete Data Folder from Open Metadata",
                       "Delete the requested DataFolder asset using the template placeholder properties used to create it.",
                       ContentCollectionDefinition.DELETE_ACTIONS,
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * catalog-file-folder
     */
    CATALOG_FILE_FOLDER("catalog-file-folder",
                        null,
                        null,
                        getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.GENERAL_FOLDER_CATALOGUER.getGUID()),
                        GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                        "33fb5cd5-b84d-4c17-95b3-b1b2b99840e0",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getQualifiedName(),
                        "553a6cd6-282c-4358-9c5a-4ac4c2d10b99",
                        "Configure the File Folder Cataloguer",
                        "Link the File Folder asset to the File Folder cataloguer.",
                        null,
                        IntegrationConnectorDefinition.GENERAL_FOLDER_CATALOGUER.getSolutionComponentGUID(),
                        false,
                        ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                        ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * catalog-data-folder
     */
    CATALOG_DATA_FOLDER("catalog-data-folder",
                        null,
                        null,
                        getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.MAINTAIN_LAST_UPDATE_CATALOGUER.getGUID()),
                        GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                        "85424989-b821-49b6-8824-29b1b06536f5",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                        "7ff8405b-a72d-40dd-8b68-e5e96e2fcf47",
                        "Configure the Data Folder Cataloguer",
                        "Link the Data Folder asset to the Data Folder cataloguer.",
                        null,
                        IntegrationConnectorDefinition.GENERAL_FOLDER_CATALOGUER.getSolutionComponentGUID(),
                        false,
                        ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                        ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * create-software-server
     */
    CREATE_SOFTWARE_SERVER("create-software-server",
                           null,
                           null,
                           null,
                           GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                           GovernanceServiceDefinition.CREATE_ASSET,
                           "2be30523-5c6a-4c5d-a9ca-595ea491a047",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           null,
                           "d7c2b07f-f316-43d8-abda-ce53d9a7521f",
                           "Create a software server in Open Metadata",
                           "Create a SoftwareServer asset in open metadata to represent a software server.",
                           ContentCollectionDefinition.CREATE_ACTIONS,
                           ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * delete-software-server
     */
    DELETE_SOFTWARE_SERVER("delete-software-server",
                           null,
                           null,
                           null,
                           GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                           GovernanceServiceDefinition.DELETE_ASSET,
                           "b45aa1ba-690e-4a6d-aaf7-1f6498ea0ea9",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           DeployedImplementationType.SOFTWARE_SERVER.getQualifiedName(),
                           "67bbc3e9-9237-4ef4-b15d-99127bf01704",
                           "Delete a software server from Open Metadata",
                           "Delete the requested SoftwareServer asset using the template placeholder properties used to create it.",
                           ContentCollectionDefinition.DELETE_ACTIONS,
                           ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * catalog-software-server
     */
    CATALOG_SOFTWARE_SERVER("catalog-software-server",
                            null,
                            null,
                            null,
                            GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                            GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                            "134d6840-9f9d-42bb-bd84-a936b6401541",
                            GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                            DeployedImplementationType.SOFTWARE_SERVER.getQualifiedName(),
                            "60a8d234-bb90-4065-986b-362a4d56dd9c",
                            "Configure a software server cataloguer",
                            "Link the SoftwareServer asset to the requested SoftwareServer cataloguer.",
                            ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                            ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * create-kafka-topic
     */
    CREATE_KAFKA_TOPIC("create-kafka-topic",
                        null,
                        getManageAssetRequestParameters(DataAssetTemplateDefinition.KAFKA_TOPIC_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                        GovernanceServiceDefinition.CREATE_ASSET,
                        "3e880bc6-729e-4666-8124-3c9d033f54fd",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName(),
                       "65ef6295-6ddf-4c88-9764-eff5a84f402b",
                       "Create Apache Kafka Topic in Open Metadata",
                       "Create a KafkaTopic asset in open metadata.",
                       ContentCollectionDefinition.CREATE_ACTIONS,
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * harvest-open-lineage-topic
     */
    HARVEST_OPEN_LINEAGE_TOPIC("harvest-open-lineage-topic",
                               null,
                               null,
                               getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getGUID()),
                               GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                               GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                               "9b7e56ca-d145-48e1-8c69-2bc1327f008b",
                               GovernanceDomain.DATA.getOrdinal(),
                               DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName(),
                               "7fc9f128-2b7f-43fb-8e95-171415240aff",
                               "Set up Open Lineage Event capture",
                               "Configure a listener to capture events from the Open Lineage Proxy (via Kafka)",
                               null, // see SolutionLinkingWire
                               IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getSolutionComponentGUID(),
                               false,
                               ContentCollectionDefinition.ANALYTICAL_ACTIONS,
                               ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * delete-kafka-topic
     */
    DELETE_KAFKA_TOPIC("delete-kafka-topic",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.KAFKA_TOPIC_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                       GovernanceServiceDefinition.CREATE_ASSET,
                       "7a800598-6d62-460b-b7be-0c545535622c",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName(),
                       "6546488f-d8a2-415d-9d03-6ab72ba14c40",
                       "Delete an Apache Kafka Topic from Open Metadata",
                       "Delete the asset from the metadata store.",
                       ContentCollectionDefinition.DELETE_ACTIONS,
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * create-unity-catalog-server
     */
    CREATE_UC_SERVER("create-unity-catalog-server",
                     null,
                     getManageAssetRequestParameters(SoftwareServerTemplateDefinition.UNITY_CATALOG_SERVER_TEMPLATE.getTemplateGUID()),
                     null,
                     GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                     GovernanceServiceDefinition.CREATE_ASSET,
                     "78e47705-a159-4e3d-9199-3a2c9400dcee",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                     "e9ca091e-f9e7-4e5b-bd45-6d4f0e2d55aa",
                     "Create an OSS Unity Catalog Server in Open Metadata",
                     "Create the asset in the metadata store.",
                     ContentCollectionDefinition.CREATE_ACTIONS,
                     ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * delete-unity-catalog-server
     */
    DELETE_UC_SERVER("delete-unity-catalog-server",
                     null,
                     getManageAssetRequestParameters(SoftwareServerTemplateDefinition.UNITY_CATALOG_SERVER_TEMPLATE.getTemplateGUID()),
                     null,
                     GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                     GovernanceServiceDefinition.DELETE_ASSET,
                     "986d550a-c5d8-4c44-9f94-601a15fc25f1",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                     "835b4fe4-03cf-4f4d-b0b5-e25d7dfe3299",
                     "Delete an OSS Unity Catalog Server from Open Metadata",
                     "Delete asset from the metadata repository.",
                     ContentCollectionDefinition.DELETE_ACTIONS,
                     ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * create-databricks-unity-catalog-server
     */
    CREATE_DB_UC_SERVER("create-databricks-unity-catalog-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.DATABRICKS_UC_SERVER_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CREATE_ASSET,
                        "323d8a5c-4f79-4bc0-a35a-0c39d1990a9e",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName(),
                        "8bba4467-0430-4a94-b8c5-b881fd3f9396",
                        "Create a Databricks Unity Catalog Server in Open Metadata",
                        "Create asset in the metadata repository.",
                        ContentCollectionDefinition.CREATE_ACTIONS,
                        ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * delete-databricks-unity-catalog-server
     */
    DELETE_DB_UC_SERVER("delete-databricks-unity-catalog-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.DATABRICKS_UC_SERVER_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.DELETE_ASSET,
                        "cfeafd56-a6dd-41e5-bf0e-33b65639085d",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName(),
                        "c0ac2017-d7f4-49c2-9e9e-e995ced94177",
                        "Delete a Databricks Unity Catalog Server from Open Metadata",
                        "Delete asset from the metadata repository.",
                        ContentCollectionDefinition.DELETE_ACTIONS,
                        ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * catalog-unity-catalog-server
     */
    CATALOG_UC_SERVER("catalog-unity-catalog-server",
                      null,
                      null,
                      getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getGUID()),
                      GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                      GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                      "1b2d71c8-b7f9-4b9b-a466-f20e529391ef",
                      GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                      UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                      "5db21b73-bd6a-41ea-b4ff-235ede908bdf",
                      "Configure the Unity Catalog Server Cataloguer",
                      "Link the Unity Catalog Server asset to the Unity Catalog Server cataloguer.",
                      null,
                      IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getSolutionComponentGUID(),
                      false,
                      ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                      ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * create-apache-atlas-server
     */
    CREATE_ATLAS_SERVER("create-apache-atlas-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.APACHE_ATLAS_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.ATLAS_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CREATE_ASSET,
                        "c4ea5182-1707-4e43-9151-ad3c42107b00",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                        "cff46354-5b55-4b2f-bbb8-83db28507630",
                        "Create Apache Atlas Server in Open Metadata",
                        "Create an asset that represents the server.",
                        ContentCollectionDefinition.CREATE_ACTIONS,
                        ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * delete-apache-atlas-server
     */
    DELETE_ATLAS_SERVER("delete-apache-atlas-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.APACHE_ATLAS_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.ATLAS_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.DELETE_ASSET,
                        "7bed9078-085f-40fd-9f72-168196d7b277",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                        "521c46f9-b1a8-44a5-a5a8-ddae9bc21230",
                        "Delete Apache Atlas Server from Open Metadata",
                        "Delete asset from the metadata repository.",
                        ContentCollectionDefinition.DELETE_ACTIONS,
                        ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * catalog-apache-atlas-server
     */
    CATALOG_ATLAS_SERVER("catalog-apache-atlas-server",
                         null,
                         null,
                         getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.APACHE_ATLAS_EXCHANGE.getGUID()),
                         GovernanceEngineDefinition.ATLAS_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                         "95a89892-e66f-4ad7-913a-9b10ce7c64ac",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                         "d5eba93e-06d7-4f08-a082-5aeaa6c70b2b",
                         "Configure Apache Atlas Server Cataloguer",
                         "Link the Apache Atlas server asset to the Apache Atlas Server cataloguer.",
                        null,
                         IntegrationConnectorDefinition.APACHE_ATLAS_EXCHANGE.getSolutionComponentGUID(),
                         false,
                         ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                         ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * create-apache-kafka-server
     */
    CREATE_KAFKA_SERVER("create-apache-kafka-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.KAFKA_SERVER_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.KAFKA_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CREATE_ASSET,
                        "8f735dbc-7bc3-442f-8b16-699ef43a15f3",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                        "36fa75fa-dd6e-4120-a6f4-2c1abf4e565d",
                        "Create Apache Kafka Server in Open Metadata",
                        "Create an asset that represents the server.",
                        ContentCollectionDefinition.CREATE_ACTIONS,
                        ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    /**
     * delete-apache-kafka-server
     */
    DELETE_KAFKA_SERVER("delete-apache-kafka-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.KAFKA_SERVER_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.KAFKA_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.DELETE_ASSET,
                        "9eace0dd-bcd6-41df-86f7-4b5799774411",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                        "ab494047-24c5-4643-909c-183140ff2763",
                        "Delete Apache Kafka Server from Open Metadata",
                        "Delete asset from the metadata repository.",
                        ContentCollectionDefinition.DELETE_ACTIONS,
                        ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    /**
     * catalog-apache-kafka-server
     */
    CATALOG_KAFKA_SERVER("catalog-apache-kafka-server",
                         null,
                         null,
                         getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.KAFKA_SERVER_CATALOGUER.getGUID()),
                         GovernanceEngineDefinition.KAFKA_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                         "81f0fad0-84eb-4926-865f-c518df876cab",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                         "10a883e3-e6f7-4fab-9ed4-7edf6a1837ad",
                         "Configure Apache Kafka Server Cataloguer",
                         "Link the Apache Kafka server asset to the Apache Kafka Server cataloguer.",
                         null,
                         IntegrationConnectorDefinition.KAFKA_SERVER_CATALOGUER.getSolutionComponentGUID(),
                         false,
                         ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                         ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),


    /**
     * create-omag-server-platform
     */
    CREATE_OMAG_SERVER_PLATFORM("create-omag-server-platform",
                                null,
                                getManageAssetRequestParameters(EgeriaSoftwareServerTemplateDefinition.OMAG_SERVER_PLATFORM_TEMPLATE.getTemplateGUID()),
                                null,
                                GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                                GovernanceServiceDefinition.CREATE_ASSET,
                                "2cb0bfc6-7bd9-4144-b0ad-4cd3a7acb502",
                                GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                                EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName(),
                                "1bd75045-831c-4105-938f-19bca9bfc555",
                                "Create OMAG Server Platform in Open Metadata",
                                "Create an asset that represents the platform.",
                                ContentCollectionDefinition.CREATE_ACTIONS,
                                ContentPackDefinition.EGERIA_CONTENT_PACK),

    /**
     * delete-omag-server-platform
     */
    DELETE_OMAG_SERVER_PLATFORM("delete-omag-server-platform",
                                null,
                                getManageAssetRequestParameters(EgeriaSoftwareServerTemplateDefinition.OMAG_SERVER_PLATFORM_TEMPLATE.getTemplateGUID()),
                                null,
                                GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                                GovernanceServiceDefinition.DELETE_ASSET,
                                "f24a52a9-553f-4eb2-b62e-faaf4a17c662",
                                GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                                EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName(),
                                "4ef6b15b-cd35-413b-b63f-c6b605edda92",
                                "Delete OMAG Server Platform from Open Metadata",
                                "Delete asset from the metadata repository.",
                                ContentCollectionDefinition.DELETE_ACTIONS,
                                ContentPackDefinition.EGERIA_CONTENT_PACK),

    /**
     * catalog-omag-server-platform
     */
    CATALOG_OMAG_SERVER_PLATFORM("catalog-omag-server-platform",
                                 null,
                                 null,
                                 getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getGUID()),
                                 GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE,
                                 GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                                 "e22b0fbb-f63e-4aa2-9436-6b34dc0246c7",
                                 GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                                 EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName(),
                                 "ce653660-36bd-4f44-a9c3-b94075f41813",
                                 "Configure the OMAG Server Platform Cataloguer",
                                 "Link the OMAG Server Platform asset to the OMAG Server Platform cataloguer.",
                                 null,
                                 IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getSolutionComponentGUID(),
                                 false,
                                 ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                                 ContentPackDefinition.EGERIA_CONTENT_PACK),


    /**
     * create-postgres-server
     */
    CREATE_POSTGRES_SERVER("create-postgres-server",
                           null,
                           getManageAssetRequestParameters(SoftwareServerTemplateDefinition.POSTGRES_SERVER_TEMPLATE.getTemplateGUID()),
                           null,
                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                           GovernanceServiceDefinition.CREATE_ASSET,
                           "3facbdba-43c6-44b8-a222-ad0ad2c3c3d5",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                           "0e5f926f-19aa-4a4b-865b-ed80d63d9752",
                           "Create PostgreSQL Server asset in Open Metadata",
                           "Create an asset that represents the server.",
                           ContentCollectionDefinition.CREATE_ACTIONS,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * delete-postgres-server
     */
    DELETE_POSTGRES_SERVER("delete-postgres-server",
                           null,
                           getManageAssetRequestParameters(SoftwareServerTemplateDefinition.POSTGRES_SERVER_TEMPLATE.getTemplateGUID()),
                           null,
                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                           GovernanceServiceDefinition.DELETE_ASSET,
                           "5c49625e-8935-44fa-9076-5e099cf392ca",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                           "e84da60e-cf37-4506-8594-aac93826185a",
                           "Delete PostgreSQL Server from Open Metadata",
                           "Delete asset from the metadata repository.",
                           ContentCollectionDefinition.DELETE_ACTIONS,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * catalog-postgres-server
     */
    CATALOG_POSTGRES_SERVER("catalog-postgres-server",
                            null,
                            null,
                            getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getGUID()),
                            GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                            GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                            "dab2303b-7bac-4985-b8eb-4a706e77d036",
                            GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                            PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                            "015d6b33-e6bc-4d77-822c-4a9d536ef542",
                            "Configure PostgreSQL Server Cataloguer",
                            "Link the PostgreSQL server asset to the PostgreSQL Server cataloguer.",
                            null,
                            IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getSolutionComponentGUID(),
                            false,
                            ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                            ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * create-postgres-database
     */
    CREATE_POSTGRES_DB("create-postgres-database",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.POSTGRES_DATABASE_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.CREATE_ASSET,
                       "47353b59-b1cd-453a-841f-3873130703b7",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                       "1e13b511-e452-4fe4-a075-c0a5c021c051",
                       "Create PostgreSQL Database from Open Metadata",
                       "Create an open metadata element that represents a PostgreSQL database using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the database and the connection.  The schemas, tables and columns are not cataloged by this component.",
                       ContentCollectionDefinition.CREATE_ACTIONS,
                       ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * delete-postgres-database
     */
    DELETE_POSTGRES_DB("delete-postgres-database",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.POSTGRES_DATABASE_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.DELETE_ASSET,
                       "610d8562-7e48-4ba8-b596-d66b7888e268",
                       GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                       PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                       "c743bbc6-6e69-485c-8082-aee444ccc88e",
                       "Delete PostgreSQL Database from Open Metadata",
                       "Locate and delete the open metadata element that represents the PostgreSQL database described in the request.  The real database on the PostgreSQL server (if any) is unaffected by this component.",
                       ContentCollectionDefinition.DELETE_ACTIONS,
                       ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * catalog-postgres-database
     */
    CATALOG_POSTGRES_DATABASE("catalog-postgres-database",
                              null,
                              null,
                              getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.JDBC_CATALOGUER.getGUID()),
                              GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                              GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                              "32ca425d-6aeb-40f0-bc7c-508a9689d24e",
                              GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                              PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                              "493d7286-7423-4621-8e64-3bb714e8a45a",
                              "Configure PostgreSQL Database Cataloguer",
                              "Link the PostgreSQL database asset to the PostgreSQL Database cataloguer.",
                              null,
                              IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                              false,
                              ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                              ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * create-mssql-server
     */
    CREATE_MSSQL_SERVER("create-mssql-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.MSSQL_SERVER_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.MSSQL_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CREATE_ASSET,
                        "7b9f329b-55cd-4914-be9c-eeb21d1271d8",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        MSSQLDeployedImplementationType.MSSQL_SERVER.getQualifiedName(),
                        "100535e1-90a0-497d-8f2a-430f2481c161",
                        "Create Microsoft SQL Server asset in Open Metadata",
                        "Create an asset that represents the server.",
                        ContentCollectionDefinition.CREATE_ACTIONS,
                        ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * delete-mssql-server
     */
    DELETE_MSSQL_SERVER("delete-mssql-server",
                        null,
                        getManageAssetRequestParameters(SoftwareServerTemplateDefinition.MSSQL_SERVER_TEMPLATE.getTemplateGUID()),
                        null,
                        GovernanceEngineDefinition.MSSQL_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.DELETE_ASSET,
                        "a05bece3-3fba-4d3d-9836-46c4fec35c8c",
                        GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                        MSSQLDeployedImplementationType.MSSQL_SERVER.getQualifiedName(),
                        "36d5b51f-5505-4a12-b61f-f7b769c3de23",
                        "Delete Microsoft SQL Server from Open Metadata",
                        "Delete asset from the metadata repository.",
                        ContentCollectionDefinition.DELETE_ACTIONS,
                        ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * catalog-mssql-server
     */
    CATALOG_MSSQL_SERVER("catalog-mssql-server",
                         null,
                         null,
                         getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.MSSQL_SERVER_CATALOGUER.getGUID()),
                         GovernanceEngineDefinition.MSSQL_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                         "90c63e62-ad51-4457-904c-d8bc1613a3ca",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         MSSQLDeployedImplementationType.MSSQL_SERVER.getQualifiedName(),
                         "5a842b20-ace2-425c-91d8-3fb14e92d992",
                         "Configure Microsoft SQL Server Cataloguer",
                         "Link the Microsoft SQL Server asset to the Microsoft SQL Server cataloguer.",
                         null,
                         IntegrationConnectorDefinition.MSSQL_SERVER_CATALOGUER.getSolutionComponentGUID(),
                         false,
                         ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                         ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * create-mssql-database
     */
    CREATE_MSSQL_DB("create-mssql-database",
                    null,
                    getManageAssetRequestParameters(DataAssetTemplateDefinition.MSSQL_DATABASE_TEMPLATE.getTemplateGUID()),
                    null,
                    GovernanceEngineDefinition.MSSQL_GOVERNANCE_ENGINE,
                    GovernanceServiceDefinition.CREATE_ASSET,
                    "80e676cc-e1f1-4040-9b78-e8be8085cfad",
                    GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                    MSSQLDeployedImplementationType.MSSQL_DATABASE.getQualifiedName(),
                    "4470bc7f-2b98-49d4-96fe-9f9d2b89650c",
                    "Create Microsoft SQL Server Database from Open Metadata",
                    "Create an open metadata element that represents a Microsoft SQL Server database using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the database and the connection.  The schemas, tables and columns are not cataloged by this component.",
                    ContentCollectionDefinition.CREATE_ACTIONS,
                    ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * delete-mssql-database
     */
    DELETE_MSSQL_DB("delete-mssql-database",
                    null,
                    getManageAssetRequestParameters(DataAssetTemplateDefinition.MSSQL_DATABASE_TEMPLATE.getTemplateGUID()),
                    null,
                    GovernanceEngineDefinition.MSSQL_GOVERNANCE_ENGINE,
                    GovernanceServiceDefinition.DELETE_ASSET,
                    "bb13577c-54a1-4c7e-95e2-f8a9747a9d91",
                    GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                    MSSQLDeployedImplementationType.MSSQL_DATABASE.getQualifiedName(),
                    "14e2d369-9d7a-43b4-9b18-b8227f891a76",
                    "Delete Microsoft SQL Server Database from Open Metadata",
                    "Locate and delete the open metadata element that represents the Microsoft SQL Server database described in the request.  The real database on the Microsoft SQL Server (if any) is unaffected by this component.",
                    ContentCollectionDefinition.DELETE_ACTIONS,
                    ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * catalog-mssql-database
     */
    CATALOG_MSSQL_DATABASE("catalog-mssql-database",
                           null,
                           null,
                           getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.JDBC_CATALOGUER.getGUID()),
                           GovernanceEngineDefinition.MSSQL_GOVERNANCE_ENGINE,
                           GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                           "72cfc770-612f-4f19-9e99-7474df30b66b",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           MSSQLDeployedImplementationType.MSSQL_DATABASE.getQualifiedName(),
                           "abaee504-eb4e-4b73-b3b0-e946979adf9b",
                           "Configure Microsoft SQL Server Database Cataloguer",
                           "Link the Microsoft SQL Server database asset to the Microsoft SQL Server Database cataloguer.",
                           null,
                           IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                           false,
                           ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                           ContentPackDefinition.MSSQL_CONTENT_PACK),

    /**
     * create-oracle-server
     */
    CREATE_ORACLE_SERVER("create-oracle-server",
                         null,
                         getManageAssetRequestParameters(SoftwareServerTemplateDefinition.ORACLE_SERVER_TEMPLATE.getTemplateGUID()),
                         null,
                         GovernanceEngineDefinition.ORACLE_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.CREATE_ASSET,
                         "5a00ad8d-341f-4100-99dc-fb75224337f9",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         OracleDeployedImplementationType.ORACLE_SERVER.getQualifiedName(),
                         "b141bae4-8a27-4165-a4ed-20792a1e2814",
                         "Create Oracle Database Server asset in Open Metadata",
                         "Create an asset that represents the server.",
                         ContentCollectionDefinition.CREATE_ACTIONS,
                         ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * delete-oracle-server
     */
    DELETE_ORACLE_SERVER("delete-oracle-server",
                         null,
                         getManageAssetRequestParameters(SoftwareServerTemplateDefinition.ORACLE_SERVER_TEMPLATE.getTemplateGUID()),
                         null,
                         GovernanceEngineDefinition.ORACLE_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.DELETE_ASSET,
                         "6f0fa1d1-24bb-4da1-ba1e-2d98ffc1c191",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         OracleDeployedImplementationType.ORACLE_SERVER.getQualifiedName(),
                         "54659978-5c1a-4888-8a35-2a753bf5859c",
                         "Delete Oracle Database Server from Open Metadata",
                         "Delete asset from the metadata repository.",
                         ContentCollectionDefinition.DELETE_ACTIONS,
                         ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * catalog-oracle-server
     */
    CATALOG_ORACLE_SERVER("catalog-oracle-server",
                          null,
                          null,
                          getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.ORACLE_SERVER_CATALOGUER.getGUID()),
                          GovernanceEngineDefinition.ORACLE_GOVERNANCE_ENGINE,
                          GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                          "ff947ba4-cda7-4a79-a9e4-7beb4abeddb6",
                          GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                          OracleDeployedImplementationType.ORACLE_SERVER.getQualifiedName(),
                          "934f2655-8e4b-471a-930f-9261f3bec67e",
                          "Configure Oracle Database Server Cataloguer",
                          "Link the Oracle Database Server asset to the Oracle Database Server cataloguer.",
                          null,
                          IntegrationConnectorDefinition.ORACLE_SERVER_CATALOGUER.getSolutionComponentGUID(),
                          false,
                          ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                          ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * create-oracle-database
     */
    CREATE_ORACLE_DB("create-oracle-database",
                     null,
                     getManageAssetRequestParameters(DataAssetTemplateDefinition.ORACLE_DATABASE_TEMPLATE.getTemplateGUID()),
                     null,
                     GovernanceEngineDefinition.ORACLE_GOVERNANCE_ENGINE,
                     GovernanceServiceDefinition.CREATE_ASSET,
                     "a99e9d85-b949-40dc-b8b0-fefdbb995d1a",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     OracleDeployedImplementationType.ORACLE_DATABASE.getQualifiedName(),
                     "8f0a13dc-9e09-421a-acaa-e2c0d5def0de",
                     "Create Oracle Pluggable Database from Open Metadata",
                     "Create an open metadata element that represents an Oracle pluggable database using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the database and the connection.  The schemas, tables and columns are not cataloged by this component.",
                     ContentCollectionDefinition.CREATE_ACTIONS,
                     ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * delete-oracle-database
     */
    DELETE_ORACLE_DB("delete-oracle-database",
                     null,
                     getManageAssetRequestParameters(DataAssetTemplateDefinition.ORACLE_DATABASE_TEMPLATE.getTemplateGUID()),
                     null,
                     GovernanceEngineDefinition.ORACLE_GOVERNANCE_ENGINE,
                     GovernanceServiceDefinition.DELETE_ASSET,
                     "a219e805-e827-42ce-bd31-f20274b1120e",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     OracleDeployedImplementationType.ORACLE_DATABASE.getQualifiedName(),
                     "d50d0601-bab4-4a4a-bcc3-52c59d899b8f",
                     "Delete Oracle Pluggable Database from Open Metadata",
                     "Locate and delete the open metadata element that represents the Oracle pluggable database described in the request.  The real pluggable database on the Oracle Database Server (if any) is unaffected by this component.",
                     ContentCollectionDefinition.DELETE_ACTIONS,
                     ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * catalog-oracle-database
     */
    CATALOG_ORACLE_DATABASE("catalog-oracle-database",
                            null,
                            null,
                            getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.JDBC_CATALOGUER.getGUID()),
                            GovernanceEngineDefinition.ORACLE_GOVERNANCE_ENGINE,
                            GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                            "404a914b-a386-436c-9aa2-bf033c8ae925",
                            GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                            OracleDeployedImplementationType.ORACLE_DATABASE.getQualifiedName(),
                            "73b92b06-d6a6-4b53-9356-2cb2aec61ae1",
                            "Configure Oracle Pluggable Database Cataloguer",
                            "Link the Oracle pluggable database asset to the Oracle Pluggable Database cataloguer.",
                            null,
                            IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                            false,
                            ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                            ContentPackDefinition.ORACLE_CONTENT_PACK),

    /**
     * create-db2luw-server
     */
    CREATE_DB2LUW_SERVER("create-db2luw-server",
                         null,
                         getManageAssetRequestParameters(SoftwareServerTemplateDefinition.DB2LUW_SERVER_TEMPLATE.getTemplateGUID()),
                         null,
                         GovernanceEngineDefinition.DB2LUW_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.CREATE_ASSET,
                         "2b6b2a1a-1ff2-4658-994b-9796f6339307",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         DB2LUWDeployedImplementationType.DB2LUW_SERVER.getQualifiedName(),
                         "61a99c0e-becf-4abb-8053-ea89fbac3e1d",
                         "Create Db2 for Linux, UNIX and Windows Server asset in Open Metadata",
                         "Create an asset that represents the server.",
                         ContentCollectionDefinition.CREATE_ACTIONS,
                         ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * delete-db2luw-server
     */
    DELETE_DB2LUW_SERVER("delete-db2luw-server",
                         null,
                         getManageAssetRequestParameters(SoftwareServerTemplateDefinition.DB2LUW_SERVER_TEMPLATE.getTemplateGUID()),
                         null,
                         GovernanceEngineDefinition.DB2LUW_GOVERNANCE_ENGINE,
                         GovernanceServiceDefinition.DELETE_ASSET,
                         "f695f8d1-8dd2-40c9-ae03-4da84e41716a",
                         GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                         DB2LUWDeployedImplementationType.DB2LUW_SERVER.getQualifiedName(),
                         "6cece9ac-6b6b-478f-83e1-40ab2e48b030",
                         "Delete Db2 for Linux, UNIX and Windows Server from Open Metadata",
                         "Delete asset from the metadata repository.",
                         ContentCollectionDefinition.DELETE_ACTIONS,
                         ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * catalog-db2luw-server
     */
    CATALOG_DB2LUW_SERVER("catalog-db2luw-server",
                          null,
                          null,
                          getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.DB2LUW_SERVER_CATALOGUER.getGUID()),
                          GovernanceEngineDefinition.DB2LUW_GOVERNANCE_ENGINE,
                          GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                          "cbecbcf2-f0ef-49cf-9611-00edea53b92a",
                          GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                          DB2LUWDeployedImplementationType.DB2LUW_SERVER.getQualifiedName(),
                          "d44251ec-ad6b-4eae-933f-8d3f3a138d78",
                          "Configure Db2 for Linux, UNIX and Windows Server Cataloguer",
                          "Link the Db2 for Linux, UNIX and Windows Server asset to the Db2 for Linux, UNIX and Windows Server cataloguer.",
                          null,
                          IntegrationConnectorDefinition.DB2LUW_SERVER_CATALOGUER.getSolutionComponentGUID(),
                          false,
                          ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                          ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * create-db2luw-database
     */
    CREATE_DB2LUW_DB("create-db2luw-database",
                     null,
                     getManageAssetRequestParameters(DataAssetTemplateDefinition.DB2LUW_DATABASE_TEMPLATE.getTemplateGUID()),
                     null,
                     GovernanceEngineDefinition.DB2LUW_GOVERNANCE_ENGINE,
                     GovernanceServiceDefinition.CREATE_ASSET,
                     "1ae47726-ec62-4d21-9bb9-f581a70e0664",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getQualifiedName(),
                     "de47221a-2a14-46eb-a403-f563ee4275d8",
                     "Create Db2 for Linux, UNIX and Windows Database from Open Metadata",
                     "Create an open metadata element that represents a Db2 for Linux, UNIX and Windows database using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the database and the connection.  The schemas, tables and columns are not cataloged by this component.",
                     ContentCollectionDefinition.CREATE_ACTIONS,
                     ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * delete-db2luw-database
     */
    DELETE_DB2LUW_DB("delete-db2luw-database",
                     null,
                     getManageAssetRequestParameters(DataAssetTemplateDefinition.DB2LUW_DATABASE_TEMPLATE.getTemplateGUID()),
                     null,
                     GovernanceEngineDefinition.DB2LUW_GOVERNANCE_ENGINE,
                     GovernanceServiceDefinition.DELETE_ASSET,
                     "c538a503-9fba-4780-9982-2a1e18ac3785",
                     GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                     DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getQualifiedName(),
                     "46ad7ad7-45df-4d2f-8475-6a3bfcda2f61",
                     "Delete Db2 for Linux, UNIX and Windows Database from Open Metadata",
                     "Locate and delete the open metadata element that represents the Db2 for Linux, UNIX and Windows database described in the request.  The real database on the Db2 for Linux, UNIX and Windows Server (if any) is unaffected by this component.",
                     ContentCollectionDefinition.DELETE_ACTIONS,
                     ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * catalog-db2luw-database
     */
    CATALOG_DB2LUW_DATABASE("catalog-db2luw-database",
                            null,
                            null,
                            getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.JDBC_CATALOGUER.getGUID()),
                            GovernanceEngineDefinition.DB2LUW_GOVERNANCE_ENGINE,
                            GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                            "cf6c8f8c-0d21-4114-8587-59a652bacd31",
                            GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                            DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getQualifiedName(),
                            "74c7579e-9541-4052-a0ea-7fe280bd3a3a",
                            "Configure Db2 for Linux, UNIX and Windows Database Cataloguer",
                            "Link the Db2 for Linux, UNIX and Windows database asset to the Db2 for Linux, UNIX and Windows Database cataloguer.",
                            null,
                            IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                            false,
                            ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                            ContentPackDefinition.DB2LUW_CONTENT_PACK),

    /**
     * create-postgres-schema
     */
    CREATE_POSTGRES_SCHEMA("create-postgres-schema",
                           null,
                           getManageAssetRequestParameters(DataAssetTemplateDefinition.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID()),
                           null,
                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                           GovernanceServiceDefinition.CREATE_ASSET,
                           "b0c07a56-2d4b-4665-93a3-e33cbb1aba61",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                           "c9779e50-9585-4e65-9cd7-4bc00757fe97",
                           "Create PostgreSQL Schema in Open Metadata",
                           "Create an open metadata element that represents a PostgreSQL schema using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the schema and the connection.  The tables and columns are not cataloged by this component.",
                           ContentCollectionDefinition.CREATE_ACTIONS,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * delete-postgres-schema
     */
    DELETE_POSTGRES_SCHEMA("delete-postgres-schema",
                           null,
                           getManageAssetRequestParameters(DataAssetTemplateDefinition.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID()),
                           null,
                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                           GovernanceServiceDefinition.DELETE_ASSET,
                           "08fa65f0-8925-46bd-8c19-806d47ce2da1",
                           GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                           PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                           "f2dd4107-88c1-4759-b62b-6ca68f5b8d8b",
                           "Delete PostgreSQL Schema from Open Metadata",
                           "Locate and delete the open metadata element that represents the PostgreSQL schema described in the request.  The real schema on the PostgreSQL server (if any) is unaffected by this component.",
                           ContentCollectionDefinition.DELETE_ACTIONS,
                           ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * catalog-postgres-schema
     */
    CATALOG_POSTGRES_SCHEMA("catalog-postgres-schema",
                            null,
                            null,
                            getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.JDBC_CATALOGUER.getGUID()),
                            GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                            GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                            "c4702cb9-9787-4756-889d-b7d8efd3d240",
                            GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                            "cd5c93c0-dc22-4be4-bd28-2a5b3ae11c36",
                            "Configure PostgreSQL Schema Cataloguer",
                            "Link the PostgreSQL schema asset to the PostgreSQL Schema cataloguer.",
                            null,
                            IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                            false,
                            ContentCollectionDefinition.CATALOG_ASSET_CONTENTS,
                            ContentPackDefinition.POSTGRES_CONTENT_PACK),


    ;

    private final String                               governanceRequestType;
    private final String                               serviceRequestType;
    private final Map<String, String>                  requestParameters;
    private final List<NewActionTarget>                actionTargets;
    private final GovernanceEngineDefinition           governanceEngine;
    private final GovernanceServiceDefinition          governanceService;
    private final String                               governanceActionTypeGUID;
    private final int                                  governanceDomainIdentifier;
    private final String                               supportedElementQualifiedName;
    private final String                               solutionComponentGUID;
    private final String                               solutionComponentName;
    private final String                               solutionComponentDescription;
    private final DeployedImplementationTypeDefinition worksWithTechnology;
    private final String                               configuresComponentGUID;
    private final boolean                              linkToMetadataServerSolutionComponent;
    private final ContentCollectionDefinition          folder;
    private final ContentPackDefinition                contentPackDefinition;

    /**
     * Set up request parameters.
     *
     * @return map
     */
    static Map<String, String> getFolderSurveyRequestParameters()
    {
        Map<String,String> requestParameters = new HashMap<>();

        requestParameters.put(FolderRequestParameter.ANALYSIS_LEVEL.getName(), "TOP_LEVEL_ONLY");

        return requestParameters;
    }


    /**
     * Set up request parameters.
     *
     * @return map
     */
    static Map<String, String> getAllFoldersSurveyRequestParameters()
    {
        Map<String,String> requestParameters = new HashMap<>();

        requestParameters.put(FolderRequestParameter.ANALYSIS_LEVEL.getName(), "ALL_FOLDERS");

        return requestParameters;
    }

    /**
     * Set up request parameters.
     *
     * @return map
     */
    static Map<String, String> getFolderAndFilesSurveyRequestParameters()
    {
        Map<String,String> requestParameters = new HashMap<>();

        requestParameters.put(FolderRequestParameter.ANALYSIS_LEVEL.getName(), "TOP_LEVEL_AND_FILES");

        return requestParameters;
    }

    /**
     * Set up request parameters.
     *
     * @return map
     */
    static Map<String, String> getAllFolderAndFilesSurveyRequestParameters()
    {
        Map<String,String> requestParameters = new HashMap<>();

        requestParameters.put(FolderRequestParameter.ANALYSIS_LEVEL.getName(), "ALL_FOLDERS_AND_FILES");

        return requestParameters;
    }


    /**
     * Set up request parameters.
     *
     * @return map
     */
    static Map<String, String> getManageAssetRequestParameters(String templateGUID)
    {
        Map<String,String> requestParameters = new HashMap<>();

        requestParameters.put(ManageAssetRequestParameter.TEMPLATE_GUID.getName(), templateGUID);

        return requestParameters;
    }


    /**
     * Return the list of action targets that should be attached to the consuming governance action type.
     *
     * @return list of action targets
     */
    static List<NewActionTarget> getCatalogTargetAssetActionTargets(String integrationConnectorGUID)
    {
        List<NewActionTarget> actionTargets = new ArrayList<>();

        NewActionTarget newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(ActionTarget.INTEGRATION_CONNECTOR.name);
        newActionTarget.setActionTargetGUID(integrationConnectorGUID);

        actionTargets.add(newActionTarget);

        return actionTargets;
    }


    /**
     * Return the request type enum value.
     *
     * @param governanceRequestType request type used by the caller
     * @param serviceRequestType option map to a request type known by the service
     * @param requestParameters pre-defined request parameters
     * @param actionTargets predefined action targets (for governance action type)
     * @param governanceEngine governance engine that supports this request type
     * @param governanceService governance service that implements this request type
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param governanceDomainIdentifier identifier of the governance domain
     * @param solutionComponentGUID unique identifier of the solution component for the governance action type
     * @param solutionComponentName display name of the solution component for the governance action type
     * @param solutionComponentDescription description of the solution component for the governance action type
     * @param folder folder for the design library
     * @param contentPackDefinition which content pack?
     */
    RequestTypeDefinition(String                      governanceRequestType,
                          String                      serviceRequestType,
                          Map<String, String>         requestParameters,
                          List<NewActionTarget>       actionTargets,
                          GovernanceEngineDefinition  governanceEngine,
                          GovernanceServiceDefinition governanceService,
                          String                      governanceActionTypeGUID,
                          int                         governanceDomainIdentifier,
                          String                      supportedElementQualifiedName,
                          String                      solutionComponentGUID,
                          String                      solutionComponentName,
                          String                      solutionComponentDescription,
                          ContentCollectionDefinition folder,
                          ContentPackDefinition       contentPackDefinition)
    {
        this.governanceRequestType                 = governanceRequestType;
        this.serviceRequestType                    = serviceRequestType;
        this.requestParameters                     = requestParameters;
        this.actionTargets                         = actionTargets;
        this.governanceEngine                      = governanceEngine;
        this.governanceService                     = governanceService;
        this.governanceActionTypeGUID              = governanceActionTypeGUID;
        this.governanceDomainIdentifier            = governanceDomainIdentifier;
        this.supportedElementQualifiedName         = supportedElementQualifiedName;
        this.solutionComponentGUID                 = solutionComponentGUID;
        this.solutionComponentName                 = solutionComponentName;
        this.solutionComponentDescription          = solutionComponentDescription;
        this.contentPackDefinition                 = contentPackDefinition;
        this.worksWithTechnology                   = null;
        this.configuresComponentGUID               = null;
        this.folder                                = folder;
        this.linkToMetadataServerSolutionComponent = true;
    }


    /**
     * Return the request type enum value.
     *
     * @param governanceRequestType request type used by the caller
     * @param serviceRequestType option map to a request type known by the service
     * @param requestParameters pre-defined request parameters
     * @param actionTargets predefined action targets (for governance action type)
     * @param governanceEngine governance engine that supports this request type
     * @param governanceService governance service that implements this request type
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param governanceDomainIdentifier identifier of the governance domain
     * @param solutionComponentGUID unique identifier of the solution component for the governance action type
     * @param solutionComponentName display name of the solution component for the governance action type
     * @param solutionComponentDescription description of the solution component for the governance action type
     * @param worksWithTechnology technology that should be linked to the solution component
     * @param configuresComponentGUID component that is configured by this request type
     * @param linkToMetadataServerSolutionComponent should this component link to the open metadata server solution component
     * @param folder folder for the design library
     * @param contentPackDefinition which content pack?
     */
    RequestTypeDefinition(String                               governanceRequestType,
                          String                               serviceRequestType,
                          Map<String, String>                  requestParameters,
                          List<NewActionTarget>                actionTargets,
                          GovernanceEngineDefinition           governanceEngine,
                          GovernanceServiceDefinition          governanceService,
                          String                               governanceActionTypeGUID,
                          int                                  governanceDomainIdentifier,
                          String                               supportedElementQualifiedName,
                          String                               solutionComponentGUID,
                          String                               solutionComponentName,
                          String                               solutionComponentDescription,
                          DeployedImplementationTypeDefinition worksWithTechnology,
                          String                               configuresComponentGUID,
                          boolean                              linkToMetadataServerSolutionComponent,
                          ContentCollectionDefinition          folder,
                          ContentPackDefinition                contentPackDefinition)
    {
        this.governanceRequestType                 = governanceRequestType;
        this.serviceRequestType                    = serviceRequestType;
        this.requestParameters                     = requestParameters;
        this.actionTargets                         = actionTargets;
        this.governanceEngine                      = governanceEngine;
        this.governanceService                     = governanceService;
        this.governanceActionTypeGUID              = governanceActionTypeGUID;
        this.governanceDomainIdentifier            = governanceDomainIdentifier;
        this.supportedElementQualifiedName         = supportedElementQualifiedName;
        this.solutionComponentGUID                 = solutionComponentGUID;
        this.solutionComponentName                 = solutionComponentName;
        this.solutionComponentDescription          = solutionComponentDescription;
        this.worksWithTechnology                   = worksWithTechnology;
        this.configuresComponentGUID               = configuresComponentGUID;
        this.linkToMetadataServerSolutionComponent = linkToMetadataServerSolutionComponent;
        this.folder                                = folder;
        this.contentPackDefinition                 = contentPackDefinition;
    }


    /**
     * Return the Request Type.
     *
     * @return string
     */
    public String getGovernanceRequestType()
    {
        return governanceRequestType;
    }


    /**
     * Return the service request type to map to.
     *
     * @return string
     */
    public String getServiceRequestType()
    {
        return serviceRequestType;
    }


    /**
     * Return the request parameters (if needed).
     *
     * @return map or null
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Return predefined action targets used by this service.  They are attached to the governance action type.
     *
     * @return list
     */
    public List<NewActionTarget> getActionTargets()
    {
        return actionTargets;
    }


    /**
     * Return the governance engine where this request type belongs to.
     *
     * @return governance engine definition enum
     */
    public GovernanceEngineDefinition getGovernanceEngine()
    {
        return governanceEngine;
    }


    /**
     * Return the governance service that this request type maps to,
     *
     * @return governance service definition enum
     */
    public GovernanceServiceDefinition getGovernanceService()
    {
        return governanceService;
    }


    /**
     * Return the unique identifier of the governance action type.
     *
     * @return string
     */
    public String getGovernanceActionTypeGUID()
    {
        return governanceActionTypeGUID;
    }


    /**
     * Return the governance domain identifier of the governance action type.
     *
     * @return integer
     */
    public int getGovernanceDomainIdentifier()
    {
        return governanceDomainIdentifier;
    }


    /**
     * Return the element that is supported by this request.
     *
     * @return qualified name string
     */
    public String getSupportedElementQualifiedName()
    {
        return supportedElementQualifiedName;
    }


    /**
     * Retrieves the GUID of the solution component for the governance action type.
     *
     * @return string
     */
    public String getSolutionComponentGUID()
    {
        return solutionComponentGUID;
    }


    /**
     * Retrieves the name of the solution component for the governance action type.
     *
     * @return string
     */
    public String getSolutionComponentName()
    {
        return solutionComponentName;
    }


    /**
     * Retrieves the description of the solution component for the governance action type.
     *
     * @return string
     */
    public String getSolutionComponentDescription()
    {
        return solutionComponentDescription;
    }


    /**
     * Retrieves the technology that this solution component works with.
     *
     * @return deployed implementation type
     */
    public DeployedImplementationTypeDefinition getWorksWithTechnology()
    {
        return worksWithTechnology;
    }


    /**
     * Retrieves the GUID of the component that this solution component configures.
     *
     * @return string
     */
    public String getConfiguresComponentGUID()
    {
        return configuresComponentGUID;
    }


    /**
     * Retrieves whether this component should link to the open metadata server solution component
     *
     * @return boolean
     */
    public boolean linkToMetadataServerSolutionComponent()
    {
        return linkToMetadataServerSolutionComponent;
    }


    /**
     * Retrieves the folder for the design library
     *
     * @return folder
     */
    public ContentCollectionDefinition getFolder()
    {
        return folder;
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
        return "RequestTypeDefinition{" + "name='" + governanceRequestType + '\'' + "}";
    }
}
