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
 * are related to the IBM Db2 for Linux, UNIX and Windows (LUW) technology - formerly known as Db2 UDB
 * (Universal Database).  This covers the distributed/open-systems implementation of Db2, not Db2 for z/OS.
 */
public enum DB2LUWDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A database hosted on an IBM Db2 for Linux, UNIX and Windows Server.
     */
    DB2LUW_DATABASE("5c664f10-5c6e-4787-b140-19b9a192596c",
                    "Db2 for Linux, UNIX and Windows Relational Database",
                    DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                    OpenMetadataType.RELATIONAL_DATABASE.typeName,
                    null,
                    "A database hosted on an IBM Db2 for Linux, UNIX and Windows (LUW) Server.",
                    "https://www.ibm.com/products/db2",
                    "8f8324f3-057e-4d4e-814e-394af707de65",
                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                    "DB2LUW-DATABASE"),


    /**
     * A database schema hosted on a relational Db2 for Linux, UNIX and Windows database capable of being called through a JDBC Driver.
     */
    DB2LUW_DATABASE_SCHEMA("fa984132-5b44-4e23-aeca-33ad52671465",
                           "Db2 for Linux, UNIX and Windows Relational Database Schema",
                           DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA,
                           OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                           null,
                           "A database schema hosted on a relational Db2 for Linux, UNIX and Windows database capable of being called through a JDBC Driver.",
                           "https://www.ibm.com/products/db2",
                           "d0a72946-be8c-4b9c-af96-a57e538fa9f2",
                           SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                           "DB2LUW-DATABASE-SCHEMA"),


    /**
     * A database server running IBM Db2 for Linux, UNIX and Windows software.
     */
    DB2LUW_SERVER("74ade7ad-a333-41ea-8af0-3da43a41c4ab",
                 "Db2 for Linux, UNIX and Windows Database Server",
                 DeployedImplementationType.DATABASE_SERVER,
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 null,
                 "A database server running IBM Db2 for Linux, UNIX and Windows (formerly Db2 UDB) software.  Db2 for LUW is a multi-model relational database management system.",
                 "https://www.ibm.com/products/db2",
                 "35505dcc-15a8-46e3-8435-5f1e76473570",
                 SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                 "DB2LUW-SERVER"),

    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    DB2LUW_DATABASE_MANAGER("cbc02cdb-4fa6-438e-83ba-066c58848c6f",
                            "Db2 for Linux, UNIX and Windows database manager (RDBMS)",
                            DeployedImplementationType.RELATIONAL_DATABASE_MANAGER,
                            OpenMetadataType.DATABASE_MANAGER.typeName,
                            null,
                            "The IBM Db2 for Linux, UNIX and Windows capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                            OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                            "e3609051-fdbf-420c-95de-9f1280301fa2",
                            SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                            "DB2LUW-DATABASE-MANAGER"),

    /**
     * A database table hosted on an IBM Db2 for Linux, UNIX and Windows database that has the tabular data set interface.
     */
    DB2LUW_TABULAR_DATA_SET("1d826989-51c7-4e5f-ace3-326c9d71166d",
                            "Db2 for Linux, UNIX and Windows Tabular Data Set",
                            DeployedImplementationType.TABULAR_DATA_SET,
                            OpenMetadataType.TABULAR_DATA_SET.typeName,
                            null,
                            "A database table hosted on an IBM Db2 for Linux, UNIX and Windows database that has the tabular data set interface.",
                            "https://www.ibm.com/products/db2",
                            "762ec602-a0b7-4692-8042-03422126400e",
                            SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                            "DB2LUW-TABULAR-DATA-SET"),

    /**
     * A database schema hosted on an IBM Db2 for Linux, UNIX and Windows database that has the tabular data set collection interface.
     */
    DB2LUW_TABULAR_DATA_SET_COLLECTION("46fb8d8b-059c-4006-9909-d8643f0f1425",
                                       "Db2 for Linux, UNIX and Windows Tabular Data Set Collection",
                                       DeployedImplementationType.TABULAR_DATA_SET_COLLECTION,
                                       OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                                       null,
                                       "A database schema hosted on an IBM Db2 for Linux, UNIX and Windows database that has the tabular data set collection interface.",
                                       "https://www.ibm.com/products/db2",
                                       "fa4e861e-d9b6-48dd-bde3-0c10807815ab",
                                       SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                       "DB2LUW-TABULAR-DATA-SET-COLLECTION"),

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
            for (DB2LUWDeployedImplementationType definition : DB2LUWDeployedImplementationType.values())
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

        for (DB2LUWDeployedImplementationType definition : DB2LUWDeployedImplementationType.values())
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
    DB2LUWDeployedImplementationType(String                               guid,
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
