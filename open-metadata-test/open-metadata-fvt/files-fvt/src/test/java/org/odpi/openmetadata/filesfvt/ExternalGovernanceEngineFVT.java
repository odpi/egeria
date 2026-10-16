/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RelatedEngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ExternalGovernanceEngineFVT stands in for a governance engine that runs outside Egeria.  Egeria still
 * coordinates its work - it creates the engine actions, and a governance action process decides which step runs
 * next - but no engine host runs the engine, so the engine finds, claims and completes its engine actions itself
 * through the Governance Engine OMVS.
 * <br>
 * The suite defines its own governance engine and a two-step governance action process whose steps both run on
 * it.  Nothing else in the deployment knows the engine, so every engine action created for it waits at APPROVED
 * until this test picks it up - which also makes it a convenient way to have a process with a step that has
 * finished and a step that has not, for the cancel tests:
 * <ul>
 *     <li>cancelling the process cancels the step that has not finished, leaves the finished step COMPLETED,
 *     and gives the process instance its completion time;</li>
 *     <li>cancelling an engine action that has already completed leaves it COMPLETED.</li>
 * </ul>
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExternalGovernanceEngineFVT
{
    private static final String ENGINE_QUALIFIED_NAME  = FilesFvtTestSupport.TEST_MARKER + "::ExternalGovernanceEngine";
    private static final String SERVICE_QUALIFIED_NAME = FilesFvtTestSupport.TEST_MARKER + "::ExternalGovernanceService";
    private static final String PROCESS_QUALIFIED_NAME =FilesFvtTestSupport.TEST_MARKER + "::ExternalEngineProcess";
    private static final String STEP_ONE_REQUEST_TYPE  = "external-step-one";
    private static final String STEP_TWO_REQUEST_TYPE  = "external-step-two";
    private static final String STEP_ONE_GUARD         = "external-step-one-done";
    private static final String PASSED_ON_PARAMETER    = "externalEngineResult";

    private static final String OTHER_USER_ID = "filesfvtotheruser";

    private static String governanceEngineGUID = null;

    private final AutomatedCurationClient automatedCurationClient = new AutomatedCurationClient();
    private final GovernanceEngineClient  governanceEngineClient  = new GovernanceEngineClient();


    /**
     * Define the governance engine and the two-step governance action process that runs on it.  The names
     * carry the suite's marker, so the clean-up at start-up removes the previous run's definitions.
     *
     * @throws Exception problem creating the definitions
     */
    @BeforeAll
    static void defineExternalEngineAndProcess() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        PropertyHelper    propertyHelper    = new PropertyHelper();

        OpenMetadataElement existingEngine = openMetadataStore.getMetadataElementByUniqueName(ENGINE_QUALIFIED_NAME,
                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name);

        if (existingEngine != null)
        {
            /*
             * Left by a run with files.fvt.clear.down=false.  The process was defined with it.
             */
            governanceEngineGUID = existingEngine.getElementGUID();
            return;
        }

        ElementProperties engineProperties = propertyHelper.addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              ENGINE_QUALIFIED_NAME);
        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                            "External governance engine");
        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DESCRIPTION.name,
                                                            "A governance engine that no engine host runs; files-fvt claims and completes its engine actions through the Governance Engine OMVS.");

        governanceEngineGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                                              new NewElementProperties(engineProperties));

        /*
         * An engine action can only be created for a request type the engine declares, and an engine declares
         * its request types through SupportedGovernanceService relationships - even when, as here, no engine
         * host will ever instantiate the governance service at the other end.  This one stands for the service
         * the external engine runs.
         */
        ElementProperties serviceProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               SERVICE_QUALIFIED_NAME);
        serviceProperties = propertyHelper.addStringProperty(serviceProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             "External governance service");

        String governanceServiceGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName,
                                                                                      new NewElementProperties(serviceProperties));

        for (String requestType : List.of(STEP_ONE_REQUEST_TYPE, STEP_TWO_REQUEST_TYPE))
        {
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                           governanceEngineGUID,
                                                           governanceServiceGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                            requestType));
        }

        ElementProperties processProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               PROCESS_QUALIFIED_NAME);
        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             "External engine process");

        String processGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                            null,
                                                                            null,
                                                                            true,
                                                                            null,
                                                                            new NewElementProperties(processProperties),
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            false);

        /*
         * Step one is the process's first step; step two follows it only when step one produces the guard.
         */
        String stepOneGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                            null,
                                                                            processGUID,
                                                                            false,
                                                                            null,
                                                                            new NewElementProperties(processStepProperties(propertyHelper, 1)),
                                                                            processGUID,
                                                                            OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                            null,
                                                                            true);

        ElementProperties nextStepProperties = propertyHelper.addStringProperty(null,
                                                                                OpenMetadataProperty.GUARD.name,
                                                                                STEP_ONE_GUARD);
        nextStepProperties = propertyHelper.addBooleanProperty(nextStepProperties,
                                                               OpenMetadataProperty.MANDATORY_GUARD.name,
                                                               false);

        String stepTwoGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                            null,
                                                                            processGUID,
                                                                            false,
                                                                            null,
                                                                            new NewElementProperties(processStepProperties(propertyHelper, 2)),
                                                                            stepOneGUID,
                                                                            OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName,
                                                                            new NewElementProperties(nextStepProperties),
                                                                            true);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                       stepOneGUID,
                                                       governanceEngineGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null,
                                                                                        OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                        STEP_ONE_REQUEST_TYPE));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                       stepTwoGUID,
                                                       governanceEngineGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null,
                                                                                        OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                        STEP_TWO_REQUEST_TYPE));
    }


    @Test
    @Order(1)
    @DisplayName("An external engine claims its step through the Governance Engine OMVS, and its output guard starts the next step")
    void externalEngineClaimsAndCompletesItsStep() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PROCESS_QUALIFIED_NAME, null, null);
        String stepOneActionGUID   = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine process");

        /*
         * Nothing runs the engine, so step one waits at APPROVED until this test finds it.
         */
        EngineActionElement approvedAction = waitForApprovedAction(stepOneActionGUID, "step one");

        assertEquals(STEP_ONE_REQUEST_TYPE, approvedAction.getRequestType(), "Step one should carry its executor's request type");
        assertEquals(ENGINE_QUALIFIED_NAME, approvedAction.getGovernanceEngineName(), "Step one should name the external engine");
        assertNull(approvedAction.getProcessingEngineUserId(), "Nobody should have claimed step one yet");

        /*
         * Claim it.  It leaves the approved list and joins this caller's active claimed list.
         */
        governanceEngineClient.claimEngineAction(stepOneActionGUID);

        EngineActionElement claimedAction = getEngineAction(stepOneActionGUID);

        assertEquals(OMAGPlatformExtension.USER_ID, claimedAction.getProcessingEngineUserId(), "The claim should record the caller as the processing engine");
        assertEquals(ActivityStatus.ACTIVATING, claimedAction.getActionStatus(), "A claimed engine action should be ACTIVATING");
        assertFalse(containsAction(governanceEngineClient.getApprovedEngineActions(governanceEngineGUID), stepOneActionGUID),
                    "A claimed engine action should no longer be offered as approved");
        assertTrue(containsAction(governanceEngineClient.getActiveClaimedEngineActions(governanceEngineGUID), stepOneActionGUID),
                   "A claimed engine action should be among the caller's active claimed engine actions");

        /*
         * Nobody else can claim it now - not another user, and not the same caller a second time.
         */
        assertThrows(InvalidParameterException.class,
                     () -> newOpenGovernanceClient().claimEngineAction(OTHER_USER_ID, stepOneActionGUID),
                     "A second claim by another user should be refused");
        assertThrows(AssertionError.class,
                     () -> governanceEngineClient.claimEngineAction(stepOneActionGUID),
                     "A second claim by the same caller should be refused");
        assertEquals(OMAGPlatformExtension.USER_ID, getEngineAction(stepOneActionGUID).getProcessingEngineUserId(),
                     "A refused claim should not change who is processing the engine action");

        /*
         * Complete it with the guard that leads to step two, passing a request parameter on.
         */
        governanceEngineClient.recordCompletionStatus(stepOneActionGUID,
                                                      CompletionStatus.ACTIONED,
                                                      List.of(STEP_ONE_GUARD),
                                                      Map.of(PASSED_ON_PARAMETER, "done"),
                                                      null,
                                                      "Step one ran outside Egeria");

        EngineActionElement completedAction = getEngineAction(stepOneActionGUID);

        assertEquals(ActivityStatus.COMPLETED, completedAction.getActionStatus(), "Step one should be COMPLETED");
        assertNotNull(completedAction.getCompletionGuards(), "Step one should record its completion guards");
        assertTrue(completedAction.getCompletionGuards().contains(STEP_ONE_GUARD), "Step one's completion guards: " + completedAction.getCompletionGuards());
        assertEquals("Step one ran outside Egeria", completedAction.getCompletionMessage());

        /*
         * The guard creates step two's engine action, for the same engine, and it is offered as approved.
         */
        String stepTwoActionGUID = waitForFollowOnAction(stepOneActionGUID);

        EngineActionElement stepTwoAction = waitForApprovedAction(stepTwoActionGUID, "step two");

        assertEquals(STEP_TWO_REQUEST_TYPE, stepTwoAction.getRequestType(), "Step two should carry its executor's request type");
        assertEquals(ENGINE_QUALIFIED_NAME, stepTwoAction.getGovernanceEngineName(), "Step two should name the external engine");
        assertNotNull(stepTwoAction.getReceivedGuards(), "Step two should record the guard that started it");
        assertTrue(stepTwoAction.getReceivedGuards().contains(STEP_ONE_GUARD), "Step two's received guards: " + stepTwoAction.getReceivedGuards());
        assertNotNull(stepTwoAction.getRequestParameters(), "Step two should receive the request parameters step one passed on");
        assertEquals("done", stepTwoAction.getRequestParameters().get(PASSED_ON_PARAMETER),
                     "Step two's request parameters: " + stepTwoAction.getRequestParameters());

        /*
         * Step two would wait for ever; this run is finished with.
         */
        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);
    }


    @Test
    @Order(2)
    @DisplayName("Cancelling a process cancels its unfinished steps, leaves finished ones COMPLETED, and completes the process instance")
    void cancellingAProcessCancelsItsUnfinishedSteps() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PROCESS_QUALIFIED_NAME, null, null);

        String stepOneActionGUID = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine process");
        String stepTwoActionGUID = completeStepOne(stepOneActionGUID);

        waitForApprovedAction(stepTwoActionGUID, "step two");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertNull(getCompletionTime(openMetadataStore, processInstanceGUID),
                   "The process instance should have no completion time while step two is waiting");

        Date stepOneCompletionTime = getEngineAction(stepOneActionGUID).getCompletionTime();

        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);

        EngineActionElement stepOneAction = getEngineAction(stepOneActionGUID);
        EngineActionElement stepTwoAction = getEngineAction(stepTwoActionGUID);

        assertEquals(ActivityStatus.COMPLETED, stepOneAction.getActionStatus(), "A step that had finished should stay COMPLETED");
        assertEquals(stepOneCompletionTime, stepOneAction.getCompletionTime(), "A step that had finished should keep its completion time");
        assertEquals(ActivityStatus.CANCELLED, stepTwoAction.getActionStatus(), "A step that had not finished should be CANCELLED");
        assertFalse(containsAction(governanceEngineClient.getApprovedEngineActions(governanceEngineGUID), stepTwoActionGUID),
                    "A cancelled engine action should no longer be offered as approved");
        assertNotNull(getCompletionTime(openMetadataStore, processInstanceGUID),
                      "The cancelled process instance should have a completion time");
    }


    @Test
    @Order(3)
    @DisplayName("Cancelling an engine action that has already completed leaves it COMPLETED")
    void cancellingACompletedEngineActionLeavesItCompleted() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PROCESS_QUALIFIED_NAME, null, null);

        String stepOneActionGUID = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine process");

        completeStepOne(stepOneActionGUID);

        EngineActionElement beforeCancel = getEngineAction(stepOneActionGUID);

        automatedCurationClient.cancelEngineAction(stepOneActionGUID);

        EngineActionElement afterCancel = getEngineAction(stepOneActionGUID);

        assertEquals(ActivityStatus.COMPLETED, afterCancel.getActionStatus(), "Cancelling a completed engine action should leave it COMPLETED");
        assertEquals(beforeCancel.getCompletionTime(), afterCancel.getCompletionTime(), "Cancelling a completed engine action should leave its completion time");
        assertEquals(beforeCancel.getCompletionMessage(), afterCancel.getCompletionMessage(), "Cancelling a completed engine action should leave its completion message");

        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);
    }


    /**
     * Run step one as the external engine would: wait for it to be offered, claim it, and complete it with the
     * guard that leads to step two.
     *
     * @param stepOneActionGUID step one's engine action
     * @return step two's engine action
     * @throws Exception step one could not be run, or step two never appeared
     */
    private String completeStepOne(String stepOneActionGUID) throws Exception
    {
        waitForApprovedAction(stepOneActionGUID, "step one");

        governanceEngineClient.claimEngineAction(stepOneActionGUID);
        governanceEngineClient.recordCompletionStatus(stepOneActionGUID,
                                                      CompletionStatus.ACTIONED,
                                                      List.of(STEP_ONE_GUARD),
                                                      null,
                                                      null,
                                                      "Step one ran outside Egeria");

        assertEquals(ActivityStatus.COMPLETED, getEngineAction(stepOneActionGUID).getActionStatus(), "Step one should be COMPLETED");

        return waitForFollowOnAction(stepOneActionGUID);
    }


    /**
     * Wait for an engine action to be offered to the external engine as approved, and return it as offered.
     *
     * @param engineActionGUID engine action
     * @param description which step it is, for the failure message
     * @return the engine action from the approved list
     * @throws Exception it was never offered
     */
    private EngineActionElement waitForApprovedAction(String engineActionGUID,
                                                      String description) throws Exception
    {
        EngineActionElement[] holder = new EngineActionElement[1];

        FilesFvtTestSupport.waitFor("The external engine's " + description + " engine action " + engineActionGUID + " to be offered as approved",
                                    "files.fvt.engine.action.timeout.seconds",
                                    300,
                                    () ->
                                    {
                                        for (EngineActionElement engineAction : governanceEngineClient.getApprovedEngineActions(governanceEngineGUID))
                                        {
                                            if (engineActionGUID.equals(engineAction.getElementHeader().getGUID()))
                                            {
                                                holder[0] = engineAction;
                                                return true;
                                            }
                                        }

                                        return false;
                                    });

        return holder[0];
    }


    /**
     * Wait for a completed step's follow-on engine action.  The engine marks a step complete before it creates
     * the actions its guards trigger, so the link may not be there on the first read.
     *
     * @param engineActionGUID completed step
     * @return the follow-on engine action
     * @throws Exception none appeared
     */
    private String waitForFollowOnAction(String engineActionGUID) throws Exception
    {
        String[] holder = new String[1];

        FilesFvtTestSupport.waitFor("Step two's engine action to follow " + engineActionGUID,
                                    "files.fvt.engine.action.timeout.seconds",
                                    300,
                                    () ->
                                    {
                                        List<RelatedEngineActionElement> followOnActions = getEngineAction(engineActionGUID).getFollowOnActions();

                                        if (followOnActions != null)
                                        {
                                            for (RelatedEngineActionElement followOnAction : followOnActions)
                                            {
                                                if ((followOnAction != null) && (followOnAction.getRelatedAction() != null))
                                                {
                                                    holder[0] = followOnAction.getRelatedAction().getGUID();
                                                    return true;
                                                }
                                            }
                                        }

                                        return false;
                                    });

        return holder[0];
    }


    /**
     * Read an engine action from the metadata access store.
     *
     * @param engineActionGUID engine action
     * @return the engine action
     * @throws Exception problem reading it
     */
    private EngineActionElement getEngineAction(String engineActionGUID) throws Exception
    {
        EngineActionElement engineAction = new EngineActionWaiter().getEngineAction(engineActionGUID);

        assertNotNull(engineAction, "Engine action " + engineActionGUID + " could not be read");

        return engineAction;
    }


    /**
     * Return the completion time of a governance action process instance.
     *
     * @param openMetadataStore store to read through
     * @param processInstanceGUID process instance
     * @return completion time, or null if the instance has none
     * @throws Exception problem reading it
     */
    private Object getCompletionTime(OpenMetadataStore openMetadataStore,
                                     String            processInstanceGUID) throws Exception
    {
        OpenMetadataElement processInstance = openMetadataStore.getMetadataElementByGUID(processInstanceGUID);

        assertNotNull(processInstance, "Process instance " + processInstanceGUID + " could not be read");

        return (processInstance.getElementProperties() == null) ? null
                : processInstance.getElementProperties().getPropertyValue(OpenMetadataProperty.COMPLETION_TIME.name);
    }


    /**
     * Determine whether a list of engine actions contains one.
     *
     * @param engineActions list
     * @param engineActionGUID engine action
     * @return true if it is there
     */
    private static boolean containsAction(List<EngineActionElement> engineActions,
                                          String                    engineActionGUID)
    {
        for (EngineActionElement engineAction : engineActions)
        {
            if (engineActionGUID.equals(engineAction.getElementHeader().getGUID()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Create an Open Governance client for the metadata access store.  It takes the userId on each call, which
     * is what lets the test make a claim as somebody other than the suite's own user.
     *
     * @return client
     * @throws Exception problem creating it
     */
    private static EgeriaOpenGovernanceClient newOpenGovernanceClient() throws Exception
    {
        return new EgeriaOpenGovernanceClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                              OMAGPlatformExtension.getPlatformURLRoot(),
                                              null,
                                              null,
                                              null,
                                              FilesFvtTestSupport.MAX_PAGE_SIZE,
                                              null);
    }


    /**
     * Build the properties of one process step.
     *
     * @param propertyHelper helper
     * @param stepNumber step number
     * @return properties
     */
    private static ElementProperties processStepProperties(PropertyHelper propertyHelper,
                                                           int            stepNumber)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        PROCESS_QUALIFIED_NAME + ":processStep" + stepNumber);

        return propertyHelper.addStringProperty(properties,
                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                "External engine step " + stepNumber);
    }
}
