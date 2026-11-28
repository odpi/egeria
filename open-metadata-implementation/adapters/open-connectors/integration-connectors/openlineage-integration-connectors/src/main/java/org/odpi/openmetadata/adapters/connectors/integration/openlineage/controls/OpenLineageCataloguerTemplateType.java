/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.controls;

import org.odpi.openmetadata.adapters.connectors.controls.DataPipelineTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.DatabaseTableTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.DocumentManagementTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.EventStreamTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.FileStoreTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.NoSQLTemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenLineageCataloguerTemplateType defines the names that the Open Lineage Cataloguer integration connector uses to
 * choose the catalog template for a job or dataset that is not catalogued yet, and the default template for each name.
 * The names are built from values found in the OpenLineage events:
 * <ul>
 *     <li><b>job:{integration}:{jobType}</b>, <b>job:{integration}</b> and <b>job:*:{jobType}</b> - from the
 *     integration and jobType values of the job's jobType facet.</li>
 *     <li><b>dataset:{scheme}:{variant}</b> and <b>dataset:{scheme}</b> - from the scheme of the dataset's
 *     namespace (the part before <i>://</i>).  The variant is <i>folder</i> when the dataset's name ends in
 *     a slash, and <i>subscription</i> for a Google Pub/Sub subscription.</li>
 *     <li><b>job:*</b> and <b>dataset:*</b> - any job or dataset that has no more specific name.  These have
 *     no default template.</li>
 * </ul>
 * The connector tries the names for a job or dataset from the most specific to the least specific and uses the
 * first one that has a template.  The same names are used in the templates property of the connector's catalog
 * targets to replace the default template for a name, or to supply a template for a name that has no default
 * (such as an integration or namespace scheme that is not listed here).  Names are not case-sensitive.
 */
public enum OpenLineageCataloguerTemplateType
{
    /*
     * Jobs
     */
    AIRFLOW_DAG_JOB(jobName("AIRFLOW", "DAG"), DataPipelineTemplateType.APACHE_AIRFLOW_DAG_TEMPLATE),
    AIRFLOW_JOB(jobName("AIRFLOW", null), DataPipelineTemplateType.APACHE_AIRFLOW_TASK_TEMPLATE),
    SPARK_JOB(jobName("SPARK", null), DataPipelineTemplateType.APACHE_SPARK_JOB_TEMPLATE),
    DEBEZIUM_JOB(jobName("DEBEZIUM", null), DataPipelineTemplateType.DEBEZIUM_TASK_TEMPLATE),
    SQL_JOB(jobName("SQL", null), DataPipelineTemplateType.SQL_JOB_TEMPLATE),
    QUERY_JOB(jobName(null, "QUERY"), DataPipelineTemplateType.SQL_JOB_TEMPLATE),

    /*
     * Database tables
     */
    POSTGRES_DATASET(dataSetName("postgres", null), DatabaseTableTemplateType.POSTGRESQL_TABLE_TEMPLATE),
    SQLSERVER_DATASET(dataSetName("sqlserver", null), DatabaseTableTemplateType.AZURE_SYNAPSE_TABLE_TEMPLATE),
    MSSQL_DATASET(dataSetName("mssql", null), DatabaseTableTemplateType.MSSQL_TABLE_TEMPLATE),
    ORACLE_DATASET(dataSetName("oracle", null), DatabaseTableTemplateType.ORACLE_TABLE_TEMPLATE),
    DB2_DATASET(dataSetName("db2", null), DatabaseTableTemplateType.DB2LUW_TABLE_TEMPLATE),
    AWSATHENA_DATASET(dataSetName("awsathena", null), DatabaseTableTemplateType.AMAZON_ATHENA_TABLE_TEMPLATE),
    ARN_DATASET(dataSetName("arn", null), DatabaseTableTemplateType.AWS_GLUE_TABLE_TEMPLATE),
    AZUREKUSTO_DATASET(dataSetName("azurekusto", null), DatabaseTableTemplateType.AZURE_DATA_EXPLORER_TABLE_TEMPLATE),
    BIGQUERY_DATASET(dataSetName("bigquery", null), DatabaseTableTemplateType.GOOGLE_BIGQUERY_TABLE_TEMPLATE),
    FABRIC_WAREHOUSE_DATASET(dataSetName("fabric-warehouse", null), DatabaseTableTemplateType.MICROSOFT_FABRIC_WAREHOUSE_TABLE_TEMPLATE),
    MYSQL_DATASET(dataSetName("mysql", null), DatabaseTableTemplateType.MYSQL_TABLE_TEMPLATE),
    CRATE_DATASET(dataSetName("crate", null), DatabaseTableTemplateType.CRATEDB_TABLE_TEMPLATE),
    HIVE_DATASET(dataSetName("hive", null), DatabaseTableTemplateType.APACHE_HIVE_TABLE_TEMPLATE),
    OCEANBASE_DATASET(dataSetName("oceanbase", null), DatabaseTableTemplateType.OCEANBASE_TABLE_TEMPLATE),
    TERADATA_DATASET(dataSetName("teradata", null), DatabaseTableTemplateType.TERADATA_TABLE_TEMPLATE),
    REDSHIFT_DATASET(dataSetName("redshift", null), DatabaseTableTemplateType.AMAZON_REDSHIFT_TABLE_TEMPLATE),
    SNOWFLAKE_DATASET(dataSetName("snowflake", null), DatabaseTableTemplateType.SNOWFLAKE_TABLE_TEMPLATE),
    SPANNER_DATASET(dataSetName("spanner", null), DatabaseTableTemplateType.GOOGLE_SPANNER_TABLE_TEMPLATE),
    TRINO_DATASET(dataSetName("trino", null), DatabaseTableTemplateType.TRINO_TABLE_TEMPLATE),
    UNITYCATALOG_DATASET(dataSetName("unitycatalog", null), DatabaseTableTemplateType.UNITY_CATALOG_TABLE_TEMPLATE),

    /*
     * NoSQL stores
     */
    CASSANDRA_DATASET(dataSetName("cassandra", null), NoSQLTemplateType.APACHE_CASSANDRA_TABLE_TEMPLATE),
    AZURECOSMOS_DATASET(dataSetName("azurecosmos", null), NoSQLTemplateType.AZURE_COSMOS_DB_COLLECTION_TEMPLATE),
    MILVUS_DATASET(dataSetName("milvus", null), NoSQLTemplateType.MILVUS_COLLECTION_TEMPLATE),

    /*
     * File stores
     */
    S3_DATASET(dataSetName("s3", null), FileStoreTemplateType.AMAZON_S3_OBJECT_TEMPLATE),
    S3_FOLDER_DATASET(dataSetName("s3", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.AMAZON_S3_FOLDER_TEMPLATE),
    GS_DATASET(dataSetName("gs", null), FileStoreTemplateType.GOOGLE_CLOUD_STORAGE_OBJECT_TEMPLATE),
    GS_FOLDER_DATASET(dataSetName("gs", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.GOOGLE_CLOUD_STORAGE_FOLDER_TEMPLATE),
    WASB_DATASET(dataSetName("wasb", null), FileStoreTemplateType.AZURE_BLOB_STORAGE_OBJECT_TEMPLATE),
    WASB_FOLDER_DATASET(dataSetName("wasb", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.AZURE_BLOB_STORAGE_FOLDER_TEMPLATE),
    WASBS_DATASET(dataSetName("wasbs", null), FileStoreTemplateType.AZURE_BLOB_STORAGE_OBJECT_TEMPLATE),
    WASBS_FOLDER_DATASET(dataSetName("wasbs", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.AZURE_BLOB_STORAGE_FOLDER_TEMPLATE),
    ABFS_DATASET(dataSetName("abfs", null), FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_FILE_TEMPLATE),
    ABFS_FOLDER_DATASET(dataSetName("abfs", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_DIRECTORY_TEMPLATE),
    ABFSS_DATASET(dataSetName("abfss", null), FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_FILE_TEMPLATE),
    ABFSS_FOLDER_DATASET(dataSetName("abfss", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_DIRECTORY_TEMPLATE),
    HDFS_DATASET(dataSetName("hdfs", null), FileStoreTemplateType.HDFS_FILE_TEMPLATE),
    HDFS_FOLDER_DATASET(dataSetName("hdfs", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.HDFS_DIRECTORY_TEMPLATE),
    DBFS_DATASET(dataSetName("dbfs", null), FileStoreTemplateType.DATABRICKS_FILE_SYSTEM_FILE_TEMPLATE),
    DBFS_FOLDER_DATASET(dataSetName("dbfs", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.DATABRICKS_FILE_SYSTEM_DIRECTORY_TEMPLATE),
    FILE_DATASET(dataSetName("file", null), FileStoreTemplateType.LOCAL_FILE_TEMPLATE),
    FILE_FOLDER_DATASET(dataSetName("file", OpenLineageCataloguerTemplateType.FOLDER_VARIANT), FileStoreTemplateType.LOCAL_FOLDER_TEMPLATE),

    /*
     * Document management
     */
    BOX_DATASET(dataSetName("box", null), DocumentManagementTemplateType.BOX_FILE_TEMPLATE),
    FILENET_DATASET(dataSetName("filenet", null), DocumentManagementTemplateType.IBM_FILENET_DOCUMENT_TEMPLATE),
    MSSHAREPOINT_DATASET(dataSetName("mssharepoint", null), DocumentManagementTemplateType.MICROSOFT_SHAREPOINT_DOCUMENT_TEMPLATE),

    /*
     * Event streams
     */
    PUBSUB_DATASET(dataSetName("pubsub", null), EventStreamTemplateType.GOOGLE_PUBSUB_TOPIC_TEMPLATE),
    PUBSUB_SUBSCRIPTION_DATASET(dataSetName("pubsub", OpenLineageCataloguerTemplateType.SUBSCRIPTION_VARIANT), EventStreamTemplateType.GOOGLE_PUBSUB_SUBSCRIPTION_TEMPLATE),
    KAFKA_DATASET(dataSetName("kafka", null), KafkaTemplateType.KAFKA_TOPIC_TEMPLATE),

    /*
     * Data held in memory by a job
     */
    INMEMORY_DATASET(dataSetName("inmemory", null), DataPipelineTemplateType.IN_MEMORY_DATA_SET_TEMPLATE),

    ;

    /**
     * Prefix of the template names for jobs.
     */
    public static final String JOB_PREFIX = "job";

    /**
     * Prefix of the template names for datasets.
     */
    public static final String DATA_SET_PREFIX = "dataset";

    /**
     * Stands for any integration, or any job or dataset, in a template name.
     */
    public static final String ANY = "*";

    /**
     * Variant of a dataset whose name ends in a slash.
     */
    public static final String FOLDER_VARIANT = "folder";

    /**
     * Variant of a Google Pub/Sub dataset whose name begins with "subscription:".
     */
    public static final String SUBSCRIPTION_VARIANT = "subscription";

    private static final String SEPARATOR = ":";


    private final String             templateName;
    private final TemplateDefinition defaultTemplate;


    /**
     * Create a specific Enum constant.
     *
     * @param templateName name used to select the template
     * @param defaultTemplate template used unless a catalog target supplies a different one for this name
     */
    OpenLineageCataloguerTemplateType(String             templateName,
                                      TemplateDefinition defaultTemplate)
    {
        this.templateName    = templateName;
        this.defaultTemplate = defaultTemplate;
    }


    /**
     * Return the name used to select the template.  This is the key to use in the templates property of a catalog
     * target.
     *
     * @return name
     */
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the template that is used for this name unless a catalog target supplies a different one.
     *
     * @return template definition
     */
    public TemplateDefinition getDefaultTemplate()
    {
        return defaultTemplate;
    }


    /**
     * Build the template name for a job.
     *
     * @param integration integration from the jobType facet (null for any integration)
     * @param jobType jobType from the jobType facet (null if the name covers every type of job from the integration)
     * @return template name
     */
    public static String jobName(String integration,
                                 String jobType)
    {
        String name = "job" + ":" + ((integration == null) ? "*" : integration.toUpperCase());

        if (jobType != null)
        {
            name = name + ":" + jobType.toUpperCase();
        }

        return name;
    }


    /**
     * Build the template name for a dataset.
     *
     * @param scheme scheme of the dataset's namespace (null for any dataset)
     * @param variant folder, subscription or null
     * @return template name
     */
    public static String dataSetName(String scheme,
                                     String variant)
    {
        String name = "dataset" + ":" + ((scheme == null) ? "*" : scheme.toLowerCase());

        if (variant != null)
        {
            name = name + ":" + variant;
        }

        return name;
    }


    /**
     * Return the template names for a job, from the most specific to the least specific.
     *
     * @param integration integration from the jobType facet (may be null)
     * @param jobType jobType from the jobType facet (may be null)
     * @return list of template names
     */
    public static List<String> getJobTemplateNames(String integration,
                                                   String jobType)
    {
        List<String> templateNames = new ArrayList<>();

        boolean hasIntegration = (integration != null) && (! integration.isBlank());
        boolean hasJobType     = (jobType != null) && (! jobType.isBlank());

        if ((hasIntegration) && (hasJobType))
        {
            templateNames.add(jobName(integration, jobType));
        }

        if (hasIntegration)
        {
            templateNames.add(jobName(integration, null));
        }

        if (hasJobType)
        {
            templateNames.add(jobName(null, jobType));
        }

        templateNames.add(jobName(null, null));

        return templateNames;
    }


    /**
     * Return the template names for a dataset, from the most specific to the least specific.  The scheme as it
     * appears in the namespace comes before the canonical scheme, so that (for example) sqlserver can have a
     * different template from mssql.
     *
     * @param rawScheme scheme as it appears in the dataset's namespace (may be null)
     * @param canonicalScheme canonical form of the scheme (may be null)
     * @param dataSetName name of the dataset (may be null)
     * @return list of template names
     */
    public static List<String> getDataSetTemplateNames(String rawScheme,
                                                       String canonicalScheme,
                                                       String dataSetName)
    {
        List<String> templateNames = new ArrayList<>();
        String       variant       = null;

        if (dataSetName != null)
        {
            if (dataSetName.endsWith("/"))
            {
                variant = FOLDER_VARIANT;
            }
            else if (("pubsub".equalsIgnoreCase(canonicalScheme)) && (dataSetName.startsWith("subscription:")))
            {
                variant = SUBSCRIPTION_VARIANT;
            }
        }

        addDataSetTemplateNames(templateNames, rawScheme, variant);
        addDataSetTemplateNames(templateNames, canonicalScheme, variant);

        templateNames.add(dataSetName(null, null));

        return templateNames;
    }


    /**
     * Add the template names for a scheme to the list.
     *
     * @param templateNames list to add to
     * @param scheme scheme (may be null)
     * @param variant variant (may be null)
     */
    private static void addDataSetTemplateNames(List<String> templateNames,
                                                String       scheme,
                                                String       variant)
    {
        if ((scheme == null) || (scheme.isBlank()))
        {
            return;
        }

        if (variant != null)
        {
            String variantName = dataSetName(scheme, variant);

            if (! templateNames.contains(variantName))
            {
                templateNames.add(variantName);
            }
        }

        String schemeName = dataSetName(scheme, null);

        if (! templateNames.contains(schemeName))
        {
            templateNames.add(schemeName);
        }
    }


    /**
     * Return the default template for a template name.
     *
     * @param templateName template name (not case-sensitive)
     * @return template definition, or null if the name has no default template
     */
    public static TemplateDefinition getDefaultTemplate(String templateName)
    {
        if (templateName != null)
        {
            for (OpenLineageCataloguerTemplateType templateType : OpenLineageCataloguerTemplateType.values())
            {
                if (templateType.templateName.equalsIgnoreCase(templateName))
                {
                    return templateType.defaultTemplate;
                }
            }
        }

        return null;
    }


    /**
     * Return the definition of a default template from its unique identifier.
     *
     * @param templateGUID unique identifier of a template
     * @return template definition, or null if the template is not one of the defaults
     */
    public static TemplateDefinition getDefaultTemplateByGUID(String templateGUID)
    {
        if (templateGUID != null)
        {
            for (OpenLineageCataloguerTemplateType templateType : OpenLineageCataloguerTemplateType.values())
            {
                if (templateGUID.equals(templateType.defaultTemplate.getTemplateGUID()))
                {
                    return templateType.defaultTemplate;
                }
            }
        }

        return null;
    }


    /**
     * Return the default templates as a map of template name to the unique identifier of the template - the same
     * shape as the templates property of a catalog target.
     *
     * @return map of template name to template GUID
     */
    public static Map<String, String> getDefaultTemplates()
    {
        Map<String, String> templates = new LinkedHashMap<>();

        for (OpenLineageCataloguerTemplateType templateType : OpenLineageCataloguerTemplateType.values())
        {
            templates.put(templateType.templateName, templateType.defaultTemplate.getTemplateGUID());
        }

        return templates;
    }


    /**
     * Return all the template types defined by this enum.
     *
     * @return list of template types
     */
    public static List<TemplateType> getTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        for (OpenLineageCataloguerTemplateType templateType : OpenLineageCataloguerTemplateType.values())
        {
            templateTypes.add(templateType.getTemplateType());
        }

        return templateTypes;
    }


    /**
     * Return the template type for a specific template name.
     *
     * @return template type
     */
    public TemplateType getTemplateType()
    {
        TemplateType templateType = new TemplateType();

        templateType.setName(templateName);
        templateType.setOpenMetadataTypeName(defaultTemplate.getDeployedImplementationType().getAssociatedTypeName());
        templateType.setDescription(defaultTemplate.getTemplateDescription());
        templateType.setRequired(false);
        templateType.setDefaultTemplateGUID(defaultTemplate.getTemplateGUID());

        return templateType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "OpenLineageCataloguerTemplateType{templateName='" + templateName + "'}";
    }
}
