/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CompletionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.ActionTargetStatusRequestBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GovernanceEngineFVT covers every request of the Governance Engine OMVS, the API an external governance
 * engine uses to run engine actions: find the approved engine actions for its engine, claim one, report its
 * progress and the status of each action target, and record its completion.
 * <br>
 * Every request reads or updates the engine action in the metadata store, so no engine host is needed.  The
 * engine action is initiated through Automated Curation for the Core Content Pack's Stewardship engine.  No
 * engine host runs in this suite, so nothing else claims it.  The suite's userId plays the external engine.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GovernanceEngineFVT
{
    private static final ViewServiceClient governanceEngine = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_ENGINE);
    private static final ViewServiceClient assetMaker       = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final String OUTPUT_GUARD = "audit-log-written";

    private static String targetGUID           = null;
    private static String engineActionGUID     = null;
    private static String governanceEngineGUID = null;


    /**
     * A newly initiated engine action is listed among its engine's approved engine actions, unclaimed.
     */
    @Test
    @Order(1)
    void listApprovedEngineActions()
    {
        targetGUID       = AutomatedCurationFVT.createTargetAsset("governance-engine-target");
        engineActionGUID = AutomatedCurationFVT.initiateEngineAction("governance-engine", targetGUID);

        EngineActionProperties engineAction = AutomatedCurationFVT.readEngineAction(engineActionGUID);

        governanceEngineGUID = engineAction.getExecutorEngineGUID();

        assertNotNull(governanceEngineGUID, "the engine action does not name its governance engine");

        EngineActionElement approved = find(approvedEngineActions());

        assertNotNull(approved, "getApprovedEngineActions does not list engine action " + engineActionGUID);
        assertEquals(ActivityStatus.APPROVED, approved.getActionStatus());
        assertNull(approved.getProcessingEngineUserId(), "an approved engine action should not be claimed");
        assertNull(find(activeClaimedEngineActions()), "an unclaimed engine action is listed as claimed");
    }


    /**
     * Claiming the engine action makes it ACTIVATING and claimed by the caller, so it moves from the approved
     * list to the caller's claimed list.  A second claim is refused.
     */
    @Test
    @Order(2)
    void claimEngineAction()
    {
        governanceEngine.call("/engine-actions/" + engineActionGUID + "/claim", null);

        EngineActionProperties engineAction = AutomatedCurationFVT.readEngineAction(engineActionGUID);

        assertEquals(ActivityStatus.ACTIVATING, engineAction.getActivityStatus());
        assertEquals(OMAGPlatformExtension.USER_ID, engineAction.getProcessingEngineUserId());

        assertNull(find(approvedEngineActions()), "a claimed engine action is still listed as approved");
        assertNotNull(find(activeClaimedEngineActions()), "getActiveClaimedEngineActions does not list the claimed engine action");

        governanceEngine.postForFailure("/engine-actions/" + engineActionGUID + "/claim", null);
    }


    /**
     * The engine reports that the work is in progress, and then that its action target is finished.
     */
    @Test
    @Order(3)
    void updateStatuses()
    {
        EngineActionStatusRequestBody statusRequestBody = new EngineActionStatusRequestBody();

        statusRequestBody.setStatus(ActivityStatus.IN_PROGRESS);

        governanceEngine.call("/engine-actions/" + engineActionGUID + "/status/update", statusRequestBody);

        assertEquals(ActivityStatus.IN_PROGRESS, AutomatedCurationFVT.readEngineAction(engineActionGUID).getActivityStatus());

        EngineActionElement claimed = find(activeClaimedEngineActions());

        assertNotNull(claimed, "an engine action in progress is not listed as claimed");
        assertEquals(ActivityStatus.IN_PROGRESS, claimed.getActionStatus());

        ActionTargetElement actionTarget = actionTarget(claimed);

        Date completionDate = new Date();

        ActionTargetStatusRequestBody targetRequestBody = new ActionTargetStatusRequestBody();

        targetRequestBody.setActionTargetGUID(actionTarget.getActionTargetRelationshipGUID());
        targetRequestBody.setStatus(ActivityStatus.COMPLETED);
        targetRequestBody.setStartDate(completionDate);
        targetRequestBody.setCompletionDate(completionDate);
        targetRequestBody.setCompletionMessage("Target audited");

        governanceEngine.call("/engine-actions/action-targets/update", targetRequestBody);

        ActionTargetElement updatedTarget = actionTarget(find(activeClaimedEngineActions()));

        assertEquals(ActivityStatus.COMPLETED, updatedTarget.getStatus());
        assertEquals("Target audited", updatedTarget.getCompletionMessage());
    }


    /**
     * Recording the completion status finishes the engine action: it is COMPLETED with the output guard and
     * message, and is no longer listed as claimed.
     */
    @Test
    @Order(4)
    void recordCompletionStatus()
    {
        CompletionStatusRequestBody requestBody = new CompletionStatusRequestBody();

        requestBody.setStatus(CompletionStatus.ACTIONED);
        requestBody.setOutputGuards(List.of(OUTPUT_GUARD));
        requestBody.setRequestParameters(Map.of("result", "written"));
        requestBody.setCompletionMessage("Audit log written");

        governanceEngine.call("/engine-actions/" + engineActionGUID + "/completion-status", requestBody);

        EngineActionProperties engineAction = AutomatedCurationFVT.readEngineAction(engineActionGUID);

        assertEquals(ActivityStatus.COMPLETED, engineAction.getActivityStatus());
        assertEquals("Audit log written", engineAction.getCompletionMessage());
        assertNotNull(engineAction.getCompletionGuards());
        assertTrue(engineAction.getCompletionGuards().contains(OUTPUT_GUARD), "the output guard was not recorded: " + engineAction.getCompletionGuards());

        assertNull(find(activeClaimedEngineActions()), "a completed engine action is still listed as claimed");
        assertNull(find(approvedEngineActions()), "a completed engine action is listed as approved");
    }


    /**
     * Delete what the tests created.
     */
    @AfterAll
    static void cleanUp()
    {
        for (String guid : new String[]{engineActionGUID, targetGUID})
        {
            if (guid != null)
            {
                assetMaker.post("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
                ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
            }
        }
    }


    private static List<EngineActionElement> approvedEngineActions()
    {
        return governanceEngine.get("/governance-engines/" + governanceEngineGUID + "/engine-actions/approved?startFrom=0&pageSize=0",
                                    EngineActionElementsResponse.class).getElements();
    }


    private static List<EngineActionElement> activeClaimedEngineActions()
    {
        return governanceEngine.get("/governance-engines/" + governanceEngineGUID + "/engine-actions/active-claimed?startFrom=0&pageSize=0",
                                    EngineActionElementsResponse.class).getElements();
    }


    private static EngineActionElement find(List<EngineActionElement> engineActions)
    {
        if (engineActions != null)
        {
            for (EngineActionElement engineAction : engineActions)
            {
                if (engineActionGUID.equals(engineAction.getElementHeader().getGUID()))
                {
                    return engineAction;
                }
            }
        }

        return null;
    }


    private static ActionTargetElement actionTarget(EngineActionElement engineAction)
    {
        assertNotNull(engineAction);
        assertNotNull(engineAction.getActionTargetElements(), "the engine action has no action targets");

        for (ActionTargetElement actionTarget : engineAction.getActionTargetElements())
        {
            if (targetGUID.equals(actionTarget.getActionTargetGUID()))
            {
                assertNotNull(actionTarget.getActionTargetRelationshipGUID(), "the action target has no relationship GUID");

                return actionTarget;
            }
        }

        throw new AssertionError("the engine action does not have action target " + targetGUID);
    }
}
