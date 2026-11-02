/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.adapters.connectors.EgeriaSolutionComponent;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;

/**
 * Describes the additional solution linking wires that cannot be derived from standard definition.
 * EG friendship relationships
 */
public enum ContentPackSolutionLinkingWire
{
    /**
     * Pass responsibility for cataloguing schema, tables, and columns.
     */
    POSTGRES_FRIENDSHIP(IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getSolutionComponentGUID(),
                        IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                        "handoff",
                        "Pass responsibility for cataloguing schema, tables, and columns.",
                        ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * Pass responsibility for cataloguing data assets found in a Unity Catalog (UC) catalog.
     */
    UNITY_CATALOG_FRIENDSHIP(IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getSolutionComponentGUID(),
                             IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getSolutionComponentGUID(),
                             "handoff",
                             "Pass responsibility for cataloguing data assets found in a Unity Catalog (UC) catalog.",
                             ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    /**
     * Babbage runs the Lovelace services that are registered with it as catalog targets (see
     * LovelaceServiceDefinition).  Each wire is in the content pack that supplies the Lovelace service.
     */
    BABBAGE_ZONE_MEMBERSHIP_PROFILE(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getSolutionComponentGUID(),
                                    RequestTypeDefinition.BUILD_ZONE_MEMBERSHIP_PROFILE.getSolutionComponentGUID(),
                                    "schedules",
                                    "Runs the zone membership profiler on each refresh.",
                                    ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),

    /**
     * Runs the popularity profiler on each refresh.
     */
    BABBAGE_MEMBER_POPULARITY_PROFILE(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getSolutionComponentGUID(),
                                      RequestTypeDefinition.BUILD_MEMBER_POPULARITY_PROFILE.getSolutionComponentGUID(),
                                      "schedules",
                                      "Runs the popularity profiler on each refresh.",
                                      ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Runs the OpenLineage run profiler on each refresh.
     */
    BABBAGE_OPEN_LINEAGE_RUN_PROFILE(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getSolutionComponentGUID(),
                                     RequestTypeDefinition.PROFILE_OPEN_LINEAGE_RUNS.getSolutionComponentGUID(),
                                     "schedules",
                                     "Runs the OpenLineage run profiler on each refresh.",
                                     ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Runs the OpenLineage data scope analysis on each refresh.
     */
    BABBAGE_OPEN_LINEAGE_DATA_SCOPE(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getSolutionComponentGUID(),
                                    RequestTypeDefinition.REFINE_OPEN_LINEAGE_DATA_SCOPE.getSolutionComponentGUID(),
                                    "schedules",
                                    "Runs the OpenLineage data scope analysis on each refresh.",
                                    ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Runs the OpenLineage data quality summary on each refresh.
     */
    BABBAGE_OPEN_LINEAGE_DATA_QUALITY_SUMMARY(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getSolutionComponentGUID(),
                                              RequestTypeDefinition.SUMMARISE_OPEN_LINEAGE_DATA_QUALITY.getSolutionComponentGUID(),
                                              "schedules",
                                              "Runs the OpenLineage data quality summary on each refresh.",
                                              ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Passes the data product documents found in the files to the data product cataloguer.
     */
    BITOL_FILES_RECEIVER_TO_DATA_PRODUCT_CATALOGUER(IntegrationConnectorDefinition.BITOL_FILES_RECEIVER.getSolutionComponentGUID(),
                                                    IntegrationConnectorDefinition.BITOL_DATA_PRODUCT_CATALOGUER.getSolutionComponentGUID(),
                                                    "distribute documents",
                                                    "Passes the data product documents found in the files to the data product cataloguer.",
                                                    ContentPackDefinition.BITOL_CONTENT_PACK),

    /**
     * Passes the data contract documents found in the files to the data contract cataloguer.
     */
    BITOL_FILES_RECEIVER_TO_DATA_CONTRACT_CATALOGUER(IntegrationConnectorDefinition.BITOL_FILES_RECEIVER.getSolutionComponentGUID(),
                                                     IntegrationConnectorDefinition.BITOL_DATA_CONTRACT_CATALOGUER.getSolutionComponentGUID(),
                                                     "distribute documents",
                                                     "Passes the data contract documents found in the files to the data contract cataloguer.",
                                                     ContentPackDefinition.BITOL_CONTENT_PACK),

    /**
     * Passes the data product documents received from Apache Kafka to the data product cataloguer.
     */
    BITOL_EVENT_RECEIVER_TO_DATA_PRODUCT_CATALOGUER(IntegrationConnectorDefinition.BITOL_EVENT_RECEIVER.getSolutionComponentGUID(),
                                                    IntegrationConnectorDefinition.BITOL_DATA_PRODUCT_CATALOGUER.getSolutionComponentGUID(),
                                                    "distribute documents",
                                                    "Passes the data product documents received from Apache Kafka to the data product cataloguer.",
                                                    ContentPackDefinition.BITOL_CONTENT_PACK),

    /**
     * Passes the data contract documents received from Apache Kafka to the data contract cataloguer.
     */
    BITOL_EVENT_RECEIVER_TO_DATA_CONTRACT_CATALOGUER(IntegrationConnectorDefinition.BITOL_EVENT_RECEIVER.getSolutionComponentGUID(),
                                                     IntegrationConnectorDefinition.BITOL_DATA_CONTRACT_CATALOGUER.getSolutionComponentGUID(),
                                                     "distribute documents",
                                                     "Passes the data contract documents received from Apache Kafka to the data contract cataloguer.",
                                                     ContentPackDefinition.BITOL_CONTENT_PACK),

    /**
     * Passes the documents received from Apache Kafka to the file store.
     */
    BITOL_EVENT_RECEIVER_TO_FILE_STORE(IntegrationConnectorDefinition.BITOL_EVENT_RECEIVER.getSolutionComponentGUID(),
                                       IntegrationConnectorDefinition.BITOL_FILE_STORE.getSolutionComponentGUID(),
                                       "distribute documents",
                                       "Passes the documents received from Apache Kafka to the file store.",
                                       ContentPackDefinition.BITOL_CONTENT_PACK),

    /**
     * Passes the documents generated from open metadata to the file store.
     */
    BITOL_DOCUMENT_PUBLISHER_TO_FILE_STORE(IntegrationConnectorDefinition.BITOL_DOCUMENT_PUBLISHER.getSolutionComponentGUID(),
                                           IntegrationConnectorDefinition.BITOL_FILE_STORE.getSolutionComponentGUID(),
                                           "distribute documents",
                                           "Passes the documents generated from open metadata to the file store.",
                                           ContentPackDefinition.BITOL_CONTENT_PACK),

    /**
     * Receive and push open lineage events to the other open lineage components.
     */
    RECEIVE_OL_EVENTS(EgeriaSolutionComponent.OL_KAFKA_TOPIC.getGUID(),
                      IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getSolutionComponentGUID(),
                      "receives events",
                      "Receive and push open lineage events to the other open lineage components.",
                      ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Passes events to the file publisher.
     */
    PUBLISH_OL_EVENTS_TO_FILE(IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getSolutionComponentGUID(),
                              IntegrationConnectorDefinition.OPEN_LINEAGE_FILE_PUBLISHER.getSolutionComponentGUID(),
                              "distribute events events",
                              "Passes events to the file publisher.",
                              ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Passes events to the API publisher.
     */
    PUBLISH_OL_EVENTS_TO_API(IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getSolutionComponentGUID(),
                             IntegrationConnectorDefinition.OPEN_LINEAGE_API_PUBLISHER.getSolutionComponentGUID(),
                             "distribute events events",
                             "Passes events to the API publisher.",
                             ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Passes events to the Catalouger.
     */
    PUBLISH_OL_EVENTS_TO_CATALOGUER(IntegrationConnectorDefinition.OPEN_LINEAGE_KAFKA_LISTENER.getSolutionComponentGUID(),
                             IntegrationConnectorDefinition.OPEN_LINEAGE_CATALOGUER.getSolutionComponentGUID(),
                             "distribute events events",
                             "Passes events to the open metadata cataloguer.",
                             ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Passes Egeria's open lineage events to the file publisher.
     */
    PUBLISH_EGERIA_EVENTS_TO_OL(EgeriaSolutionComponent.OPEN_METADATA_TOPIC.getGUID(),
                                IntegrationConnectorDefinition.OPEN_LINEAGE_GA_PUBLISHER.getSolutionComponentGUID(),
                                "distribute events events",
                                "Passes events to the file publisher.",
                                ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * PostgreSQL is a JDBC database server, and the contents of one of its databases can be catalogued through JDBC.
     */
    JDBC_AND_POSTGRESQL(IntegrationConnectorDefinition.JDBC_CATALOGUER.getSolutionComponentGUID(),
                        PostgresDeployedImplementationType.POSTGRESQL_SERVER.getSolutionComponentGUID(),
                        "works with",
                        "PostgreSQL is a JDBC database server, and the contents of one of its databases can be catalogued through JDBC.",
                        ContentPackDefinition.POSTGRES_CONTENT_PACK),

    ;

    private final String end1GUID;
    private final String end2GUID;
    private final String label;
    private final String description;
    private final ContentPackDefinition contentPackDefinition;


    ContentPackSolutionLinkingWire(String end1GUID, String end2GUID, String label, String description, ContentPackDefinition contentPackDefinition)
    {
        this.end1GUID              = end1GUID;
        this.end2GUID              = end2GUID;
        this.label                 = label;
        this.description           = description;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Return the unique identifier for end 1 of the relationship.
     *
     * @return string
     */
    public String getEnd1GUID()
    {
        return end1GUID;
    }


    /**
     * Return the unique identifier for end 2 of the relationship.
     *
     * @return string
     */
    public String getEnd2GUID()
    {
        return end2GUID;
    }


    /**
     * Return the name of the relationship.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Return the description of the relationship.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Get identifier of content pack where this definition should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionLinkingWireDefinition{name='" + name() + "'}";
    }
}
