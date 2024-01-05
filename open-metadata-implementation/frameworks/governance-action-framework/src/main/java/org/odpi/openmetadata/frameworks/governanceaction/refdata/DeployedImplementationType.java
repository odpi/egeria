/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.refdata;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;

import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria. These are encoded in the
 * OpenConnectorsArchive.omarchive and are available in the open metadata repository as valid values.
 */
public enum DeployedImplementationType
{
    /**
     * A file containing externally accessible data - other fields provide information on the internal format.
     */
    DATA_FILE("Data File",
              OpenMetadataType.DATA_FILE_TYPE_NAME,
              "A file containing externally accessible data - other fields provide information on the internal format.",
              "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A file containing program logic.
     */
    PROGRAM_FILE("Program File",
                 OpenMetadataType.DATA_FILE_TYPE_NAME,
                 "A file containing program logic.",
                 "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A file containing log records.
     */
    LOG_FILE("Log File",
             OpenMetadataType.DATA_FILE_TYPE_NAME,
             "A file containing log records.",
             "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A directory (folder) that holds files representing a single data source.
     */
    DATA_FOLDER("Data Folder",
                OpenMetadataType.DATA_FOLDER_TYPE_NAME,
                "A directory (folder) that holds files representing a single data source.",
                "https://egeria-project.org/concepts/deployed-implementation-type/"),


    /**
     * A directory (folder) that holds files that are potential data sources.
     */
    FILE_FOLDER("FileFolder",
                OpenMetadataType.FILE_FOLDER_TYPE_NAME,
                "A directory (folder) that holds files that are potential data sources.",
                "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A file containing an organized collection of files.
     */
    ARCHIVE_FILE("Archive File",
                 OpenMetadataType.DATA_FILE_TYPE_NAME,
                 "A file containing an organized collection of files.",
                 "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A database server running the PostgreSQL software.
     */
    POSTGRESQL_SERVER("PostgreSQL Server",
                      OpenMetadataType.SOFTWARE_SERVER_TYPE_NAME,
                      "A database server running the PostgreSQL software.",
                      "https://www.postgresql.org/"),

    /**
     * A database hosted on a PostgreSQL server.
     */
    POSTGRESQL_DATABASE("PostgreSQL Relational Database",
                        OpenMetadataType.RELATIONAL_DATABASE_TYPE_NAME,
                        "A database hosted on a PostgreSQL server.",
                        "https://www.postgresql.org/"),

    /**
     * A data catalog for the Hadoop ecosystem.
     */
    APACHE_ATLAS("Apache Atlas",
                 OpenMetadataType.SOFTWARE_SERVER_TYPE_NAME,
                 "A data catalog for the Hadoop ecosystem.",
                 "https://atlas.apache.org/"),

    /**
     * An event broker supporting high speed, reliable topic-based event exchange.
     */
    APACHE_KAFKA_SERVER("Apache Kafka Server",
                        OpenMetadataType.SOFTWARE_SERVER_TYPE_NAME,
                        "An event broker supporting high speed, reliable topic-based event exchange.",
                        "https://kafka.apache.org/"),

    /**
     * An event topic supporting high speed, reliable event exchange.
     */
    APACHE_KAFKA_TOPIC("Apache Kafka Topic",
                       OpenMetadataType.KAFKA_TOPIC_TYPE_NAME,
                       "An event topic supporting high speed, reliable event exchange.",
                       "https://kafka.apache.org/"),

    /**
     * A system that manages hierarchically organized files on persistent storage.
     */
    FILE_SYSTEM("File System",
                OpenMetadataType.DATA_MANAGER_TYPE_NAME,
                "A system that manages hierarchically organized files on persistent storage.",
                "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * An Open Metadata and Governance (OMAG) platform for running one to many OMAG Servers.
     */
    OMAG_SERVER_PLATFORM("OMAG Server Platform",
                         OpenMetadataType.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                         "An Open Metadata and Governance (OMAG) platform for running one to many OMAG Servers.",
                         "https://egeria-project.org/concepts/omag-server-platform/"),

    /**
     * An Open Metadata and Governance (OMAG) runtime for running a single OMAG Server.
     */
    OMAG_SERVER_RUNTIME("OMAG Server Runtime",
                        OpenMetadataType.SOFTWARE_SERVER_PLATFORM_TYPE_NAME,
                        "An Open Metadata and Governance (OMAG) runtime for running a single OMAG Server.",
                        "https://egeria-project.org/concepts/omag-server-runtime/"),

    /**
     * OMRS Repository Connector - Maps open metadata calls to a metadata repository.
     */
    REPOSITORY_CONNECTOR("OMRS Repository Connector",
                         OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME,
                         "Maps open metadata calls to a metadata repository.",
                         "https://egeria-project.org/concepts/repository-connector/"),

    /**
     * Open Discovery Service - A connector that analyzing the contents of a digital resource.
     */
    OPEN_DISCOVERY_SERVICE_CONNECTOR("Open Discovery Service",
                                     OpenMetadataType.DISCOVERY_SERVICE_TYPE_NAME,
                                     "A connector that analyzing the contents of a digital resource and produces a discovery analysis report.",
                                     "https://egeria-project.org/concepts/open-discovery-service/"),

    /**
     * Open Discovery Engine - A governance engine that runs open discovery services.
     */
    OPEN_DISCOVERY_ENGINE("Open Discovery Engine",
                          OpenMetadataType.DISCOVERY_ENGINE_TYPE_NAME,
                          "A governance engine that runs open discovery services.",
                          "https://egeria-project.org/concepts/open-discovery-engine/"),

    /**
     * Governance Action Service - A connector that coordinates governance of digital resources and metadata.
     */
    GOVERNANCE_ACTION_SERVICE_CONNECTOR("Governance Action Service",
                                        OpenMetadataType.GOVERNANCE_ACTION_SERVICE_TYPE_NAME,
                                        "A connector that coordinates governance of digital resources and metadata.",
                                        "https://egeria-project.org/concepts/governance-action-service/"),


    /**
     * Governance Action Engine - A governance engine that runs governance action services.
     */
    GOVERNANCE_ACTION_ENGINE("Governance Action Engine",
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE_TYPE_NAME,
                             "A governance engine that runs governance action services.",
                             "https://egeria-project.org/concepts/governance-action-engine/"),


    /**
     * Event Action Service - A connector that coordinates governance of context events.
     */
    EVENT_ACTION_SERVICE_CONNECTOR("Event Action Service",
                                   OpenMetadataType.EVENT_ACTION_SERVICE_TYPE_NAME,
                                   "A connector that coordinates governance of context events.",
                                   "https://egeria-project.org/concepts/event-action-service/"),


    /**
     * Event Action Engine - A governance engine that runs event action services.
     */
    EVENT_ACTION_ENGINE("Event Action Engine",
                        OpenMetadataType.EVENT_ACTION_ENGINE_TYPE_NAME,
                        "A governance engine that runs event action services.",
                        "https://egeria-project.org/concepts/event-action-engine/"),


    /**
     * Repository Governance Service - A connector that dynamically governs the activity of the open metadata repositories.
     */
    REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR("Repository Governance Service Connector",
                                            OpenMetadataType.REPOSITORY_GOVERNANCE_SERVICE_TYPE_NAME,
                                            "A connector that dynamically governs the activity of the open metadata repositories.",
                                            "https://egeria-project.org/concepts/repository-governance-service"),

    /**
     * A governance engine that runs repository governance services.
     */
    REPOSITORY_GOVERNANCE_ENGINE("Repository Governance Engine",
                                 OpenMetadataType.REPOSITORY_GOVERNANCE_ENGINE_TYPE_NAME,
                                 "A governance engine that runs repository governance services.",
                                 "https://egeria-project.org/concepts/repository-governance-engine/"),

    /**
     * Analytics Integration Connector - Connector that manages metadata exchange with a third-party analytics technology.
     */
    ANALYTICS_INTEGRATION_CONNECTOR("Analytics Integration Connector",
                                    OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                    "Connector that manages metadata exchange with a third party analytics technology.",
                                    "https://egeria-project.org/services/omvs/analytics-integrator/overview"),


    /**
     * API Integration Connector - Connector that manages metadata exchange with a third-party API management technology.
     */
    API_INTEGRATION_CONNECTOR("API Integration Connector",
                              OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                              "Connector that manages metadata exchange with a third party API management technology.",
                              "https://egeria-project.org/services/omvs/api-integrator/overview"),

    /**
     * Catalog Integration Connector - Connector that manages metadata exchange with a third-party metadata catalog technology.
     */
    CATALOG_INTEGRATION_CONNECTOR("Catalog Integration Connector",
                                  OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                  "Connector that manages metadata exchange with a third party metadata catalog technology.",
                                  "https://egeria-project.org/services/omvs/catalog-integrator/overview"),

    /**
     * Database Integration Connector - Connector that manages metadata exchange with a third-party database technology.
     */
    DATABASE_INTEGRATION_CONNECTOR("Database Integration Connector",
                                   OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                   "Connector that manages metadata exchange with a third party database technology.",
                                   "https://egeria-project.org/services/omvs/database-integrator/overview"),

    /**
     * Display Integration Connector - Connector that manages metadata exchange with a third-party display (user interaction) technology.
     */
    DISPLAY_INTEGRATION_CONNECTOR("Display Integration Connector",
                                  OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                  "Connector that manages metadata exchange with a third party display (user interaction) technology.",
                                  "https://egeria-project.org/services/omvs/display-integrator/overview"),

    /**
     * Files Integration Connector - Connector that manages metadata exchange with a third-party filesystem technology.
     */
    FILES_INTEGRATION_CONNECTOR("Files Integration Connector",
                                OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                "Connector that manages metadata exchange with a third party filesystem technology.",
                                "https://egeria-project.org/services/omvs/files-integrator/overview"),

    /**
     * Infrastructure Integration Connector - Connector that manages metadata exchange with a third-party infrastructure catalog (CMDB) technology.
     */
    INFRASTRUCTURE_INTEGRATION_CONNECTOR("Infrastructure Integration Connector",
                                         OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                         "Connector that manages metadata exchange with a third party infrastructure catalog (CMDB) technology.",
                                         "https://egeria-project.org/services/omvs/infrastructure-integrator/overview"),


    /**
     * Lineage Integration Connector - Connector that manages metadata exchange with a third-party lineage capture technology.
     */
    LINEAGE_INTEGRATION_CONNECTOR("Lineage Integration Connector",
                                  OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                  "Connector that manages metadata exchange with a third party lineage capture technology.",
                                  "https://egeria-project.org/services/omvs/lineage-integrator/overview"),


    /**
     * Organization Integration Connector - Connector that manages metadata exchange with a third-party application containing data about people and organizations.
     */
    ORGANIZATION_INTEGRATION_CONNECTOR("Organization Integration Connector",
                                       OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                       "Connector that manages metadata exchange with a third party application containing data about people and organizations.",
                                       "https://egeria-project.org/services/omvs/organization-integrator/overview"),

    /**
     * Search Integration Connector - Connector that manages metadata exchange with a third-party search technology.
     */
    SEARCH_INTEGRATION_CONNECTOR("Search Integration Connector",
                                 OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                 "Connector that manages metadata exchange with a third party search technology.",
                                 "https://egeria-project.org/services/omvs/search-integrator/overview"),

    /**
     * Security Integration Connector - Connector that manages metadata exchange with a third-party security management technology.
     */
    SECURITY_INTEGRATION_CONNECTOR("Security Integration Connector",
                                   OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                   "Connector that manages metadata exchange with a third party security management technology.",
                                   "https://egeria-project.org/services/omvs/security-integrator/overview"),

    /**
     * Topic Integration Connector - Connector that manages metadata exchange with a third-party event management technology.
     */
    TOPIC_INTEGRATION_CONNECTOR("Topic Integration Connector",
                                OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                "Connector that manages metadata exchange with a third party event management technology.",
                                "https://egeria-project.org/services/omvs/topic-integrator/overview"),

    /**
     * Platform Metadata Security Connector - Connector that manages authorization requests to the OMAG Server Platform.
     */
    PLATFORM_SECURITY_CONNECTOR("Platform Metadata Security Connector",
                                OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                                "Connector that manages authorization requests to the OMAG Server Platform.",
                                "https://egeria-project.org/concepts/platform-metadata-security-connector/"),

    /**
     * Server Metadata Security Connector - Connector that manages authorization requests to the OMAG Server.
     */
    SERVER_SECURITY_CONNECTOR("Server Metadata Security Connector",
                              OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                              "Connector that manages authorization requests to the OMAG Server.",
                              "https://egeria-project.org/concepts/server-metadata-security-connector/"),

    /**
     * Open Metadata Archive Store Connector - Reads and writes open metadata types and instances to an open metadata archive.
     */
    ARCHIVE_STORE_CONNECTOR("Open Metadata Archive Store Connector",
                            OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                            "Reads and writes open metadata types and instances to an open metadata archive.",
                            "https://egeria-project.org/concepts/open-metadata-archive-store-connector/"),

    /**
     * Cohort Registry Store - Stores information about the repositories registered in the open metadata repository cohort.
     */
    COHORT_REGISTRY_STORE("Cohort Registry Store",
                          OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                          "Stores information about the repositories registered in the open metadata repository cohort.",
                          "https://egeria-project.org/concepts/cohort-registry-store-connector/"),

    /**
     * Audit Log Destination - Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.
     */
    AUDIT_LOG_DESTINATION("Audit Log Destination",
                          OpenMetadataType.DEPLOYED_CONNECTOR_TYPE_NAME,
                          "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
                          "https://egeria-project.org/concepts/audit-log/"),

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
    private final String associatedTypeName;
    private final String description;
    private final String wikiLink;


    /**
     * Constructor for individual enum value.
     *
     * @param deployedImplementationType value for deployedImplementationType
     * @param associatedTypeName the open metadata type where this value is used
     * @param description description of the type
     * @param wikiLink url link to more information (optional)
     */
    DeployedImplementationType(String deployedImplementationType,
                               String associatedTypeName,
                               String description,
                               String wikiLink)
    {
        this.deployedImplementationType = deployedImplementationType;
        this.associatedTypeName = associatedTypeName;
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
     * Return the type name that this deployed implementation type is associated with.
     * 
     * @return string
     */
    public String getAssociatedTypeName()
    {
        return associatedTypeName;
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
