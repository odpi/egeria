/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria that
 * are related to the DuckDB technology.  DuckDB is an embedded/in-process OLAP database engine - there is no
 * server tier (a DuckDB "database" is either a single file on disk or an in-memory session opened directly
 * over JDBC), so unlike the other relational database technologies supported by Egeria, there is no
 * DUCKDB_SERVER entry in this enum.
 */
public enum DuckDBDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * An embedded/file-based database managed by the DuckDB engine.
     */
    DUCKDB_DATABASE("07e91e40-adce-4c66-b34d-9d00cba1c783",
                    "DuckDB Relational Database",
                    DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                    OpenMetadataType.RELATIONAL_DATABASE.typeName,
                    null,
                    "An embedded, file-based (or in-memory) database managed by the DuckDB engine and accessed directly through its JDBC driver.",
                    "https://duckdb.org/",
                    "761ff3e0-d96c-4ab9-9d33-0b124256cc87",
                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                    "DUCKDB-DATABASE"),


    /**
     * A database schema hosted on a relational DuckDB database capable of being called through a JDBC Driver.
     */
    DUCKDB_DATABASE_SCHEMA("8bc4b109-ce2e-424a-9730-7142327799a5",
                           "DuckDB Relational Database Schema",
                           DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                           OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                           null,
                           "A database schema hosted on a relational DuckDB database capable of being called through a JDBC Driver.",
                           "https://duckdb.org/",
                           "7eae03bd-fab0-4b0e-9200-48365aebd6b8",
                           SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                           "DUCKDB-DATABASE-SCHEMA"),

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
            for (DuckDBDeployedImplementationType definition : DuckDBDeployedImplementationType.values())
            {
                if (definition.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    return definition;
                }
            }
        }

        return null;
    }


    /**
     * Return a list of definitions for this set of deployed implementation types.
     *
     * @return array of definitions
     */
    public static DeployedImplementationTypeDefinition[] getDefinitions()
    {
        DeployedImplementationTypeDefinition[] definitions = new DeployedImplementationTypeDefinition[values().length];

        for (DuckDBDeployedImplementationType definition : DuckDBDeployedImplementationType.values())
        {
            definitions[definition.ordinal()] = definition;
        }

        return definitions;
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
    DuckDBDeployedImplementationType(String                               guid,
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
