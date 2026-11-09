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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.MonitoredResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;

/**
 * RootElementMermaidGraphsFVT checks, on a real element read back through a view service, that an
 * OpenMetadataRootElement's relationships are split between its three flowcharts by relationship purpose.
 * <br>
 * The root element is an Action with:
 * <ul>
 *     <li>an assignee (AssignmentScope - purpose Assignment), which belongs in the context graph (mermaidGraph);</li>
 *     <li>a requester (ActionRequester) and an action target (ActionTarget) - purpose Activity - which belong in
 *     activityMermaidGraph only;</li>
 *     <li>a notification type monitoring it (MonitoredResource - purpose Action), which belongs in
 *     actionMermaidGraph only.</li>
 * </ul>
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RootElementMermaidGraphsFVT
{
    private static final ViewServiceClient actorManager        = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final ViewServiceClient notificationManager = new ViewServiceClient(ViewServiceDescription.NOTIFICATION_MANAGER);

    private static final String REQUESTER         = "graph-split-requester";
    private static final String ASSIGNEE          = "graph-split-assignee";
    private static final String TARGET            = "graph-split-target";
    private static final String NOTIFICATION_TYPE = "graph-split-notification-type";

    private static String actionGUID           = null;
    private static String notificationTypeGUID = null;

    private static final List<String> assetGUIDs = new ArrayList<>();
    private static final List<String> actorGUIDs = new ArrayList<>();


    /**
     * Build the action and its four kinds of relationship.
     */
    @Test
    @Order(1)
    void createActionWithRelationshipsOfEachPurpose()
    {
        String requesterGUID = createPerson(REQUESTER);
        String assigneeGUID  = createPerson(ASSIGNEE);
        String targetGUID    = AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", TARGET));

        assetGUIDs.add(targetGUID);

        ActionProperties actionProperties = named(new ActionProperties(), "Action", "graph-split-action");

        actionProperties.setActivityStatus(ActivityStatus.REQUESTED);

        ActionRequestBody actionRequestBody = new ActionRequestBody();

        actionRequestBody.setProperties(actionProperties);
        actionRequestBody.setOriginatorGUID(requesterGUID);
        actionRequestBody.setAssignToActorGUID(assigneeGUID);

        actionGUID = assetMaker.create("/actions", actionRequestBody);
        assetGUIDs.add(0, actionGUID);

        ActionTargetProperties actionTargetProperties = new ActionTargetProperties();

        actionTargetProperties.setActionTargetName("graph-split-target");

        NewRelationshipRequestBody actionTargetRequestBody = new NewRelationshipRequestBody();

        actionTargetRequestBody.setProperties(actionTargetProperties);

        assetMaker.create("/actions/" + actionGUID + "/action-targets/" + targetGUID + "/attach", actionTargetRequestBody);

        NotificationTypeProperties notificationTypeProperties = named(new NotificationTypeProperties(), "NotificationType", NOTIFICATION_TYPE);

        notificationTypeGUID = ClassificationExplorerFvtFixtures.createGovernanceDefinition(notificationTypeProperties);

        MonitoredResourceProperties monitoredResourceProperties = new MonitoredResourceProperties();

        monitoredResourceProperties.setLabel("watched");

        NewRelationshipRequestBody monitoredResourceRequestBody = new NewRelationshipRequestBody();

        monitoredResourceRequestBody.setProperties(monitoredResourceProperties);

        notificationManager.call("/notification-types/" + notificationTypeGUID + "/monitored-resources/" + actionGUID + "/attach",
                                 monitoredResourceRequestBody);
    }


    /**
     * Each related element appears in exactly the graph its relationship's purpose selects.
     */
    @Test
    @Order(2)
    void graphsAreSplitByRelationshipPurpose()
    {
        OpenMetadataRootElement action = assetMaker.element("/assets/" + actionGUID + "/retrieve", new GetRequestBody());

        assertNotNull(action);

        String context  = action.getMermaidGraph();
        String actions  = action.getActionMermaidGraph();
        String activity = action.getActivityMermaidGraph();

        System.out.println("RootElementMermaidGraphsFVT mermaidGraph:\n" + context);
        System.out.println("RootElementMermaidGraphsFVT actionMermaidGraph:\n" + actions);
        System.out.println("RootElementMermaidGraphsFVT activityMermaidGraph:\n" + activity);

        assertNotNull(context, "the context graph is missing");
        assertNotNull(actions, "the action graph is missing");
        assertNotNull(activity, "the activity graph is missing");

        assertTrue(context.contains(ASSIGNEE), "the assignee (Assignment) is missing from the context graph:\n" + context);
        assertFalse(context.contains(REQUESTER), "the requester (Activity) is in the context graph:\n" + context);
        assertFalse(context.contains(TARGET), "the action target (Activity) is in the context graph:\n" + context);
        assertFalse(context.contains(NOTIFICATION_TYPE), "the monitoring notification type (Action) is in the context graph:\n" + context);

        assertTrue(activity.contains(REQUESTER), "the requester is missing from the activity graph:\n" + activity);
        assertTrue(activity.contains(TARGET), "the action target is missing from the activity graph:\n" + activity);
        assertFalse(activity.contains(ASSIGNEE), "the assignee is in the activity graph:\n" + activity);
        assertFalse(activity.contains(NOTIFICATION_TYPE), "the notification type is in the activity graph:\n" + activity);

        assertTrue(actions.contains(NOTIFICATION_TYPE), "the notification type is missing from the action graph:\n" + actions);
        assertFalse(actions.contains(ASSIGNEE), "the assignee is in the action graph:\n" + actions);
        assertFalse(actions.contains(REQUESTER), "the requester is in the action graph:\n" + actions);
        assertFalse(actions.contains(TARGET), "the action target is in the action graph:\n" + actions);
    }


    @AfterAll
    static void cleanUp()
    {
        /*
         * The notification type goes first: once the action is deleted for lineage it is a Memento, and the
         * MonitoredResource link to it would make the notification type's delete fail.
         */
        if (notificationTypeGUID != null)
        {
            ClassificationExplorerFvtFixtures.deleteGovernanceDefinition(notificationTypeGUID);
        }

        DeleteElementRequestBody forLineage = new DeleteElementRequestBody();

        forLineage.setForLineage(true);

        for (String guid : assetGUIDs)
        {
            assetMaker.postUnchecked("/assets/" + guid + "/delete", forLineage, VoidResponse.class);
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
}
