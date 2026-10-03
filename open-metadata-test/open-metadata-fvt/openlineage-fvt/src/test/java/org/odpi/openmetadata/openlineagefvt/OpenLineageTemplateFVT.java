/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.DataPipelineDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.DataPipelineTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.DatabaseTableTemplateType;
import org.odpi.openmetadata.adapters.connectors.controls.DataWarehouseDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.FileStoreDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.controls.FileStoreTemplateType;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageJobFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageJobTypeJobFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Verifies that datasets and jobs that are not catalogued yet are created from the catalog templates for their
 * technology: a Snowflake table and an Amazon S3 object from the templates their namespaces name, a dataset from an
 * unknown technology as a generic DataStore, and an Apache Airflow task (identified by its jobType facet) with its
 * parent job as an Apache Airflow DAG.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OpenLineageTemplateFVT
{
    private static final String JOB_NAMESPACE     = "airflow://openlineage-fvt";
    private static final String DAG_NAME          = "openlineage_fvt_dag";
    private static final String TASK_NAME         = DAG_NAME + ".load_sales";
    private static final String SNOWFLAKE_NS      = "snowflake://openlineage-fvt-account";
    private static final String SNOWFLAKE_TABLE   = "SALES.PUBLIC.ORDERS";
    private static final String S3_NS             = "s3://openlineage-fvt-bucket";
    private static final String S3_OBJECT         = "landing/orders.csv";
    private static final String UNKNOWN_NS        = "mystery://openlineage-fvt:1234";
    private static final String UNKNOWN_NAME      = "orders_summary";


    @Test
    @DisplayName("Datasets and jobs that are not catalogued are created from the templates for their technology")
    void datasetsAndJobsAreCreatedFromTemplates() throws Exception
    {
        OpenLineageRunEvent event = OpenLineageEventFactory.runEvent("COMPLETE", Instant.now(), UUID.randomUUID(), TASK_NAME);

        /*
         * An Apache Airflow task, whose parent is its DAG.
         */
        event.getJob().setNamespace(JOB_NAMESPACE);

        OpenLineageJobTypeJobFacet jobType = new OpenLineageJobTypeJobFacet();

        jobType.setProcessingType("BATCH");
        jobType.setIntegration("AIRFLOW");
        jobType.setJobType("TASK");

        OpenLineageJobFacets jobFacets = new OpenLineageJobFacets();

        jobFacets.setJobType(jobType);
        event.getJob().setFacets(jobFacets);

        event.getRun().getFacets().getParent().getJob().setNamespace(JOB_NAMESPACE);
        event.getRun().getFacets().getParent().getJob().setName(DAG_NAME);

        OpenLineageInputDataSet snowflakeTable = new OpenLineageInputDataSet();

        snowflakeTable.setNamespace(SNOWFLAKE_NS);
        snowflakeTable.setName(SNOWFLAKE_TABLE);

        OpenLineageInputDataSet s3Object = new OpenLineageInputDataSet();

        s3Object.setNamespace(S3_NS);
        s3Object.setName(S3_OBJECT);

        OpenLineageOutputDataSet unknownDataSet = new OpenLineageOutputDataSet();

        unknownDataSet.setNamespace(UNKNOWN_NS);
        unknownDataSet.setName(UNKNOWN_NAME);

        event.setInputs(List.of(snowflakeTable, s3Object));
        event.setOutputs(List.of(unknownDataSet));

        OpenLineageFvtTestSupport.publish(event);

        /*
         * The jobs.
         */
        OpenMetadataRootElement task = OpenLineageFvtTestSupport.waitForAsset(OpenLineageFvtTestSupport.processQualifiedName(JOB_NAMESPACE, TASK_NAME), "the Airflow task");
        OpenMetadataRootElement dag  = OpenLineageFvtTestSupport.waitForAsset(OpenLineageFvtTestSupport.processQualifiedName(JOB_NAMESPACE, DAG_NAME), "the Airflow DAG");

        assertEquals(DataPipelineDeployedImplementationType.APACHE_AIRFLOW_TASK.getDeployedImplementationType(), technologyType(task),
                     "A job with an AIRFLOW/TASK jobType facet should be created from the Apache Airflow Task template");
        assertEquals(DeployedImplementationType.AIRFLOW_DAG.getDeployedImplementationType(), technologyType(dag),
                     "The parent of an Airflow task should be created from the Apache Airflow DAG template");
        assertTrue(OpenLineageFvtTestSupport.areLinked(dag.getElementHeader().getGUID(), task.getElementHeader().getGUID(), OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName),
                   "The DAG should own the task");

        /*
         * The datasets.
         */
        OpenMetadataRootElement snowflake = OpenLineageFvtTestSupport.waitForAsset(OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.DATA_SET.typeName, SNOWFLAKE_NS, SNOWFLAKE_TABLE), "the Snowflake table");
        OpenMetadataRootElement s3       = OpenLineageFvtTestSupport.waitForAsset(OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.DATA_FILE.typeName, S3_NS, S3_OBJECT), "the S3 object");
        OpenMetadataRootElement unknown  = OpenLineageFvtTestSupport.waitForAsset(OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.DATA_STORE.typeName, UNKNOWN_NS, UNKNOWN_NAME), "the dataset from an unknown technology");

        assertEquals(OpenMetadataType.DATA_SET.typeName, snowflake.getElementHeader().getType().getTypeName(), "A Snowflake table should be a DataSet");
        assertEquals(DataWarehouseDeployedImplementationType.SNOWFLAKE_TABLE.getDeployedImplementationType(), technologyType(snowflake),
                     "A snowflake:// dataset should be created from the Snowflake Table template");
        assertEquals(OpenMetadataType.DATA_FILE.typeName, s3.getElementHeader().getType().getTypeName(), "An S3 object should be a DataFile");
        assertEquals(FileStoreDeployedImplementationType.AMAZON_S3_OBJECT.getDeployedImplementationType(), technologyType(s3),
                     "An s3:// dataset should be created from the Amazon S3 Object template");
        assertEquals(OpenMetadataType.DATA_STORE.typeName, unknown.getElementHeader().getType().getTypeName(),
                     "A dataset from an unknown technology should be a generic DataStore");
        assertEquals(UNKNOWN_NAME, ((AssetProperties) unknown.getProperties()).getResourceName(), "The unknown dataset should carry its OpenLineage name");
        assertEquals(UNKNOWN_NS, ((AssetProperties) unknown.getProperties()).getNamespacePath(), "The unknown dataset should carry its OpenLineage namespace");

        /*
         * The assets came from the templates (creating an element from a template links it to the template with
         * SourcedFrom), rather than being created directly because a template was missing.
         */
        assertSourcedFrom(task, DataPipelineTemplateType.APACHE_AIRFLOW_TASK_TEMPLATE.getTemplateGUID());
        assertSourcedFrom(dag, DataPipelineTemplateType.APACHE_AIRFLOW_DAG_TEMPLATE.getTemplateGUID());
        assertSourcedFrom(snowflake, DatabaseTableTemplateType.SNOWFLAKE_TABLE_TEMPLATE.getTemplateGUID());
        assertSourcedFrom(s3, FileStoreTemplateType.AMAZON_S3_OBJECT_TEMPLATE.getTemplateGUID());

        assertTrue(OpenLineageFvtTestSupport.areLinked(snowflake.getElementHeader().getGUID(), task.getElementHeader().getGUID(), OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName),
                   "The Snowflake table should flow into the task");
        assertTrue(OpenLineageFvtTestSupport.areLinked(task.getElementHeader().getGUID(), unknown.getElementHeader().getGUID(), OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName),
                   "The task should flow into the unknown dataset");
    }


    /**
     * Check that an element was created from a template.
     *
     * @param element element
     * @param templateGUID template
     * @throws Exception repository problem
     */
    private static void assertSourcedFrom(OpenMetadataRootElement element,
                                          String                  templateGUID) throws Exception
    {
        assertTrue(OpenLineageFvtTestSupport.areLinked(element.getElementHeader().getGUID(), templateGUID, OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName),
                   "The element " + element.getElementHeader().getGUID() + " should have been created from the template " + templateGUID);
    }


    /**
     * Return the technology type (deployedImplementationType) of an asset.
     *
     * @param asset asset
     * @return technology type
     */
    private static String technologyType(OpenMetadataRootElement asset)
    {
        return ((AssetProperties) asset.getProperties()).getDeployedImplementationType();
    }
}
