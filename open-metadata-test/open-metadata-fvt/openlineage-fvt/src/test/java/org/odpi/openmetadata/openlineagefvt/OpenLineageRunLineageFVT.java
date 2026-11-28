/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.controls.OpenLineageCataloguerConfigurationProperty;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Verifies the catalogRunLineage option of the Open Lineage Cataloguer.  With the option on, each run becomes a
 * TransientEmbeddedProcess owned by the job's process and is linked to the datasets that it read and wrote, while
 * the job's process is still linked to every dataset that any of its runs used.  Two runs of one job use different
 * inputs, so the test can tell the lineage of a run from the lineage of the job.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OpenLineageRunLineageFVT
{
    private static final String JOB_NAME       = "run_lineage_job";
    private static final String DATA_NAMESPACE = "runlineage://openlineage-fvt";
    private static final String FIRST_INPUT    = "first_input";
    private static final String SECOND_INPUT   = "second_input";
    private static final String OUTPUT         = "run_lineage_output";


    @Test
    @DisplayName("With catalogRunLineage on, each run is linked to the datasets it used and the job to all of them")
    void runsAreLinkedToTheirDataSets() throws Exception
    {
        setCatalogRunLineage(true);

        try
        {
            UUID firstRunId  = UUID.randomUUID();
            UUID secondRunId = UUID.randomUUID();

            OpenLineageFvtTestSupport.publish(runEvent(firstRunId, FIRST_INPUT));
            OpenLineageFvtTestSupport.publish(runEvent(secondRunId, SECOND_INPUT));

            OpenMetadataRootElement job       = OpenLineageFvtTestSupport.waitForAsset(OpenLineageFvtTestSupport.processQualifiedName(OpenLineageEventFactory.JOB_NAMESPACE, JOB_NAME), "the job");
            OpenMetadataRootElement firstRun  = OpenLineageFvtTestSupport.waitForAsset(runQualifiedName(firstRunId), "the first run");
            OpenMetadataRootElement secondRun = OpenLineageFvtTestSupport.waitForAsset(runQualifiedName(secondRunId), "the second run");
            OpenMetadataRootElement first     = OpenLineageFvtTestSupport.waitForAsset(dataSetQualifiedName(FIRST_INPUT), "the first input");
            OpenMetadataRootElement second    = OpenLineageFvtTestSupport.waitForAsset(dataSetQualifiedName(SECOND_INPUT), "the second input");
            OpenMetadataRootElement output    = OpenLineageFvtTestSupport.waitForAsset(dataSetQualifiedName(OUTPUT), "the output");

            String jobGUID       = job.getElementHeader().getGUID();
            String firstRunGUID  = firstRun.getElementHeader().getGUID();
            String secondRunGUID = secondRun.getElementHeader().getGUID();
            String firstGUID     = first.getElementHeader().getGUID();
            String secondGUID    = second.getElementHeader().getGUID();
            String outputGUID    = output.getElementHeader().getGUID();

            assertEquals(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS.typeName, firstRun.getElementHeader().getType().getTypeName(), "A run should be a TransientEmbeddedProcess");
            assertTrue(OpenLineageFvtTestSupport.areLinked(jobGUID, firstRunGUID, OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName), "The job should own the first run");
            assertTrue(OpenLineageFvtTestSupport.areLinked(jobGUID, secondRunGUID, OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName), "The job should own the second run");

            /*
             * The lineage of each run is the last thing written for its event.
             */
            OpenLineageFvtTestSupport.waitFor("the lineage of the second run", () -> OpenLineageFvtTestSupport.areLinked(secondRunGUID, outputGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName));
            OpenLineageFvtTestSupport.waitFor("the lineage of the first run", () -> OpenLineageFvtTestSupport.areLinked(firstRunGUID, outputGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName));

            /*
             * Each run is linked to the datasets it used - and only those.
             */
            assertTrue(OpenLineageFvtTestSupport.areLinked(firstGUID, firstRunGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The first input should flow into the first run");
            assertFalse(OpenLineageFvtTestSupport.areLinked(secondGUID, firstRunGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The second input was not read by the first run");
            assertTrue(OpenLineageFvtTestSupport.areLinked(secondGUID, secondRunGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The second input should flow into the second run");
            assertFalse(OpenLineageFvtTestSupport.areLinked(firstGUID, secondRunGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The first input was not read by the second run");

            /*
             * The job is still linked to everything its runs used.
             */
            assertTrue(OpenLineageFvtTestSupport.areLinked(firstGUID, jobGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The first input should flow into the job");
            assertTrue(OpenLineageFvtTestSupport.areLinked(secondGUID, jobGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The second input should flow into the job");
            assertTrue(OpenLineageFvtTestSupport.areLinked(jobGUID, outputGUID, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName), "The job should flow into the output");
        }
        finally
        {
            /*
             * The other tests in the suite expect the cataloguer's default behaviour.
             */
            setCatalogRunLineage(false);
        }
    }


    /**
     * Turn the cataloguer's catalogRunLineage option on or off.  The integration daemon restarts the connector
     * with the new configuration properties.
     *
     * @param catalogRunLineage new value
     * @throws Exception problem calling the integration daemon
     */
    private static void setCatalogRunLineage(boolean catalogRunLineage) throws Exception
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(OpenLineageCataloguerConfigurationProperty.CATALOG_RUN_LINEAGE.getName(), Boolean.toString(catalogRunLineage));

        IntegrationDaemon integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();
        String            connectorName     = IntegrationConnectorDefinition.OPEN_LINEAGE_CATALOGUER.getConnectorName();

        integrationDaemon.updateConfigurationProperties(connectorName, true, configurationProperties);

        /*
         * The new instance of the connector registers for events when the daemon's refresh thread starts it.  An
         * event published before then is not delivered to it.
         */
        OpenLineageFvtTestSupport.waitFor("the cataloguer to restart", () ->
        {
            IntegrationGroupSummary summary = integrationDaemon.getIntegrationGroupSummary(OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName());

            if ((summary != null) && (summary.getIntegrationConnectorReports() != null))
            {
                for (IntegrationConnectorReport connectorReport : summary.getIntegrationConnectorReports())
                {
                    if (connectorName.equals(connectorReport.getConnectorName()))
                    {
                        return connectorReport.getConnectorStatus() == IntegrationConnectorStatus.WAITING;
                    }
                }
            }

            return false;
        });
    }


    /**
     * Build a COMPLETE event for a run that reads one input and writes the output.
     *
     * @param runId run identifier
     * @param inputName name of the input dataset
     * @return event
     */
    private static OpenLineageRunEvent runEvent(UUID   runId,
                                                String inputName)
    {
        OpenLineageRunEvent event = OpenLineageEventFactory.runEvent("COMPLETE", Instant.now(), runId, JOB_NAME);

        OpenLineageInputDataSet input = new OpenLineageInputDataSet();

        input.setNamespace(DATA_NAMESPACE);
        input.setName(inputName);

        OpenLineageOutputDataSet output = new OpenLineageOutputDataSet();

        output.setNamespace(DATA_NAMESPACE);
        output.setName(OUTPUT);

        event.setInputs(List.of(input));
        event.setOutputs(List.of(output));

        return event;
    }


    /**
     * Build the qualified name the cataloguer uses for a run.
     *
     * @param runId run identifier
     * @return qualified name
     */
    private static String runQualifiedName(UUID runId)
    {
        return "OpenLineageRun::" + OpenLineageEventFactory.JOB_NAMESPACE + "::" + JOB_NAME + "::" + runId;
    }


    /**
     * Build the qualified name the cataloguer uses for one of this test's datasets (a generic DataStore, since the
     * namespace does not name a known technology).
     *
     * @param name dataset name
     * @return qualified name
     */
    private static String dataSetQualifiedName(String name)
    {
        return OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.DATA_STORE.typeName, DATA_NAMESPACE, name);
    }
}
