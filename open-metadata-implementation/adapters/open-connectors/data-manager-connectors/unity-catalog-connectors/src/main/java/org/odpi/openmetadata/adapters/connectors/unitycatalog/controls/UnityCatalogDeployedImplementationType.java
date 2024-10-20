/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
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
    OSS_UC_VOLUME("Unity Catalog Volume",
                  DeployedImplementationType.DATA_FOLDER,
                  OpenMetadataType.DATA_FOLDER.typeName,
                  null,
                  "A collection of related data files within the Unity Catalog (UC) 'catalog of catalogs'.",
                  "https://github.com/unitycatalog/unitycatalog"),

    /**
     * An analytics model deployed to Unity Catalog (UC).
     */
    OSS_UC_REGISTERED_MODEL("Unity Catalog Registered Model",
                  DeployedImplementationType.SOFTWARE_COMPONENT,
                  OpenMetadataType.DEPLOYED_ANALYTICS_MODEL.typeName,
                  null,
                  "An analytics model deployed to Unity Catalog (UC).",
                  "https://github.com/unitycatalog/unitycatalog"),


    /**
     * A version of an analytics model deployed to Unity Catalog (UC).
     */
    OSS_UC_REGISTERED_MODEL_VERSION("Unity Catalog Registered Model Version",
                            DeployedImplementationType.SOFTWARE_COMPONENT,
                            OpenMetadataType.ANALYTICS_MODEL_RUN.typeName,
                            null,
                            "A version of an analytics model deployed to Unity Catalog (UC).",
                            "https://github.com/unitycatalog/unitycatalog"),


    /**
     * A function found in Unity Catalog (UC) that is working with data.
     */
    OSS_UC_FUNCTION("Unity Catalog Function",
                  null,
                  OpenMetadataType.DEPLOYED_API.typeName,
                  null,
                  "A function found in Unity Catalog (UC) that is working with data.",
                  "https://github.com/unitycatalog/unitycatalog"),

    /**
     * A relational table within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_TABLE("Unity Catalog Table",
                 DeployedImplementationType.DATA_ASSET,
                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName,
                 null,
                 "A relational table within the Unity Catalog (UC) 'catalog of catalogs'.",
                 "https://github.com/unitycatalog/unitycatalog"),


    /**
     * A schema that organizes data assets for an operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_SCHEMA("Unity Catalog Schema",
                  DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                  OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                  null,
                  "A schema that organizes data assets for an operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.",
                  "https://github.com/unitycatalog/unitycatalog"),

    /**
     * An operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_CATALOG("Unity Catalog Catalog",
                   DeployedImplementationType.ASSET_CATALOG,
                   OpenMetadataType.CATALOG.typeName,
                   OpenMetadataType.ASSET_MANAGER.typeName,
                   "An operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.",
                   "https://github.com/unitycatalog/unitycatalog"),

    /**
     * An operational metadata catalog that supports controlled access to data managed through a data platform.
     */
    OSS_UNITY_CATALOG_SERVER("Unity Catalog Server",
                             DeployedImplementationType.SOFTWARE_SERVER,
                             OpenMetadataType.SOFTWARE_SERVER.typeName,
                             OpenMetadataType.METADATA_SERVER_CLASSIFICATION.typeName,
                             "The OSS Unity Catalog (UC) Server is an operational data platform 'catalog of catalogs' that supports controlled access to data managed through a related data platforms.",
                             "https://github.com/unitycatalog/unitycatalog"),


    /**
     * An operational metadata catalog that supports controlled access to data managed through a data platform.
     */
    DB_UNITY_CATALOG_SERVER("Databricks Unity Catalog Server",
                             OSS_UNITY_CATALOG_SERVER,
                             OpenMetadataType.SOFTWARE_SERVER.typeName,
                             OpenMetadataType.METADATA_SERVER_CLASSIFICATION.typeName,
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


    private final String                               deployedImplementationType;
    private final DeployedImplementationTypeDefinition isATypeOf;
    private final String                               associatedTypeName;
    private final String                               associatedClassification;
    private final String                               description;
    private final String                               wikiLink;


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
    UnityCatalogDeployedImplementationType(String                               deployedImplementationType,
                                           DeployedImplementationTypeDefinition isATypeOf,
                                           String                               associatedTypeName,
                                           String                               associatedClassification,
                                           String                               description,
                                           String                               wikiLink)
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
    public DeployedImplementationTypeDefinition getIsATypeOf()
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
