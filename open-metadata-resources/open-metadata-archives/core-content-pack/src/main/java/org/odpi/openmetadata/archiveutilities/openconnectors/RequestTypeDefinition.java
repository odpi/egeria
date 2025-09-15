/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetRequestParameter;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog.GovernanceActionTypeDefinition;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.FolderRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
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
                        ContentPackDefinition.CORE_CONTENT_PACK),

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
              ContentPackDefinition.CORE_CONTENT_PACK),

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
              ContentPackDefinition.CORE_CONTENT_PACK),

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
                ContentPackDefinition.CORE_CONTENT_PACK),

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
                          ContentPackDefinition.CORE_CONTENT_PACK),

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
                ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * "set-zone-membership"
     */
    ZONE_MEMBER("set-zone-membership",
                null,
                null,
                null,
                GovernanceEngineDefinition.ASSET_ONBOARDING_ENGINE,
                GovernanceServiceDefinition.ZONE_PUBLISHER,
                "05df4044-bc0a-40cd-b729-66aef891e7f0",
                DeployedImplementationType.ASSET.getQualifiedName(),
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
                         ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * evaluate-annotations
     */
    PRINT_SURVEY_REPORT("print-survey-report",
                         null,
                         null,
                         null,
                         GovernanceEngineDefinition.STEWARDSHIP_ENGINE,
                         GovernanceServiceDefinition.PRINT_SURVEY_REPORT,
                         "8b81d9c1-3320-43b1-90a7-57772855460b",
                        DeployedImplementationType.ASSET.getQualifiedName(),
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
                    ContentPackDefinition.CORE_CONTENT_PACK),

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
                     ContentPackDefinition.CORE_CONTENT_PACK),

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
                  ContentPackDefinition.CORE_CONTENT_PACK),

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
                            ContentPackDefinition.CORE_CONTENT_PACK),

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
                       ContentPackDefinition.CORE_CONTENT_PACK),

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
                                 ContentPackDefinition.CORE_CONTENT_PACK),

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
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * create-file-folder
     */
    CREATE_DATA_FOLDER("create-data-folder",
                       null,
                       getManageAssetRequestParameters(DataAssetTemplateDefinition.DATA_FOLDER_TEMPLATE.getTemplateGUID()),
                       null,
                       GovernanceEngineDefinition.FILE_GOVERNANCE_ENGINE,
                       GovernanceServiceDefinition.CREATE_ASSET,
                       "246a8a18-7b10-402a-95d8-acf7115046ff",
                       DeployedImplementationType.DATA_FOLDER.getQualifiedName(),
                       ContentPackDefinition.CORE_CONTENT_PACK),

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
                       ContentPackDefinition.CORE_CONTENT_PACK),

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
                       ContentPackDefinition.CORE_CONTENT_PACK),

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
                            ContentPackDefinition.CORE_CONTENT_PACK),

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
                        ContentPackDefinition.CORE_CONTENT_PACK),

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
                        ContentPackDefinition.CORE_CONTENT_PACK),

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
                                ContentPackDefinition.NANNY_CONTENT_PACK),

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
                                ContentPackDefinition.NANNY_CONTENT_PACK),

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
                                 ContentPackDefinition.NANNY_CONTENT_PACK),


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
