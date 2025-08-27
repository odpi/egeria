/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria. These are encoded in the
 * CoreContentPack.omarchive and are available in the open metadata repository as valid values.
 */
public enum DeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * Root technology type.
     */
    TECHNOLOGY("Root Technology Type",
          null,
          OpenMetadataType.REFERENCEABLE.typeName,
          null,
          "Root technology type.",
          "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A description of a digital resource.
     */
    ASSET("Asset",
          DeployedImplementationType.TECHNOLOGY,
          OpenMetadataType.ASSET.typeName,
          null,
          "A description of a digital resource.",
          "https://egeria-project.org/concepts/asset/"),

    /**
     * A well-defined set of processing steps and decisions that drive a particular aspect of the organization's business.
     */
    PROCESS("Process",
            DeployedImplementationType.ASSET,
            OpenMetadataType.PROCESS.typeName,
            null,
            "A well-defined set of processing steps and decisions that drive a particular aspect of the organization's business.",
            "https://egeria-project.org/concepts/process/"),

    /**
     * A collection of data, either at rest or in motion.
     */
    DATA_ASSET("Data Asset",
               DeployedImplementationType.ASSET,
               OpenMetadataType.DATA_ASSET.typeName,
               null,
               "A collection of data, either at rest or in motion.",
               "https://egeria-project.org/concepts/asset/"),

    /**
     * A collection of data, either at rest or in motion.
     */
    DATA_SET("Data Set",
               DeployedImplementationType.DATA_ASSET,
               OpenMetadataType.DATA_ASSET.typeName,
               null,
               "A logical collection of data, either at rest or in motion.",
               "https://egeria-project.org/concepts/asset/"),

    /**
     * A file containing externally accessible data - other fields provide information on the internal format.
     */
    FILE("File",
         DeployedImplementationType.DATA_ASSET,
         OpenMetadataType.DATA_FILE.typeName,
         null,
         OpenMetadataType.DATA_FILE.description,
         OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * A file containing program logic.
     */
    PROGRAM_FILE("Program File",
                 DeployedImplementationType.FILE,
                 OpenMetadataType.DATA_FILE.typeName,
                 null,
                 "A file containing program logic.",
                 OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),

    /**
     * A file containing program logic.
     */
    DATA_FILE("Data File",
                 DeployedImplementationType.FILE,
                 OpenMetadataType.DATA_FILE.typeName,
                 null,
                 "A file containing data.  This may be structured data, text or some form or media file.",
                 OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * CSV Data File.
     */
    CSV_FILE("CSV Data File",
              DeployedImplementationType.DATA_FILE,
              OpenMetadataType.CSV_FILE.typeName,
             null,
             OpenMetadataType.CSV_FILE.description,
             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * Avro Data File
     */
    AVRO_FILE("Avro Data File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.AVRO_FILE.typeName,
             null,
              OpenMetadataType.AVRO_FILE.description,
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * JSON Data File
     */
    JSON_FILE("JSON Data File",
              DeployedImplementationType.DATA_FILE,
              OpenMetadataType.JSON_FILE.typeName,
              null,
              OpenMetadataType.JSON_FILE.description,
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * Parquet Data File
     */
    PARQUET_FILE("Parquet Data File",
                  DeployedImplementationType.DATA_FILE,
                  OpenMetadataType.PARQUET_FILE.typeName,
              null,
              OpenMetadataType.PARQUET_FILE.description,
                 OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * Spreadsheet Data File
     */
    SPREADSHEET_FILE("Spreadsheet Data File",
                     DeployedImplementationType.DATA_FILE,
                     OpenMetadataType.SPREADSHEET_FILE.typeName,
                     null,
                     OpenMetadataType.SPREADSHEET_FILE.description,
                     OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * XML Data File
     */
    XML_FILE("XML Data File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.XML_FILE.typeName,
             null,
             OpenMetadataType.XML_FILE.description,
             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * Document File
     */
    DOCUMENT("Document File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.DOCUMENT.typeName,
             null,
             OpenMetadataType.DOCUMENT.description,
             OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * Audio Data File
     */
    AUDIO_DATA_FILE("Audio Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.AUDIO_FILE.typeName,
                    null,
                    OpenMetadataType.AUDIO_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * Video Data File
     */
    VIDEO_DATA_FILE("Video Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.VIDEO_FILE.typeName,
                    null,
                    OpenMetadataType.VIDEO_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * 3D Image Data File
     */
    THREE_D_IMAGE_DATA_FILE("3D Image Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.THREE_D_IMAGE_FILE.typeName,
                    null,
                    OpenMetadataType.THREE_D_IMAGE_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),

    /**
     * Raster Data File
     */
    RASTER_DATA_FILE("Raster Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.RASTER_FILE.typeName,
                    null,
                    OpenMetadataType.RASTER_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),

    /**
     * Vector Data File
     */
    VECTOR_DATA_FILE("Vector Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.VECTOR_FILE.typeName,
                    null,
                    OpenMetadataType.VECTOR_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * Source Code File
     */
    SOURCE_CODE_FILE("Source Code File",
                 DeployedImplementationType.PROGRAM_FILE,
                 OpenMetadataType.SOURCE_CODE_FILE.typeName,
                 null,
                 OpenMetadataType.SOURCE_CODE_FILE.description,
                 OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),

    /**
     * Build Instruction File
     */
    BUILD_FILE("Build Instruction File",
                     DeployedImplementationType.PROGRAM_FILE,
                     OpenMetadataType.BUILD_INSTRUCTION_FILE.typeName,
                     null,
                     OpenMetadataType.BUILD_INSTRUCTION_FILE.description,
                     OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),

    /**
     * Executable File
     */
    EXECUTABLE_FILE("Executable File",
               DeployedImplementationType.PROGRAM_FILE,
               OpenMetadataType.EXECUTABLE_FILE.typeName,
               null,
               OpenMetadataType.EXECUTABLE_FILE.description,
               OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * Script File
     */
    SCRIPT_FILE("Script File",
                DeployedImplementationType.PROGRAM_FILE,
                OpenMetadataType.SCRIPT_FILE.typeName,
                null,
                OpenMetadataType.SCRIPT_FILE.description,
                OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * Properties File
     */
    PROPERTIES_FILE("Properties File",
                DeployedImplementationType.PROGRAM_FILE,
                OpenMetadataType.PROPERTIES_FILE.typeName,
                null,
                OpenMetadataType.PROPERTIES_FILE.description,
                OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * YAML File
     */
   YAML_FILE("YAML File",
                    DeployedImplementationType.PROPERTIES_FILE,
                    OpenMetadataType.YAML_FILE.typeName,
                    null,
                    OpenMetadataType.YAML_FILE.description,
                    OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * Log File
     */
    LOG_FILE("Log File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.LOG_FILE.typeName,
             null,
             OpenMetadataType.LOG_FILE.description,
             OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS),


    /**
     * Archive File
     */
    ARCHIVE_FILE("Archive File",
                 DeployedImplementationType.DATA_FILE,
                 OpenMetadataType.ARCHIVE_FILE.typeName,
                 null,
                 OpenMetadataType.ARCHIVE_FILE.description,
                 OpenMetadataWikiPages.MODEL_0226_ARCHIVE_FILES),


    /**
     * Keystore File
     */
    KEYSTORE_FILE("Keystore File",
                 DeployedImplementationType.DATA_FILE,
                 OpenMetadataType.KEYSTORE_FILE.typeName,
                 null,
                  OpenMetadataType.KEYSTORE_FILE.description,
                 OpenMetadataWikiPages.MODEL_0226_ARCHIVE_FILES),


    /**
     * A directory (folder) that holds files that are potential data sources.
     */
    FILE_FOLDER("File System Directory",
                DeployedImplementationType.DATA_ASSET,
                OpenMetadataType.FILE_FOLDER.typeName,
                null,
                "A directory (folder) that holds files that are potential data sources in a file system.",
                OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * A directory (folder) that holds files representing a single data source.
     */
    DATA_FOLDER("Data Folder",
                DeployedImplementationType.FILE_FOLDER,
                OpenMetadataType.DATA_FOLDER.typeName,
                null,
                "A directory (folder) that holds files representing a single data source.",
                OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * A collection of logically related files representing a single data source.
     */
    DATA_FILE_COLLECTION("Data File Collection",
                DeployedImplementationType.DATA_SET,
                OpenMetadataType.DATA_FILE_COLLECTION.typeName,
                null,
                "A collection of logically related files representing a single data source.",
                OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * A database hosted on a relational database server capable of being called through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE("JDBC Relational Database",
                             DeployedImplementationType.DATA_ASSET,
                             OpenMetadataType.RELATIONAL_DATABASE.typeName,
                             null,
                             "A database hosted on a relational database server capable of being called through a JDBC Driver.",
                             "https://en.wikipedia.org/wiki/Java_Database_Connectivity"),


    /**
     * A database schema hosted on a relational database server capable of being called through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE_SCHEMA("JDBC Relational Database Schema",
                             DeployedImplementationType.DATA_ASSET,
                             OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                             null,
                             "A database schema hosted on a relational database server capable of being called through a JDBC Driver.",
                             "https://en.wikipedia.org/wiki/Java_Database_Connectivity"),

    /**
     * A database table hosted on a relational database server capable of being called through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE_TABLE("JDBC Relational Database Table",
                                    DeployedImplementationType.DATA_ASSET,
                                    OpenMetadataType.TABLE_DATA_SET.typeName,
                                    null,
                                    "A database table hosted on a relational database server capable of being called through a JDBC Driver.",
                                    "https://en.wikipedia.org/wiki/Java_Database_Connectivity"),


    /**
     * A computer (hardware) with operating system for running software.
     */
    BARE_METAL_COMPUTER("Bare Metal Computer",
                        DeployedImplementationType.TECHNOLOGY,
                        OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                        null,
                        "A computer (hardware) with operating system for running software.",
                        OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A portable computer with screen, keyboard and battery power.
     */
    LAPTOP_COMPUTER("Laptop Computer",
                        DeployedImplementationType.BARE_METAL_COMPUTER,
                        OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                        null,
                        "A portable computer with screen, keyboard and battery power.",
                        OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A professional laptop supplied by Apple that runs the macOS operating system.
     */
    MACBOOK_PRO("Apple MacBook Pro",
                DeployedImplementationType.LAPTOP_COMPUTER,
                OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                null,
                "A professional laptop supplied by Apple that runs the macOS operating system.",
                OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A small hardware server enclosed in a protective case that can sit under a desk.
     */
    SMALL_FORM_FACTOR_COMPUTER("Small Form Factor (SFF) Computer",
                               DeployedImplementationType.BARE_METAL_COMPUTER,
                               OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                               null,
                               "A small hardware server enclosed in a protective case that can sit under a desk.",
                               OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A callable software server.
     */
    SOFTWARE_SERVER("Software Server",
                    DeployedImplementationType.ASSET,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "A callable software server.",
                    OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS),


    /**
     * A callable software capability supporting specific types of assets.
     */
    SOFTWARE_CAPABILITY("Software Capability",
                        DeployedImplementationType.TECHNOLOGY,
                        OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                        null,
                        "A callable software capability supporting specific types of assets.",
                        OpenMetadataWikiPages.MODEL_0042_SOFTWARE_CAPABILITIES),

    /**
     * A data catalog for data observability.
     */
    MARQUEZ_SERVER("Marquez Server",
                        DeployedImplementationType.SOFTWARE_SERVER,
                        OpenMetadataType.SOFTWARE_SERVER.typeName,
                        OpenMetadataType.METADATA_SERVER_CLASSIFICATION.typeName,
                        "A data catalog for data observability.",
                        "https://marquezproject.ai/"),

    /**
     * A system that manages hierarchically organized files on persistent storage.
     */
    FILE_SYSTEM("File System",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.DATA_MANAGER.typeName,
                OpenMetadataType.FILE_SYSTEM_CLASSIFICATION.typeName,
                "A system that manages hierarchically organized files on persistent storage.",
                OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS),

    /**
     * A UNIX capability that manages hierarchically organized files on persistent storage.
     */
    UNIX_FILE_SYSTEM("UNIX File System",
                     DeployedImplementationType.SOFTWARE_CAPABILITY,
                     OpenMetadataType.DATA_MANAGER.typeName,
                     OpenMetadataType.FILE_SYSTEM_CLASSIFICATION.typeName,
                     "A Unix capability that manages hierarchically organized files on persistent storage.",
                     OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS),

    /**
     * Software supporting a business function.
     */
    APPLICATION("Business Application",
                  DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.APPLICATION.typeName,
                  null,
                  "Software supporting a business function.",
                  OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * Software supporting a business function.
     */
    USER_AUTHENTICATION_MANAGER("User Authentication Manager",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.USER_AUTHENTICATION_MANAGER.typeName,
                null,
                "Function that validates the identity of a user via password or other form of identification",
                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A catalog of metadata that describes assets such as deployed systems, data sources and processes.
     */
    ASSET_CATALOG("Asset Metadata Catalog",
                  DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.INVENTORY_CATALOG.typeName,
                  OpenMetadataType.ASSET_MANAGER.typeName,
                  "A catalog of metadata that describes assets such as deployed systems, data sources and processes.",
                  OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A software capability that provides callable APIs.
     */
    API_MANAGER("API Manager",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.API_MANAGER.typeName,
                null,
                "A software capability that provides callable APIs.",
                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A callable network address.
     */
    ENDPOINT("Endpoint",
             DeployedImplementationType.TECHNOLOGY,
             OpenMetadataType.ENDPOINT.typeName,
             null,
             "A callable network address.",
             OpenMetadataWikiPages.MODEL_0026_ENDPOINTS),

    /**
     * A callable network address supporting the REST protocol.
     */
    REST_API_ENDPOINT("REST API Endpoint",
                     DeployedImplementationType.ENDPOINT,
                     OpenMetadataType.ENDPOINT.typeName,
                     null,
                     "A callable network address supporting the REST protocol.",
                     OpenMetadataWikiPages.MODEL_0026_ENDPOINTS),


    /**
     * A callable network address supporting the JDBC protocol.
     */
    JDBC_ENDPOINT("JDBC Endpoint",
                      DeployedImplementationType.ENDPOINT,
                      OpenMetadataType.ENDPOINT.typeName,
                      null,
                      "A callable network address supporting the JDBC protocol.",
                      OpenMetadataWikiPages.MODEL_0026_ENDPOINTS),


    /**
     * A software capability that provides callable APIs supporting the REST protocol.
     */
    REST_API_MANAGER("REST API Manager",
                     DeployedImplementationType.API_MANAGER,
                     OpenMetadataType.API_MANAGER.typeName,
                     null,
                     "A software capability that provides callable APIs supporting the REST protocol.",
                     OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A system that manages the asynchronous exchange of messages (events) from once to potentially many recipients.  Typically, this exchange of events is organized into groups called topics.
     */
    EVENT_BROKER("Event Broker",
                 DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.EVENT_BROKER.typeName,
                 null,
                  "A system that manages the asynchronous exchange of messages (events) from once to potentially many recipients.  Typically, this exchange of events is organized into groups called topics.",
                 OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),


    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    RELATIONAL_DATABASE_MANAGER("Relational database manager (RDBMS)",
                                DeployedImplementationType.SOFTWARE_CAPABILITY,
                                OpenMetadataType.DATABASE_MANAGER.typeName,
                                null,
                                "A capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A deployable software component.
     */
    SOFTWARE_COMPONENT("Software Component",
                       DeployedImplementationType.PROCESS,
                       OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                       null,
                       "A deployable software component.",
                       OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS),

    /**
     * A deployable software component.
     */
    AIRFLOW_DAG("Airflow DAG",
                       DeployedImplementationType.PROCESS,
                       OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                       null,
                       "A python component that implements a micro-workflow that runs in Apache Airflow.  DAG stands for Directed Acyclic Graph which describes the structure of its implementation.",
                       OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS),

    /**
     * A pluggable software component that conforms to the Open Connector Framework (OCF).
     */
    OCF_CONNECTOR("Open Connector Framework (OCF) Connector",
                  DeployedImplementationType.SOFTWARE_COMPONENT,
                  OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                  null,
                  "A pluggable software component that conforms to the Open Connector Framework (OCF).",
                  "https://egeria-project.org/concepts/connector/"),

    /**
     * OMRS Repository Connector - Maps open metadata calls to a metadata repository.
     */
    REPOSITORY_CONNECTOR("OMRS Repository Connector",
                         DeployedImplementationType.OCF_CONNECTOR,
                         OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                         null,
                         "Maps open metadata repository calls defined by the Open Metadata Repository Services (OMRS) to a metadata repository API and event notifications.",
                         "https://egeria-project.org/concepts/repository-connector/"),


    /**
     * Manages the execution of automated governance activity requested via engine actions.
     */
    GOVERNANCE_ENGINE("Governance Engine",
                      DeployedImplementationType.SOFTWARE_CAPABILITY,
                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                      null,
                      "Manages the execution of automated governance activity requested via engine actions.",
                      "https://egeria-project.org/concepts/governance-engine/"),

    /**
     * Provides the description of a component that implements an automated governance activity.
     */
    GOVERNANCE_SERVICE("Governance Service",
                      DeployedImplementationType.OCF_CONNECTOR,
                      OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                       null,
                      "Provides the description of a component that implements an automated governance activity.",
                      "https://egeria-project.org/concepts/governance-service/"),

    /**
     * Governance Action Service - A connector that coordinates governance of digital resources and metadata.
     */
    GOVERNANCE_ACTION_SERVICE_CONNECTOR("Governance Action Service",
                                        DeployedImplementationType.GOVERNANCE_SERVICE,
                                        OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName,
                                        null,
                                        "A connector that coordinates governance of digital resources and metadata.",
                                        "https://egeria-project.org/concepts/governance-action-service/"),

    /**
     * Governance Action Engine - A governance engine that runs governance action services.
     */
    GOVERNANCE_ACTION_ENGINE("Governance Action Engine",
                             DeployedImplementationType.GOVERNANCE_ENGINE,
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             null,
                             "A governance engine that runs governance action services.",
                             "https://egeria-project.org/concepts/governance-action-engine/"),


    /**
     * Survey Action Service - A connector that coordinates asset surveys.
     */
    SURVEY_ACTION_SERVICE_CONNECTOR("Survey Action Service",
                                    DeployedImplementationType.GOVERNANCE_SERVICE,
                                    OpenMetadataType.SURVEY_ACTION_SERVICE.typeName,
                                    null,
                                    "A connector that coordinates asset surveys.",
                                    "https://egeria-project.org/concepts/survey-action-service/"),

    /**
     * Watchdog Action Service - A connector that coordinates notifications based on situations/events.
     */
    WATCHDOG_ACTION_SERVICE_CONNECTOR("Watchdog Action Service",
                                    DeployedImplementationType.GOVERNANCE_SERVICE,
                                    OpenMetadataType.WATCHDOG_ACTION_SERVICE.typeName,
                                    null,
                                    "A connector that coordinates notifications based on situations/events.",
                                    "https://egeria-project.org/concepts/watchdog-action-service/"),

    /**
     * Survey Action Engine - A governance engine that runs survey action services.
     */
    SURVEY_ACTION_ENGINE("Survey Action Engine",
                         DeployedImplementationType.GOVERNANCE_ENGINE,
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         null,
                         "A governance engine that runs survey action services.",
                         "https://egeria-project.org/concepts/survey-action-engine/"),

    /**
     * Watchdog Action Engine - A governance engine that runs survey action services.
     */
    WATCHDOG_ACTION_ENGINE("Watchdog Action Engine",
                         DeployedImplementationType.GOVERNANCE_ENGINE,
                         OpenMetadataType.WATCHDOG_ACTION_ENGINE.typeName,
                         null,
                         "A governance engine that runs watchdog action services.",
                         "https://egeria-project.org/concepts/watchdog-action-engine/"),

    /**
     * Repository Governance Service - A connector that dynamically governs the activity of the open metadata repositories.
     */
    REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR("Repository Governance Service Connector",
                                            DeployedImplementationType.GOVERNANCE_SERVICE,
                                            OpenMetadataType.REPOSITORY_GOVERNANCE_SERVICE.typeName,
                                            null,
                                            "A connector that dynamically governs the activity of the open metadata repositories.",
                                            "https://egeria-project.org/concepts/repository-governance-service"),

    /**
     * A governance engine that runs repository governance services.
     */
    REPOSITORY_GOVERNANCE_ENGINE("Repository Governance Engine",
                                 DeployedImplementationType.GOVERNANCE_ENGINE,
                                 OpenMetadataType.REPOSITORY_GOVERNANCE_ENGINE.typeName,
                                 null,
                                 "A governance engine that runs repository governance services.",
                                 "https://egeria-project.org/concepts/repository-governance-engine/"),


    /**
     * Governance Action Service - A connector that coordinates governance of digital resources and metadata.
     */
    GOVERNANCE_ACTION_PROCESS("Governance Action Process",
                              DeployedImplementationType.PROCESS,
                              OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                              null,
                              "A modelled workflow process to perform a governance action.",
                                        "https://egeria-project.org/concepts/governance-action-process/"),

    /**
     * Connector that manages metadata exchange with a third party technology.
     */
    INTEGRATION_CONNECTOR("Integration Connector",
                          DeployedImplementationType.OCF_CONNECTOR,
                          OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                          null,
                                    "Connector that manages metadata exchange with a third party technology.",
                                    "https://egeria-project.org/services/omvs/analytics-integrator/overview"),

    /**
     * Analytics Integration Connector - Connector that manages metadata exchange with a third-party analytics technology.
     */
    ANALYTICS_INTEGRATION_CONNECTOR("Analytics Integration Connector",
                                    DeployedImplementationType.INTEGRATION_CONNECTOR,
                                    OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                    null,
                                    "Connector that manages metadata exchange with a third party analytics technology.",
                                    "https://egeria-project.org/services/omvs/analytics-integrator/overview"),

    /**
     * API Integration Connector - Connector that manages metadata exchange with a third-party API management technology.
     */
    API_INTEGRATION_CONNECTOR("API Integration Connector",
                              DeployedImplementationType.INTEGRATION_CONNECTOR,
                              OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                              null,
                              "Connector that manages metadata exchange with a third party API management technology.",
                              "https://egeria-project.org/services/omvs/api-integrator/overview"),

    /**
     * Catalog Integration Connector - Connector that manages metadata exchange with a third-party metadata catalog technology.
     */
    CATALOG_INTEGRATION_CONNECTOR("Catalog Integration Connector",
                                  DeployedImplementationType.INTEGRATION_CONNECTOR,
                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                  null,
                                  "Connector that manages metadata exchange with a third party metadata catalog technology.",
                                  "https://egeria-project.org/services/omvs/catalog-integrator/overview"),

    /**
     * Database Integration Connector - Connector that manages metadata exchange with a third-party database technology.
     */
    DATABASE_INTEGRATION_CONNECTOR("Database Integration Connector",
                                   DeployedImplementationType.INTEGRATION_CONNECTOR,
                                   OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                   null,
                                   "Connector that manages metadata exchange with a third party database technology.",
                                   "https://egeria-project.org/services/omvs/database-integrator/overview"),

    /**
     * Display Integration Connector - Connector that manages metadata exchange with a third-party display (user interaction) technology.
     */
    DISPLAY_INTEGRATION_CONNECTOR("Display Integration Connector",
                                  DeployedImplementationType.INTEGRATION_CONNECTOR,
                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                  null,
                                  "Connector that manages metadata exchange with a third party display (user interaction) technology.",
                                  "https://egeria-project.org/services/omvs/display-integrator/overview"),

    /**
     * Files Integration Connector - Connector that manages metadata exchange with a third-party filesystem technology.
     */
    FILES_INTEGRATION_CONNECTOR("Files Integration Connector",
                                DeployedImplementationType.INTEGRATION_CONNECTOR,
                                OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                null,
                                "Connector that manages metadata exchange with a third party filesystem technology.",
                                "https://egeria-project.org/services/omvs/files-integrator/overview"),

    /**
     * Infrastructure Integration Connector - Connector that manages metadata exchange with a third-party infrastructure catalog (CMDB) technology.
     */
    INFRASTRUCTURE_INTEGRATION_CONNECTOR("Infrastructure Integration Connector",
                                         DeployedImplementationType.INTEGRATION_CONNECTOR,
                                         OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                         null,
                                         "Connector that manages metadata exchange with a third party infrastructure catalog (CMDB) technology.",
                                         "https://egeria-project.org/services/omvs/infrastructure-integrator/overview"),

    /**
     * Lineage Integration Connector - Connector that manages metadata exchange with a third-party lineage capture technology.
     */
    LINEAGE_INTEGRATION_CONNECTOR("Lineage Integration Connector",
                                  DeployedImplementationType.INTEGRATION_CONNECTOR,
                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                  null,
                                  "Connector that manages metadata exchange with a third party lineage capture technology.",
                                  "https://egeria-project.org/services/omvs/lineage-integrator/overview"),

    /**
     * Organization Integration Connector - Connector that manages metadata exchange with a third-party application containing data about people and organizations.
     */
    ORGANIZATION_INTEGRATION_CONNECTOR("Organization Integration Connector",
                                       DeployedImplementationType.INTEGRATION_CONNECTOR,
                                       OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                       null,
                                       "Connector that manages metadata exchange with a third party application containing data about people and organizations.",
                                       "https://egeria-project.org/services/omvs/organization-integrator/overview"),

    /**
     * Search Integration Connector - Connector that manages metadata exchange with a third-party search technology.
     */
    SEARCH_INTEGRATION_CONNECTOR("Search Integration Connector",
                                 DeployedImplementationType.INTEGRATION_CONNECTOR,
                                 OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                 null,
                                 "Connector that manages metadata exchange with a third party search technology.",
                                 "https://egeria-project.org/services/omvs/search-integrator/overview"),

    /**
     * Security Integration Connector - Connector that manages metadata exchange with a third-party security management technology.
     */
    SECURITY_INTEGRATION_CONNECTOR("Security Integration Connector",
                                   DeployedImplementationType.INTEGRATION_CONNECTOR,
                                   OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                   null,
                                   "Connector that manages metadata exchange with a third party security management technology.",
                                   "https://egeria-project.org/services/omvs/security-integrator/overview"),

    /**
     * Topic Integration Connector - Connector that manages metadata exchange with a third-party event management technology.
     */
    TOPIC_INTEGRATION_CONNECTOR("Topic Integration Connector",
                                DeployedImplementationType.INTEGRATION_CONNECTOR,
                                OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                null,
                                "Connector that manages metadata exchange with a third party event management technology.",
                                "https://egeria-project.org/services/omvs/topic-integrator/overview"),

    /**
     * Platform Metadata Security Connector - Connector that manages authorization requests to the OMAG Server Platform.
     */
    PLATFORM_SECURITY_CONNECTOR("Platform Metadata Security Connector",
                                DeployedImplementationType.OCF_CONNECTOR,
                                OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                null,
                                "Connector that manages authorization requests to the OMAG Server Platform.",
                                "https://egeria-project.org/concepts/platform-metadata-security-connector/"),

    /**
     * Server Metadata Security Connector - Connector that manages authorization requests to the OMAG Server.
     */
    SERVER_SECURITY_CONNECTOR("Server Metadata Security Connector",
                              DeployedImplementationType.OCF_CONNECTOR,
                              OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                              null,
                              "Connector that manages authorization requests to the OMAG Server.",
                              "https://egeria-project.org/concepts/server-metadata-security-connector/"),

    /**
     * Open Metadata Archive Store Connector - Reads and writes open metadata types and instances to an open metadata archive.
     */
    ARCHIVE_STORE_CONNECTOR("Open Metadata Archive Store Connector",
                            DeployedImplementationType.OCF_CONNECTOR,
                            OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                            null,
                            "Reads and writes open metadata types and instances to an open metadata archive.",
                            "https://egeria-project.org/concepts/open-metadata-archive-store-connector/"),

    /**
     * Cohort Registry Store - Stores information about the repositories registered in the open metadata repository cohort.
     */
    COHORT_REGISTRY_STORE("Cohort Registry Store",
                          DeployedImplementationType.OCF_CONNECTOR,
                          OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                          null,
                          "Stores information about the repositories registered in the open metadata repository cohort.",
                          "https://egeria-project.org/concepts/cohort-registry-store-connector/"),

    /**
     * Audit Log Destination - Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.
     */
    AUDIT_LOG_DESTINATION("Audit Log Destination",
                          DeployedImplementationType.OCF_CONNECTOR,
                          OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                          null,
                          "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
                          "https://egeria-project.org/concepts/audit-log/"),

    /**
     * Provides the list of integration connectors that should run in an Integration Daemon.  The Integration Daemon is configured with the qualified names of the integration group(s) that provide its connector list.
     */
    INTEGRATION_GROUP("Dynamic Integration Group",
                      DeployedImplementationType.SOFTWARE_CAPABILITY,
                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                      null,
                      "Provides the list of integration connectors that should run in an Integration Daemon.  The Integration Daemon is configured with the qualified names of the integration group(s) that provide its connector list.",
                      "https://egeria-project.org/concepts/integration-group/"),


    /**
     * An event topic supporting high speed, reliable event exchange.
     */
    APACHE_KAFKA_TOPIC("Apache Kafka Topic",
                       DeployedImplementationType.DATA_ASSET,
                       OpenMetadataType.KAFKA_TOPIC.typeName,
                       null,
                       "An event topic supporting high speed, reliable event exchange.",
                       "https://kafka.apache.org/"),
    ;


    /**
     * Return the matching ENUM to make use of the full definition for the deployed implementation type.
     *
     * @param deployedImplementationType value to match on
     * @return DeployedImplementationType definition
     */
    public static DeployedImplementationType getDefinitionFromDeployedImplementationType(String deployedImplementationType)
    {
        if (deployedImplementationType != null)
        {
            for (DeployedImplementationType definition : DeployedImplementationType.values())
            {
                if (definition.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    return definition;
                }
            }
        }

        return null;
    }


    private final String deployedImplementationType;
    private final DeployedImplementationType isATypeOf;
    private final String associatedTypeName;
    private final String associatedClassification;
    private final String description;
    private final String wikiLink;


    /**
     * Constructor for individual enum value.
     *
     * @param deployedImplementationType value for deployedImplementationType
     * @param isATypeOf optional deployed implementation type that this type "inherits" from
     * @param associatedTypeName the open metadata type where this value is used
     * @param associatedClassification the open metadata classification where this value is used
     * @param description description of the type
     * @param wikiLink url link to more information (optional)
     */
    DeployedImplementationType(String                     deployedImplementationType,
                               DeployedImplementationType isATypeOf,
                               String                     associatedTypeName,
                               String                     associatedClassification,
                               String                     description,
                               String                     wikiLink)
    {
        this.deployedImplementationType = deployedImplementationType;
        this.isATypeOf = isATypeOf;
        this.associatedTypeName = associatedTypeName;
        this.associatedClassification = associatedClassification;
        this.description = description;
        this.wikiLink = wikiLink;
    }


    /**
     * Return preferred value for deployed implementation type.
     * 
     * @return string
     */
    @Override
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the optional deployed implementation type that this technology is a tye of.
     *
     * @return deployed implementation type enum
     */
    @Override
    public DeployedImplementationTypeDefinition getIsATypeOf()
    {
        return isATypeOf;
    }


    /**
     * Return the type name that this deployed implementation type is associated with.
     * 
     * @return string
     */
    @Override
    public String getAssociatedTypeName()
    {
        return associatedTypeName;
    }


    /**
     * Return the optional classification name that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getAssociatedClassification()
    {
        return associatedClassification;
    }


    /**
     * Return the qualified name for this deployed implementation type.
     *
     * @return string
     */
    @Override
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(associatedTypeName,
                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                null,
                                                deployedImplementationType);
    }


    /**
     * Return the category for this deployed implementation type.
     *
     * @return string
     */
    @Override
    public String getNamespace()
    {
        return constructValidValueNamespace(associatedTypeName,
                                            OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                            null);
    }


    /**
     * Return the description for this value.
     * 
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL to more information.
     *
     * @return string url
     */
    @Override
    public String getWikiLink()
    {
        return wikiLink;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "DeployedImplementationType{" + deployedImplementationType + '}';
    }
}
