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
import org.odpi.openmetadata.commonservices.ffdc.rest.ActionRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActivityStatusRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFVT.contains;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;

/**
 * AssetMakerActionsFVT covers the Asset Maker's action requests: creating an action with an originator, sponsor
 * and assignee, its action targets (attach, retrieve, update, list from both ends with activity status filters),
 * assigning, reassigning and unassigning actors, and the sponsor/requester/assignee queries.
 * <br>
 * The actors are Person profiles created through the Actor Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetMakerActionsFVT
{
    private static final ViewServiceClient actorManager   = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final PropertyHelper    propertyHelper = new PropertyHelper();

    private static String requesterGUID    = null;
    private static String firstActorGUID   = null;
    private static String secondActorGUID  = null;
    private static String targetGUID       = null;
    private static String actionGUID       = null;
    private static String actionTargetGUID = null;

    private static final List<String> actorGUIDs = new ArrayList<>();
    private static final List<String> assetGUIDs = new ArrayList<>();


    /**
     * Create the actors and the element the action targets.
     */
    @Test
    @Order(1)
    void createActorsAndTarget()
    {
        requesterGUID   = createPerson("action-requester");
        firstActorGUID  = createPerson("first-assignee");
        secondActorGUID = createPerson("second-assignee");

        targetGUID = AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", "action-target"));
        assetGUIDs.add(targetGUID);
    }


    /**
     * createAction links the action to its originator, sponsor and assignee, and the three queries return it.
     */
    @Test
    @Order(2)
    void createAction()
    {
        ActionProperties properties = named(new ActionProperties(), "Action", "asset-maker-action");

        properties.setSituation("The view services FVT needs something done");
        properties.setActivityStatus(ActivityStatus.REQUESTED);

        ActionRequestBody requestBody = new ActionRequestBody();

        requestBody.setProperties(properties);
        requestBody.setOriginatorGUID(requesterGUID);
        requestBody.setActionSponsorGUID(requesterGUID);
        requestBody.setAssignToActorGUID(firstActorGUID);

        actionGUID = assetMaker.create("/actions", requestBody);
        assetGUIDs.add(actionGUID);

        OpenMetadataRootElement action = assetMaker.element("/assets/" + actionGUID + "/retrieve", new GetRequestBody());

        assertNotNull(action);
        assertTrue(action.getProperties() instanceof ActionProperties actionProperties &&
                           properties.getQualifiedName().equals(actionProperties.getQualifiedName()) &&
                           ActivityStatus.REQUESTED == actionProperties.getActivityStatus(),
                   "the action's properties are wrong: " + action.getProperties());

        assertTrue(contains(assetMaker.elements("/elements/" + requesterGUID + "/sponsored/actions", new ActivityStatusRequestBody()), actionGUID),
                   "getActionsForSponsor did not return the action");
        assertTrue(contains(assetMaker.elements("/elements/" + requesterGUID + "/requested/actions", new ActivityStatusRequestBody()), actionGUID),
                   "getActionsFromRequester did not return the action");
        assertTrue(contains(assetMaker.elements("/actors/" + firstActorGUID + "/assigned/actions", new ActivityStatusRequestBody()), actionGUID),
                   "getAssignedActions did not return the action");

        ActivityStatusRequestBody completedOnly = new ActivityStatusRequestBody();

        completedOnly.setActivityStatusList(List.of(ActivityStatus.COMPLETED));

        assertFalse(contains(assetMaker.elements("/actors/" + firstActorGUID + "/assigned/actions", completedOnly), actionGUID),
                    "getAssignedActions returned a requested action for COMPLETED");
        assertFalse(contains(assetMaker.elements("/elements/" + secondActorGUID + "/sponsored/actions", new ActivityStatusRequestBody()), actionGUID),
                    "getActionsForSponsor returned the action for someone who is not its sponsor");
    }


    /**
     * An action target is attached, read back by its relationship GUID and listed from both ends.
     */
    @Test
    @Order(3)
    void addActionTarget()
    {
        ActionTargetProperties properties = new ActionTargetProperties();

        properties.setActionTargetName("file-to-fix");
        properties.setActivityStatus(ActivityStatus.IN_PROGRESS);

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        actionTargetGUID = assetMaker.create("/actions/" + actionGUID + "/action-targets/" + targetGUID + "/attach", requestBody);

        OpenMetadataRelationship relationship = getActionTarget();

        assertEquals(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName, relationship.getRelationshipType().getTypeName());
        assertEquals(actionGUID, relationship.getElementGUIDAtEnd1());
        assertEquals(targetGUID, relationship.getElementGUIDAtEnd2());
        assertEquals("file-to-fix", propertyHelper.getStringProperty("test", "actionTargetName", relationship.getRelationshipProperties(), "addActionTarget"));

        ActivityStatusRequestBody inProgress = new ActivityStatusRequestBody();

        inProgress.setActivityStatusList(List.of(ActivityStatus.IN_PROGRESS));

        assertTrue(contains(assetMaker.elements("/actions/" + actionGUID + "/action-targets", inProgress), targetGUID),
                   "getActionTargets did not return the in-progress target");
        assertTrue(contains(assetMaker.elements("/elements/" + targetGUID + "/action-targets/actions", new ActivityStatusRequestBody()), actionGUID),
                   "getActionsForActionTarget did not return the action");

        AssetMakerFvtSupport.assertLinked(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName, actionGUID, targetGUID);
    }


    /**
     * Updating the action target's status moves it between the status filters.
     */
    @Test
    @Order(4)
    void updateActionTarget()
    {
        ActionTargetProperties properties = new ActionTargetProperties();

        properties.setActionTargetName("file-to-fix");
        properties.setActivityStatus(ActivityStatus.COMPLETED);
        properties.setCompletionMessage("Fixed by the view services FVT");

        UpdateRelationshipRequestBody requestBody = new UpdateRelationshipRequestBody();

        requestBody.setProperties(properties);

        assetMaker.call("/actions/action-targets/" + actionTargetGUID + "/update", requestBody);

        assertEquals("Fixed by the view services FVT",
                     propertyHelper.getStringProperty("test", "completionMessage", getActionTarget().getRelationshipProperties(), "updateActionTarget"));

        ActivityStatusRequestBody statusFilter = new ActivityStatusRequestBody();

        statusFilter.setActivityStatusList(List.of(ActivityStatus.COMPLETED));

        assertTrue(contains(assetMaker.elements("/actions/" + actionGUID + "/action-targets", statusFilter), targetGUID),
                   "getActionTargets did not return the completed target");

        statusFilter.setActivityStatusList(List.of(ActivityStatus.IN_PROGRESS));

        assertFalse(contains(assetMaker.elements("/actions/" + actionGUID + "/action-targets", statusFilter), targetGUID),
                    "getActionTargets still returns the target as in progress");
        assertFalse(contains(assetMaker.elements("/elements/" + targetGUID + "/action-targets/actions", statusFilter), actionGUID),
                    "getActionsForActionTarget still returns the action for an in-progress target");
    }


    /**
     * assignAction adds an assignee, reassignAction replaces all assignees, and unassignAction removes one.
     */
    @Test
    @Order(5)
    void assignReassignAndUnassign()
    {
        AssignmentScopeProperties properties = new AssignmentScopeProperties();

        properties.setAssignmentType("Contributor");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        assetMaker.call("/actions/" + actionGUID + "/assign/" + secondActorGUID, requestBody);

        assertTrue(isAssigned(firstActorGUID), "assignAction removed the original assignee");
        assertTrue(isAssigned(secondActorGUID), "assignAction did not assign the second actor");

        assetMaker.call("/actions/" + actionGUID + "/reassign/" + secondActorGUID, new NewRelationshipRequestBody());

        assertFalse(isAssigned(firstActorGUID), "reassignAction left the first actor assigned");
        assertTrue(isAssigned(secondActorGUID), "reassignAction did not assign the second actor");

        assetMaker.call("/actions/" + actionGUID + "/unassign/" + secondActorGUID, new DeleteRelationshipRequestBody());

        assertFalse(isAssigned(secondActorGUID), "unassignAction left the second actor assigned");
    }


    /**
     * Delete the action's target, then the action, then the actors.  The target is deleted first: deleting the
     * action first makes it a memento (actions are kept for lineage), and the target then can only be deleted with
     * forLineage set - see deleteTargetOfDeletedActionNeedsForLineage.
     */
    @Test
    @Order(6)
    void deleteEverything()
    {
        for (String guid : List.copyOf(assetGUIDs))
        {
            AssetMakerFvtSupport.delete(guid);
            assetGUIDs.remove(guid);
        }

        for (String guid : List.copyOf(actorGUIDs))
        {
            actorManager.call("/actor-profiles/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + guid + "/retrieve");
            actorGUIDs.remove(guid);
        }
    }


    /**
     * Deleting an action that has lineage relationships (here ActionRequester) makes it a memento that keeps its
     * relationships, and a memento is only visible to a request that sets forLineage.  So the delete of an element
     * the deleted action targeted is refused with OMAG-REPOSITORY-HANDLER-400-010 unless it sets forLineage=true,
     * and succeeds when it does.  The refused delete changes nothing - the target and its ActionTarget
     * relationship are both still there.
     */
    @Test
    @Order(7)
    void deleteTargetOfDeletedActionNeedsForLineage()
    {
        String target = AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", "target-of-deleted-action"));

        assetGUIDs.add(target);

        ActionRequestBody requestBody = new ActionRequestBody();

        requestBody.setProperties(named(new ActionProperties(), "Action", "deleted-action"));
        requestBody.setOriginatorGUID(createPerson("deleted-action-requester"));

        String action = assetMaker.create("/actions", requestBody);

        String link = assetMaker.create("/actions/" + action + "/action-targets/" + target + "/attach", targetRequestBody());

        AssetMakerFvtSupport.delete(action);

        String refusal = assetMaker.postForFailure("/assets/" + target + "/delete", new DeleteElementRequestBody())
                                   .path("exceptionErrorMessage").asText();

        assertTrue(refusal.contains("OMAG-REPOSITORY-HANDLER-400-010"),
                   "the delete without forLineage was refused for the wrong reason: " + refusal);
        assertNotNull(assetMaker.element("/assets/" + target + "/retrieve", new GetRequestBody()),
                      "the refused delete removed the target anyway");

        GetRequestBody lineageRead = new GetRequestBody();

        lineageRead.setForLineage(true);

        assertNotNull(assetMaker.post("/actions/action-targets/" + link + "/retrieve", lineageRead, OpenMetadataRelationshipResponse.class).getElement(),
                      "the refused delete removed the ActionTarget relationship anyway");

        DeleteElementRequestBody forLineage = new DeleteElementRequestBody();

        forLineage.setForLineage(true);

        assetMaker.call("/assets/" + target + "/delete", forLineage);
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + target + "/retrieve");

        assetGUIDs.remove(target);
    }


    private static NewRelationshipRequestBody targetRequestBody()
    {
        ActionTargetProperties properties = new ActionTargetProperties();

        properties.setActionTargetName("target");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    @AfterAll
    static void cleanUp()
    {
        for (String guid : assetGUIDs)
        {
            assetMaker.postUnchecked("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
        }

        for (String guid : actorGUIDs)
        {
            actorManager.postUnchecked("/actor-profiles/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
        }
    }


    private static String createPerson(String name)
    {
        PersonProperties properties = named(new PersonProperties(), "Person", name);

        properties.setFullName(name);

        String guid = actorManager.create("/actor-profiles", AssetMakerFvtSupport.newElement(properties));

        actorGUIDs.add(guid);

        return guid;
    }


    private static OpenMetadataRelationship getActionTarget()
    {
        OpenMetadataRelationship relationship = assetMaker.post("/actions/action-targets/" + actionTargetGUID + "/retrieve",
                                                                new GetRequestBody(),
                                                                OpenMetadataRelationshipResponse.class).getElement();

        assertNotNull(relationship, "getActionTarget returned nothing");
        assertEquals(actionTargetGUID, relationship.getRelationshipGUID());

        return relationship;
    }


    private static boolean isAssigned(String actorGUID)
    {
        return contains(assetMaker.elements("/actors/" + actorGUID + "/assigned/actions", new ActivityStatusRequestBody()), actionGUID);
    }
}
