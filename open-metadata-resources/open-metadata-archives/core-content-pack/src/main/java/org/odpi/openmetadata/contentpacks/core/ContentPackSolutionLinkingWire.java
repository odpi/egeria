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
     * Initializes the karma point awarding process.
     */
    BABBAGE_KARMA(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getSolutionComponentGUID(),
                  RequestTypeDefinition.AWARD_KARMA_POINTS.getSolutionComponentGUID(),
                  "schedules",
                  "Initializes the karma point awarding process.",
                  ContentPackDefinition.ORGANIZATION_INSIGHT_CONTENT_PACK),

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
