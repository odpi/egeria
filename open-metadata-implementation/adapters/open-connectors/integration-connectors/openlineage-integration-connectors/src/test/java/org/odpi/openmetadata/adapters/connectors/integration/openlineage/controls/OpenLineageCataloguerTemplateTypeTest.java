/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.controls;

import org.odpi.openmetadata.adapters.connectors.controls.DataPipelineTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.DatabaseTableTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.EventStreamTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.FileStoreTemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * Verifies the template names that the Open Lineage Cataloguer builds from the values in OpenLineage events, and
 * the default template that each resolves to.
 */
public class OpenLineageCataloguerTemplateTypeTest
{
    /**
     * Default constructor
     */
    public OpenLineageCataloguerTemplateTypeTest()
    {
    }


    /**
     * Return the first default template found for a list of template names - the choice the cataloguer makes when
     * its catalog targets supply no templates.
     *
     * @param templateNames template names, most specific first
     * @return template or null
     */
    private TemplateDefinition firstDefault(List<String> templateNames)
    {
        for (String templateName : templateNames)
        {
            TemplateDefinition template = OpenLineageCataloguerTemplateType.getDefaultTemplate(templateName);

            if (template != null)
            {
                return template;
            }
        }

        return null;
    }


    /**
     * The names for a job run from the most specific to the least specific.
     */
    @Test public void testJobTemplateNames()
    {
        assertEquals(OpenLineageCataloguerTemplateType.getJobTemplateNames("airflow", "dag"),
                     List.of("job:AIRFLOW:DAG", "job:AIRFLOW", "job:*:DAG", "job:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getJobTemplateNames("SPARK", null), List.of("job:SPARK", "job:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getJobTemplateNames(null, "QUERY"), List.of("job:*:QUERY", "job:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getJobTemplateNames(null, null), List.of("job:*"));
    }


    /**
     * The default templates for jobs.
     */
    @Test public void testJobDefaults()
    {
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("AIRFLOW", "DAG")), DataPipelineTemplateType.APACHE_AIRFLOW_DAG_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("AIRFLOW", "TASK")), DataPipelineTemplateType.APACHE_AIRFLOW_TASK_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("Airflow", null)), DataPipelineTemplateType.APACHE_AIRFLOW_TASK_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("SPARK", "QUERY")), DataPipelineTemplateType.APACHE_SPARK_JOB_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("DEBEZIUM", "TASK")), DataPipelineTemplateType.DEBEZIUM_TASK_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("SQL", null)), DataPipelineTemplateType.SQL_JOB_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("DBT", "QUERY")), DataPipelineTemplateType.SQL_JOB_TEMPLATE);
        assertNull(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames("DBT", "JOB")));
        assertNull(firstDefault(OpenLineageCataloguerTemplateType.getJobTemplateNames(null, null)));
    }


    /**
     * The names for a dataset: the scheme as written comes before the canonical scheme, and the variant before the
     * plain scheme.
     */
    @Test public void testDataSetTemplateNames()
    {
        assertEquals(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("postgres", "postgres", "db.schema.table"),
                     List.of("dataset:postgres", "dataset:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("sqlserver", "mssql", "db.schema.table"),
                     List.of("dataset:sqlserver", "dataset:mssql", "dataset:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("s3a", "s3", "landing/"),
                     List.of("dataset:s3a:folder", "dataset:s3a", "dataset:s3:folder", "dataset:s3", "dataset:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("pubsub", "pubsub", "subscription:project:sub"),
                     List.of("dataset:pubsub:subscription", "dataset:pubsub", "dataset:*"));
        assertEquals(OpenLineageCataloguerTemplateType.getDataSetTemplateNames(null, null, "name"), List.of("dataset:*"));
    }


    /**
     * The default templates for datasets.
     */
    @Test public void testDataSetDefaults()
    {
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("postgres", "postgres", "db.schema.table")), DatabaseTableTemplateType.POSTGRESQL_TABLE_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("postgresql", "postgres", "db.schema.table")), DatabaseTableTemplateType.POSTGRESQL_TABLE_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("sqlserver", "mssql", "db.schema.table")), DatabaseTableTemplateType.AZURE_SYNAPSE_TABLE_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("mssql", "mssql", "db.schema.table")), DatabaseTableTemplateType.MSSQL_TABLE_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("s3", "s3", "landing/orders.csv")), FileStoreTemplateType.AMAZON_S3_OBJECT_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("s3a", "s3", "landing/")), FileStoreTemplateType.AMAZON_S3_FOLDER_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("abfss", "abfss", "container/dir/")), FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_DIRECTORY_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("file", "file", "/tmp/orders.csv")), FileStoreTemplateType.LOCAL_FILE_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("pubsub", "pubsub", "topic:project:orders")), EventStreamTemplateType.GOOGLE_PUBSUB_TOPIC_TEMPLATE);
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("pubsub", "pubsub", "subscription:project:orders")), EventStreamTemplateType.GOOGLE_PUBSUB_SUBSCRIPTION_TEMPLATE);
        assertNull(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("mystery", "mystery", "orders")));

        /*
         * A folder in a technology with no folder template falls back to the technology's template.
         */
        assertSame(firstDefault(OpenLineageCataloguerTemplateType.getDataSetTemplateNames("postgres", "postgres", "odd/")), DatabaseTableTemplateType.POSTGRESQL_TABLE_TEMPLATE);
    }


    /**
     * Each template name is defined once and is found whatever its case.
     */
    @Test public void testTemplateNamesAreUnique()
    {
        Set<String> templateNames = new HashSet<>();

        for (OpenLineageCataloguerTemplateType templateType : OpenLineageCataloguerTemplateType.values())
        {
            assertTrue(templateNames.add(templateType.getTemplateName().toLowerCase()), templateType.getTemplateName() + " is defined twice");
            assertSame(OpenLineageCataloguerTemplateType.getDefaultTemplate(templateType.getTemplateName().toUpperCase()), templateType.getDefaultTemplate());
        }

        assertEquals(OpenLineageCataloguerTemplateType.getDefaultTemplates().size(), OpenLineageCataloguerTemplateType.values().length);
        assertEquals(OpenLineageCataloguerTemplateType.getTemplateTypes().size(), OpenLineageCataloguerTemplateType.values().length);
    }
}
