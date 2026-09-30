/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

/**
 * DataPipelineDeployedImplementationType describes the standard deployed implementation types (technology types)
 * supplied with Egeria for jobs that run in data pipelines, and the temporary data they use.  Many of these technologies do not yet have connectors;
 * their catalog templates create just the asset, so that the resources can be catalogued (for example from
 * OpenLineage events) and linked into lineage.
 */
public enum DataPipelineDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A task in an Apache Airflow DAG.  OpenLineage names it {namespace} + {dag_id}.{task_id}.
     */
    APACHE_AIRFLOW_TASK("205fdb1b-26d6-4368-b447-237d511ee68a",
                        "Apache Airflow Task",
                        DeployedImplementationType.PROCESS,
                        OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                        null,
                        "A task in an Apache Airflow DAG.  OpenLineage names it {namespace} + {dag_id}.{task_id}.",
                        "https://airflow.apache.org/",
                        null,
                        null,
                        null),

    /**
     * A job run by an Apache Spark application.  OpenLineage names it {namespace} + {appName}.{command}.{table}.
     */
    APACHE_SPARK_JOB("bdafb946-3730-4d90-aa64-0d2886597fdd",
                     "Apache Spark Job",
                     DeployedImplementationType.PROCESS,
                     OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                     null,
                     "A job run by an Apache Spark application.  OpenLineage names it {namespace} + {appName}.{command}.{table}.",
                     "https://spark.apache.org/",
                     null,
                     null,
                     null),

    /**
     * A job that runs SQL statements against a database.  OpenLineage names it {namespace} + {schema}.{table}.
     */
    SQL_JOB("9f6c60ba-dabe-4b3a-b6c1-78da948ab2ac",
            "SQL Job",
            DeployedImplementationType.PROCESS,
            OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
            null,
            "A job that runs SQL statements against a database.  OpenLineage names it {namespace} + {schema}.{table}.",
            "https://openlineage.io/docs/spec/naming/",
            null,
            null,
            null),

    /**
     * A task of a Debezium change data capture connector.  OpenLineage names it {namespace} + {topic.prefix}.{taskId}.
     */
    DEBEZIUM_TASK("6f9a3c6f-5d74-455f-8259-3a10a6f10e89",
                  "Debezium Connector Task",
                  DeployedImplementationType.PROCESS,
                  OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                  null,
                  "A task of a Debezium change data capture connector.  OpenLineage names it {namespace} + {topic.prefix}.{taskId}.",
                  "https://debezium.io/",
                  null,
                  null,
                  null),

    /**
     * A temporary dataset held in memory by a data pipeline.  OpenLineage names it inmemory:// + {temporary dataset name or ID}.
     */
    IN_MEMORY_DATA_SET("771f07d6-85ef-4ee7-bb9e-dae5e1cc6985",
                       "In-Memory Data Set",
                       DeployedImplementationType.DATA_SET,
                       OpenMetadataType.DATA_SET.typeName,
                       null,
                       "A temporary dataset held in memory by a data pipeline.  OpenLineage names it inmemory:// + {temporary dataset name or ID}.",
                       "https://openlineage.io/docs/spec/naming/",
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
            for (DataPipelineDeployedImplementationType definition : DataPipelineDeployedImplementationType.values())
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

        for (DataPipelineDeployedImplementationType definition : DataPipelineDeployedImplementationType.values())
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
    DataPipelineDeployedImplementationType(String                               guid,
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
        return "DataPipelineDeployedImplementationType{" + deployedImplementationType + '}';
    }
}
