/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria. These are encoded in the
 * OpenConnectorsArchive.omarchive and are available in the open metadata repository as valid values.
 */
public enum DeployedImplementationType
{
    /**
     * A collection of data, either at rest or in motion.
     */
    DATA_ASSET("Data Asset",
               null,
               OpenMetadataType.ASSET.typeName,
               null,
               "A collection of data, either at rest or in motion.",
               "https://egeria-project.org/concepts/asset/"),

    /**
     * A file stored on a file system.
     */
    FILE("File",
         DeployedImplementationType.DATA_ASSET,
         OpenMetadataType.DATA_FILE.typeName,
         null,
         "A file stored on a file system.",
         "https://egeria-project.org/types/2/0220-Files-and-Folders/"),

    /**
     * A file containing externally accessible data - other fields provide information on the internal format.
     */
    DATA_FILE("Data File",
              DeployedImplementationType.FILE,
              OpenMetadataType.DATA_FILE.typeName,
              null,
              "A file containing externally accessible data - other fields provide information on the internal format.",
              "https://egeria-project.org/types/2/0220-Files-and-Folders/"),

    /**
     * A file containing externally accessible data - other fields provide information on the internal format.
     */
    CSV_FILE("Data File",
              DeployedImplementationType.DATA_FILE,
              OpenMetadataType.CSV_FILE.typeName,
             null,
              "A file containing comma-separated (or similar delimited) data.",
              "https://egeria-project.org/types/2/0220-Files-and-Folders/"),

    /**
     * A file containing program logic.
     */
    PROGRAM_FILE("Program File",
                 DeployedImplementationType.FILE,
                 OpenMetadataType.DATA_FILE.typeName,
                 null,
                 "A file containing program logic.",
                 "https://egeria-project.org/types/2/0280-Software-Development-Assets/"),

    /**
     * A file containing log records.
     */
    LOG_FILE("Log File",
             DeployedImplementationType.FILE,
             OpenMetadataType.DATA_FILE.typeName,
             null,
             "A file containing log records.",
             "https://egeria-project.org/types/2/0223-Events-and-Logs/"),


    /**
     * A file containing an organized collection of files.
     */
    ARCHIVE_FILE("Archive File",
                 DeployedImplementationType.FILE,
                 OpenMetadataType.DATA_FILE.typeName,
                 null,
                 "A file containing an organized collection of files.",
                 "https://egeria-project.org/types/2/0226-Archive-Files/"),


    /**
     * A directory (folder) that holds files that are potential data sources.
     */
    FILE_FOLDER("FileFolder",
                DeployedImplementationType.DATA_ASSET,
                OpenMetadataType.FILE_FOLDER.typeName,
                null,
                "A directory (folder) that holds files that are potential data sources.",
                "https://egeria-project.org/types/2/0220-Files-and-Folders/"),


    /**
     * A directory (folder) that holds files representing a single data source.
     */
    DATA_FOLDER("Data Folder",
                DeployedImplementationType.FILE_FOLDER,
                OpenMetadataType.DATA_FOLDER.typeName,
                null,
                "A directory (folder) that holds files representing a single data source.",
                "https://egeria-project.org/types/2/0220-Files-and-Folders/"),


    /**
     * A database hosted on a relational database server capable of being called through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE("JDBC Relational Database",
                             DeployedImplementationType.DATA_ASSET,
                             OpenMetadataType.RELATIONAL_DATABASE_TYPE_NAME,
                             null,
                             "A database hosted on a relational database server capable of being called through a JDBC Driver.",
                             "https://en.wikipedia.org/wiki/Java_Database_Connectivity"),


    /**
     * A database schema hosted on a relational database server capable of being called through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE_SCHEMA("JDBC Relational Database Schema",
                             DeployedImplementationType.DATA_ASSET,
                             OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
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
     * A database hosted on a PostgreSQL server.
     */
    POSTGRESQL_DATABASE("PostgreSQL Relational Database",
                        DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                        OpenMetadataType.RELATIONAL_DATABASE_TYPE_NAME,
                        null,
                        "A database hosted on a PostgreSQL server.",
                        "https://www.postgresql.org/"),

    /**
     * A database schema hosted on a relational PostgreSQL database server capable of being called through a JDBC Driver.
     */
    POSTGRESQL_DATABASE_SCHEMA("PostgreSQL Relational Database Schema",
                                    DeployedImplementationType.DATA_ASSET,
                                    OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                    null,
                                    "A database schema hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.",
                                    "https://www.postgresql.org/"),

    /**
     * A database table hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.
     */
    POSTGRESQL_DATABASE_TABLE("PostgreSQL Relational Database Table",
                              DeployedImplementationType.DATA_ASSET,
                              OpenMetadataType.TABLE_DATA_SET.typeName,
                              null,
                              "A database table hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.",
                              "https://www.postgresql.org/"),

    /**
     * A callable software server.
     */
    SOFTWARE_SERVER("Software Server",
                    null,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "A callable software server.",
                    "https://egeria-project.org/types/0/0040-Software-Servers/"),


    /**
     * A callable software capability supporting specific types of assets.
     */
    SOFTWARE_CAPABILITY("Software Capability",
                    null,
                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                    null,
                    "A callable software capability supporting specific types of assets.",
                    "https://egeria-project.org/types/0/0042-Software-Capabilities/"),


    /**
     * A database server running the PostgreSQL software.
     */
    POSTGRESQL_SERVER("PostgreSQL Server",
                      DeployedImplementationType.SOFTWARE_SERVER,
                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                      null,
                      "A database server running the PostgreSQL software.",
                      "https://www.postgresql.org/"),


    /**
     * A data catalog for the Hadoop ecosystem.
     */
    APACHE_ATLAS_SERVER("Apache Atlas Server",
                        DeployedImplementationType.SOFTWARE_SERVER,
                        OpenMetadataType.SOFTWARE_SERVER.typeName,
                        null,
                        "A data catalog for the Hadoop ecosystem.",
                        "https://atlas.apache.org/"),

    /**
     * A software server supporting an event broker that enables high speed, reliable topic-based event exchange.
     */
    APACHE_KAFKA_SERVER("Apache Kafka Server",
                        DeployedImplementationType.SOFTWARE_SERVER,
                        OpenMetadataType.SOFTWARE_SERVER.typeName,
                        null,
                        "A software server supporting an event broker that enables high speed, reliable topic-based event exchange.",
                        "https://kafka.apache.org/"),

    /**
     * A software capability that enables high speed, reliable topic-based event exchange.
     */
    APACHE_KAFKA_EVENT_BROKER("Apache Kafka Event Broker",
                        DeployedImplementationType.SOFTWARE_CAPABILITY,
                        OpenMetadataType.EVENT_BROKER.typeName,
                        null,
                        "A software capability that enables high speed, reliable topic-based event exchange.",
                        "https://kafka.apache.org/"),

    /**
     * An event topic supporting high speed, reliable event exchange.
     */
    APACHE_KAFKA_TOPIC("Apache Kafka Topic",
                       DeployedImplementationType.DATA_ASSET,
                       OpenMetadataType.KAFKA_TOPIC.typeName,
                       null,
                       "An event topic supporting high speed, reliable event exchange.",
                       "https://kafka.apache.org/"),

    /**
     * A system that manages hierarchically organized files on persistent storage.
     */
    FILE_SYSTEM("File System",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.DATA_MANAGER.typeName,
                OpenMetadataType.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME,
                "A system that manages hierarchically organized files on persistent storage.",
                "https://egeria-project.org/types/0/0056-Resource-Managers/"),


    /**
     * A catalog of metadata that describes assets such as deployed systems, data sources and processes.
     */
    ASSET_CATALOG("Asset Metadata Catalog",
                  DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.CATALOG.typeName,
                  OpenMetadataType.ASSET_MANAGER_TYPE_NAME,
                  "A catalog of metadata that describes assets such as deployed systems, data sources and processes.",
                "https://egeria-project.org/types/0/0050-Applications-and-Processes/"),


    /**
     * A software capability that provides callable APIs.
     */
    API_MANAGER("API Manager",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.API_MANAGER.typeName,
                  null,
                  "A software capability that provides callable APIs.",
                  "https://egeria-project.org/types/0/0050-Applications-and-Processes/"),


    /**
     * A software capability that provides callable APIs supporting the REST protocol.
     */
    REST_API_MANAGER("REST API Manager",
                DeployedImplementationType.API_MANAGER,
                OpenMetadataType.API_MANAGER.typeName,
                null,
                "A software capability that provides callable APIs supporting the REST protocol.",
                "https://egeria-project.org/types/0/0050-Applications-and-Processes/"),

    /**
     * A system that manages the asynchronous exchange of messages (events) from once to potentially many recipients.  Typically, this exchange of events is organized into groups called topics.
     */
    EVENT_BROKER("Event Broker",
                 DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.EVENT_BROKER.typeName,
                 null,
                  "A system that manages the asynchronous exchange of messages (events) from once to potentially many recipients.  Typically, this exchange of events is organized into groups called topics.",
                  "https://egeria-project.org/types/0/0050-Applications-and-Processes/"),


    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    RELATIONAL_DATABASE_MANAGER("Relational database manager (RDBMS)",
                                DeployedImplementationType.SOFTWARE_CAPABILITY,
                                OpenMetadataType.DATABASE_MANAGER.typeName,
                                null,
                                "A capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                                "https://egeria-project.org/types/0/0050-Applications-and-Processes/"),

    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    POSTGRESQL_DATABASE_MANAGER("PostgreSQL database manager (RDBMS)",
                                DeployedImplementationType.RELATIONAL_DATABASE_MANAGER,
                                OpenMetadataType.DATABASE_MANAGER.typeName,
                                null,
                                "The PostgreSQL capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                                "https://egeria-project.org/types/0/0050-Applications-and-Processes/"),


    /**
     * An Open Metadata and Governance (OMAG) platform for running one to many OMAG Servers.
     */
    OMAG_SERVER_PLATFORM("OMAG Server Platform",
                         DeployedImplementationType.SOFTWARE_SERVER,
                         OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                         null,
                         "An Open Metadata and Governance (OMAG) platform for running one to many OMAG Servers.",
                         "https://egeria-project.org/concepts/omag-server-platform/"),

    /**
     * An Open Metadata and Governance (OMAG) runtime for running a single OMAG Server.
     */
    OMAG_SERVER_RUNTIME("OMAG Server Runtime",
                        DeployedImplementationType.SOFTWARE_SERVER,
                        OpenMetadataType.SOFTWARE_SERVER.typeName,
                        null,
                        "An Open Metadata and Governance (OMAG) runtime for running a single OMAG Server.",
                        "https://egeria-project.org/concepts/omag-server-runtime/"),


    /**
     * A deployable software component.
     */
    SOFTWARE_COMPONENT("Software Component",
                  null,
                  OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME,
                       null,
                  "A deployable software component.",
                  "https://egeria-project.org/types/2/0215-Software-Components/"),

    /**
     * A pluggable software component that conforms to the Open Connector Framework (OCF).
     */
    OCF_CONNECTOR("Open Connector Framework (OCF) Connector",
                  null,
                  OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME,
                  null,
                  "A pluggable software component that conforms to the Open Connector Framework (OCF).",
                  "https://egeria-project.org/concepts/connector/"),

    /**
     * OMRS Repository Connector - Maps open metadata calls to a metadata repository.
     */
    REPOSITORY_CONNECTOR("OMRS Repository Connector",
                         null,
                         OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME,
                         null,
                         "Maps open metadata repository calls defined by the Open Metadata Repository Services (OMRS) to a metadata repository API and event notifications.",
                         "https://egeria-project.org/concepts/repository-connector/"),


    /**
     * Manages the execution of automated governance activity requested via engine actions.
     */
    GOVERNANCE_ENGINE("Governance Engine",
                      null,
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
     * Open Discovery Service - A connector that analyzing the contents of a digital resource.
     */
    OPEN_DISCOVERY_SERVICE_CONNECTOR("Open Discovery Service",
                                     DeployedImplementationType.GOVERNANCE_SERVICE,
                                     OpenMetadataType.OPEN_DISCOVERY_SERVICE.typeName,
                                     null,
                                     "A connector that analyzing the contents of a digital resource and produces a discovery analysis report.",
                                     "https://egeria-project.org/concepts/open-discovery-service/"),

    /**
     * Open Discovery Engine - A governance engine that runs open discovery services.
     */
    OPEN_DISCOVERY_ENGINE("Open Discovery Engine",
                          DeployedImplementationType.GOVERNANCE_ENGINE,
                          OpenMetadataType.OPEN_DISCOVERY_ENGINE.typeName,
                          null,
                          "A governance engine that runs open discovery services.",
                          "https://egeria-project.org/concepts/open-discovery-engine/"),

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
     * Context Event Service - A connector that coordinates governance of context events.
     */
    CONTEXT_EVENT_SERVICE_CONNECTOR("Context Event Service",
                                    DeployedImplementationType.GOVERNANCE_SERVICE,
                                    OpenMetadataType.CONTEXT_EVENT_SERVICE.typeName,
                                    null,
                                    "A connector that coordinates governance of context events.",
                                    "https://egeria-project.org/concepts/context-event-service/"),


    /**
     * Context Event Engine - A governance engine that runs context event services.
     */
    CONTEXT_EVENT_ENGINE("Context Event Engine",
                         DeployedImplementationType.GOVERNANCE_ENGINE,
                         OpenMetadataType.CONTEXT_EVENT_ENGINE.typeName,
                         null,
                         "A governance engine that runs context event services.",
                         "https://egeria-project.org/concepts/context-event-engine/"),

    /**
     * Event Action Service - A connector that coordinates asset surveys.
     */
    SURVEY_ACTION_SERVICE_CONNECTOR("Survey Action Service",
                                    DeployedImplementationType.GOVERNANCE_SERVICE,
                                    OpenMetadataType.SURVEY_ACTION_SERVICE.typeName,
                                    null,
                                    "A connector that coordinates asset surveys.",
                                    "https://egeria-project.org/concepts/survey-action-service/"),


    /**
     * Event Action Engine - A governance engine that runs survey action services.
     */
    SURVEY_ACTION_ENGINE("Survey Action Engine",
                         DeployedImplementationType.GOVERNANCE_ENGINE,
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         null,
                         "A governance engine that runs survey action services.",
                         "https://egeria-project.org/concepts/survey-action-engine/"),


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
                                OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                                null,
                                "Connector that manages authorization requests to the OMAG Server Platform.",
                                "https://egeria-project.org/concepts/platform-metadata-security-connector/"),

    /**
     * Server Metadata Security Connector - Connector that manages authorization requests to the OMAG Server.
     */
    SERVER_SECURITY_CONNECTOR("Server Metadata Security Connector",
                              DeployedImplementationType.OCF_CONNECTOR,
                              OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                              null,
                              "Connector that manages authorization requests to the OMAG Server.",
                              "https://egeria-project.org/concepts/server-metadata-security-connector/"),

    /**
     * Open Metadata Archive Store Connector - Reads and writes open metadata types and instances to an open metadata archive.
     */
    ARCHIVE_STORE_CONNECTOR("Open Metadata Archive Store Connector",
                            DeployedImplementationType.OCF_CONNECTOR,
                            OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                            null,
                            "Reads and writes open metadata types and instances to an open metadata archive.",
                            "https://egeria-project.org/concepts/open-metadata-archive-store-connector/"),

    /**
     * Cohort Registry Store - Stores information about the repositories registered in the open metadata repository cohort.
     */
    COHORT_REGISTRY_STORE("Cohort Registry Store",
                          DeployedImplementationType.OCF_CONNECTOR,
                          OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                          null,
                          "Stores information about the repositories registered in the open metadata repository cohort.",
                          "https://egeria-project.org/concepts/cohort-registry-store-connector/"),

    /**
     * Audit Log Destination - Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.
     */
    AUDIT_LOG_DESTINATION("Audit Log Destination",
                          DeployedImplementationType.OCF_CONNECTOR,
                          OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                          null,
                          "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
                          "https://egeria-project.org/concepts/audit-log/"),


    /**
     * Provides the list of integration connectors that should run in an Integration Daemon.  The Integration Daemon is configured with the qualified names of the integration group(s) that provide its connector list.
     */
    INTEGRATION_GROUP("Dynamic Integration Group",
                      null,
                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                      null,
                      "Provides the list of integration connectors that should run in an Integration Daemon.  The Integration Daemon is configured with the qualified names of the integration group(s) that provide its connector list.",
                      "https://egeria-project.org/concepts/integration-group/"),

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
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the optional deployed implementation type that this technology is a tye of.
     *
     * @return deployed implementation type enum
     */
    public DeployedImplementationType getIsATypeOf()
    {
        return isATypeOf;
    }

    /**
     * Return the type name that this deployed implementation type is associated with.
     * 
     * @return string
     */
    public String getAssociatedTypeName()
    {
        return associatedTypeName;
    }


    /**
     * Return the optional classification name that this deployed implementation type is associated with.
     *
     * @return string
     */
    public String getAssociatedClassification()
    {
        return associatedClassification;
    }


    /**
     * Return the qualified name for this deployed implementation type.
     *
     * @return string
     */
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
    public String getCategory()
    {
        return constructValidValueCategory(associatedTypeName,
                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                           null);
    }


    /**
     * Return the description for this value.
     * 
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL to more information.
     *
     * @return string url
     */
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
