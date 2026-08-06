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
 * are related to the Microsoft SQL Server technology.
 */
public enum MSSQLDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A database hosted on a Microsoft SQL Server.
     */
    MSSQL_DATABASE("1852931a-0118-4c30-867a-49e7c31fbcab",
                   "Microsoft SQL Server Relational Database",
                   DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                   OpenMetadataType.RELATIONAL_DATABASE.typeName,
                   null,
                   "A database hosted on a Microsoft SQL Server.",
                   "https://www.microsoft.com/en-us/sql-server/",
                   "4fb1aa8f-6b82-4256-9041-27415eb9ca9a",
                   SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                   "MSSQL-DATABASE"),


    /**
     * A database schema hosted on a relational Microsoft SQL Server database capable of being called through a JDBC Driver.
     */
    MSSQL_DATABASE_SCHEMA("d1e7f6a9-ce15-4923-ab89-e744ce9a71dc",
                          "Microsoft SQL Server Relational Database Schema",
                          DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                          OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                          null,
                          "A database schema hosted on a Microsoft SQL Server relational database capable of being called through a JDBC Driver.",
                          "https://www.microsoft.com/en-us/sql-server/",
                          "003e2492-6263-4d43-9833-fadb1a45bb23",
                          SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                          "MSSQL-DATABASE-SCHEMA"),


    /**
     * A database server running the Microsoft SQL Server software.
     */
    MSSQL_SERVER("bc5b08f7-14ec-4de7-87cb-902865898642",
                "Microsoft SQL Server",
                DeployedImplementationType.DATABASE_SERVER,
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                null,
                "A database server running the Microsoft SQL Server software. Microsoft SQL Server is a relational database management system.",
                "https://www.microsoft.com/en-us/sql-server/",
                "93f91870-2ca0-4fbe-8f04-2c9659d1f043",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                "MSSQL-SERVER"),

    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    MSSQL_DATABASE_MANAGER("23c40c64-4715-44c0-8877-0b4935e8a416",
                           "Microsoft SQL Server database manager (RDBMS)",
                           DeployedImplementationType.RELATIONAL_DATABASE_MANAGER,
                           OpenMetadataType.DATABASE_MANAGER.typeName,
                           null,
                           "The Microsoft SQL Server capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                           OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                           "e226ace4-8936-4370-88a5-cfbdcb317844",
                           SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                           "MSSQL-DATABASE-MANAGER"),

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
            for (MSSQLDeployedImplementationType definition : MSSQLDeployedImplementationType.values())
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

        for (MSSQLDeployedImplementationType definition : MSSQLDeployedImplementationType.values())
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
    MSSQLDeployedImplementationType(String                               guid,
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
