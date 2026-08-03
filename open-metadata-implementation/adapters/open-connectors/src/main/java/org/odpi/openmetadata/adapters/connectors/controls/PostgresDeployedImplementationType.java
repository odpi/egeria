/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria that
 * are related to the PostgreSQL technology.
 */
public enum PostgresDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A database hosted on a PostgreSQL server.
     */
    POSTGRESQL_DATABASE("ff56fc56-c4a1-469e-8040-472e8fe54694",
                        "PostgreSQL Relational Database",
                        DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                        OpenMetadataType.RELATIONAL_DATABASE.typeName,
                        null,
                        "A database hosted on a PostgreSQL server.",
                        "https://www.postgresql.org/",
                        "d14e713e-ef5b-4a6b-8495-d6f298cfd149",
                        SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                        "POSTGRESQL-DATABASE"),


    /**
     * A database schema hosted on a relational PostgreSQL database server capable of being called through a JDBC Driver.
     */
    POSTGRESQL_DATABASE_SCHEMA("e46811ac-8b86-42c3-aaea-2fe55f474044",
                               "PostgreSQL Relational Database Schema",
                               DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                               OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                               null,
                               "A database schema hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.",
                               "https://www.postgresql.org/",
                               "c6d87bb4-a663-4e6c-a009-537cd15fb763",
                               SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                               "POSTGRESQL-DATABASE-SCHEMA"),


    /**
     * A database table hosted on a PostgreSQL relational database server that has the tabular data set interface.
     */
    POSTGRESQL_TABULAR_DATA_SET("aa50ce4c-917d-4880-a987-d6c771b822b2",
                                "PostgreSQL Tabular Data Set",
                                DeployedImplementationType.TABULAR_DATA_SET,
                                OpenMetadataType.TABULAR_DATA_SET.typeName,
                                null,
                                "A database table hosted on a PostgreSQL relational database server that has the tabular data set interface.",
                                "https://www.postgresql.org/",
                                "46a31804-127c-4416-941e-12b55cb9a224",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                "POSTGRESQL-TABULAR-DATA-SET"),

    /**
     * A database schema hosted on a PostgreSQL relational database server that has the tabular data set collection interface.
     */
    POSTGRESQL_TABULAR_DATA_SET_COLLECTION("c113b127-ae8d-417f-90f2-92108c141270",
                                           "PostgreSQL Tabular Data Set Collection",
                                           DeployedImplementationType.TABULAR_DATA_SET_COLLECTION,
                                           OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                                           null,
                                           "A database schema hosted on a PostgreSQL relational database server that has the tabular data set collection interface.",
                                           "https://www.postgresql.org/",
                                           "89a6c506-862d-4fa8-9a3e-a1d6ee44f23b",
                                           SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                           "POSTGRESQL-TABULAR-DATA-SET-COLLECTION"),

    /**
     * A database server running the PostgreSQL software.
     */
    POSTGRESQL_SERVER("ff7c8d09-9813-453e-9895-7a2b0a6b0be3",
                      "PostgreSQL Server",
                      DeployedImplementationType.DATABASE_SERVER,
                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                      null,
                      "A database server running the PostgreSQL software. PostgreSQL is an advanced open source relational database.PostgreSQL is an advanced open source relational database.",
                      "https://www.postgresql.org/",
                      "b2a1f014-d00e-4956-bbad-0ae1d5498841",
                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                      "POSTGRESQL-SERVER"),

    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    POSTGRESQL_DATABASE_MANAGER("cdfedb9c-8020-422d-b89e-40c57df22bd0",
                                "PostgreSQL database manager (RDBMS)",
                                DeployedImplementationType.RELATIONAL_DATABASE_MANAGER,
                                OpenMetadataType.DATABASE_MANAGER.typeName,
                                null,
                                "The PostgreSQL capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                                "ed8eaba6-55d7-4f41-99e3-139b581492cb",
                                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                                "POSTGRESQL-DATABASE-MANAGER"),

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
            for (PostgresDeployedImplementationType definition : PostgresDeployedImplementationType.values())
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

        for (PostgresDeployedImplementationType definition : PostgresDeployedImplementationType.values())
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
    PostgresDeployedImplementationType(String                               guid,
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
