/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;

/**
 * Describes the integration groups in the core content packs.
 */
public enum IntegrationGroupDefinition
{
    DEFAULT("2648e33f-7501-4789-b524-f025bb9eaa57",
            OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_QUALIFIED_NAME,
            OpenMetadataValidValues.DEFAULT_INTEGRATION_GROUP_NAME,
            "Dynamic integration group to use with an Integration Daemon configuration.",
            ContentPackDefinition.CORE_CONTENT_PACK),

            ;

    private final String guid;
    private final String qualifiedName;
    private final String name;
    private final String description;
    private final ContentPackDefinition contentPackDefinition;


    IntegrationGroupDefinition(String guid, String qualifiedName, String name, String description, ContentPackDefinition contentPackDefinition)
    {
        this.guid                  = guid;
        this.qualifiedName         = qualifiedName;
        this.name                  = name;
        this.description           = description;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Return the unique identifier for the integration group.
     *
     * @return
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the integration group.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the name of the integration group.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the integration group.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupDefinition{name='" + name() + "'}";
    }
}
