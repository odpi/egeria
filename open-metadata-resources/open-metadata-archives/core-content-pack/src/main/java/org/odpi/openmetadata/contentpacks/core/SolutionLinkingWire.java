/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

/**
 * Describes the additional solution linking wires that can not be derived from standard definition.
 * EG friendship relationships
 */
public enum SolutionLinkingWire
{
    /**
     * Pass responsibility for cataloguing schema, tables and columns.
     */
    POSTGRES_FRIENDSHIP(IntegrationConnectorDefinition.POSTGRES_SERVER_CATALOGUER.getGUID(),
                        IntegrationConnectorDefinition.POSTGRES_DB_CATALOGUER.getGUID(),
                        "handoff",
                        "Pass responsibility for cataloguing schema, tables and columns.",
                        ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * Pass responsibility for cataloguing data assets found in a Unity Catalog (UC) catalog.
     */
    UNITY_CATALOG_FRIENDSHIP(IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getGUID(),
                             IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getGUID(),
                             "handoff",
                             "Pass responsibility for cataloguing data assets found in a Unity Catalog (UC) catalog.",
                             ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),


    ;

    private final String end1GUID;
    private final String end2GUID;
    private final String label;
    private final String description;
    private final ContentPackDefinition contentPackDefinition;


    SolutionLinkingWire(String end1GUID, String end2GUID, String label, String description, ContentPackDefinition contentPackDefinition)
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
