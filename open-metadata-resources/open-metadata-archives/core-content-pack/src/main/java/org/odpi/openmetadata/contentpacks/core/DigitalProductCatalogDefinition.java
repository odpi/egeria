/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Describes the integration groups in the core content packs.
 */
public enum DigitalProductCatalogDefinition
{
    ROOT("dcec6ddb-317e-4c64-907e-be508ceba6d9",
         OpenMetadataType.ROOT_COLLECTION.typeName,
         "Egeria::DigitalProductCatalogsRoot",
         "Digital Product Catalogs Root",
         "Root collection linking the digital product catalogs together.",
         null,
         null,
         ContentPackDefinition.CORE_CONTENT_PACK),


    ;

    private final String                          guid;
    private final String                          typeName;
    private final String                          qualifiedName;
    private final String                          name;
    private final String                          description;
    private final String                          anchorScopeGUID;
    private final ContentPackDefinition           contentPackDefinition;
    private final DigitalProductCatalogDefinition parent;


    DigitalProductCatalogDefinition(String guid, String typeName, String qualifiedName, String name, String description, DigitalProductCatalogDefinition parent, String anchorScopeGUID, ContentPackDefinition contentPackDefinition)
    {
        this.guid                  = guid;
        this.typeName              = typeName;
        this.qualifiedName         = qualifiedName;
        this.name                  = name;
        this.description           = description;
        this.parent                = parent;
        this.anchorScopeGUID       = anchorScopeGUID;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Return the unique identifier for the integration group.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the type name to use.
     *
     * @return string
     */
    public String getTypeName()
    {
        return typeName;
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

    public DigitalProductCatalogDefinition getParent()
    {
        return parent;
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
     * Get the anchorScopeGUID.
     *
     * @return string
     */
    public String getAnchorScopeGUID()
    {
        return anchorScopeGUID;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DigitalProductCatalogDefinition{name='" + name() + "'}";
    }
}
