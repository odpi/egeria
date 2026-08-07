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
 * are related to the Oracle Database technology.
 */
public enum OracleDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A pluggable database hosted on an Oracle Database Server.
     */
    ORACLE_DATABASE("301bd39f-8b41-46d5-a769-25162145c235",
                    "Oracle Relational Database",
                    DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                    OpenMetadataType.RELATIONAL_DATABASE.typeName,
                    null,
                    "A pluggable database (PDB) hosted on an Oracle Database Server.",
                    "https://www.oracle.com/database/",
                    "2aed7bba-5da8-4560-93fe-f31c9df66e10",
                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                    "ORACLE-DATABASE"),


    /**
     * A database schema (Oracle user) hosted on a relational Oracle database capable of being called through a JDBC Driver.
     */
    ORACLE_DATABASE_SCHEMA("aee0f055-854e-43ee-9fad-a228a58d2365",
                           "Oracle Relational Database Schema",
                           DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                           OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                           null,
                           "A database schema (Oracle user) hosted on a relational Oracle database capable of being called through a JDBC Driver.",
                           "https://www.oracle.com/database/",
                           "9eaf77b7-247a-42aa-ac2e-512f83fec7c5",
                           SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                           "ORACLE-DATABASE-SCHEMA"),


    /**
     * A database server running the Oracle Database software.
     */
    ORACLE_SERVER("f98b41b9-9c37-4913-a17f-35935034505f",
                 "Oracle Database Server",
                 DeployedImplementationType.DATABASE_SERVER,
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 null,
                 "A database server running the Oracle Database software. Oracle Database is a multi-model relational database management system.",
                 "https://www.oracle.com/database/",
                 "8044fce0-bb2d-46c8-8b45-08fa124ff130",
                 SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                 "ORACLE-SERVER"),

    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    ORACLE_DATABASE_MANAGER("cf330c9a-ba3c-4b8d-a7e3-dea13f912538",
                            "Oracle database manager (RDBMS)",
                            DeployedImplementationType.RELATIONAL_DATABASE_MANAGER,
                            OpenMetadataType.DATABASE_MANAGER.typeName,
                            null,
                            "The Oracle Database capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                            OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                            "2b503ad7-d72b-4c9a-95b3-895d88341d95",
                            SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                            "ORACLE-DATABASE-MANAGER"),

    /**
     * A database table hosted on an Oracle Database that has the tabular data set interface.
     */
    ORACLE_TABULAR_DATA_SET("27d9e17e-4e0e-4f33-bbea-1af94b46e3af",
                            "Oracle Tabular Data Set",
                            DeployedImplementationType.TABULAR_DATA_SET,
                            OpenMetadataType.TABULAR_DATA_SET.typeName,
                            null,
                            "A database table hosted on an Oracle Database that has the tabular data set interface.",
                            "https://www.oracle.com/database/",
                            "3349f9a1-c183-4149-b4a3-36cb1e5caaad",
                            SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                            "ORACLE-TABULAR-DATA-SET"),

    /**
     * A database schema hosted on an Oracle Database that has the tabular data set collection interface.
     */
    ORACLE_TABULAR_DATA_SET_COLLECTION("5f5da711-1ee8-403a-ad64-1114bb96f475",
                                       "Oracle Tabular Data Set Collection",
                                       DeployedImplementationType.TABULAR_DATA_SET_COLLECTION,
                                       OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                                       null,
                                       "A database schema hosted on an Oracle Database that has the tabular data set collection interface.",
                                       "https://www.oracle.com/database/",
                                       "ad9c2d0a-d1fc-478b-82a7-9c9c1e29fbb9",
                                       SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                       "ORACLE-TABULAR-DATA-SET-COLLECTION"),

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
            for (OracleDeployedImplementationType definition : OracleDeployedImplementationType.values())
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

        for (OracleDeployedImplementationType definition : OracleDeployedImplementationType.values())
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
    OracleDeployedImplementationType(String                               guid,
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
