/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RelatedEngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Follows an engine action - or a whole governance action process - from the moment it is requested to the
 * moment it finishes, and says what it did.
 * <br>
 * Nothing in this suite runs a governance service itself.  The Automated Curation API records a request in the
 * repository and returns; the metadata access store publishes it; the engine host claims it and runs it.  So
 * every test that asks for something to happen has to wait for it, and the interesting part of the waiting is
 * <em>how it failed</em> when it does:
 * <ul>
 *     <li>an action still at {@code REQUESTED} or {@code WAITING} when the wait expires was never claimed -
 *     no engine host heard about it, which usually means the Kafka broker is not reachable or the engine that
 *     supports the request type is not the one the engine host is running;</li>
 *     <li>an action at {@code IN_PROGRESS} was claimed and is running - the governance service is doing
 *     something slow, or is stuck talking to the resource;</li>
 *     <li>an action at {@code FAILED} ran and reported why, and its completion message is what matters.</li>
 * </ul>
 * Those are three different problems, so the failure messages distinguish them rather than all saying
 * "timed out".
 * <br>
 * A <b>process</b> is followed rather than just its first step.  Initiating a process returns the engine
 * action for step one; each step's completion guards decide which step runs next, and each of those becomes a
 * new engine action linked to its predecessor.  Waiting only for the first action would declare
 * "create the server and then survey it" finished as soon as the server had been created.
 */
class EngineActionWaiter
{
    /**
     * Statuses that mean the action will not change again by itself.
     */
    private static final Set<ActivityStatus> TERMINAL_STATUSES = Set.of(ActivityStatus.COMPLETED,
                                                                        ActivityStatus.INVALID,
                                                                        ActivityStatus.IGNORED,
                                                                        ActivityStatus.FAILED,
                                                                        ActivityStatus.CANCELLED,
                                                                        ActivityStatus.ABANDONED);

    private final EgeriaOpenGovernanceClient openGovernanceClient;
    private final OpenMetadataStore          openMetadataStore;
    private final long                       timeoutMilliseconds;
    private final long                       pollMilliseconds;


    /**
     * Create a waiter that reads engine actions from this suite's metadata access store.
     *
     * @throws Exception problem creating the client
     */
    EngineActionWaiter() throws Exception
    {
        this.openGovernanceClient = new EgeriaOpenGovernanceClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                                    OMAGPlatformExtension.getPlatformURLRoot(),
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    PostgresFvtTestSupport.MAX_PAGE_SIZE,
                                                                    null);

        this.openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        this.timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("postgres.fvt.engine.action.timeout.seconds", 300) * 1000;
        this.pollMilliseconds    = OMAGPlatformExtension.getLongProperty("postgres.fvt.engine.action.poll.seconds", 2) * 1000;
    }


    /**
     * Return the current state of one engine action.
     *
     * @param engineActionGUID action to read
     * @return the action
     * @throws Exception problem reading it
     */
    EngineActionElement getEngineAction(String engineActionGUID) throws Exception
    {
        return openGovernanceClient.getEngineAction(OMAGPlatformExtension.USER_ID, engineActionGUID);
    }


    /**
     * Wait for one engine action to reach a terminal status, and insist that it completed.
     *
     * @param engineActionGUID action to wait for
     * @param description what was asked for, used in the failure message
     * @return the finished action, so that the caller can read its action targets and completion guards
     * @throws Exception the action failed, or never finished
     */
    EngineActionElement waitForCompletion(String engineActionGUID,
                                          String description) throws Exception
    {
        EngineActionElement engineAction = waitForTerminalStatus(engineActionGUID, description);

        if (engineAction.getActionStatus() != ActivityStatus.COMPLETED)
        {
            throw new AssertionError(description + " ended as " + engineAction.getActionStatus()
                                             + " rather than COMPLETED.  Request type '" + engineAction.getRequestType()
                                             + "' on engine '" + engineAction.getGovernanceEngineName() + "' said: "
                                             + engineAction.getCompletionMessage());
        }

        return engineAction;
    }


    /**
     * Wait for one engine action to stop changing, whatever it settles on.  Used by the caller above, and
     * directly by tests that expect an action <em>not</em> to complete.
     *
     * @param engineActionGUID action to wait for
     * @param description what was asked for, used in the failure message
     * @return the finished action
     * @throws Exception it never finished
     */
    EngineActionElement waitForTerminalStatus(String engineActionGUID,
                                              String description) throws Exception
    {
        long                giveUpTime   = System.currentTimeMillis() + timeoutMilliseconds;
        EngineActionElement engineAction = null;

        while (System.currentTimeMillis() < giveUpTime)
        {
            engineAction = getEngineAction(engineActionGUID);

            if ((engineAction != null) && (engineAction.getActionStatus() != null)
                        && TERMINAL_STATUSES.contains(engineAction.getActionStatus()))
            {
                return engineAction;
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new AssertionError(describeStuckAction(description, engineAction));
    }


    /**
     * Wait for a whole governance action process to finish, and return every engine action it ran, in the
     * order they were reached.
     * <br>
     * The chain is followed one step at a time: each finished action names the actions that follow it, and a
     * step that has been triggered but not yet picked up appears as a follow-on before it has a status of its
     * own.  A completed action with no follow-ons is the end of that branch.
     *
     * @param processInstanceGUID the GUID returned when the process was initiated
     * @param description what was asked for, used in the failure message
     * @return the engine actions the process ran
     * @throws Exception a step failed, or the process never finished
     */
    List<EngineActionElement> waitForProcess(String processInstanceGUID,
                                             String description) throws Exception
    {
        String firstEngineActionGUID = getFirstEngineAction(processInstanceGUID, description);

        List<EngineActionElement> completedActions = new ArrayList<>();

        /*
         * A set rather than a list, and ordered, so that a step reached by two branches is only waited for
         * once but the order the process ran in is still readable in a failure message.
         */
        Set<String> toVisit = new LinkedHashSet<>();
        Set<String> visited = new LinkedHashSet<>();

        toVisit.add(firstEngineActionGUID);

        while (! toVisit.isEmpty())
        {
            String engineActionGUID = toVisit.iterator().next();

            toVisit.remove(engineActionGUID);
            visited.add(engineActionGUID);

            EngineActionElement engineAction = waitForCompletion(engineActionGUID,
                                                                  description + " (step " + (completedActions.size() + 1) + ")");

            completedActions.add(engineAction);

            if (engineAction.getFollowOnActions() != null)
            {
                for (RelatedEngineActionElement followOnAction : engineAction.getFollowOnActions())
                {
                    if ((followOnAction != null) && (followOnAction.getRelatedAction() != null))
                    {
                        String followOnGUID = followOnAction.getRelatedAction().getGUID();

                        if ((followOnGUID != null) && (! visited.contains(followOnGUID)))
                        {
                            toVisit.add(followOnGUID);
                        }
                    }
                }
            }
        }

        return completedActions;
    }


    /**
     * Return the engine action for the first step of a process run.
     * <br>
     * Initiating a process returns the GUID of the {@code GovernanceActionProcessInstance} that represents the
     * run, not of an engine action - the first engine action is linked to it by an {@code ActionRequester}
     * relationship.  The two are created in the same request but not in the same instant, so this waits for the
     * link rather than assuming it is already there.
     *
     * @param processInstanceGUID the GUID returned when the process was initiated
     * @param description what was asked for, used in the failure message
     * @return GUID of the engine action for step one
     * @throws Exception the process instance never acquired an engine action
     */
    private String getFirstEngineAction(String processInstanceGUID,
                                        String description) throws Exception
    {
        long giveUpTime = System.currentTimeMillis() + timeoutMilliseconds;

        while (System.currentTimeMillis() < giveUpTime)
        {
            RelatedMetadataElementList requesters =
                    openMetadataStore.getRelatedMetadataElements(processInstanceGUID,
                                                                 1,
                                                                 OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
                                                                 0,
                                                                 PostgresFvtTestSupport.MAX_PAGE_SIZE);

            if ((requesters != null) && (requesters.getElementList() != null))
            {
                for (RelatedMetadataElement requester : requesters.getElementList())
                {
                    if ((requester != null) && (requester.getElement() != null))
                    {
                        return requester.getElement().getElementGUID();
                    }
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new AssertionError(description + " was accepted, and created process instance " + processInstanceGUID
                                         + ", but no engine action was ever linked to it.  The process has an implementation"
                                         + " (its GovernanceActionProcessFlow), but nothing was scheduled to run it.");
    }


    /**
     * Return the GUID of a named action target from anywhere in a finished process.
     * <br>
     * Which step carries a given action target is not obvious, and it is not the step that produced it.  A
     * governance service records its output through {@code recordCompletionStatus}, and those completion action
     * targets are attached to the <em>follow-on</em> engine action - they are how one step hands its work to the
     * next.  So the asset created by step one appears on step two, and the survey report produced by step two
     * appears on step three.  Scanning the whole process for the name avoids encoding that offset in every
     * assertion, and keeps the tests working if a step is inserted.
     *
     * @param engineActions the actions the process ran, in order
     * @param actionTargetName name of the action target wanted, for example "newAsset"
     * @return the action target's GUID, or null if no step has one by that name
     */
    static String getActionTargetGUID(List<EngineActionElement> engineActions,
                                      String                    actionTargetName)
    {
        for (EngineActionElement engineAction : engineActions)
        {
            String actionTargetGUID = getActionTargetGUID(engineAction, actionTargetName);

            if (actionTargetGUID != null)
            {
                return actionTargetGUID;
            }
        }

        return null;
    }


    /**
     * Return the GUID of one of a finished action's action targets.
     * <br>
     * This is how a test gets hold of what a governance service produced.  The "create asset" services record
     * the element they created as an action target named {@code newAsset}, and the process passes it on to the
     * next step - so reading it here is also a check that the hand-off between steps happened.
     *
     * @param engineAction finished action
     * @param actionTargetName name of the action target wanted, for example "newAsset"
     * @return the action target's GUID, or null if the action does not have one by that name
     */
    static String getActionTargetGUID(EngineActionElement engineAction,
                                      String              actionTargetName)
    {
        if (engineAction.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTarget : engineAction.getActionTargetElements())
            {
                if ((actionTarget != null) && (actionTargetName.equals(actionTarget.getActionTargetName())))
                {
                    return actionTarget.getActionTargetGUID();
                }
            }
        }

        return null;
    }


    /**
     * Return the engines the engine host says it is running, for the failure message above.
     *
     * @return engine names, or a description of why they could not be read
     */
    private String getRunningEngineNames()
    {
        try
        {
            List<GovernanceEngineSummary> summaries = OMAGPlatformExtension.getEngineHostClient().getGovernanceEngineSummaries();

            if ((summaries == null) || summaries.isEmpty())
            {
                return "no engines at all";
            }

            List<String> engineNames = new ArrayList<>();

            for (GovernanceEngineSummary summary : summaries)
            {
                engineNames.add(summary.getGovernanceEngineName() + " (" + summary.getGovernanceEngineStatus() + ")");
            }

            return engineNames.toString();
        }
        catch (Exception error)
        {
            return "engines that could not be listed (" + error.getClass().getSimpleName() + ": " + error.getMessage() + ")";
        }
    }


    /**
     * Build a failure message that says what kind of stuck this is.  See the class comment for why the three
     * cases are worth telling apart.
     *
     * @param description what was asked for
     * @param engineAction the action's last known state, may be null if it could not be read at all
     * @return message
     */
    private String describeStuckAction(String              description,
                                       EngineActionElement engineAction)
    {
        String preamble = description + " did not finish within " + (timeoutMilliseconds / 1000) + " seconds";

        if (engineAction == null)
        {
            return preamble + ", and the engine action could not be read back from "
                           + OMAGPlatformExtension.METADATA_STORE_NAME + " at all.";
        }

        ActivityStatus status = engineAction.getActionStatus();

        if ((status == ActivityStatus.REQUESTED) || (status == ActivityStatus.APPROVED) || (status == ActivityStatus.WAITING))
        {
            /*
             * An engine host claims an engine action only when both of these hold: the action names the engine
             * it is running, and the action is APPROVED.  Nothing is logged when either fails - the engine host
             * simply moves on - so the two values are reported here alongside the engines the host says it is
             * running, which is the comparison that decided the outcome.
             */
            return preamble + " - it is still " + status + ", so no engine host claimed it."
                           + "  The action names engine '" + engineAction.getGovernanceEngineName()
                           + "' for request type '" + engineAction.getRequestType() + "'; "
                           + OMAGPlatformExtension.ENGINE_HOST_NAME + " reports it is running " + getRunningEngineNames() + "."
                           + "  If the names match and the status is APPROVED, the action was eligible and was still not"
                           + " picked up: an engine host claims work either from an out topic event or from the sweep it"
                           + " does when an engine refreshes its configuration, and that refresh is deliberately"
                           + " throttled to once every ten minutes - so an action requested shortly after start-up can"
                           + " wait out that window.  Raise postgres.fvt.engine.action.timeout.seconds past ten minutes"
                           + " to tell that apart from an action nobody is listening for.";
        }

        return preamble + " - it reached " + status + " and was still there, so the governance service behind request type '"
                       + engineAction.getRequestType() + "' was running but did not finish.  Its audit log entries are in"
                       + " build/postgres-fvt-data/logs/audit.log.";
    }
}
