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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionExecutorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.NextGovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.TargetForGovernanceActionProperties;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceActionProcessGraph;
import org.odpi.openmetadata.frameworks.opengovernance.properties.NextGovernanceActionProcessStepLink;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.GovernanceActionProcessGraphResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateGovernanceActionProcessRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateGovernanceActionTypeRequestBody;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ActionAuthorFVT covers every request of the Action Author OMVS, which links governance action definitions
 * together so they can run: a governance action type to the governance engine that executes it and to its
 * predefined action targets, and a governance action process to its first step and each step to the next.
 * <br>
 * Each link is proved by using it.  The governance action definitions are created with the Governance Officer,
 * and Automated Curation's initiate requests (covered here, because they need these links) turn them into
 * engine actions: an initiated governance action type runs on the engine it is linked to, with its predefined
 * action target, and an initiated process starts at its first step.  The process graph from the Governance
 * Officer shows the first step and the links between steps.  Once its executor is detached, a governance action
 * type can no longer be initiated.
 * <br>
 * The engine actions are recorded APPROVED in the metadata store and no engine host runs in this suite, so
 * they are cancelled (directly, or by cancelling the process instance) and deleted.  The engine is the Core
 * Content Pack's Stewardship engine and its "write-to-audit-log" request type.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActionAuthorFVT
{
    private static final ViewServiceClient actionAuthor      = new ViewServiceClient(ViewServiceDescription.ACTION_AUTHOR);
    private static final ViewServiceClient governanceOfficer = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    private static final ViewServiceClient automatedCuration = new ViewServiceClient(ViewServiceDescription.AUTOMATED_CURATION);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    /*
     * The Core Content Pack's Stewardship governance engine.
     */
    private static final String STEWARDSHIP_ENGINE_GUID = "c79ada2b-15ae-4194-b47e-6171591cf5fd";

    private static final String TARGET_NAME = "auditedElement";
    private static final String GUARD       = "audit-log-written";

    private static final String governanceActionTypeQN = ViewServicesFvtTestSupport.qualifiedName("GovernanceActionType", "audit");
    private static final String processQN              = ViewServicesFvtTestSupport.qualifiedName("GovernanceActionProcess", "audit");

    private static String targetGUID               = null;
    private static String governanceActionTypeGUID = null;
    private static String processGUID              = null;
    private static String firstStepGUID            = null;
    private static String secondStepGUID           = null;
    private static String nextStepLinkGUID         = null;
    private static String processInstanceGUID      = null;

    private static final List<String> engineActionGUIDs = new ArrayList<>();


    /**
     * A governance action type linked to the Stewardship engine and to a predefined action target, when
     * initiated, becomes an engine action for that engine with that action target.
     */
    @Test
    @Order(1)
    void linkExecutorAndTarget()
    {
        targetGUID = AutomatedCurationFVT.createTargetAsset("action-author-target");

        GovernanceActionTypeProperties properties = new GovernanceActionTypeProperties();

        properties.setQualifiedName(governanceActionTypeQN);
        properties.setDisplayName("Write to the audit log");
        properties.setProducedGuards(List.of(GUARD));

        governanceActionTypeGUID = governanceOfficer.create("/governance-definitions", newElement(properties));

        actionAuthor.call("/governance-action-types/" + governanceActionTypeGUID + "/governance-engine-executor/" + STEWARDSHIP_ENGINE_GUID + "/attach",
                          executor());

        TargetForGovernanceActionProperties targetProperties = new TargetForGovernanceActionProperties();

        targetProperties.setActionTargetName(TARGET_NAME);

        actionAuthor.call("/governance-actions/" + governanceActionTypeGUID + "/action-targets/" + targetGUID + "/attach",
                          newRelationship(targetProperties));

        String engineActionGUID = initiateGovernanceActionType();

        EngineActionProperties engineAction = AutomatedCurationFVT.readEngineAction(engineActionGUID);

        assertEquals(ActivityStatus.APPROVED, engineAction.getActivityStatus());
        assertEquals(AutomatedCurationFVT.STEWARDSHIP_ENGINE_NAME, engineAction.getExecutorEngineName());
        assertEquals(AutomatedCurationFVT.WRITE_TO_AUDIT_LOG, engineAction.getRequestType());
        assertEquals(governanceActionTypeGUID, engineAction.getGovernanceActionTypeGUID());

        EngineActionElement active = AutomatedCurationFVT.findActiveEngineAction(engineActionGUID);

        assertNotNull(active, "the engine action initiated from the governance action type is not active");
        assertNotNull(active.getActionTargetElements(), "the engine action has no action targets");
        assertTrue(active.getActionTargetElements().stream().anyMatch(target -> targetGUID.equals(target.getActionTargetGUID()) &&
                                                                                       TARGET_NAME.equals(target.getActionTargetName())),
                   "the engine action does not have the governance action type's predefined action target");

        cancel(engineActionGUID);
    }


    /**
     * Once the action target is detached, an initiated governance action type has no action target; once the
     * executor is detached, it cannot be initiated at all.
     */
    @Test
    @Order(2)
    void detachTargetAndExecutor()
    {
        actionAuthor.call("/governance-actions/" + governanceActionTypeGUID + "/action-targets/" + targetGUID + "/detach",
                          new DeleteRelationshipRequestBody());

        String engineActionGUID = initiateGovernanceActionType();

        EngineActionElement active = AutomatedCurationFVT.findActiveEngineAction(engineActionGUID);

        assertNotNull(active, "the engine action initiated from the governance action type is not active");
        assertTrue((active.getActionTargetElements() == null) ||
                           active.getActionTargetElements().stream().noneMatch(target -> targetGUID.equals(target.getActionTargetGUID())),
                   "the detached action target is still passed to the engine action");

        cancel(engineActionGUID);

        actionAuthor.call("/governance-action-types/" + governanceActionTypeGUID + "/governance-engine-executor/" + STEWARDSHIP_ENGINE_GUID + "/detach",
                          new DeleteRelationshipRequestBody());

        GUIDResponse refused = automatedCuration.postUnchecked("/governance-action-types/initiate", initiateTypeRequest(), GUIDResponse.class);

        assertNotNull(refused);
        assertTrue(refused.getRelatedHTTPCode() != 200, "a governance action type with no executor was initiated as " + refused.getGUID());

        if (refused.getGUID() != null)
        {
            engineActionGUIDs.add(refused.getGUID());
        }
    }


    /**
     * A process's first step and the link to its next step are shown in the process graph, and an update to the
     * link's guard is visible there.
     */
    @Test
    @Order(3)
    void linkProcessSteps()
    {
        GovernanceActionProcessProperties processProperties = new GovernanceActionProcessProperties();

        processProperties.setQualifiedName(processQN);
        processProperties.setDisplayName("Audit process");

        processGUID    = governanceOfficer.create("/governance-definitions", newElement(processProperties));
        firstStepGUID  = createStep("first");
        secondStepGUID = createStep("second");

        GovernanceActionProcessFlowProperties flowProperties = new GovernanceActionProcessFlowProperties();

        flowProperties.setGuard("start");

        actionAuthor.call("/governance-action-processes/" + processGUID + "/first-process-step/" + firstStepGUID + "/attach",
                          newRelationship(flowProperties));

        NextGovernanceActionProcessStepProperties nextProperties = new NextGovernanceActionProcessStepProperties();

        nextProperties.setGuard(GUARD);
        nextProperties.setMandatoryGuard(false);

        nextStepLinkGUID = actionAuthor.create("/governance-action-process-steps/" + firstStepGUID + "/next-process-steps/" + secondStepGUID + "/attach",
                                               newRelationship(nextProperties));

        GovernanceActionProcessGraph graph = processGraph();

        assertNotNull(graph.getFirstProcessStep(), "the process graph has no first step");
        assertEquals(firstStepGUID, graph.getFirstProcessStep().getElement().getElementHeader().getGUID());
        assertEquals("start", graph.getFirstProcessStep().getGuard());

        NextGovernanceActionProcessStepLink link = nextStepLink(graph);

        assertNotNull(link, "the process graph does not show the link to the next step " + nextStepLinkGUID);
        assertEquals(firstStepGUID, link.getPreviousProcessStep().getGUID());
        assertEquals(secondStepGUID, link.getNextProcessStep().getGUID());
        assertEquals(GUARD, link.getGuard());

        NextGovernanceActionProcessStepProperties updatedProperties = new NextGovernanceActionProcessStepProperties();

        updatedProperties.setGuard(GUARD + "-updated");
        updatedProperties.setMandatoryGuard(true);

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setProperties(updatedProperties);

        actionAuthor.call("/governance-action-process-steps/next-process-steps/" + nextStepLinkGUID + "/update", updateRequestBody);

        NextGovernanceActionProcessStepLink updatedLink = nextStepLink(processGraph());

        assertNotNull(updatedLink, "the updated link to the next step is missing from the process graph");
        assertEquals(GUARD + "-updated", updatedLink.getGuard());
        assertTrue(updatedLink.getMandatoryGuard(), "the update to mandatoryGuard is not visible");
    }


    /**
     * An initiated process starts with an engine action for its first step, and cancelling the process
     * instance cancels that engine action.
     */
    @Test
    @Order(4)
    void initiateAndCancelProcess()
    {
        InitiateGovernanceActionProcessRequestBody requestBody = new InitiateGovernanceActionProcessRequestBody();

        requestBody.setProcessQualifiedName(processQN);

        processInstanceGUID = automatedCuration.create("/governance-action-processes/initiate", requestBody);

        List<String> firstStepActions = new ArrayList<>();

        List<EngineActionElement> activeEngineActions = automatedCuration.get("/engine-actions/active?startFrom=0&pageSize=0",
                                                                              EngineActionElementsResponse.class).getElements();

        if (activeEngineActions != null)
        {
            for (EngineActionElement engineAction : activeEngineActions)
            {
                if (firstStepGUID.equals(engineAction.getProcessStepGUID()))
                {
                    firstStepActions.add(engineAction.getElementHeader().getGUID());
                }
            }
        }

        assertEquals(1, firstStepActions.size(), "the process instance should have started one engine action for its first step");

        engineActionGUIDs.addAll(firstStepActions);

        automatedCuration.call("/governance-action-process-instances/" + processInstanceGUID + "/cancel", null);

        assertEquals(ActivityStatus.CANCELLED, AutomatedCurationFVT.readEngineAction(firstStepActions.get(0)).getActivityStatus(),
                     "cancelling the process instance did not cancel its engine action");
        assertNull(AutomatedCurationFVT.findActiveEngineAction(firstStepActions.get(0)), "the process's engine action is still active");
    }


    /**
     * Removing the links leaves the process with no first step and no links between steps.
     */
    @Test
    @Order(5)
    void removeProcessStepLinks()
    {
        actionAuthor.call("/governance-action-process-steps/next-process-step/" + nextStepLinkGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNull(nextStepLink(processGraph()), "the detached link to the next step is still in the process graph");

        actionAuthor.call("/governance-action-processes/" + processGUID + "/first-process-step/" + firstStepGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNull(processGraph().getFirstProcessStep(), "the detached first step is still in the process graph");
    }


    /**
     * A process instance is anchored to its process.  Once the instance has been deleted it stays in the
     * repository as a Memento, which is only visible to a request that sets forLineage.  So deleting the process,
     * whose delete cascade reads the instance, is refused with OMAG-REPOSITORY-HANDLER-400-010 unless it sets
     * forLineage=true, and succeeds when it does.
     */
    @Test
    @Order(6)
    void deleteProcessAfterItsInstanceNeedsForLineage()
    {
        GovernanceActionProcessProperties processProperties = new GovernanceActionProcessProperties();
        String                            secondProcessQN   = ViewServicesFvtTestSupport.qualifiedName("GovernanceActionProcess", "deleted-instance");

        processProperties.setQualifiedName(secondProcessQN);
        processProperties.setDisplayName("Process whose instance is deleted first");

        String secondProcessGUID = governanceOfficer.create("/governance-definitions", newElement(processProperties));
        String stepGUID          = createStep("deleted-instance");

        actionAuthor.call("/governance-action-processes/" + secondProcessGUID + "/first-process-step/" + stepGUID + "/attach",
                          newRelationship(new GovernanceActionProcessFlowProperties()));

        InitiateGovernanceActionProcessRequestBody requestBody = new InitiateGovernanceActionProcessRequestBody();

        requestBody.setProcessQualifiedName(secondProcessQN);

        String instanceGUID = automatedCuration.create("/governance-action-processes/initiate", requestBody);

        automatedCuration.call("/governance-action-process-instances/" + instanceGUID + "/cancel", null);

        assetMaker.call("/assets/" + instanceGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + instanceGUID + "/retrieve");

        String refusal = governanceOfficer.postForFailure("/governance-definitions/" + secondProcessGUID + "/delete", new DeleteElementRequestBody())
                                          .path("exceptionErrorMessage").asText();

        assertTrue(refusal.contains("OMAG-REPOSITORY-HANDLER-400-010"),
                   "the delete without forLineage was refused for the wrong reason: " + refusal);

        DeleteElementRequestBody forLineage = new DeleteElementRequestBody();

        forLineage.setForLineage(true);

        governanceOfficer.call("/governance-definitions/" + secondProcessGUID + "/delete", forLineage);
        ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + secondProcessGUID + "/retrieve");

        governanceOfficer.call("/governance-definitions/" + stepGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + stepGUID + "/retrieve");
    }


    /**
     * Delete what the tests created.  The governance definitions go first: the process instance is anchored to
     * its process, and deleting the process after the instance needs forLineage - see
     * deleteProcessAfterItsInstanceNeedsForLineage.
     * Deleting the process may take its instance with it, so the asset deletes are not checked - only that
     * everything is gone afterwards.
     */
    @AfterAll
    static void cleanUp()
    {
        for (String guid : new String[]{processGUID, secondStepGUID, firstStepGUID, governanceActionTypeGUID})
        {
            if (guid != null)
            {
                governanceOfficer.call("/governance-definitions/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + guid + "/retrieve");
            }
        }

        List<String> assets = new ArrayList<>(engineActionGUIDs);

        assets.add(processInstanceGUID);
        assets.add(targetGUID);

        for (String guid : assets)
        {
            if (guid != null)
            {
                assetMaker.postUnchecked("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
                ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
            }
        }
    }


    private static String initiateGovernanceActionType()
    {
        String engineActionGUID = automatedCuration.create("/governance-action-types/initiate", initiateTypeRequest());

        engineActionGUIDs.add(engineActionGUID);

        return engineActionGUID;
    }


    private static InitiateGovernanceActionTypeRequestBody initiateTypeRequest()
    {
        InitiateGovernanceActionTypeRequestBody requestBody = new InitiateGovernanceActionTypeRequestBody();

        requestBody.setGovernanceActionTypeQualifiedName(governanceActionTypeQN);

        return requestBody;
    }


    private static void cancel(String engineActionGUID)
    {
        automatedCuration.call("/engine-actions/" + engineActionGUID + "/cancel", null);

        assertEquals(ActivityStatus.CANCELLED, AutomatedCurationFVT.readEngineAction(engineActionGUID).getActivityStatus());
    }


    private static String createStep(String name)
    {
        GovernanceActionProcessStepProperties properties = new GovernanceActionProcessStepProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GovernanceActionProcessStep", name));
        properties.setDisplayName("Audit step " + name);
        properties.setProducedGuards(List.of(GUARD));

        String stepGUID = governanceOfficer.create("/governance-definitions", newElement(properties));

        actionAuthor.call("/governance-action-types/" + stepGUID + "/governance-engine-executor/" + STEWARDSHIP_ENGINE_GUID + "/attach", executor());

        return stepGUID;
    }


    private static NewRelationshipRequestBody executor()
    {
        GovernanceActionExecutorProperties properties = new GovernanceActionExecutorProperties();

        properties.setRequestType(AutomatedCurationFVT.WRITE_TO_AUDIT_LOG);

        return newRelationship(properties);
    }


    private static GovernanceActionProcessGraph processGraph()
    {
        GovernanceActionProcessGraph graph = governanceOfficer.post("/governance-action-processes/" + processGUID + "/graph",
                                                                    new ResultsRequestBody(),
                                                                    GovernanceActionProcessGraphResponse.class).getElement();

        assertNotNull(graph, "no graph for process " + processGUID);

        return graph;
    }


    private static NextGovernanceActionProcessStepLink nextStepLink(GovernanceActionProcessGraph graph)
    {
        if (graph.getProcessStepLinks() != null)
        {
            for (NextGovernanceActionProcessStepLink link : graph.getProcessStepLinks())
            {
                if (nextStepLinkGUID.equals(link.getNextProcessStepLinkGUID()))
                {
                    return link;
                }
            }
        }

        return null;
    }


    private static NewElementRequestBody newElement(org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static NewRelationshipRequestBody newRelationship(org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties properties)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }
}
