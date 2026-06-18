/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ContentCollectionDefinition describes the different folders that are used to organize various definitions for
 * Egeria.
 */
public enum ContentCollectionDefinition
{
    /**
     * Egeria Design Library
     */
    EGERIA_DESIGN_LIBRARY("7e591a63-93a5-420d-9bbf-54f2a6c2ccb4",
                          OpenMetadataType.ROOT_COLLECTION.typeName,
                          null,
                          null,
                          "Egeria Design Library",
                          "Definitions describing how Egeria is designed and configured.",
                          null,
                          ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Egeria Solutions
     */
    EGERIA_SOLUTIONS("276c74bf-2d3f-4e23-a46b-8b12007e4213",
                          OpenMetadataType.COLLECTION_FOLDER.typeName,
                          null,
                          EGERIA_DESIGN_LIBRARY,
                          "Egeria Solutions",
                          "Solution Blueprints describing how Egeria is designed and configured.",
                          null,
                          ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Egeria Supply Chains
     */
    EGERIA_SUPPLY_CHAINS("db88d140-7cc2-4077-ab4f-ae5e3858480b",
                     OpenMetadataType.COLLECTION_FOLDER.typeName,
                     null,
                     EGERIA_SOLUTIONS,
                     "Egeria Supply Chains",
                     "Information Supply chains showing how open metadata flows between tools and Egeria components.",
                     null,
                     ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Content Pack Contents
     */
    CONTENT_PACK_CONTENTS("e60a8ca2-7013-44d8-9824-03163988640d",
                         OpenMetadataType.COLLECTION_FOLDER.typeName,
                         null,
                         EGERIA_DESIGN_LIBRARY,
                         "Content Pack Contents",
                         "Descriptions of the content packs that are part of Egeria.",
                         null,
                          ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Integration Groups
     */
    INTEGRATION_GROUPS("6566f455-1293-41c6-8913-0c3b06afd6eb",
                          OpenMetadataType.COLLECTION_FOLDER.typeName,
                          null,
                          EGERIA_DESIGN_LIBRARY,
                          "Integration Groups",
                          "The integration groups describe collections of integration connectors that are configured ready to run in an integration daemon.",
                          null,
                          ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Governance Engines
     */
    GOVERNANCE_ENGINES("75379192-f995-4eb2-a6e2-272617abeead",
                       OpenMetadataType.COLLECTION_FOLDER.typeName,
                       null,
                       EGERIA_DESIGN_LIBRARY,
                       "Governance Engines",
                       "The governance engines describe collections of governance services that are configured ready to run in an engine host.",
                       null,
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Open Connectors
     */
    OPEN_CONNECTORS("af3a2d66-93a7-4777-b34b-eaffc036e370",
                       OpenMetadataType.COLLECTION_FOLDER.typeName,
                       null,
                       EGERIA_DESIGN_LIBRARY,
                       "Open Connectors",
                       "List of connectors supplied with Egeria.",
                       null,
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Open Connectors
     */
    LOVELACE_SERVICES("2da7b0dc-9848-402e-82ca-148a88bed6cb",
                    OpenMetadataType.COLLECTION_FOLDER.typeName,
                    null,
                    EGERIA_DESIGN_LIBRARY,
                    "Analytical Services",
                    "List of analytical services supplied with Egeria.  They are called Lovelace Services and they are registered with the Babbage Analytical engine.",
                    null,
                    ContentPackDefinition.CORE_CONTENT_PACK),

    ;


    private final String                      guid;
    private final String                      typeName;
    private final String                      classificationName;
    private final ContentCollectionDefinition parent;
    private final String                      displayName;
    private final String                      description;
    private final String                      category;
    private final ContentPackDefinition           contentPackDefinition;


    /**
     * Constructor for enum value.
     *
     * @param guid unique identifier
     * @param typeName type of folder
     * @param classificationName optional classification
     * @param parent optional parent folder
     * @param displayName display name
     * @param description description
     * @param category category
     */
    ContentCollectionDefinition(String                   guid,
                                String                   typeName,
                                String                   classificationName,
                                ContentCollectionDefinition parent,
                                String                   displayName,
                                String                   description,
                                String                   category,
                                ContentPackDefinition contentPackDefinition)
    {
        this.guid               = guid;
        this.typeName           = typeName;
        this.classificationName = classificationName;
        this.parent             = parent;
        this.displayName        = displayName;
        this.description        = description;
        this.category           = category;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Returns the unique identifier for the collection entity.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }

    /**
     * Returns the unique name for the collection entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return typeName + "::Coco::" + displayName;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Return the entry of the parent folder - null for top level.
     *
     * @return enum
     */
    public ContentCollectionDefinition getParent()
    {
        return parent;
    }


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns an optional category for the collection.
     *
     * @return string
     */
    public String getCategory()
    {
        return category;
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
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "ContentCollectionDefinition{" +
                "typeName='" + typeName + '\'' +
                ", classificationName='" + classificationName + '\'' +
                ", parent=" + parent +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                "} " + super.toString();
    }
}
