/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
                  "https://github.com/unitycatalog/unitycatalog",
                  "d02a65ad-a0a3-4fec-ade3-2050bf04d0e4",
                  SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                  "UNITY-CATALOG-VOLUME"),

    /**
     * An analytics model deployed to Unity Catalog (UC).
     */
    OSS_UC_REGISTERED_MODEL("2a3bed81-64bd-449d-a498-ebc4baa6cbf7",
                            "Unity Catalog Registered Model",
                            DeployedImplementationType.SOFTWARE_COMPONENT,
                            OpenMetadataType.DEPLOYED_ANALYTICS_MODEL.typeName,
                            null,
                            "An analytics model deployed to Unity Catalog (UC).",
                            "https://github.com/unitycatalog/unitycatalog",
                            "aa001263-3f98-4cb6-8169-4431bc4760ee",
                            SolutionComponentType.INSIGHT_MODEL.getSolutionComponentType(),
                            "UNITY-CATALOG-REGISTERED-MODEL"),

    /**
     * A version of an analytics model deployed to Unity Catalog (UC).
     */
    OSS_UC_REGISTERED_MODEL_VERSION("9f0c7636-9ce4-44a0-a944-5e6fda006ce4",
                                    "Unity Catalog Registered Model Version",
                                    DeployedImplementationType.SOFTWARE_COMPONENT,
                                    OpenMetadataType.ANALYTICS_MODEL_RUN.typeName,
                                    null,
                                    "A version of an analytics model deployed to Unity Catalog (UC).",
                                    "https://github.com/unitycatalog/unitycatalog",
                                    "95c593dc-848b-462a-a0de-9680645b5306",
                                    SolutionComponentType.INSIGHT_MODEL.getSolutionComponentType(),
                                    "UNITY-CATALOG-REGISTERED-MODEL-VERSION"),

    /**
     * A function found in Unity Catalog (UC) that is working with data.
     */
    OSS_UC_FUNCTION("963d62f5-b902-460d-ac27-eb756853646b",
                    "Unity Catalog Function",
                    DeployedImplementationType.SOFTWARE_COMPONENT,
                    OpenMetadataType.DEPLOYED_API.typeName,
                    null,
                    "A function found in Unity Catalog (UC) that is working with data.",
                    "https://github.com/unitycatalog/unitycatalog",
                    "50fb626b-e0b3-4048-87db-1abcb0d46041",
                    SolutionComponentType.AUTOMATED_ACTION.getSolutionComponentType(),
                    "UNITY-CATALOG-FUNCTION"),

    /**
     * A relational table within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_TABLE("23c2982a-2152-47c1-82d7-1de467545829",
                 "Unity Catalog Table",
                 DeployedImplementationType.DATA_ASSET,
                 OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName,
                 null,
                 "A relational table within the Unity Catalog (UC) 'catalog of catalogs'.",
                 "https://github.com/unitycatalog/unitycatalog",
                 "6d90e382-c527-46e2-9d59-60a2343d21d0",
                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                 "UNITY-CATALOG-TABLE"),


    /**
     * A schema that organizes data assets for an operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_SCHEMA("51331e2a-3b29-460c-8fd6-f6a258cc9e3a",
                  "Unity Catalog Schema",
                  DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                  OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                  null,
                  "A schema that organizes data assets for an operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.",
                  "https://github.com/unitycatalog/unitycatalog",
                  "2bd1a0da-11ff-4197-a585-3d1811cb915d",
                  SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                  "UNITY-CATALOG-SCHEMA"),

    /**
     * An operational data platform manager within the Unity Catalog (UC) 'catalog of catalogs'.
     */
    OSS_UC_CATALOG("3175a489-d2ce-4cbf-a971-e39a8d7192fe",
                   "Unity Catalog Catalog",
                   null,
                   OpenMetadataType.NAMESPACE_COLLECTION.typeName,
                   null,
                   "An operational data platform catalog within the Unity Catalog (UC) 'catalog of catalogs'.",
                   "https://github.com/unitycatalog/unitycatalog",
                   "275036b5-19fa-43d6-8ba1-a66f3880a3c2",
                   SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                   "UNITY-CATALOG-CATALOG"),

    /**
     * An operational metadata catalog that supports controlled access to data managed through a data platform.
     */
    OSS_UNITY_CATALOG_SERVER("755dc072-81c4-4b55-a51a-16f6f7f74e6f",
                             "Unity Catalog Server",
                             DeployedImplementationType.METADATA_SERVER,
                             OpenMetadataType.SOFTWARE_SERVER.typeName,
                             null,
                             "The OSS Unity Catalog (UC) Server is an operational data platform 'catalog of catalogs' that supports controlled access to data managed through a related data platforms.",
                             "https://github.com/unitycatalog/unitycatalog",
                             "e9d51678-4377-41cb-8415-959886530933",
                             SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                             "UNITY-CATALOG-SERVER"),


    /**
     * An operational metadata catalog that supports controlled access to data managed through a data platform.
     */
    DB_UNITY_CATALOG_SERVER("93657be6-acab-4711-a4eb-549b82dc7c86",
                            "Databricks Unity Catalog Server",
                            OSS_UNITY_CATALOG_SERVER,
                            OpenMetadataType.SOFTWARE_SERVER.typeName,
                            null,
                            "The Databricks Unity Catalog (UC) Server is an operational data platform 'catalog of catalogs' that supports controlled access to data managed through a related data platforms.",
                            "https://github.com/unitycatalog/unitycatalog",
                            "f9258f64-4423-4528-8da9-71df263a06bf",
                            SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                            "UNITY-CATALOG-SERVER"),

    ;


    /**
     * Return the matching ENUM for the full definition for the deployed implementation type.
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
    private final String                               solutionComponentGUID;
    private final String                               solutionComponentType;
    private final String                               solutionComponentIdentifier;


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
     * @param solutionComponentGUID unique identifier of the solution component that this deployed implementation type is associated with (optional)
     * @param solutionComponentType type of the solution component that this deployed implementation type is associated with (optional)
     * @param solutionComponentIdentifier  identifier of the solution component that this deployed implementation type is associated with (optional)
     */
    UnityCatalogDeployedImplementationType(String                               guid,
                                           String                               deployedImplementationType,
                                           DeployedImplementationTypeDefinition isATypeOf,
                                           String                               associatedTypeName,
                                           String                               associatedClassification,
                                           String                               description,
                                           String                               wikiLink,
                                           String                               solutionComponentGUID,
                                           String                               solutionComponentType,
                                           String                               solutionComponentIdentifier)
    {
        this.guid = guid;
        this.deployedImplementationType = deployedImplementationType;
        this.isATypeOf = isATypeOf;
        this.associatedTypeName = associatedTypeName;
        this.associatedClassification = associatedClassification;
        this.description = description;
        this.wikiLink = wikiLink;
        this.solutionComponentGUID = solutionComponentGUID;
        this.solutionComponentType = solutionComponentType;
        this.solutionComponentIdentifier = solutionComponentIdentifier;
    }


    /**
     * Return a list of GUIDs for this set of deployed implementation types.
     *
     * @return array of guids
     */
    public static String[] getGUIDs()
    {
        String[] guids = new String[values().length];

        for (UnityCatalogDeployedImplementationType definition : UnityCatalogDeployedImplementationType.values())
        {
            guids[definition.ordinal()] = definition.getGUID();
        }

        return guids;
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
     * Return the optional unique identifier of the solution component that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentGUID()
    {
        return solutionComponentGUID;
    }


    /**
     * Return the solution component type that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentType()
    {
        return solutionComponentType;
    }


    /**
     * Return the solution component identifier that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentIdentifier()
    {
        return solutionComponentIdentifier;
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
