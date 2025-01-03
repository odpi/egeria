/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria that
 * are related to the PostgreSQL technology.
 */
public enum PostgresDeployedImplementationType implements DeployedImplementationTypeDefinition
{

    /**
     * A database hosted on a PostgreSQL server.
     */
    POSTGRESQL_DATABASE("PostgreSQL Relational Database",
                        DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                        OpenMetadataType.RELATIONAL_DATABASE.typeName,
                        null,
                        "A database hosted on a PostgreSQL server.",
                        "https://www.postgresql.org/"),


    /**
     * A database schema hosted on a relational PostgreSQL database server capable of being called through a JDBC Driver.
     */
    POSTGRESQL_DATABASE_SCHEMA("PostgreSQL Relational Database Schema",
                               DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                               OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                               null,
                               "A database schema hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.",
                               "https://www.postgresql.org/"),


    /**
     * A database table hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.
     */
    POSTGRESQL_DATABASE_TABLE("PostgreSQL Relational Database Table",
                              DeployedImplementationType.DATA_ASSET,
                              OpenMetadataType.TABLE_DATA_SET.typeName,
                              null,
                              "A database table hosted on a PostgreSQL relational database server capable of being called through a JDBC Driver.",
                              "https://www.postgresql.org/"),


    /**
     * A database server running the PostgreSQL software.
     */
    POSTGRESQL_SERVER("PostgreSQL Server",
                      DeployedImplementationType.SOFTWARE_SERVER,
                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                      OpenMetadataType.DATABASE_SERVER_CLASSIFICATION.typeName,
                      "A database server running the PostgreSQL software.",
                      "https://www.postgresql.org/"),

    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    POSTGRESQL_DATABASE_MANAGER("PostgreSQL database manager (RDBMS)",
                                DeployedImplementationType.RELATIONAL_DATABASE_MANAGER,
                                OpenMetadataType.DATABASE_MANAGER.typeName,
                                null,
                                "The PostgreSQL capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),


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
    PostgresDeployedImplementationType(String                               deployedImplementationType,
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
     * Return the category for this deployed implementation type.
     *
     * @return string
     */
    @Override
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
