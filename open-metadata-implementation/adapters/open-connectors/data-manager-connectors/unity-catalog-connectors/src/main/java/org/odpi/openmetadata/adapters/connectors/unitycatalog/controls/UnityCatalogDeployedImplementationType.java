/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * UnityCatalogDeployedImplementationType describes the standard deployed implementation types supplied with Egeria for
 * the technology surrounding Unity Catalog.  This includes the Open Source and commercial versions.
 */
public enum UnityCatalogDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A collection of related data files within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_VOLUME("2c0867cc-99de-4e29-bab4-675f480a8048",
                  "Unity Catalog Volume",
                  DeployedImplementationType.DATA_FOLDER,
                  OpenMetadataType.DATA_FOLDER.typeName,
                  null,
                  "A collection of related data files within the Unity Catalog (UC) 'catalog of catalogs'.",
                  "https://github.com/unitycatalog/unitycatalog"),

    /**
     * An analytics model deployed to Unity Catalog (UC).
     */
    OSS_UC_REGISTERED_MODEL("2a3bed81-64bd-449d-a498-ebc4baa6cbf7",
                            "Unity Catalog Registered Model",
                            DeployedImplementationType.SOFTWARE_COMPONENT,
                            OpenMetadataType.DEPLOYED_ANALYTICS_MODEL.typeName,
                            null,
                            "An analytics model deployed to Unity Catalog (UC).",
                            "https://github.com/unitycatalog/unitycatalog"),


    /**
     * A version of an analytics model deployed to Unity Catalog (UC).
     */
    OSS_UC_REGISTERED_MODEL_VERSION("9f0c7636-9ce4-44a0-a944-5e6fda006ce4",
                                    "Unity Catalog Registered Model Version",
                                    DeployedImplementationType.SOFTWARE_COMPONENT,
                                    OpenMetadataType.ANALYTICS_MODEL_RUN.typeName,
                                    null,
                                    "A version of an analytics model deployed to Unity Catalog (UC).",
                                    "https://github.com/unitycatalog/unitycatalog"),


    /**
     * A function found in Unity Catalog (UC) that is working with data.
     */
    OSS_UC_FUNCTION("963d62f5-b902-460d-ac27-eb756853646b",
                    "Unity Catalog Function",
                    DeployedImplementationType.SOFTWARE_COMPONENT,
                    OpenMetadataType.DEPLOYED_API.typeName,
                    null,
                    "A function found in Unity Catalog (UC) that is working with data.",
                    "https://github.com/unitycatalog/unitycatalog"),

    /**
     * A relational table within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_TABLE("23c2982a-2152-47c1-82d7-1de467545829",
                 "Unity Catalog Table",
                 DeployedImplementationType.DATA_ASSET,
                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName,
                 null,
                 "A relational table within the Unity Catalog (UC) 'catalog of catalogs'.",
                 "https://github.com/unitycatalog/unitycatalog"),


    /**
     * A schema that organizes data assets for an operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_SCHEMA("51331e2a-3b29-460c-8fd6-f6a258cc9e3a",
                  "Unity Catalog Schema",
                  DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                  OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                  null,
                  "A schema that organizes data assets for an operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.",
                  "https://github.com/unitycatalog/unitycatalog"),

    /**
     * An operational data platform manager within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_CATALOG("3175a489-d2ce-4cbf-a971-e39a8d7192fe",
                   "Unity Catalog Catalog",
                   null,
                   OpenMetadataType.DATABASE.typeName,
                   null,
                   "An operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.",
                   "https://github.com/unitycatalog/unitycatalog"),

    /**
     * An operational metadata catalog that supports controlled access to data managed through a data platform.
     */
    OSS_UNITY_CATALOG_SERVER("755dc072-81c4-4b55-a51a-16f6f7f74e6f",
                             "Unity Catalog Server",
                             DeployedImplementationType.METADATA_SERVER,
                             OpenMetadataType.SOFTWARE_SERVER.typeName,
                             null,
                             "The OSS Unity Catalog (UC) Server is an operational data platform 'catalog of catalogs' that supports controlled access to data managed through a related data platforms.",
                             "https://github.com/unitycatalog/unitycatalog"),


    /**
     * An operational metadata catalog that supports controlled access to data managed through a data platform.
     */
    DB_UNITY_CATALOG_SERVER("93657be6-acab-4711-a4eb-549b82dc7c86",
                            "Databricks Unity Catalog Server",
                            OSS_UNITY_CATALOG_SERVER,
                            OpenMetadataType.SOFTWARE_SERVER.typeName,
                            null,
                            "The Databricks Unity Catalog (UC) Server is an operational data platform 'catalog of catalogs' that supports controlled access to data managed through a related data platforms.",
                            "https://github.com/unitycatalog/unitycatalog"),

    ;


    /**
     * Return the matching ENUM to make use of the full definition for the deployed implementation type.
     *
     * @param deployedImplementationType value to match on
     * @return DeployedImplementationType definition
     */
    public static DeployedImplementationTypeDefinition getDefinitionFromDeployedImplementationType(String deployedImplementationType)
    {
        if (deployedImplementationType != null)
        {
            for (UnityCatalogDeployedImplementationType definition : UnityCatalogDeployedImplementationType.values())
            {
                if (definition.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    return definition;
                }
            }
        }

        return null;
    }


    private final String                               guid;
    private final String                               deployedImplementationType;
    private final DeployedImplementationTypeDefinition isATypeOf;
    private final String                               associatedTypeName;
    private final String                               associatedClassification;
    private final String                               description;
    private final String                               wikiLink;


    /**
     * Constructor for individual enum value.
     *
     * @param guid unique identifier of technology type (deployedImplementationType)
     * @param deployedImplementationType value for deployedImplementationType
     * @param isATypeOf optional deployed implementation type that this type "inherits" from
     * @param associatedTypeName the open metadata type where this value is used
     * @param associatedClassification the open metadata classification where this value is used
     * @param description description of the type
     * @param wikiLink url link to more information (optional)
     */
    UnityCatalogDeployedImplementationType(String                               guid,
                                           String                               deployedImplementationType,
                                           DeployedImplementationTypeDefinition isATypeOf,
                                           String                               associatedTypeName,
                                           String                               associatedClassification,
                                           String                               description,
                                           String                               wikiLink)
    {
        this.guid = guid;
        this.deployedImplementationType = deployedImplementationType;
        this.isATypeOf = isATypeOf;
        this.associatedTypeName = associatedTypeName;
        this.associatedClassification = associatedClassification;
        this.description = description;
        this.wikiLink = wikiLink;
    }



    /**
     * Return the guid for the deployed technology type - can be null.
     *
     * @return string
     */
    @Override
    public String getGUID()
    {
        return guid;
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
