/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.GovernanceActionTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.GovernanceEngineDefinition;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEgeriaGovernanceActionRunFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageExecutionParametersRunFacetParameter;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.RunMetricsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Verifies the events that the governance action publisher builds from an engine action: data asset action targets
 * become datasets named according to the OpenLineage naming conventions (destination targets are outputs), the
 * invocation details become execution parameters, the egeria_governanceAction facet carries the information supply
 * chain, and the cataloguer resolves a dataset in the egeria namespace back to its asset and tags the lineage with
 * the information supply chain.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class GovernanceActionPublisherFVT
{
    private static final String S3_BUCKET        = "openlineage-fvt-bucket";
    private static final String S3_KEY           = "raw/orders.csv";
    private static final String FILE_QN          = "openlineage-fvt::publisher::orders-file";
    private static final String DATA_SET_QN      = "openlineage-fvt::publisher::orders-data-set";
    private static final String FILE_TARGET      = "fvtFile";
    private static final String DATA_SET_TARGET  = "destinationDataSet";
    private static final String ISC_QN           = "openlineage-fvt::information-supply-chain";
    private static final String PROCESS_QN       = "GovernanceActionProcess::openlineage-fvt::profile-runs";
    private static final String PROCESS_DATA_SET_QN = "openlineage-fvt::publisher::profile-destination";
    private static final String GA_NAMESPACE     = "GovernanceActions";

    private static final File LOG_STORE = new File("logs/openlineage");


    @Test
    @DisplayName("Action targets become OpenLineage datasets and the invocation details become execution parameters")
    void actionTargetsBecomeDataSets() throws Exception
    {
        AssetClient assetClient = ConnectorContextFactory.newContext().getAssetClient();

        /*
         * A file with a path name but no OpenLineage identity, and a data set with neither.
         */
        DataFileProperties fileProperties = new DataFileProperties();

        fileProperties.setQualifiedName(FILE_QN);
        fileProperties.setDisplayName("orders.csv");
        fileProperties.setPathName("s3://" + S3_BUCKET + "/" + S3_KEY);

        String fileGUID = createAsset(assetClient, fileProperties);

        DataSetProperties dataSetProperties = new DataSetProperties();

        dataSetProperties.setQualifiedName(DATA_SET_QN);
        dataSetProperties.setDisplayName("Orders data set");

        String dataSetGUID = createAsset(assetClient, dataSetProperties);

        /*
         * The run profiler ignores action targets it does not recognise, so it can carry these two.
         */
        LOG_STORE.mkdirs();

        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put("logStoreDirectory", LOG_STORE.getAbsolutePath());
        requestParameters.put("analysisWindowDays", "30");

        EngineActionWaiter waiter = new EngineActionWaiter();

        String qualifiedName = GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE.getName() + "::" +
                GovernanceActionTypeDefinition.PROFILE_OPEN_LINEAGE_RUNS.getGovernanceRequestType();

        String engineActionGUID = waiter.getOpenGovernanceClient().initiateGovernanceActionType(OMAGPlatformExtension.USER_ID,
                                                                                                qualifiedName,
                                                                                                null,
                                                                                                null,
                                                                                                List.of(newActionTarget(FILE_TARGET, fileGUID),
                                                                                                        newActionTarget(DATA_SET_TARGET, dataSetGUID)),
                                                                                                null,
                                                                                                requestParameters,
                                                                                                "openlineage-fvt",
                                                                                                null,
                                                                                                ISC_QN);

        waiter.waitForCompletion(engineActionGUID, "the run profiler with extra action targets");

        /*
         * The run id is the engine action's guid (without any type name prefix).
         */
        String runId = engineActionGUID.substring(Math.max(0, engineActionGUID.length() - 36));

        File[] holder = new File[1];

        OpenLineageFvtTestSupport.waitFor("the governance action publisher's COMPLETE event to be written to the log store",
                                          () -> (holder[0] = findEventFile(LOG_STORE, runId, "COMPLETE")) != null);

        OpenLineageRunEvent event = new ObjectMapper().readValue(holder[0], OpenLineageRunEvent.class);

        /*
         * Inputs and outputs: the two data assets, named by the naming conventions - and nothing in the job's
         * namespace.  The destination is an output.
         */
        assertNotNull(event.getInputs(), "The event should have inputs");
        assertEquals(1, event.getInputs().size(), "Only the file is an input: " + event.getInputs());

        OpenLineageInputDataSet fileDataSet = event.getInputs().get(0);

        assertNotEquals(GA_NAMESPACE, fileDataSet.getNamespace(), "Guards and request parameters are not datasets: " + event.getInputs());
        assertEquals("s3://" + S3_BUCKET, fileDataSet.getNamespace(), "The file's path name should give an s3 namespace");
        assertEquals(S3_KEY, fileDataSet.getName(), "An S3 dataset is named by its object key");

        assertNotNull(event.getOutputs(), "The destinationDataSet action target should be an output");
        assertEquals(1, event.getOutputs().size(), "Only the destination is an output: " + event.getOutputs());

        OpenLineageOutputDataSet egeriaDataSet = event.getOutputs().get(0);

        assertEquals("egeria", egeriaDataSet.getNamespace(), "A data set with no OpenLineage identity should be in the egeria namespace");
        assertEquals(DATA_SET_QN, egeriaDataSet.getName(), "An egeria dataset is named by its qualified name");

        /*
         * Egeria's governance action facet.
         */
        OpenLineageEgeriaGovernanceActionRunFacet governanceActionFacet = event.getRun().getFacets().getEgeriaGovernanceAction();

        assertNotNull(governanceActionFacet, "The run should carry the egeria_governanceAction facet");
        assertEquals(ISC_QN, governanceActionFacet.getIscQualifiedName(), "The facet should name the information supply chain");
        assertEquals(engineActionGUID, governanceActionFacet.getEngineActionGUID(), "The facet should name the engine action");
        assertEquals(GovernanceActionTypeDefinition.PROFILE_OPEN_LINEAGE_RUNS.getGovernanceRequestType(), governanceActionFacet.getRequestType());

        /*
         * Execution parameters: request parameters, action targets and guards.
         */
        assertNotNull(event.getRun().getFacets().getExecutionParameters(), "The invocation details should be in the executionParameters facet");

        Map<String, String> parameters = new HashMap<>();

        for (OpenLineageExecutionParametersRunFacetParameter parameter : event.getRun().getFacets().getExecutionParameters().getParameters())
        {
            parameters.put(parameter.getKey(), parameter.getValue());
        }

        assertEquals("30", parameters.get("analysisWindowDays"), "Request parameters are passed by name: " + parameters);
        assertEquals(FILE_QN, parameters.get("actionTarget:" + FILE_TARGET), "Action targets are passed by qualified name: " + parameters);
        assertEquals(DATA_SET_QN, parameters.get("actionTarget:" + DATA_SET_TARGET), "Action targets are passed by qualified name: " + parameters);
        assertNotNull(parameters.get("completionGuards"), "A COMPLETE event carries the completion guards: " + parameters);

        /*
         * The cataloguer resolves the egeria dataset to the data set itself rather than creating a new asset.
         */
        String processQN = OpenLineageFvtTestSupport.processQualifiedName(GA_NAMESPACE, event.getJob().getName());

        OpenMetadataRootElement process = OpenLineageFvtTestSupport.waitForAsset(processQN, "the governance action's process to be catalogued");

        OpenLineageFvtTestSupport.waitFor("the governance action's process to be linked to the data set by DataFlow",
                                          () -> OpenLineageFvtTestSupport.areLinked(dataSetGUID,
                                                                                    process.getElementHeader().getGUID(),
                                                                                    OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName));

        PropertyHelper propertyHelper = new PropertyHelper();
        boolean        taggedWithISC  = false;

        for (RelatedMetadataElement dataFlow : OpenLineageFvtTestSupport.getRelatedElements(dataSetGUID, 0, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName))
        {
            if ((dataFlow.getElement() != null) && (process.getElementHeader().getGUID().equals(dataFlow.getElement().getElementGUID())) &&
                (ISC_QN.equals(propertyHelper.getStringProperty("openlineage-fvt", OpenMetadataProperty.ISC_QUALIFIED_NAME.name, dataFlow.getRelationshipProperties(), "actionTargetsBecomeDataSets"))))
            {
                taggedWithISC = true;
            }
        }

        assertTrue(taggedWithISC, "The DataFlow should carry the information supply chain from the egeria_governanceAction facet");

        assertNull(OpenLineageFvtTestSupport.getAsset(OpenLineageFvtTestSupport.assetQualifiedName(OpenMetadataType.DATA_SET.typeName, "egeria", DATA_SET_QN)),
                   "The cataloguer should not create a new asset for a dataset in the egeria namespace");
    }


    @Test
    @DisplayName("A governance action process run is attached to the process, with run metrics on the process step")
    void processRunsAreAttachedToTheProcessAndItsStep() throws Exception
    {
        ConnectorContextBase context     = ConnectorContextFactory.newContext();
        AssetClient          assetClient = context.getAssetClient();

        DataSetProperties dataSetProperties = new DataSetProperties();

        dataSetProperties.setQualifiedName(PROCESS_DATA_SET_QN);
        dataSetProperties.setDisplayName("Profile destination data set");

        String dataSetGUID = createAsset(assetClient, dataSetProperties);

        /*
         * A governance action process with one step - the run profiler - created from its governance action type,
         * as the subscription pipeline is created from the provisioning governance action type.
         */
        LOG_STORE.mkdirs();

        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put("logStoreDirectory", LOG_STORE.getAbsolutePath());
        requestParameters.put("analysisWindowDays", "30");

        String processGUID = context.createProcessFromGovernanceActionType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                           PROCESS_QN,
                                                                           "OpenLineage FVT profiling process",
                                                                           "Runs the OpenLineage run profiler as a governance action process.",
                                                                           0,
                                                                           GovernanceActionTypeDefinition.PROFILE_OPEN_LINEAGE_RUNS.getGovernanceActionTypeGUID(),
                                                                           requestParameters,
                                                                           null,
                                                                           null);

        EngineActionWaiter waiter = new EngineActionWaiter();

        waiter.getOpenGovernanceClient().initiateGovernanceActionProcess(OMAGPlatformExtension.USER_ID,
                                                                         PROCESS_QN,
                                                                         null,
                                                                         null,
                                                                         List.of(newActionTarget(DATA_SET_TARGET, dataSetGUID)),
                                                                         null,
                                                                         null,
                                                                         "openlineage-fvt",
                                                                         null,
                                                                         ISC_QN);

        /*
         * The lineage is attached to the governance action process itself, for the information supply chain.
         */
        PropertyHelper propertyHelper = new PropertyHelper();

        OpenLineageFvtTestSupport.waitFor("the governance action process to be linked to the data set by DataFlow for the information supply chain", () ->
        {
            for (RelatedMetadataElement dataFlow : OpenLineageFvtTestSupport.getRelatedElements(processGUID, 1, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName))
            {
                if ((dataFlow.getElement() != null) && (dataSetGUID.equals(dataFlow.getElement().getElementGUID())) &&
                    (ISC_QN.equals(propertyHelper.getStringProperty("openlineage-fvt", OpenMetadataProperty.ISC_QUALIFIED_NAME.name, dataFlow.getRelationshipProperties(), "processRunsAreAttachedToTheProcessAndItsStep"))))
                {
                    return true;
                }
            }

            return false;
        });

        /*
         * The run metrics are kept on the process step.
         */
        OpenMetadataElement processStep = context.getOpenMetadataStore().getMetadataElementByUniqueName(PROCESS_QN + ":processStep1", OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(processStep, "The process should have its first step");

        ClassificationExplorerClient classificationClient = context.getClassificationExplorerClient();

        OpenLineageFvtTestSupport.waitFor("the process step to have RunMetrics for its run", () ->
        {
            OpenMetadataRootElement step = classificationClient.getRootElementByGUID(processStep.getElementGUID(), classificationClient.getGetOptions());

            return (step != null) && (step.getElementHeader().getRunMetrics() != null) &&
                   (step.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties runMetrics) &&
                   (runMetrics.getRunCount() >= 1);
        });

        assertTrue(OpenLineageFvtTestSupport.getAsset(OpenLineageFvtTestSupport.processQualifiedName(GA_NAMESPACE, PROCESS_QN + ":processStep1")) == null,
                   "No DeployedSoftwareComponent should be created for a step of a governance action process");

        /*
         * The run profiler finds the step's runs in the log store and adds its profile to the step's RunMetrics.
         */
        OpenLineageFvtTestSupport.waitFor("the process step's COMPLETE event to be written to the log store",
                                          () -> findJobEventFile(LOG_STORE, ":processStep1", "COMPLETE") != null);

        String qualifiedName = GovernanceEngineDefinition.EGERIA_GOVERNANCE_ENGINE.getName() + "::" +
                GovernanceActionTypeDefinition.PROFILE_OPEN_LINEAGE_RUNS.getGovernanceRequestType();

        String engineActionGUID = waiter.getOpenGovernanceClient().initiateGovernanceActionType(OMAGPlatformExtension.USER_ID,
                                                                                                qualifiedName,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                requestParameters,
                                                                                                "openlineage-fvt",
                                                                                                null,
                                                                                                null);

        waiter.waitForCompletion(engineActionGUID, "the run profiler");

        OpenLineageFvtTestSupport.waitFor("the run profiler to add its profile to the process step's RunMetrics", () ->
        {
            OpenMetadataRootElement step = classificationClient.getRootElementByGUID(processStep.getElementGUID(), classificationClient.getGetOptions());

            return (step != null) && (step.getElementHeader().getRunMetrics() != null) &&
                   (step.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties runMetrics) &&
                   (runMetrics.getAdditionalProperties() != null) &&
                   (runMetrics.getAdditionalProperties().get("analysisService") != null);
        });
    }


    /**
     * Find an event file for a job and event type below a directory: the job's directory path contains the fragment.
     *
     * @param directory directory
     * @param jobFragment part of the job's name
     * @param eventType event type
     * @return file or null
     */
    private static File findJobEventFile(File   directory,
                                         String jobFragment,
                                         String eventType)
    {
        File[] files = directory.listFiles();

        if (files == null)
        {
            return null;
        }

        for (File file : files)
        {
            if (file.isDirectory())
            {
                File found = findJobEventFile(file, jobFragment, eventType);

                if (found != null)
                {
                    return found;
                }
            }
            else if ((file.getPath().contains(jobFragment)) && (file.getName().endsWith("-" + eventType + ".openlineageevent")))
            {
                return file;
            }
        }

        return null;
    }


    /**
     * Create and publish an asset, anchored to itself.
     *
     * @param assetClient client
     * @param properties properties of the asset
     * @return guid
     * @throws Exception repository problem
     */
    private static String createAsset(AssetClient     assetClient,
                                      AssetProperties properties) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        String guid = assetClient.createAsset(newElementOptions, null, properties, null);

        assetClient.publishElement(guid);

        return guid;
    }


    /**
     * Build an action target for the engine action.
     *
     * @param name action target name
     * @param guid element
     * @return action target
     */
    private static NewActionTarget newActionTarget(String name,
                                                   String guid)
    {
        NewActionTarget actionTarget = new NewActionTarget();

        actionTarget.setActionTargetName(name);
        actionTarget.setActionTargetGUID(guid);

        return actionTarget;
    }


    /**
     * Find an event file for a run and event type below a directory.
     *
     * @param directory directory
     * @param runId run id
     * @param eventType event type
     * @return file or null
     */
    private static File findEventFile(File   directory,
                                      String runId,
                                      String eventType)
    {
        File[] files = directory.listFiles();

        if (files == null)
        {
            return null;
        }

        for (File file : files)
        {
            if (file.isDirectory())
            {
                File found = findEventFile(file, runId, eventType);

                if (found != null)
                {
                    return found;
                }
            }
            else if ((file.getName().contains(runId)) && (file.getName().endsWith("-" + eventType + ".openlineageevent")))
            {
                return file;
            }
        }

        return null;
    }
}
