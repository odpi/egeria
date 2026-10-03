/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

/**
 * FileStoreDeployedImplementationType describes the standard deployed implementation types (technology types)
 * supplied with Egeria for files and folders held in object stores and distributed file systems.  Many of these
 * technologies do not yet have connectors; their catalog templates create just the asset, so that the resources
 * can be catalogued (for example from OpenLineage events) and linked into lineage.
 */
public enum FileStoreDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * An object in an Amazon S3 bucket.  OpenLineage names it s3://{bucket name} + {object key}.
     */
    AMAZON_S3_OBJECT("dbf3eb6d-ad19-4d55-a588-a92455c24e7d",
                     "Amazon S3 Object",
                     DeployedImplementationType.DATA_FILE,
                     OpenMetadataType.DATA_FILE.typeName,
                     null,
                     "An object in an Amazon S3 bucket.  OpenLineage names it s3://{bucket name} + {object key}.",
                     "https://aws.amazon.com/s3/",
                     null,
                     null,
                     null),

    /**
     * A prefix (folder) in an Amazon S3 bucket.  OpenLineage names it s3://{bucket name} + {prefix}/.
     */
    AMAZON_S3_FOLDER("3803b4d2-64f8-41bb-a63e-c47b6b2d7dfc",
                     "Amazon S3 Folder",
                     DeployedImplementationType.DATA_FOLDER,
                     OpenMetadataType.DATA_FOLDER.typeName,
                     null,
                     "A prefix (folder) in an Amazon S3 bucket.  OpenLineage names it s3://{bucket name} + {prefix}/.",
                     "https://aws.amazon.com/s3/",
                     null,
                     null,
                     null),

    /**
     * An object in a Google Cloud Storage bucket.  OpenLineage names it gs://{bucket name} + {object key}.
     */
    GOOGLE_CLOUD_STORAGE_OBJECT("d35e5f8c-1595-4ee4-a0d6-0b935142f0a3",
                                "Google Cloud Storage Object",
                                DeployedImplementationType.DATA_FILE,
                                OpenMetadataType.DATA_FILE.typeName,
                                null,
                                "An object in a Google Cloud Storage bucket.  OpenLineage names it gs://{bucket name} + {object key}.",
                                "https://cloud.google.com/storage",
                                null,
                                null,
                                null),

    /**
     * A prefix (folder) in a Google Cloud Storage bucket.  OpenLineage names it gs://{bucket name} + {prefix}/.
     */
    GOOGLE_CLOUD_STORAGE_FOLDER("3d3c5c19-ea85-4856-bdfa-1dc18a4cd935",
                                "Google Cloud Storage Folder",
                                DeployedImplementationType.DATA_FOLDER,
                                OpenMetadataType.DATA_FOLDER.typeName,
                                null,
                                "A prefix (folder) in a Google Cloud Storage bucket.  OpenLineage names it gs://{bucket name} + {prefix}/.",
                                "https://cloud.google.com/storage",
                                null,
                                null,
                                null),

    /**
     * A blob in an Azure Blob Storage container.  OpenLineage names it wasbs://{container name}@{service name}.dfs.core.windows.net + {object key}.
     */
    AZURE_BLOB_STORAGE_OBJECT("5c231da6-7b74-44dd-a392-68a29456f38c",
                              "Azure Blob Storage Object",
                              DeployedImplementationType.DATA_FILE,
                              OpenMetadataType.DATA_FILE.typeName,
                              null,
                              "A blob in an Azure Blob Storage container.  OpenLineage names it wasbs://{container name}@{service name}.dfs.core.windows.net + {object key}.",
                              "https://azure.microsoft.com/products/storage/blobs",
                              null,
                              null,
                              null),

    /**
     * A virtual directory in an Azure Blob Storage container.  OpenLineage names it wasbs://{container name}@{service name}.dfs.core.windows.net + {prefix}/.
     */
    AZURE_BLOB_STORAGE_FOLDER("0fc8ff90-ce41-4ad8-a87f-4f01d9db2a58",
                              "Azure Blob Storage Folder",
                              DeployedImplementationType.DATA_FOLDER,
                              OpenMetadataType.DATA_FOLDER.typeName,
                              null,
                              "A virtual directory in an Azure Blob Storage container.  OpenLineage names it wasbs://{container name}@{service name}.dfs.core.windows.net + {prefix}/.",
                              "https://azure.microsoft.com/products/storage/blobs",
                              null,
                              null,
                              null),

    /**
     * A file in Azure Data Lake Storage Gen2.  OpenLineage names it abfss://{container name}@{service name}.dfs.core.windows.net + {path}.
     */
    AZURE_DATA_LAKE_STORAGE_FILE("6ad3f9b5-02f0-4402-8b62-a91fdb0fd09c",
                                 "Azure Data Lake Storage File",
                                 DeployedImplementationType.DATA_FILE,
                                 OpenMetadataType.DATA_FILE.typeName,
                                 null,
                                 "A file in Azure Data Lake Storage Gen2.  OpenLineage names it abfss://{container name}@{service name}.dfs.core.windows.net + {path}.",
                                 "https://azure.microsoft.com/products/storage/data-lake-storage",
                                 null,
                                 null,
                                 null),

    /**
     * A directory in Azure Data Lake Storage Gen2.  OpenLineage names it abfss://{container name}@{service name}.dfs.core.windows.net + {path}/.
     */
    AZURE_DATA_LAKE_STORAGE_DIRECTORY("9bfccd5d-e941-4525-ae91-36dbc3153ba0",
                                      "Azure Data Lake Storage Directory",
                                      DeployedImplementationType.DATA_FOLDER,
                                      OpenMetadataType.DATA_FOLDER.typeName,
                                      null,
                                      "A directory in Azure Data Lake Storage Gen2.  OpenLineage names it abfss://{container name}@{service name}.dfs.core.windows.net + {path}/.",
                                      "https://azure.microsoft.com/products/storage/data-lake-storage",
                                      null,
                                      null,
                                      null),

    /**
     * A file in the Apache Hadoop Distributed File System.  OpenLineage names it hdfs://{namenode host}:{namenode port} + {path}.
     */
    HDFS_FILE("47144744-dc73-4248-9e7f-bb807b8910d9",
              "Apache Hadoop HDFS File",
              DeployedImplementationType.DATA_FILE,
              OpenMetadataType.DATA_FILE.typeName,
              null,
              "A file in the Apache Hadoop Distributed File System.  OpenLineage names it hdfs://{namenode host}:{namenode port} + {path}.",
              "https://hadoop.apache.org/",
              null,
              null,
              null),

    /**
     * A directory in the Apache Hadoop Distributed File System.  OpenLineage names it hdfs://{namenode host}:{namenode port} + {path}/.
     */
    HDFS_DIRECTORY("fa1395c5-6a32-42e1-84d2-bd4e7df2fa91",
                   "Apache Hadoop HDFS Directory",
                   DeployedImplementationType.DATA_FOLDER,
                   OpenMetadataType.DATA_FOLDER.typeName,
                   null,
                   "A directory in the Apache Hadoop Distributed File System.  OpenLineage names it hdfs://{namenode host}:{namenode port} + {path}/.",
                   "https://hadoop.apache.org/",
                   null,
                   null,
                   null),

    /**
     * A file in the Databricks File System (DBFS).  OpenLineage names it dbfs://{workspace name} + {path}.
     */
    DATABRICKS_FILE_SYSTEM_FILE("53a4ff9a-de4b-4fca-b2db-f4425439a6b4",
                                "Databricks File System File",
                                DeployedImplementationType.DATA_FILE,
                                OpenMetadataType.DATA_FILE.typeName,
                                null,
                                "A file in the Databricks File System (DBFS).  OpenLineage names it dbfs://{workspace name} + {path}.",
                                "https://www.databricks.com/",
                                null,
                                null,
                                null),

    /**
     * A directory in the Databricks File System (DBFS).  OpenLineage names it dbfs://{workspace name} + {path}/.
     */
    DATABRICKS_FILE_SYSTEM_DIRECTORY("1c83766d-94f3-41ba-ac60-2873cf1a07cb",
                                     "Databricks File System Directory",
                                     DeployedImplementationType.DATA_FOLDER,
                                     OpenMetadataType.DATA_FOLDER.typeName,
                                     null,
                                     "A directory in the Databricks File System (DBFS).  OpenLineage names it dbfs://{workspace name} + {path}/.",
                                     "https://www.databricks.com/",
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
            for (FileStoreDeployedImplementationType definition : FileStoreDeployedImplementationType.values())
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

        for (FileStoreDeployedImplementationType definition : FileStoreDeployedImplementationType.values())
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
    FileStoreDeployedImplementationType(String                               guid,
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
        return "FileStoreDeployedImplementationType{" + deployedImplementationType + '}';
    }
}
