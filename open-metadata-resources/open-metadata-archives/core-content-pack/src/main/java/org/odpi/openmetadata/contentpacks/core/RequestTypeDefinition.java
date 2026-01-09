/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetRequestParameter;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.GovernanceActionTypeDefinition;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.FolderRequestParameter;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

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
                        DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                        "6ba4d520-a3fd-4eb7-9fc1-b1beddfd721e",
                        "Watch for new Files in Directory (folder)",
                        "Monitors the creation of open metadata elements that represent files and initiates appropriate governance actions.",
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
              DeployedImplementationType.FILE.getQualifiedName(),
              "a709e480-ad2e-479e-84fb-dbfb6b2a62dd",
              "Copy File",
              "Copy a file from one directory to another and maintain the open metadata elements describing the files and the lineage representing the data flow between them.",
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
              DeployedImplementationType.FILE.getQualifiedName(),
              "76534d17-d674-4412-ab08-8f0069b9b053",
              "Move File",
              "Move a file from one directory to another and maintain the open metadata elements describing the two locations of the file and the lineage representing the data flow between them.",
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
                DeployedImplementationType.FILE.getQualifiedName(),
                "039ffc59-e7b5-4a8f-9b3e-49a1b9d17ec2",
                "Delete File",
                "Deletes a file and deletes the associated ope metadata element.",
                ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * monitored-resource-notification
     */
    MONITORED_RESOURCE_WATCHDOG(GovernanceActionTypeDefinition.MONITORED_RESOURCE_WATCHDOG.getGovernanceRequestType(),
                                null,
                                null,
                                null,
                                GovernanceEngineDefinition.EGERIA_WATCHDOG_ENGINE,
                                GovernanceServiceDefinition.MONITORED_RESOURCE_NOTIFICATION,
                                GovernanceActionTypeDefinition.MONITORED_RESOURCE_WATCHDOG.getGovernanceActionTypeGUID(),
                                DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                                "6f499586-26db-4997-8df7-6af9e4aa01ab",
                                "Monitored Resource Notification",
                                "Sends a notification to all subscribers when a monitored metadata element changes.",
                                ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * periodic-refresh-notification
     */
    PERIODIC_REFRESH_WATCHDOG(GovernanceActionTypeDefinition.PERIODIC_REFRESH_WATCHDOG.getGovernanceRequestType(),
                              null,
                              null,
                              null,
                              GovernanceEngineDefinition.EGERIA_WATCHDOG_ENGINE,
                              GovernanceServiceDefinition.PERIODIC_REFRESH_NOTIFICATION,
                              GovernanceActionTypeDefinition.PERIODIC_REFRESH_WATCHDOG.getGovernanceActionTypeGUID(),
                              DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                              "9753963a-05ed-44fa-b411-0315a184dc7d",
                              "Periodic Refresh Watchdog",
                              "Issues a notification to subscribers at regular intervals. The interval is specified in millisecond using the refreshInterval request parameter.",
                              ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * one-time-notification
     */
    ONE_TIME_NOTIFICATION(GovernanceActionTypeDefinition.ONE_TIME_NOTIFICATION.getGovernanceRequestType(),
                          null,
                          null,
                          null,
                          GovernanceEngineDefinition.EGERIA_WATCHDOG_ENGINE,
                          GovernanceServiceDefinition.ONE_TIME_NOTIFICATION,
                          GovernanceActionTypeDefinition.ONE_TIME_NOTIFICATION.getGovernanceActionTypeGUID(),
                          DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                          "f21427ee-4167-46bb-84e9-f06b7668b912",
                          "One-time Notification",
                          "Issues a notification to subscribers only once.",
                          ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * one-time-notification
     */
    AWARD_KARMA_POINTS(GovernanceActionTypeDefinition.AWARD_KARMA_POINTS.getGovernanceRequestType(),
                       null,
                       null,
                       null,
                       GovernanceEngineDefinition.EGERIA_WATCHDOG_ENGINE,
                       GovernanceServiceDefinition.KARMA_POINT_AWARDS,
                       GovernanceActionTypeDefinition.AWARD_KARMA_POINTS.getGovernanceActionTypeGUID(),
                       null,
                       "5ca5b9fb-2a38-43b6-9206-1465574129ce",
                       "Award Karma Points",
                       "Monitors contributions to the open metadata ecosystem and awards karma points to the users responsible.",
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
                      DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                      "15060bce-3034-4cd6-9288-15287c5a354e",
                      "Provision Tabular Data Set",
                      "Copies data from one tabular data set to another.",
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
                        DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                        "259273bf-d228-4b44-af85-58d83c2dc6cb",
                        "Create Digital Subscription",
                        "Creates a subscription to a digital product.  The current contents of the product are provisioned to the user's specified destination.  If the subscription type supports ongoing notifications, then updates to the product contents will also be provisioned to the same destination.",
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
                        DeployedImplementationType.TABULAR_DATA_SET.getQualifiedName(),
                        "aa299ce9-1b05-4ac7-9c22-a0d44e15f30e",
                        "Cancel Subscription",
                        "Cancels (removes) a subscription to a digital product. No more updates to the product data will be sent.",
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
                DeployedImplementationType.DATA_ASSET.getQualifiedName(),
                "0e982f14-b5f9-4c0d-9bdf-647a2a637efa",
                "Seek Origin of Data",
                "Using the lineage relationships, trace back to the source of data for a requested asset.  If a single DigitalResourceOrigin classification is encountered then add it to the asset.  If null, or multiple DigitalResourceOrigin classifications are encountered, raise an error.",
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
                DeployedImplementationType.ASSET.getQualifiedName(),
                "a6db6967-0e78-4898-9602-0748932f3cfa",
                "Set Zone Membership",
                "Set up the zone membership on the requested element.",
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
                     DeployedImplementationType.DATA_ASSET.getQualifiedName(),
                     "edd1702a-c7b5-47ab-8cd2-3750849fe238",
                     "Set Retention Period",
                     "Set up the retention period for the supplied asset.",
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
                 DeployedImplementationType.ASSET.getQualifiedName(),
                 "3f76aeab-bb47-4b5b-b77e-803cc73e7e13",
                 "Verify Asset",
                 "Verify that an asset has the correct classifications set up - which includes zone membership, retention and origin.",
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
                         DeployedImplementationType.ASSET.getQualifiedName(),
                         "255cb632-9bbc-4e1c-9e41-898673305341",
                         "Evaluate Annotations",
                         "Check a survey report for 'Request for Action' annotations and raise a ToDo request to the designated steward to resolve it.",
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
                        DeployedImplementationType.ASSET.getQualifiedName(),
                        "f01af176-3086-4533-956a-8d11bcd314c8",
                        "Print Survey Report",
                        "Print out a survey report as a markdown document.",
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
                    null,
                    "8fa24644-43ce-4a3f-b31f-d1d52db323f7",
                    "Write to Audit Log",
                    "Write a specific message to the audit log.",
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
                    null,
                    "0f7e7dd9-eab2-4f4f-b5f9-51699b44ad69",
                    "Get Day of the Week",
                    "Determine the day of the week and output it as a guard.",
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
                null,
                "2cfe0c4a-7f63-4081-a8d5-ab6cc0159936",
                "Qualified Name Deduplication",
                "Link elements as duplicates if they have the same qualified name.",
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
                    DeployedImplementationType.CSV_FILE.getQualifiedName(),
                    "a4e69580-123d-43ce-a7d5-408fc0bc191e",
                    "Survey CSV File",
                    "Create a survey report about a requested CSV file, including extracting information about the file's characteristics and the columns inside..",
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
                     DeployedImplementationType.DATA_FILE.getQualifiedName(),
                     "67c8554a-8353-4a0a-b98e-596104cde47b",
                     "Survey Data File",
                     "Create a survey report about a particular file that describes the characteristics of the file.  It does not look inside the file.",
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
                  DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                  "96a97dc7-10b0-4c7a-8e4f-1f4f2b0b1bf7",
                  "Survey File System Directory",
                  "Create a survey report that summarizes the files in a directory (folder) on a file system.  Nested directories are ignored.",
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
                            DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                            "7d8e6ed1-c2b8-49af-9e49-8e5c5290c064",
                            "Survey File System Directory and its Files",
                            "Create a survey report that characterises the files in a requested directory (folder) on a file system along with a summary of the directory itself.  Nested directories are ignored.",
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
                       DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                       "b6c523a0-7318-4dd9-8b69-0e1e4aa08d1f",
                       "Survey Nested Files System Directories (Folders)",
                       "Starting from a particular directory, navigate through the hierarchy of nested directories in a file system and create a survey report that includes a summary of the files found in each directory.",
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
                                 DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                                 "9c38a6ca-4e0c-4d53-b517-6cdb78e46b35",
                                 "Survey Nested Files System Directories (Folders) and Files",
                                 "Starting from a particular directory, navigate through the hierarchy of nested directories in a file system and create a survey report that includes a description of each file encountered and a summary of the files found in each directory.",
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
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                        "892eb723-0bca-4f1e-b999-95604555f6f1",
                        "Survey an Apache Atlas Server",
                        "Connect to a requested Apache Atlas server and create a survey report that describes the types and numbers of associated metadata instances stored.",
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
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                        "4b8e9b5d-d095-43ac-a1d0-61155ea6f1b1",
                        "Survey Apache Kafka Server",
                        "Connects to an Apache Kafka server (broker) and create a survey report that lists the topics being managed.",
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
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                     "ca21df08-926e-4645-8c9f-dcd3d6db6cea",
                     "Survey Unity Catalog Server",
                     "Connects to a Unity Catalog Server and create a survey report that summarizes all the catalogs, schemas and data resources listed.",
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
                      UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getQualifiedName(),
                      "2107c858-e067-457c-a8e6-0a198ea27d21",
                      "Survey Unity Catalog Catalog",
                      "Connects to a Unity Catalog Server and creates a survey report that summarizes the schemas and data resources found in a requested catalog.",
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
                     UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getQualifiedName(),
                     "a7469ade-dbc4-4958-b5cf-83350e1c719f",
                     "Survey Unity Catalog Schema",
                     "Connects to a Unity Catalog Server and creates a survey report that summarizes the data resources found in a requested catalog/schema.",
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
                     UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getQualifiedName(),
                     "608c4836-c88a-4d4c-a774-2d5c37ac7cae",
                     "Survey Unity Catalog Volume",
                     "Connects to a Unity Catalog Server and creates a survey report that summarizes the data files found in a requested catalog/schema/volume.",
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
                 UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getQualifiedName(),
                 "4aba9c28-95fe-4289-9dde-67a3836ab1bf",
                 "Provision Unity Catalog Resource",
                 "Creates a description of a Unity Catalog resource (such as a volume, table or function) for a particular Unity Catalog Server that will be picked up and added to the Unity Catalog Server by the Unity Catalog integration connector on the next refresh.",
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
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                           "88346a5c-c376-431c-9833-e90f81113cb5",
                           "Survey a PostgreSQL Server",
                           "Create a survey report of the databases found in a requested PostgreSQL Server.",
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
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                             "0cc37077-e688-4572-b6d3-ed0a24375bc1",
                             "Survey PostgreSQL Database",
                             "Create a survey report of the schemas, tables and columns found in a requested PostgreSQL Database.",
                             ContentPackDefinition.POSTGRES_CONTENT_PACK),

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
                       DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                       "44ff46f1-50f8-443c-836e-78458123340f",
                       "Create File Folder in Open Metadata",
                       "Create a FileFolder asset in open metadata to represent a file system directory.  This is typically used to request a survey or the cataloguing of the files/nested directories within the directory.",
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
                       DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                       "f1729715-1e31-471c-93d1-fc9b406a6cec",
                       "Create Data Folder in Open Metadata",
                       "Create a DataFolder asset in open metadata to represent a file system directory.  This is typically used to request a survey or the cataloguing of the files/nested directories within the directory.",
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
                       DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                       "a822725e-e721-4f78-88a0-115b48d9b787",
                       "Delete File Folder from Open Metadata",
                       "Delete the requested FileFolder asset using the template placeholder properties used to create it.",
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
                       DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                       "05e01ab6-e897-4c36-97b8-1c647ed4b85c",
                       "Delete Data Folder from Open Metadata",
                       "Delete the requested DataFolder asset using the template placeholder properties used to create it.",
                       ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * catalog-file-folder
     */
    CATALOG_FILE_FOLDER("catalog-file-folder",
                            null,
                            null,
                            null,
                            GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                            GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                            "33fb5cd5-b84d-4c17-95b3-b1b2b99840e0",
                            DeployedImplementationType.FILE_FOLDER.getQualifiedName(),
                        "553a6cd6-282c-4358-9c5a-4ac4c2d10b99",
                        "Catalog a File Folder in Open Metadata",
                        "",
                            ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * catalog-data-folder
     */
    CATALOG_DATA_FOLDER("catalog-data-folder",
                        null,
                        null,
                        null,
                        GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                        GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                        "85424989-b821-49b6-8824-29b1b06536f5",
                        DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                        "7ff8405b-a72d-40dd-8b68-e5e96e2fcf47",
                        "Catalog a Data Folder in Open Metadata",
                        "",
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
                           DeployedImplementationType.SOFTWARE_SERVER.getQualifiedName(),
                           "d7c2b07f-f316-43d8-abda-ce53d9a7521f",
                           "Create a software server in Open Metadata",
                           "",
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
                           DeployedImplementationType.SOFTWARE_SERVER.getQualifiedName(),
                           "67bbc3e9-9237-4ef4-b15d-99127bf01704",
                           "Delete a software server from Open Metadata",
                           "",
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
                            DeployedImplementationType.SOFTWARE_SERVER.getQualifiedName(),
                            "60a8d234-bb90-4065-986b-362a4d56dd9c",
                            "Catalog a software server in Open Metadata",
                            "",
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
                        DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName(),
                       "65ef6295-6ddf-4c88-9764-eff5a84f402b",
                       "Create Apache Kafka Topic in Open Metadata",
                       "",
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
                        DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName(),
                               "7fc9f128-2b7f-43fb-8e95-171415240aff",
                               "Capture Open Lineage Events from the Open Lineage Proxy (via Kafka)",
                               "",
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
                       DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName(),
                       "6546488f-d8a2-415d-9d03-6ab72ba14c40",
                       "Delete an Apache Kafka Topic from Open Metadata",
                       "",
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
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                     "e9ca091e-f9e7-4e5b-bd45-6d4f0e2d55aa",
                     "Create an OSS Unity Catalog Server in Open Metadata",
                     "",
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
                     UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                     "835b4fe4-03cf-4f4d-b0b5-e25d7dfe3299",
                     "Delete an OSS Unity Catalog Server from Open Metadata",
                     "",
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
                        UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName(),
                        "8bba4467-0430-4a94-b8c5-b881fd3f9396",
                        "Create a Databricks Unity Catalog Server in Open Metadata",
                        "",
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
                        UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName(),
                        "c0ac2017-d7f4-49c2-9e9e-e995ced94177",
                        "Delete a Databricks Unity Catalog Server from Open Metadata",
                        "",
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
                      UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName(),
                      "5db21b73-bd6a-41ea-b4ff-235ede908bdf",
                      "Catalog a Unity Catalog Server in Open Metadata",
                      "",
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
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                        "cff46354-5b55-4b2f-bbb8-83db28507630",
                        "Create Apache Atlas Server in Open Metadata",
                        "",
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
                        AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                        "521c46f9-b1a8-44a5-a5a8-ddae9bc21230",
                        "Delete Apache Atlas Server from Open Metadata",
                        "",
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
                         AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName(),
                         "d5eba93e-06d7-4f08-a082-5aeaa6c70b2b",
                         "Create Apache Atlas server in Open Metadata",
                         "",
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
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                        "36fa75fa-dd6e-4120-a6f4-2c1abf4e565d",
                        "Create Apache Kafka Server in Open Metadata",
                        "",
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
                        KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                        "Delete Apache Kafka Server from Open Metadata",
                        "",
                        "",
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
                         KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getQualifiedName(),
                         "10a883e3-e6f7-4fab-9ed4-7edf6a1837ad",
                         "Catalog Apache Kafka Server in Open Metadata",
                         "",
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
                                EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName(),
                                "1bd75045-831c-4105-938f-19bca9bfc555",
                                "Create OMAG Server Platform in Open Metadata",
                                "",
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
                                EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName(),
                                "4ef6b15b-cd35-413b-b63f-c6b605edda92",
                                "Delete OMAG Server Platform from Open Metadata",
                                "",
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
                                 EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getQualifiedName(),
                                 "ce653660-36bd-4f44-a9c3-b94075f41813",
                                 "Catalog OMAG Server Platform in Open Metadata",
                                 "Listen for the create of Software Server Platforms representing running OMAG Server Platform instances.  It creates a connector to the running platform and extracts details of the servers and connectors.  Using that information, it creates descriptions of the servers in open metadata.",
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
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                           "0e5f926f-19aa-4a4b-865b-ed80d63d9752",
                           "Create PostgreSQL Server in Open Metadata",
                           "",
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
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                           "e84da60e-cf37-4506-8594-aac93826185a",
                           "Delete PostgreSQL Server from Open Metadata",
                           "",
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
                            PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName(),
                            "015d6b33-e6bc-4d77-822c-4a9d536ef542",
                            "Catalog PostgreSQL Server in Open Metadata",
                            "Create an open metadata element that represents a PostgreSQL server using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the server and the connection.  The databases within the server are not cataloged by this component.",
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
                       PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                       "1e13b511-e452-4fe4-a075-c0a5c021c051",
                       "Create PostgreSQL Database from Open Metadata",
                       "Create an open metadata element that represents a PostgreSQL database using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the database and the connection.  The schemas, tables and columns are not cataloged by this component.",
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
                       PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                       "c743bbc6-6e69-485c-8082-aee444ccc88e",
                       "Delete PostgreSQL Database from Open Metadata",
                       "Locate and delete the open metadata element that represents the PostgreSQL database described in the request.  The real database on the PostgreSQL server (if any) is unaffected by this component.",
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
                              PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName(),
                              "493d7286-7423-4621-8e64-3bb714e8a45a",
                              "Catalog PostgreSQL Database",
                              "Retrieve the definition of a specific database from the catalog of a PostgreSQL server and create an open metadata representation of the database's schemas, tables and columns.  These are linked to the open metadata element that represents the PostgreSQL database.  It was supplied as input and provided the connection information needed to connect to the PostgreSQL server.",
                              ContentPackDefinition.POSTGRES_CONTENT_PACK),

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
                       PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                           "c9779e50-9585-4e65-9cd7-4bc00757fe97",
                           "Create PostgreSQL Schema in Open Metadata",
                           "Create an open metadata element that represents a PostgreSQL schema using the properties supplied on the request.  These properties are used to populate a standard template that includes the asset for the schema and the connection.  The tables and columns are not cataloged by this component.",
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
                           PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                           "f2dd4107-88c1-4759-b62b-6ca68f5b8d8b",
                           "Delete PostgreSQL Schema from Open Metadata",
                           "Locate and delete the open metadata element that represents the PostgreSQL schema described in the request.  The real schema on the PostgreSQL server (if any) is unaffected by this component.",
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
                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName(),
                            "cd5c93c0-dc22-4be4-bd28-2a5b3ae11c36",
                            "Catalog PostgreSQL Schema",
                            "Retrieve the definition of a specific schema from the catalog of a PostgreSQL server, and configure an integration connector (using a catalog target relationship) to create the open metadata representation of the schema's tables and columns.  These new metadata elements are linked to the open metadata element that represents the PostgreSQL schema.  The integration connector is long-running and will maintain these metadata elements as long as it is configured to do so.  This means any changes in the schema's tables and columns in the PostgreSQL server will be automatically reflected in the open metadata elements.  Details of the PostgreSQL schema to catalog is supplied as input and provided the connection information needed to connect to the PostgreSQL server.",
                              ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * harvest-surveys
     */
    HARVEST_SURVEYS("harvest-surveys",
                    null,
                    null,
                    getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.HARVEST_SURVEYS.getGUID()),
                    GovernanceEngineDefinition.NANNY_GOVERNANCE_ENGINE,
                    GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                    "e3ea80ac-728f-4b33-8557-f92d17a6ad49",
                    null,
                    "09393325-2c65-4278-8d1b-3e77cf4aa311",
                    "Harvest Surveys",
                    "Retrieve all of the information from the survey reports created by the open survey framework and summarize them in a set of database tables for further analysis.",
                    ContentPackDefinition.NANNY_CONTENT_PACK),

    /**
     * harvest-open-metadata
     */
    HARVEST_OPEN_METADATA("harvest-open-metadata",
                          null,
                          null,
                          getCatalogTargetAssetActionTargets(IntegrationConnectorDefinition.HARVEST_OPEN_METADATA.getGUID()),
                          GovernanceEngineDefinition.NANNY_GOVERNANCE_ENGINE,
                          GovernanceServiceDefinition.CATALOG_TARGET_ASSET,
                          "2f3b500f-b918-400d-bacb-dcff50772d9b",
                          null,
                          "710c48ea-6bda-47dd-bb74-6d47a88d767b",
                          "Harvest Open Metadata",
                          "Create a collection of database tables full of insight gleaned from the open metadata ecosystem.  This information may be used to analyse the health of the open metadata ecosystem, or to distribute key information to other systems.",
                          ContentPackDefinition.NANNY_CONTENT_PACK),
    ;

    private final String                      governanceRequestType;
    private final String                      serviceRequestType;
    private final Map<String, String>         requestParameters;
    private final List<NewActionTarget>       actionTargets;
    private final GovernanceEngineDefinition  governanceEngine;
    private final GovernanceServiceDefinition governanceService;
    private final String                      governanceActionTypeGUID;
    private final String                      supportedElementQualifiedName;
    private final String                      solutionComponentGUID;
    private final String                      solutionComponentName;
    private final String                      solutionComponentDescription;
    private final ContentPackDefinition       contentPackDefinition;

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
     * @param solutionComponentGUID unique identifier of the solution component for the governance action type
     * @param solutionComponentName display name of the solution component for the governance action type
     * @param solutionComponentDescription description of the solution component for the governance action type
     * @param contentPackDefinition which content pack?
     */
    RequestTypeDefinition(String                      governanceRequestType,
                          String                      serviceRequestType,
                          Map<String, String>         requestParameters,
                          List<NewActionTarget>       actionTargets,
                          GovernanceEngineDefinition  governanceEngine,
                          GovernanceServiceDefinition governanceService,
                          String                      governanceActionTypeGUID,
                          String                      supportedElementQualifiedName,
                          String                      solutionComponentGUID,
                          String                      solutionComponentName,
                          String                      solutionComponentDescription,
                          ContentPackDefinition       contentPackDefinition)
    {
        this.governanceRequestType         = governanceRequestType;
        this.serviceRequestType            = serviceRequestType;
        this.requestParameters             = requestParameters;
        this.actionTargets                 = actionTargets;
        this.governanceEngine              = governanceEngine;
        this.governanceService             = governanceService;
        this.governanceActionTypeGUID      = governanceActionTypeGUID;
        this.supportedElementQualifiedName = supportedElementQualifiedName;
        this.solutionComponentGUID         = solutionComponentGUID;
        this.solutionComponentName         = solutionComponentName;
        this.solutionComponentDescription  = solutionComponentDescription;
        this.contentPackDefinition         = contentPackDefinition;
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
