/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

/**
 * DataWarehouseDeployedImplementationType describes the standard deployed implementation types (technology types)
 * supplied with Egeria for tables held in data warehouses, lakehouses and SQL query engines.  Many of these technologies do not yet have connectors;
 * their catalog templates create just the asset, so that the resources can be catalogued (for example from
 * OpenLineage events) and linked into lineage.
 */
public enum DataWarehouseDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A table queried through Amazon Athena.  OpenLineage names it awsathena://athena.{region}.amazonaws.com + {catalog}.{database}.{table}.
     */
    AMAZON_ATHENA_TABLE("c14225f3-1cfa-45b6-ae24-2e94fb504b5d",
                        "Amazon Athena Table",
                        DeployedImplementationType.DATA_SET,
                        OpenMetadataType.DATA_SET.typeName,
                        null,
                        "A table queried through Amazon Athena.  OpenLineage names it awsathena://athena.{region}.amazonaws.com + {catalog}.{database}.{table}.",
                        "https://aws.amazon.com/athena/",
                        null,
                        null,
                        null),

    /**
     * A table defined in the AWS Glue Data Catalog.  OpenLineage names it arn:aws:glue:{region}:{account id} + table/{database}/{table}.
     */
    AWS_GLUE_TABLE("83270982-d271-4bec-ad97-82b855fa7c2e",
                   "AWS Glue Table",
                   DeployedImplementationType.DATA_SET,
                   OpenMetadataType.DATA_SET.typeName,
                   null,
                   "A table defined in the AWS Glue Data Catalog.  OpenLineage names it arn:aws:glue:{region}:{account id} + table/{database}/{table}.",
                   "https://aws.amazon.com/glue/",
                   null,
                   null,
                   null),

    /**
     * A table in an Azure Synapse Analytics SQL pool.  OpenLineage names it sqlserver://{host}:{port} + {schema}.{table}.
     */
    AZURE_SYNAPSE_TABLE("ad800be2-5ddc-4457-9965-0c8403944c14",
                        "Azure Synapse Analytics Table",
                        DeployedImplementationType.DATA_SET,
                        OpenMetadataType.DATA_SET.typeName,
                        null,
                        "A table in an Azure Synapse Analytics SQL pool.  OpenLineage names it sqlserver://{host}:{port} + {schema}.{table}.",
                        "https://azure.microsoft.com/products/synapse-analytics",
                        null,
                        null,
                        null),

    /**
     * A table in an Azure Data Explorer (Kusto) database.  OpenLineage names it azurekusto://{host}.kusto.windows.net + {database}/{table}.
     */
    AZURE_DATA_EXPLORER_TABLE("05b95fd5-a217-4dd5-93b3-6d3489908241",
                              "Azure Data Explorer Table",
                              DeployedImplementationType.DATA_SET,
                              OpenMetadataType.DATA_SET.typeName,
                              null,
                              "A table in an Azure Data Explorer (Kusto) database.  OpenLineage names it azurekusto://{host}.kusto.windows.net + {database}/{table}.",
                              "https://azure.microsoft.com/products/data-explorer",
                              null,
                              null,
                              null),

    /**
     * A table in Google BigQuery.  OpenLineage names it bigquery + {project id}.{dataset name}.{table name}.
     */
    GOOGLE_BIGQUERY_TABLE("d0c4679a-af22-4295-b798-5a2c5e30131b",
                          "Google BigQuery Table",
                          DeployedImplementationType.DATA_SET,
                          OpenMetadataType.DATA_SET.typeName,
                          null,
                          "A table in Google BigQuery.  OpenLineage names it bigquery + {project id}.{dataset name}.{table name}.",
                          "https://cloud.google.com/bigquery",
                          null,
                          null,
                          null),

    /**
     * A table in a Microsoft Fabric warehouse.  OpenLineage names it fabric-warehouse://{sql analytics endpoint} + {database}.{schema}.{table}.
     */
    MICROSOFT_FABRIC_WAREHOUSE_TABLE("768d526a-ebfa-4103-84c0-85f2b0b7c98c",
                                     "Microsoft Fabric Warehouse Table",
                                     DeployedImplementationType.DATA_SET,
                                     OpenMetadataType.DATA_SET.typeName,
                                     null,
                                     "A table in a Microsoft Fabric warehouse.  OpenLineage names it fabric-warehouse://{sql analytics endpoint} + {database}.{schema}.{table}.",
                                     "https://www.microsoft.com/microsoft-fabric",
                                     null,
                                     null,
                                     null),

    /**
     * A table in a MySQL (or MariaDB) database.  OpenLineage names it mysql://{host}:{port} + {database}.{table}.
     */
    MYSQL_TABLE("453b8b7a-8726-4596-831e-be8667bd62fb",
                "MySQL Table",
                DeployedImplementationType.DATA_SET,
                OpenMetadataType.DATA_SET.typeName,
                null,
                "A table in a MySQL (or MariaDB) database.  OpenLineage names it mysql://{host}:{port} + {database}.{table}.",
                "https://www.mysql.com/",
                null,
                null,
                null),

    /**
     * A table in a CrateDB database.  OpenLineage names it crate://{host}:{port} + {database}.{schema}.{table}.
     */
    CRATEDB_TABLE("91ddb8f3-7e3f-4fba-95f7-51147aea94c5",
                  "CrateDB Table",
                  DeployedImplementationType.DATA_SET,
                  OpenMetadataType.DATA_SET.typeName,
                  null,
                  "A table in a CrateDB database.  OpenLineage names it crate://{host}:{port} + {database}.{schema}.{table}.",
                  "https://cratedb.com/",
                  null,
                  null,
                  null),

    /**
     * A table in Apache Hive.  OpenLineage names it hive://{host}:{port} + {database}.{table}.
     */
    APACHE_HIVE_TABLE("1703bf47-73ba-48ce-adaa-eb7beeac3da7",
                      "Apache Hive Table",
                      DeployedImplementationType.DATA_SET,
                      OpenMetadataType.DATA_SET.typeName,
                      null,
                      "A table in Apache Hive.  OpenLineage names it hive://{host}:{port} + {database}.{table}.",
                      "https://hive.apache.org/",
                      null,
                      null,
                      null),

    /**
     * A table in an OceanBase database.  OpenLineage names it oceanbase://{host}:{port} + {database}.{table}.
     */
    OCEANBASE_TABLE("47ea5731-c475-49be-8cab-077aa75c2822",
                    "OceanBase Table",
                    DeployedImplementationType.DATA_SET,
                    OpenMetadataType.DATA_SET.typeName,
                    null,
                    "A table in an OceanBase database.  OpenLineage names it oceanbase://{host}:{port} + {database}.{table}.",
                    "https://en.oceanbase.com/",
                    null,
                    null,
                    null),

    /**
     * A table in a Teradata database.  OpenLineage names it teradata://{host}:{port} + {database}.{table}.
     */
    TERADATA_TABLE("1f89cce4-d713-46a8-8f42-1ba64a7df4d5",
                   "Teradata Table",
                   DeployedImplementationType.DATA_SET,
                   OpenMetadataType.DATA_SET.typeName,
                   null,
                   "A table in a Teradata database.  OpenLineage names it teradata://{host}:{port} + {database}.{table}.",
                   "https://www.teradata.com/",
                   null,
                   null,
                   null),

    /**
     * A table in an Amazon Redshift cluster.  OpenLineage names it redshift://{cluster identifier}.{region}:{port} + {database}.{schema}.{table}.
     */
    AMAZON_REDSHIFT_TABLE("abf7ee79-3c14-4034-a8ce-3457cd4a5507",
                          "Amazon Redshift Table",
                          DeployedImplementationType.DATA_SET,
                          OpenMetadataType.DATA_SET.typeName,
                          null,
                          "A table in an Amazon Redshift cluster.  OpenLineage names it redshift://{cluster identifier}.{region}:{port} + {database}.{schema}.{table}.",
                          "https://aws.amazon.com/redshift/",
                          null,
                          null,
                          null),

    /**
     * A table in Snowflake.  OpenLineage names it snowflake://{organization name}-{account name} + {database}.{schema}.{table}.
     */
    SNOWFLAKE_TABLE("f84f73fb-ab5c-43dc-985e-c172d0cbb1d6",
                    "Snowflake Table",
                    DeployedImplementationType.DATA_SET,
                    OpenMetadataType.DATA_SET.typeName,
                    null,
                    "A table in Snowflake.  OpenLineage names it snowflake://{organization name}-{account name} + {database}.{schema}.{table}.",
                    "https://www.snowflake.com/",
                    null,
                    null,
                    null),

    /**
     * A table in Google Cloud Spanner.  OpenLineage names it spanner://{projectId}:{instanceId} + {database}.{schema}.{table}.
     */
    GOOGLE_SPANNER_TABLE("a2c6ff31-6da3-4ba0-96c6-56cac2afcec8",
                         "Google Cloud Spanner Table",
                         DeployedImplementationType.DATA_SET,
                         OpenMetadataType.DATA_SET.typeName,
                         null,
                         "A table in Google Cloud Spanner.  OpenLineage names it spanner://{projectId}:{instanceId} + {database}.{schema}.{table}.",
                         "https://cloud.google.com/spanner",
                         null,
                         null,
                         null),

    /**
     * A table queried through Trino.  OpenLineage names it trino://{host}:{port} + {catalog}.{schema}.{table}.
     */
    TRINO_TABLE("3f08fb3e-182a-4cd8-98ba-dc074914289f",
                "Trino Table",
                DeployedImplementationType.DATA_SET,
                OpenMetadataType.DATA_SET.typeName,
                null,
                "A table queried through Trino.  OpenLineage names it trino://{host}:{port} + {catalog}.{schema}.{table}.",
                "https://trino.io/",
                null,
                null,
                null)

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
            for (DataWarehouseDeployedImplementationType definition : DataWarehouseDeployedImplementationType.values())
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

        for (DataWarehouseDeployedImplementationType definition : DataWarehouseDeployedImplementationType.values())
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
    DataWarehouseDeployedImplementationType(String                               guid,
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
        return "DataWarehouseDeployedImplementationType{" + deployedImplementationType + '}';
    }
}
