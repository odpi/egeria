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

import java.util.ArrayList;
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
 * The suite defines its own governance engine and two governance action processes whose steps all run on it:
 * a two-step process, and a process whose first step starts two steps that run in parallel.  Nothing else in the
 * deployment knows the engine, so every engine action created for it waits at APPROVED until this test picks it
 * up.  That puts every step under the test's control, which is what the cancel and process status tests need:
 * <ul>
 *     <li>the process instance follows its first step through APPROVED, ACTIVATING and IN_PROGRESS, and then
 *     stays IN_PROGRESS until its last step finishes, when it takes that step's status;</li>
 *     <li>a step that starts no next step only ends the process when no parallel step is still running;</li>
 *     <li>cancelling the process cancels the step that has not finished, leaves the finished step COMPLETED,
 *     and marks the process instance CANCELLED - unless the process had already finished;</li>
 *     <li>cancelling an engine action that has already completed leaves it COMPLETED, and cancelling the last
 *     unfinished one ends the process as CANCELLED.</li>
 * </ul>
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExternalGovernanceEngineFVT
{
    private static final String ENGINE_QUALIFIED_NAME  = FilesFvtTestSupport.TEST_MARKER + "::ExternalGovernanceEngine";
    private static final String SERVICE_QUALIFIED_NAME = FilesFvtTestSupport.TEST_MARKER + "::ExternalGovernanceService";
    private static final String PROCESS_QUALIFIED_NAME =FilesFvtTestSupport.TEST_MARKER + "::ExternalEngineProcess";
    private static final String PARALLEL_PROCESS_QUALIFIED_NAME = FilesFvtTestSupport.TEST_MARKER + "::ExternalEngineParallelProcess";
    private static final String WAITING_PROCESS_QUALIFIED_NAME  = FilesFvtTestSupport.TEST_MARKER + "::ExternalEngineWaitingProcess";
    private static final int    STEP_ONE_WAIT_MINUTES           = 1;
    private static final String STEP_ONE_REQUEST_TYPE  = "external-step-one";
    private static final String STEP_TWO_REQUEST_TYPE  = "external-step-two";
    private static final String STEP_ONE_GUARD         = "external-step-one-done";
    private static final String PASSED_ON_PARAMETER    = "externalEngineResult";

    private static final String OTHER_USER_ID = "filesfvtotheruser";

    private static String governanceEngineGUID = null;

    private final AutomatedCurationClient automatedCurationClient = new AutomatedCurationClient();
    private final GovernanceEngineClient  governanceEngineClient  = new GovernanceEngineClient();


    /**
     * Define the governance engine and the governance action processes that run on it.  The names carry the
     * suite's marker, so the clean-up at start-up removes the previous run's definitions.  Each is only created
     * if it is not already there - a run with files.fvt.clear.down=false leaves them behind.
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
            governanceEngineGUID = existingEngine.getElementGUID();
        }
        else
        {
            governanceEngineGUID = defineExternalEngine(openMetadataStore, propertyHelper);
        }

        if (openMetadataStore.getMetadataElementByUniqueName(PROCESS_QUALIFIED_NAME,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name) == null)
        {
            defineProcess(openMetadataStore, propertyHelper, PROCESS_QUALIFIED_NAME, "External engine process", false, 0);
        }

        if (openMetadataStore.getMetadataElementByUniqueName(PARALLEL_PROCESS_QUALIFIED_NAME,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name) == null)
        {
            defineProcess(openMetadataStore, propertyHelper, PARALLEL_PROCESS_QUALIFIED_NAME, "External engine parallel process", true, 0);
        }

        if (openMetadataStore.getMetadataElementByUniqueName(WAITING_PROCESS_QUALIFIED_NAME,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name) == null)
        {
            defineProcess(openMetadataStore, propertyHelper, WAITING_PROCESS_QUALIFIED_NAME, "External engine waiting process", false, STEP_ONE_WAIT_MINUTES);
        }
    }


    /**
     * Define the governance engine and the governance service that stands for the work it runs.
     *
     * @param openMetadataStore store to create the definitions in
     * @param propertyHelper helper
     * @return unique identifier of the governance engine
     * @throws Exception problem creating the definitions
     */
    private static String defineExternalEngine(OpenMetadataStore openMetadataStore,
                                               PropertyHelper    propertyHelper) throws Exception
    {
        ElementProperties engineProperties = propertyHelper.addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              ENGINE_QUALIFIED_NAME);
        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                            "External governance engine");
        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DESCRIPTION.name,
                                                            "A governance engine that no engine host runs; files-fvt claims and completes its engine actions through the Governance Engine OMVS.");

        String engineGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
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
                                                           engineGUID,
                                                           governanceServiceGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                            requestType));
        }

        return engineGUID;
    }


    /**
     * Define a governance action process that runs on the external engine.  Step one is the first step; step two
     * follows it when step one produces its guard.  A parallel process adds a step three, which the same guard
     * starts alongside step two.
     *
     * @param openMetadataStore store to create the definitions in
     * @param propertyHelper helper
     * @param processQualifiedName unique name of the process
     * @param displayName display name of the process
     * @param parallel add a step three that runs in parallel with step two
     * @param stepOneWaitMinutes wait time of step one, in minutes
     * @throws Exception problem creating the definitions
     */
    private static void defineProcess(OpenMetadataStore openMetadataStore,
                                      PropertyHelper    propertyHelper,
                                      String            processQualifiedName,
                                      String            displayName,
                                      boolean           parallel,
                                      int               stepOneWaitMinutes) throws Exception
    {
        ElementProperties processProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               processQualifiedName);
        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             displayName);

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

        String stepOneGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                            null,
                                                                            processGUID,
                                                                            false,
                                                                            null,
                                                                            new NewElementProperties(processStepProperties(propertyHelper, processQualifiedName, 1, stepOneWaitMinutes)),
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
                                                                            new NewElementProperties(processStepProperties(propertyHelper, processQualifiedName, 2, 0)),
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

        if (parallel)
        {
            String stepThreeGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                                  null,
                                                                                  processGUID,
                                                                                  false,
                                                                                  null,
                                                                                  new NewElementProperties(processStepProperties(propertyHelper, processQualifiedName, 3, 0)),
                                                                                  stepOneGUID,
                                                                                  OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName,
                                                                                  new NewElementProperties(nextStepProperties),
                                                                                  true);

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                           stepThreeGUID,
                                                           governanceEngineGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                            STEP_TWO_REQUEST_TYPE));
        }
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
         * The process instance follows its first step until that step is IN_PROGRESS.
         */
        OpenMetadataStore   openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        OpenMetadataElement processInstance   = openMetadataStore.getMetadataElementByGUID(processInstanceGUID);

        assertEquals(ActivityStatus.APPROVED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should follow its approved first step");
        assertNotNull(getDate(processInstance, OpenMetadataProperty.REQUESTED_TIME.name), "The process instance should record when it was requested");
        assertNotNull(getDate(processInstance, OpenMetadataProperty.REQUESTED_START_TIME.name), "The process instance should take its requested start time from its first step");
        assertNull(getDate(processInstance, OpenMetadataProperty.START_TIME.name), "The process instance should not have started before its first step is in progress");

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
        assertEquals(ActivityStatus.ACTIVATING.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should follow its claimed first step");

        /*
         * The work starts.  The process instance starts with it.
         */
        governanceEngineClient.updateEngineActionStatus(stepOneActionGUID, ActivityStatus.IN_PROGRESS);

        assertEquals(ActivityStatus.IN_PROGRESS.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be in progress once its first step is");
        assertNotNull(getEngineAction(stepOneActionGUID).getStartTime(), "Step one should record when it started");
        assertEquals(getEngineAction(stepOneActionGUID).getStartTime(),
                     getDate(openMetadataStore.getMetadataElementByGUID(processInstanceGUID), OpenMetadataProperty.START_TIME.name),
                     "The process instance should start when its first step did");

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
         * Step two is approved, but the process instance only followed its first step - it stays in progress.
         */
        assertEquals(ActivityStatus.IN_PROGRESS.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should stay in progress while step two waits");
        assertNull(getCompletionTime(openMetadataStore, processInstanceGUID),
                   "The process instance should have no completion time while step two waits");

        /*
         * Step two would wait for ever; this run is finished with.
         */
        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);
    }


    @Test
    @Order(2)
    @DisplayName("Cancelling a process cancels its unfinished steps, leaves finished ones COMPLETED, and marks the process instance CANCELLED")
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
        assertEquals(ActivityStatus.CANCELLED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be CANCELLED");
        assertEquals(stepTwoAction.getCompletionTime(), getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.COMPLETION_TIME.name),
                     "The cancelled process instance should complete when its last step was cancelled");
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

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertEquals(ActivityStatus.IN_PROGRESS.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "Cancelling a completed engine action should not end a process whose next step is waiting");

        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);
    }


    @Test
    @Order(4)
    @DisplayName("A process finishes when its last step does, and takes that step's status")
    void processTakesTheStatusOfItsLastStep() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PROCESS_QUALIFIED_NAME, null, null);

        String stepOneActionGUID = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine process");
        String stepTwoActionGUID = completeStepOne(stepOneActionGUID);

        waitForApprovedAction(stepTwoActionGUID, "step two");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        /*
         * Step one finished without ever being IN_PROGRESS, but it led on to step two, so the process is running.
         */
        assertEquals(ActivityStatus.IN_PROGRESS.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be in progress once its first step has led on to another");
        assertEquals(getEngineAction(stepOneActionGUID).getCompletionTime(),
                     getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.START_TIME.name),
                     "A first step that never reported IN_PROGRESS gives the process instance the time it finished as its start time");

        governanceEngineClient.claimEngineAction(stepTwoActionGUID);
        governanceEngineClient.recordCompletionStatus(stepTwoActionGUID,
                                                      CompletionStatus.FAILED,
                                                      null,
                                                      null,
                                                      null,
                                                      "Step two failed outside Egeria");

        assertEquals(ActivityStatus.FAILED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should take the status of its last step");

        Object completionTime = getCompletionTime(openMetadataStore, processInstanceGUID);

        assertNotNull(completionTime, "The finished process instance should have a completion time");
        assertEquals(getEngineAction(stepTwoActionGUID).getCompletionTime(),
                     getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.COMPLETION_TIME.name),
                     "The process instance should complete when its last step did");

        /*
         * The process is over, so a cancel finds nothing to do and leaves the record of how it finished.
         */
        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);

        assertEquals(ActivityStatus.FAILED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "Cancelling a finished process should leave its status");
        assertEquals(completionTime, getCompletionTime(openMetadataStore, processInstanceGUID),
                     "Cancelling a finished process should leave its completion time");
        assertEquals(ActivityStatus.FAILED, getEngineAction(stepTwoActionGUID).getActionStatus(),
                     "Cancelling a finished process should leave its last step");
    }


    @Test
    @Order(5)
    @DisplayName("A step whose guards start no next step ends the process")
    void stepThatStartsNoNextStepEndsTheProcess() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PROCESS_QUALIFIED_NAME, null, null);
        String stepOneActionGUID   = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine process");

        waitForApprovedAction(stepOneActionGUID, "step one");

        /*
         * Step two has a next step relationship from step one, but step one does not produce its guard.
         */
        governanceEngineClient.claimEngineAction(stepOneActionGUID);
        governanceEngineClient.recordCompletionStatus(stepOneActionGUID,
                                                      CompletionStatus.ACTIONED,
                                                      null,
                                                      null,
                                                      null,
                                                      "Step one ran outside Egeria and had nothing for step two");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        List<RelatedEngineActionElement> followOnActions = getEngineAction(stepOneActionGUID).getFollowOnActions();

        assertTrue((followOnActions == null) || (followOnActions.isEmpty()), "Step one should not have started step two: " + followOnActions);
        assertEquals(ActivityStatus.COMPLETED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be COMPLETED when its only step finishes without a next step");
        assertEquals(getEngineAction(stepOneActionGUID).getCompletionTime(),
                     getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.COMPLETION_TIME.name),
                     "The process instance should complete when its only step did");
    }


    @Test
    @Order(6)
    @DisplayName("Cancelling the last unfinished step of a process ends the process as CANCELLED")
    void cancellingTheLastStepCancelsTheProcess() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PROCESS_QUALIFIED_NAME, null, null);

        String stepOneActionGUID = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine process");
        String stepTwoActionGUID = completeStepOne(stepOneActionGUID);

        waitForApprovedAction(stepTwoActionGUID, "step two");

        automatedCurationClient.cancelEngineAction(stepTwoActionGUID);

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertEquals(ActivityStatus.CANCELLED, getEngineAction(stepTwoActionGUID).getActionStatus(), "Step two should be CANCELLED");
        assertEquals(ActivityStatus.CANCELLED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be CANCELLED when its last unfinished step is");
        assertEquals(getEngineAction(stepTwoActionGUID).getCompletionTime(),
                     getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.COMPLETION_TIME.name),
                     "The process instance should complete when its last step was cancelled");
    }


    @Test
    @Order(7)
    @DisplayName("A process with parallel steps only finishes when the last of them does")
    void parallelProcessFinishesWithItsLastStep() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(PARALLEL_PROCESS_QUALIFIED_NAME, null, null);
        String stepOneActionGUID   = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine parallel process");

        waitForApprovedAction(stepOneActionGUID, "step one");

        governanceEngineClient.claimEngineAction(stepOneActionGUID);
        governanceEngineClient.updateEngineActionStatus(stepOneActionGUID, ActivityStatus.IN_PROGRESS);
        governanceEngineClient.recordCompletionStatus(stepOneActionGUID,
                                                      CompletionStatus.ACTIONED,
                                                      List.of(STEP_ONE_GUARD),
                                                      null,
                                                      null,
                                                      "Step one ran outside Egeria");

        List<String> parallelActionGUIDs = waitForFollowOnActions(stepOneActionGUID, 2);

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        /*
         * The first parallel step finishes with no next step, but the other is still waiting - the process
         * is not over.
         */
        String firstParallelActionGUID  = parallelActionGUIDs.get(0);
        String secondParallelActionGUID = parallelActionGUIDs.get(1);

        waitForApprovedAction(firstParallelActionGUID, "the first parallel step");
        governanceEngineClient.claimEngineAction(firstParallelActionGUID);
        governanceEngineClient.recordCompletionStatus(firstParallelActionGUID,
                                                      CompletionStatus.ACTIONED,
                                                      null,
                                                      null,
                                                      null,
                                                      "The first parallel step ran outside Egeria");

        assertEquals(ActivityStatus.IN_PROGRESS.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should stay in progress while a parallel step is still waiting");
        assertNull(getCompletionTime(openMetadataStore, processInstanceGUID),
                   "The process instance should have no completion time while a parallel step is still waiting");

        /*
         * The last parallel step finishes - now the process is over.
         */
        waitForApprovedAction(secondParallelActionGUID, "the second parallel step");
        governanceEngineClient.claimEngineAction(secondParallelActionGUID);
        governanceEngineClient.recordCompletionStatus(secondParallelActionGUID,
                                                      CompletionStatus.ACTIONED,
                                                      null,
                                                      null,
                                                      null,
                                                      "The second parallel step ran outside Egeria");

        assertEquals(ActivityStatus.COMPLETED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be COMPLETED once its last parallel step is");
        assertEquals(getEngineAction(stepOneActionGUID).getStartTime(),
                     getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.START_TIME.name),
                     "The process instance should start when its first step did");
        assertEquals(getEngineAction(secondParallelActionGUID).getCompletionTime(),
                     getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.COMPLETION_TIME.name),
                     "The process instance should complete when its last parallel step did, not the first");
    }


    @Test
    @Order(8)
    @DisplayName("A process takes its requested start time from its first step, which includes the step's wait time in minutes")
    void processRequestedStartTimeIncludesStepWaitTime() throws Exception
    {
        String processInstanceGUID = automatedCurationClient.initiateGovernanceActionProcess(WAITING_PROCESS_QUALIFIED_NAME, null, null);
        String stepOneActionGUID   = new EngineActionWaiter().getFirstEngineAction(processInstanceGUID, "the external engine waiting process");

        waitForApprovedAction(stepOneActionGUID, "step one");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        Date requestedTime          = getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.REQUESTED_TIME.name);
        Date processRequestedStart  = getProcessDate(openMetadataStore, processInstanceGUID, OpenMetadataProperty.REQUESTED_START_TIME.name);
        Date stepOneRequestedStart  = getEngineAction(stepOneActionGUID).getRequestedStartTime();

        assertNotNull(requestedTime, "The process instance should record when it was requested");
        assertNotNull(stepOneRequestedStart, "Step one should have a requested start time");
        assertEquals(stepOneRequestedStart, processRequestedStart,
                     "The process instance should take its requested start time from its first step");

        /*
         * The wait time is in minutes.  Allow a little for the time between the request and the creation of
         * step one's engine action.
         */
        long waitMillis = processRequestedStart.getTime() - requestedTime.getTime();

        assertTrue((waitMillis >= STEP_ONE_WAIT_MINUTES * 60000L) && (waitMillis < STEP_ONE_WAIT_MINUTES * 60000L + 30000L),
                   "Step one waits " + STEP_ONE_WAIT_MINUTES + " minute(s), so the process should be requested to start that long after it was requested, but the gap was " + waitMillis + "ms");

        assertEquals(ActivityStatus.APPROVED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should follow its approved first step");

        /*
         * No need to wait for the minute to pass.
         */
        automatedCurationClient.cancelGovernanceActionProcess(processInstanceGUID);

        assertEquals(ActivityStatus.CANCELLED.name(), getProcessStatus(openMetadataStore, processInstanceGUID),
                     "The process instance should be CANCELLED");
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
     * Wait for a completed step's follow-on engine actions.  See {@link #waitForFollowOnAction(String)}.
     *
     * @param engineActionGUID completed step
     * @param expectedCount how many follow-on engine actions there should be
     * @return the follow-on engine actions
     * @throws Exception they did not all appear
     */
    private List<String> waitForFollowOnActions(String engineActionGUID,
                                                int    expectedCount) throws Exception
    {
        List<String> followOnActionGUIDs = new ArrayList<>();

        FilesFvtTestSupport.waitFor(expectedCount + " engine actions to follow " + engineActionGUID,
                                    "files.fvt.engine.action.timeout.seconds",
                                    300,
                                    () ->
                                    {
                                        followOnActionGUIDs.clear();

                                        List<RelatedEngineActionElement> followOnActions = getEngineAction(engineActionGUID).getFollowOnActions();

                                        if (followOnActions != null)
                                        {
                                            for (RelatedEngineActionElement followOnAction : followOnActions)
                                            {
                                                if ((followOnAction != null) && (followOnAction.getRelatedAction() != null))
                                                {
                                                    followOnActionGUIDs.add(followOnAction.getRelatedAction().getGUID());
                                                }
                                            }
                                        }

                                        return (followOnActionGUIDs.size() == expectedCount);
                                    });

        return followOnActionGUIDs;
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
     * Return the activity status of a governance action process instance.
     *
     * @param openMetadataStore store to read through
     * @param processInstanceGUID process instance
     * @return symbolic name of the status, or null if the instance has none
     * @throws Exception problem reading it
     */
    private String getProcessStatus(OpenMetadataStore openMetadataStore,
                                    String            processInstanceGUID) throws Exception
    {
        OpenMetadataElement processInstance = openMetadataStore.getMetadataElementByGUID(processInstanceGUID);

        assertNotNull(processInstance, "Process instance " + processInstanceGUID + " could not be read");

        return new PropertyHelper().getEnumPropertySymbolicName(this.getClass().getName(),
                                                                OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                processInstance.getElementProperties(),
                                                                "getProcessStatus");
    }


    /**
     * Return a date property of a governance action process instance.
     *
     * @param openMetadataStore store to read through
     * @param processInstanceGUID process instance
     * @param propertyName name of the property
     * @return date, or null if the instance does not have it
     * @throws Exception problem reading it
     */
    private Date getProcessDate(OpenMetadataStore openMetadataStore,
                                String            processInstanceGUID,
                                String            propertyName) throws Exception
    {
        return getDate(openMetadataStore.getMetadataElementByGUID(processInstanceGUID), propertyName);
    }


    /**
     * Return a date property of an element.
     *
     * @param element element
     * @param propertyName name of the property
     * @return date, or null if the element does not have it
     */
    private Date getDate(OpenMetadataElement element,
                         String              propertyName)
    {
        assertNotNull(element, "The element could not be read");

        return new PropertyHelper().getDateProperty(this.getClass().getName(),
                                                    propertyName,
                                                    element.getElementProperties(),
                                                    "getDate");
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
     * @param processQualifiedName unique name of the process the step belongs to
     * @param stepNumber step number
     * @param waitMinutes minutes the step waits before its engine action may start
     * @return properties
     */
    private static ElementProperties processStepProperties(PropertyHelper propertyHelper,
                                                           String         processQualifiedName,
                                                           int            stepNumber,
                                                           int            waitMinutes)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        processQualifiedName + ":processStep" + stepNumber);

        properties = propertyHelper.addIntProperty(properties,
                                                   OpenMetadataProperty.WAIT_TIME.name,
                                                   waitMinutes);

        return propertyHelper.addStringProperty(properties,
                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                "External engine step " + stepNumber);
    }
}
