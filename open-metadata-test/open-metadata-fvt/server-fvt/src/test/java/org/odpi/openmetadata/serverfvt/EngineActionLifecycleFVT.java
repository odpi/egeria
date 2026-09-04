/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EngineActionLifecycleFVT follows an engine action from the moment it is created to the moment it completes,
 * and checks at every step that the two queries an engine host uses to find its work return the right things.
 * <br><br>
 * <b>What the lifecycle actually is.</b>  An engine action is a metadata entity describing a governance
 * request type and its parameters, to be run on a named governance engine.  It is broadcast to the running
 * engine hosts; the first one whose governance engines support the request type claims it, and its own engine
 * then executes it and records the outcome.  Expressed as {@code ActivityStatus} values, and following the
 * code rather than the prose:
 * <ol>
 *     <li>{@code REQUESTED} - created, and waiting on the preconditions of the process step that raised it.
 *     Active, unclaimed.  Only reachable through a governance action process; an action raised directly is
 *     approved in the same call.</li>
 *     <li>{@code APPROVED} - its preconditions are satisfied and it may be picked up.  Active, unclaimed.</li>
 *     <li>{@code ACTIVATING} - an engine host has claimed it and is activating the governance service that
 *     will run it; the claimant is recorded as the processing engine user.  Active, <em>claimed</em>.</li>
 *     <li>{@code WAITING} - claimed and activated, but not due to run yet, so parked until its requested start
 *     time arrives.  Active, claimed - see {@link #anActionWaitingForItsStartTimeIsClaimedWork}.</li>
 *     <li>{@code IN_PROGRESS} - the governance service is running.  Active, claimed.</li>
 *     <li>{@code ACTIONED} / {@code INVALID} / {@code FAILED} - finished.  No longer active.</li>
 * </ol>
 * The claim used to set {@code WAITING} rather than {@code ACTIVATING}, which put a claimed action into the
 * status that means "nobody has claimed this yet" and left no way to tell an action an engine host was busy
 * activating from one that was going nowhere.  {@link #anActivatingEngineActionIsStillActive} is what holds
 * that distinction.
 * <br><br>
 * <b>Why this lives in server-fvt.</b>  The subject is an access service rather than an administration one,
 * which sits slightly outside this suite's usual scope.  It is here because this suite already stands up the
 * one thing the test needs - a metadata access store on an in-memory repository, reachable in under a minute
 * with no PostgreSQL and no Kafka - and because it already covers the engine host, whose whole job is finding
 * and running the engine actions this class creates.  A suite of its own for a single class would be a lot of
 * scaffolding for no extra coverage.
 * <br><br>
 * <b>What it is guarding.</b>  {@code getActiveEngineActions} and {@code getActiveClaimedEngineActions} used
 * to read every engine action of the type and discard most of them in a Java loop.  Both now push their
 * selection into the repository query.  That was a correctness change as much as a performance one - paging
 * was previously applied to the unfiltered query - so {@link #pagingReturnsEachActiveActionExactlyOnce} is as
 * much the point of this class as the lifecycle walk is.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class EngineActionLifecycleFVT
{
    /**
     * Walk one engine action through every state, asking both queries what they can see at each step.
     * <br><br>
     * The two queries answer different questions and the difference matters to an engine host: everything
     * still in flight, versus the subset this engine has claimed and is responsible for.  An action that has
     * been created but not claimed must appear in the first and not the second, which is what stops an engine
     * host from restarting work that belongs to somebody else.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEngineActionMovesThroughItsLifecycleAndTheQueriesFollowIt() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("lifecycle");
        String engineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(engineName);

        String engineActionGUID = EngineActionFvtSupport.initiateEngineAction(engineName, "lifecycle-walk");

        /*
         * APPROVED rather than REQUESTED.  An engine action is created REQUESTED and then approved in the
         * same call - "since there is no process control, the governance action moves immediately into
         * APPROVED", as OpenGovernanceRESTServices puts it.  REQUESTED is only observable for an action
         * created as a step in a governance action process, where it waits for its mandatory guards.
         */
        assertEquals(ActivityStatus.APPROVED, EngineActionFvtSupport.getStatus(engineActionGUID),
                     "An engine action raised directly should be APPROVED as soon as it is created");
        assertTrue(EngineActionFvtSupport.isActive(engineActionGUID),
                   "An APPROVED engine action is waiting to be picked up, so it should be reported as active");
        assertFalse(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                    "An APPROVED engine action has not been claimed, so it should not be reported as claimed");

        EngineActionFvtSupport.claim(engineActionGUID);

        assertEquals(ActivityStatus.ACTIVATING, EngineActionFvtSupport.getStatus(engineActionGUID),
                     "Claiming an engine action should move it to ACTIVATING - the engine host has taken it on" +
                             " and is activating the governance service that will run it");
        assertTrue(EngineActionFvtSupport.isActive(engineActionGUID),
                   "A claimed engine action is still active");
        assertTrue(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                   "Once claimed, the engine action should be reported against the engine that claimed it");

        EngineActionFvtSupport.setStatus(engineActionGUID, ActivityStatus.IN_PROGRESS);

        assertTrue(EngineActionFvtSupport.isActive(engineActionGUID),
                   "An engine action that is being executed is active");
        assertTrue(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                   "An engine action that is being executed is still claimed by its engine");

        EngineActionFvtSupport.complete(engineActionGUID, CompletionStatus.ACTIONED);

        assertFalse(EngineActionFvtSupport.isActive(engineActionGUID),
                    "A completed engine action should no longer be reported as active");
        assertFalse(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                    "A completed engine action should no longer be reported as claimed and in flight");
    }


    /**
     * Every terminal status should take an engine action out of both queries, not just the successful one.
     * <br><br>
     * Worth checking each ending separately: a governance service that fails or declines the work leaves the
     * action in a different status from one that succeeds, and an engine host that kept re-reading failed
     * actions would retry them for ever.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void everyTerminalStatusRemovesAnActionFromBothQueries() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("terminal");
        String engineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(engineName);

        for (CompletionStatus completionStatus : List.of(CompletionStatus.ACTIONED,
                                                         CompletionStatus.INVALID,
                                                         CompletionStatus.FAILED))
        {
            String engineActionGUID = EngineActionFvtSupport.initiateEngineAction(engineName,
                                                                                  "terminal-" + completionStatus.getName());

            EngineActionFvtSupport.claim(engineActionGUID);
            EngineActionFvtSupport.setStatus(engineActionGUID, ActivityStatus.IN_PROGRESS);

            assertTrue(EngineActionFvtSupport.isActive(engineActionGUID),
                       "The engine action should be active before it is completed as " + completionStatus.getName());

            EngineActionFvtSupport.complete(engineActionGUID, completionStatus);

            assertFalse(EngineActionFvtSupport.isActive(engineActionGUID),
                        "An engine action completed as " + completionStatus.getName() + " should not be active");
            assertFalse(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                        "An engine action completed as " + completionStatus.getName() + " should not be claimed");
        }
    }


    /**
     * A claimed engine action should be reported against its own engine and no other.
     * <br><br>
     * This is the selection that used to be made in memory after the fact.  It matters because an engine host
     * runs several governance engines and claims all of their work as the same user, so the engine identifier
     * is the only thing separating one engine's actions from another's.  An engine that restarted and picked
     * up a sibling engine's work would run governance services that are not its own.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void claimedActionsAreReportedAgainstTheirOwnEngineOnly() throws Exception
    {
        String firstEngine  = EngineActionFvtSupport.createGovernanceEngine("scopedA");
        String secondEngine = EngineActionFvtSupport.createGovernanceEngine("scopedB");

        String firstEngineGUID  = EngineActionFvtSupport.getGovernanceEngineGUID(firstEngine);
        String secondEngineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(secondEngine);

        String firstAction  = EngineActionFvtSupport.claimedAction(firstEngine, "scoped-first");
        String secondAction = EngineActionFvtSupport.claimedAction(secondEngine, "scoped-second");

        assertTrue(EngineActionFvtSupport.isClaimedBy(firstAction, firstEngineGUID),
                   "The first engine's action should be reported against the first engine");
        assertFalse(EngineActionFvtSupport.isClaimedBy(firstAction, secondEngineGUID),
                    "The first engine's action should not be reported against the second engine");

        assertTrue(EngineActionFvtSupport.isClaimedBy(secondAction, secondEngineGUID),
                   "The second engine's action should be reported against the second engine");
        assertFalse(EngineActionFvtSupport.isClaimedBy(secondAction, firstEngineGUID),
                    "The second engine's action should not be reported against the first engine");
    }


    /**
     * Paging through the active engine actions should return each of them exactly once.
     * <br><br>
     * This is the test that would have failed before the selection was pushed into the query, and it is the
     * reason that change was a correctness fix rather than only a performance one.  {@code startFrom} and
     * {@code pageSize} used to be applied to a query for <em>every</em> engine action, with the status filter
     * applied to the page afterwards - so a caller asking for one active action at a time received pages that
     * were mostly empty, while active actions sat further down the unfiltered list and were skipped over
     * entirely.
     * <br><br>
     * Completed actions are interleaved with the active ones deliberately.  Had they all been created first,
     * a broken pager could still have stumbled onto the right answer; mixed in, only a query that filters
     * before it pages returns every active action and nothing else.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void pagingReturnsEachActiveActionExactlyOnce() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("paging");

        List<String> expectedActive = new ArrayList<>();

        for (int i = 0; i < 5; i++)
        {
            String activeAction = EngineActionFvtSupport.initiateEngineAction(engineName, "paging-active-" + i);

            expectedActive.add(activeAction);

            /*
             * One finished action between each live one, so that a pager that filters after paging cannot
             * accidentally produce the right result.
             */
            String finishedAction = EngineActionFvtSupport.claimedAction(engineName, "paging-finished-" + i);

            EngineActionFvtSupport.complete(finishedAction, CompletionStatus.ACTIONED);
        }

        List<String> pagedGUIDs = EngineActionFvtSupport.pageThroughActiveActions(1);

        for (String expectedGUID : expectedActive)
        {
            assertTrue(pagedGUIDs.contains(expectedGUID),
                       "Paging one at a time should reach every active engine action, but did not return " +
                               expectedGUID + ".  It returned " + pagedGUIDs.size() + " results.");
        }

        assertEquals(pagedGUIDs.size(), new java.util.HashSet<>(pagedGUIDs).size(),
                     "Paging should not return the same engine action twice, but returned duplicates in " + pagedGUIDs);

        for (String pagedGUID : pagedGUIDs)
        {
            assertNotNull(EngineActionFvtSupport.getStatus(pagedGUID),
                          "Every engine action returned should be readable");
            assertFalse(EngineActionFvtSupport.isTerminal(EngineActionFvtSupport.getStatus(pagedGUID)),
                        "A query for active engine actions should not return a finished one, but returned " +
                                pagedGUID + " in status " + EngineActionFvtSupport.getStatus(pagedGUID));
        }
    }


    /**
     * An engine action that is being activated should still count as active.
     * <br><br>
     * {@code ACTIVATING} is the status a claim now sets - "the process that will perform the activity is being
     * activated", as the enum puts it - and it is the status an engine action holds for as long as the engine
     * host is building the governance service that will run it.
     * <br><br>
     * It used to be missing from the statuses {@code getActiveEngineActions} and
     * {@code getActiveClaimedEngineActions} look for, so an engine action being activated was invisible to
     * both: an engine host restarting while one of its actions was activating would not find that action, and
     * would not restart it.  The same class had always counted {@code ACTIVATING} as unfinished in the two
     * places that must not miss an incomplete action - {@code recordCompletionStatus} and the
     * incomplete-action count - so the two lists now agree.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anActivatingEngineActionIsStillActive() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("activating");
        String engineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(engineName);

        String engineActionGUID = EngineActionFvtSupport.claimedAction(engineName, "activating-action");

        EngineActionFvtSupport.setStatus(engineActionGUID, ActivityStatus.ACTIVATING);

        assertEquals(ActivityStatus.ACTIVATING, EngineActionFvtSupport.getStatus(engineActionGUID),
                     "The engine action should be ACTIVATING for this test to be asking anything");

        assertTrue(EngineActionFvtSupport.isActive(engineActionGUID),
                   "An engine action whose governance service is being activated has not finished, so it should" +
                           " be reported as active");
        assertTrue(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                   "An activating engine action has been claimed, so its engine should still see it as its own" +
                           " outstanding work");
    }


    /**
     * An engine action waiting for its start time is claimed work that has not started, and should be
     * reported as both active and claimed.
     * <br><br>
     * A governance service with a requested start time in the future is claimed and activated straight away,
     * and then parked: {@code GovernanceServiceHandler.waitForStartDate} moves the action to {@code WAITING}
     * - "waiting for its start time" - and only sets {@code IN_PROGRESS} when the time arrives.  So a delayed
     * action runs {@code ACTIVATING} to {@code WAITING} to {@code IN_PROGRESS}, and spends the whole delay in
     * a status that also means "unclaimed" for an action nobody has picked up.
     * <br><br>
     * The enum's wording covers both "waiting for its start time" and "waiting for an actor to claim it", which
     * reads as though a single status meant two different things.  In practice only the first is reachable:
     * the status can only be changed by the engine that claimed the action - an attempt on an unclaimed one is
     * refused - so nothing can put an unclaimed action into {@code WAITING}.  An action holding it has been
     * claimed, and the queries scope on the processing engine user as well, so it is unambiguously its
     * engine's outstanding work.
     * <br><br>
     * The transition itself is made by the engine host as it runs the service, which this hermetic suite does
     * not do - no engine host is watching this metadata access store.  The state is therefore set directly
     * here.  That checks the queries treat it correctly, which is what these tests are for; it does not check
     * that {@code waitForStartDate} sets it, which would need an engine host and a scheduled action.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anActionWaitingForItsStartTimeIsClaimedWork() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("startdate");
        String engineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(engineName);

        String claimedGUID = EngineActionFvtSupport.claimedAction(engineName, "startdate-claimed");

        EngineActionFvtSupport.setStatus(claimedGUID, ActivityStatus.WAITING);

        assertEquals(ActivityStatus.WAITING, EngineActionFvtSupport.getStatus(claimedGUID),
                     "The engine action should be WAITING for this test to be asking anything");
        assertTrue(EngineActionFvtSupport.isActive(claimedGUID),
                   "An engine action waiting for its start time has not run yet, so it is still active");
        assertTrue(EngineActionFvtSupport.isClaimedBy(claimedGUID, engineGUID),
                   "An engine action waiting for its start time has been claimed, so its engine should still" +
                           " count it as outstanding work");

    }


    /**
     * An engine action for one engine should not appear in another engine's claimed work even when both are
     * active at once.
     * <br><br>
     * The single-action version of this is {@link #claimedActionsAreReportedAgainstTheirOwnEngineOnly}; this
     * one keeps several actions live simultaneously, which is the state a busy engine host is actually in and
     * the one where an over-broad query does the most damage.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void severalLiveActionsAreSeparatedByEngine() throws Exception
    {
        String busyEngine  = EngineActionFvtSupport.createGovernanceEngine("busy");
        String quietEngine = EngineActionFvtSupport.createGovernanceEngine("quiet");

        String busyEngineGUID  = EngineActionFvtSupport.getGovernanceEngineGUID(busyEngine);
        String quietEngineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(quietEngine);

        List<String> busyActions = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            busyActions.add(EngineActionFvtSupport.claimedAction(busyEngine, "busy-" + i));
        }

        String quietAction = EngineActionFvtSupport.claimedAction(quietEngine, "quiet-0");

        List<String> busyClaimed  = EngineActionFvtSupport.claimedActionGUIDs(busyEngineGUID);
        List<String> quietClaimed = EngineActionFvtSupport.claimedActionGUIDs(quietEngineGUID);

        for (String busyAction : busyActions)
        {
            assertTrue(busyClaimed.contains(busyAction),
                       "The busy engine should see its own action " + busyAction + ", but saw " + busyClaimed);
            assertFalse(quietClaimed.contains(busyAction),
                        "The quiet engine should not see the busy engine's action " + busyAction);
        }

        assertTrue(quietClaimed.contains(quietAction),
                   "The quiet engine should see its own action");
        assertFalse(busyClaimed.contains(quietAction),
                    "The busy engine should not see the quiet engine's action");
    }


    /**
     * An engine action that has not been claimed should never be reported as claimed work, whichever engine
     * is asking.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void unclaimedActionsAreNotReportedAsClaimed() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("unclaimed");
        String engineGUID = EngineActionFvtSupport.getGovernanceEngineGUID(engineName);

        String engineActionGUID = EngineActionFvtSupport.initiateEngineAction(engineName, "unclaimed-action");

        assertTrue(EngineActionFvtSupport.isActive(engineActionGUID),
                   "An approved but unclaimed engine action is still active");
        assertFalse(EngineActionFvtSupport.isClaimedBy(engineActionGUID, engineGUID),
                    "Nobody has claimed this engine action, so no engine should be reported as owning it");
    }


    /**
     * The active list should contain the engine actions that are live and none of the ones that are not.
     * <br><br>
     * Where the lifecycle walk follows one action through every state, this checks the two sets are correct
     * when several actions in different states exist together - which is the situation the query is actually
     * asked about.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void onlyLiveActionsAppearInTheActiveList() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("mixture");

        String approved   = EngineActionFvtSupport.initiateEngineAction(engineName, "mixture-approved");
        String alsoApproved = EngineActionFvtSupport.initiateEngineAction(engineName, "mixture-also-approved");
        String inProgress = EngineActionFvtSupport.claimedAction(engineName, "mixture-in-progress");
        String finished   = EngineActionFvtSupport.claimedAction(engineName, "mixture-finished");

        EngineActionFvtSupport.setStatus(inProgress, ActivityStatus.IN_PROGRESS);
        EngineActionFvtSupport.complete(finished, CompletionStatus.ACTIONED);

        List<String> activeGUIDs = EngineActionFvtSupport.activeActionGUIDs();

        for (String liveGUID : List.of(approved, alsoApproved, inProgress))
        {
            assertTrue(activeGUIDs.contains(liveGUID),
                       "A live engine action should be in the active list, but " + liveGUID + " was missing");
        }

        assertFalse(activeGUIDs.contains(finished),
                    "A finished engine action should not be in the active list, but " + finished + " was there");
    }


    /**
     * Updating an engine action nobody has claimed should say that it has not been claimed, and not blame a
     * competing engine host.
     * <br><br>
     * Only the engine host that claimed an action may change its status, and there are two ways to fail that
     * test which need different responses from whoever is reading the message.  Both used to produce the same
     * one: {@code "not allowed to issue request updateEngineActionStatus ... because it is already being
     * processed by Engine Host OMAG Server with a userId of null"} - which says another engine host holds the
     * action while simultaneously showing that nobody does, and sends the reader looking for a contending
     * engine host that does not exist.
     * <br><br>
     * The genuine contention case still reports that way, and should: two engine hosts running the same
     * governance engine can both go for an action, and only one wins.  That is
     * {@link #updatingAnActionClaimedByAnotherEngineHostReportsTheContention}.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void updatingAnUnclaimedActionSaysItIsUnclaimed() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("unclaimed-update");

        String engineActionGUID = EngineActionFvtSupport.initiateEngineAction(engineName, "unclaimed-update-action");

        Exception error = assertThrows(Exception.class,
                                       () -> EngineActionFvtSupport.setStatus(engineActionGUID, ActivityStatus.IN_PROGRESS),
                                       "Updating the status of an engine action nobody has claimed should be refused");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains("has not been claimed"),
                   "The refusal should say the engine action has not been claimed.  It said: " + message);
        assertFalse(message.contains("userId of null"),
                    "The refusal should not name a processing engine host that does not exist.  It said: " + message);
    }


    /**
     * Updating an engine action that another engine host has claimed should report that contention, naming the
     * host that holds it.
     * <br><br>
     * This is the case the original message was written for - two engine hosts running the same governance
     * engine both claimed the action and only one was given it - and it stays as it was.  The pair of tests is
     * the point: the two failures are different situations and now read differently.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void updatingAnActionClaimedByAnotherEngineHostReportsTheContention() throws Exception
    {
        String engineName = EngineActionFvtSupport.createGovernanceEngine("contended");

        String engineActionGUID = EngineActionFvtSupport.claimedAction(engineName, "contended-action");

        /*
         * Claimed by this suite's user; a second engine host is another userId entirely.
         */
        Exception error = assertThrows(Exception.class,
                                       () -> EngineActionFvtSupport.setStatusAsOtherUser(engineActionGUID,
                                                                                         ActivityStatus.IN_PROGRESS),
                                       "A second engine host should not be able to update an action the first one claimed");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains("already being processed"),
                   "The refusal should say the engine action is already being processed.  It said: " + message);
        assertTrue(message.contains(OMAGPlatformExtension.USER_ID),
                   "The refusal should name the engine host that holds the engine action, so the loser of the race" +
                           " can see who won.  It said: " + message);
    }


    /**
     * Reading back an engine action should return what was stored against it, so that the assertions above are
     * checking the engine action's real state rather than a stale copy.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEngineActionReportsWhatWasStoredAgainstIt() throws Exception
    {
        String requestType = "server-fvt-request-" + UUID.randomUUID();
        String engineName  = EngineActionFvtSupport.createGovernanceEngine("readback", requestType);

        String engineActionGUID = EngineActionFvtSupport.initiateEngineAction(engineName, "readback-action", requestType);

        EngineActionElement engineAction = EngineActionFvtSupport.getEngineAction(engineActionGUID);

        assertNotNull(engineAction, "An engine action that was just created should be readable");
        assertEquals(requestType, engineAction.getRequestType(),
                     "The engine action should carry the request type it was created with");
        assertEquals(engineName, engineAction.getGovernanceEngineName(),
                     "The engine action should name the governance engine it was created for");
    }
}
