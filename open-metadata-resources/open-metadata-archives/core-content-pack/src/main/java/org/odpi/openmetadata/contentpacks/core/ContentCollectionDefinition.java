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
     * Analytical Services
     */
    LOVELACE_SERVICES("2da7b0dc-9848-402e-82ca-148a88bed6cb",
                    OpenMetadataType.COLLECTION_FOLDER.typeName,
                    null,
                    EGERIA_DESIGN_LIBRARY,
                    "Analytical Services",
                    "List of analytical services supplied with Egeria.  They are called Lovelace Services and they are registered with the Babbage Analytical engine.",
                    null,
                    ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Governance Actions
     */
    GOVERNANCE_ACTIONS("54e12d1c-0648-475b-9ced-972040a16e8c",
                       OpenMetadataType.COLLECTION_FOLDER.typeName,
                       null,
                       EGERIA_DESIGN_LIBRARY,
                       "Governance Actions",
                       "These are the reusable governance actions.  They can be executed as a single action, or used as a template for a step in a governance action process.",
                       null,
                       ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Watchdog Actions
     */
    WATCHDOG_ACTIONS("6f5f9877-0d74-4437-ae84-c6f6e7eb409f",
                      OpenMetadataType.COLLECTION_FOLDER.typeName,
                      null,
                     GOVERNANCE_ACTIONS,
                      "Watchdog Actions",
                      "These are the governance actions that listen for events that trigger other governance actions.",
                      null,
                      ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * File Management Actions
     */
    FILE_ACTIONS("c318c044-29d9-4a84-8d52-37463ba5e1e7",
                     OpenMetadataType.COLLECTION_FOLDER.typeName,
                     null,
                     GOVERNANCE_ACTIONS,
                     "File Management Actions",
                     "These are the governance actions that work with files and directories.",
                     null,
                     ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Analytical Actions
     */
    ANALYTICAL_ACTIONS("68990afe-b17a-4acb-a2c2-df3eca46e1a1",
                 OpenMetadataType.COLLECTION_FOLDER.typeName,
                 null,
                 GOVERNANCE_ACTIONS,
                 "Analytical Actions",
                 "These are the governance actions that scan the contents of the open metadata repositories and create analytical classifications to summarize the situation they find.",
                 null,
                 ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * Digital Product Catalog Actions
     */
    PRODUCT_CATALOG("3d850612-e261-491d-b6df-5918ee9cf7ed",
                       OpenMetadataType.COLLECTION_FOLDER.typeName,
                       null,
                       GOVERNANCE_ACTIONS,
                       "Digital Product Catalog Actions",
                       "These are the governance actions that support the functions of the Digital Product Catalogs.",
                       null,
                       ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * Stewardship Actions
     */
    STEWARDSHIP("aa741e23-58b0-4232-bae3-263839492eea",
                    OpenMetadataType.COLLECTION_FOLDER.typeName,
                    null,
                    GOVERNANCE_ACTIONS,
                    "Stewardship Actions",
                    "These are the governance actions that classify and link elements to to show how they should be governed.",
                    null,
                    ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * Communication Actions
     */
    COMMUNICATION_ACTIONS("4cf7e747-f069-44d4-b5d3-467cb9d9848a",
                          OpenMetadataType.COLLECTION_FOLDER.typeName,
                          null,
                          GOVERNANCE_ACTIONS,
                          "Communication Actions",
                          "These are the governance actions that help to communicate with people and systems.",
                          null,
                          ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Survey Actions
     */
    SURVEY_ACTIONS("9859ec6f-14eb-4fd1-8683-9677132e64e1",
                   OpenMetadataType.COLLECTION_FOLDER.typeName,
                   null,
                   GOVERNANCE_ACTIONS,
                   "Survey Actions",
                   "These are the governance actions that conduct surveys on digital resources.",
                   null,
                   ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Catalog Actions
     */
    CATALOG_ACTIONS("6160e663-cbb7-4bd1-b075-63e68676bdab",
                   OpenMetadataType.COLLECTION_FOLDER.typeName,
                   null,
                   GOVERNANCE_ACTIONS,
                   "Catalog Actions",
                   "These are the governance actions that catalog digital resources.",
                   null,
                   ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     * Create Asset Actions
     */
    CREATE_ACTIONS("1d597d40-e336-4c18-b198-66527647e23d",
                    OpenMetadataType.COLLECTION_FOLDER.typeName,
                    null,
                    CATALOG_ACTIONS,
                    "Create Asset Actions",
                    "These are the governance actions that create assets to represent digital resources.",
                    null,
                    ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Delete Asset Actions
     */
    DELETE_ACTIONS("c0e983c4-7549-4c41-898e-741f5442b5e1",
                   OpenMetadataType.COLLECTION_FOLDER.typeName,
                   null,
                   CATALOG_ACTIONS,
                   "Delete Asset Actions",
                   "These are the governance actions that delete assets that represent digital resources.",
                   null,
                   ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Catalog Asset Content Actions
     */
    CATALOG_ASSET_CONTENTS("c2eca349-542c-4a6c-a532-afc3391c4760",
                   OpenMetadataType.COLLECTION_FOLDER.typeName,
                   null,
                   CATALOG_ACTIONS,
                   "Catalog Asset Content Actions",
                   "These are the governance actions that catalog the contents of digital resources and link them to the assets that represent them.",
                   null,
                   ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Provision Actions
     */
    PROVISION_ACTIONS("3fc7d511-eab2-45e3-a8a6-c74c3c37dfe5",
                    OpenMetadataType.COLLECTION_FOLDER.typeName,
                    null,
                    GOVERNANCE_ACTIONS,
                    "Provision Actions",
                    "These are the governance actions that provision digital resources into other tools and systems.",
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
