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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.MonitoredResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NotificationManagerFVT covers every request of the Notification Manager OMVS: a notification type is given a
 * monitored resource and a subscriber, each visible from both ends, and then both are detached.  The
 * notification type is created and deleted with the Governance Officer since it is a governance definition.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationManagerFVT
{
    private static final ViewServiceClient notificationManager = new ViewServiceClient(ViewServiceDescription.NOTIFICATION_MANAGER);

    private static String notificationTypeGUID = null;
    private static String resourceGUID         = null;
    private static String subscriberGUID       = null;


    @Test
    @Order(1)
    void createNotificationTypeResourceAndSubscriber()
    {
        NotificationTypeProperties properties = new NotificationTypeProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("NotificationType", "data-set-changes"));
        properties.setDisplayName("Data set changes");
        properties.setDescription("Notifies the subscribers when the monitored data set changes.");

        notificationTypeGUID = ClassificationExplorerFvtFixtures.createGovernanceDefinition(properties);
        resourceGUID         = ClassificationExplorerFvtFixtures.createDataSet("notification-monitored", "Monitored data set");
        subscriberGUID       = ClassificationExplorerFvtFixtures.createDataSet("notification-subscriber", "Subscribing data set");
    }


    /**
     * linkMonitoredResource then detachMonitoredResource.
     */
    @Test
    @Order(2)
    void monitoredResource()
    {
        MonitoredResourceProperties properties = new MonitoredResourceProperties();

        properties.setLabel("watched");
        properties.setDescription("Changes to this data set trigger notifications.");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String path = "/notification-types/" + notificationTypeGUID + "/monitored-resources/" + resourceGUID;

        notificationManager.call(path + "/attach", requestBody);

        assertLinked(OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, resourceGUID, true);

        notificationManager.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertLinked(OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName, resourceGUID, false);
    }


    /**
     * linkNotificationSubscriber then detachNotificationSubscriber.
     */
    @Test
    @Order(3)
    void notificationSubscriber()
    {
        NotificationSubscriberProperties properties = new NotificationSubscriberProperties();

        properties.setLabel("subscriber");
        properties.setDescription("Receives the notifications.");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String path = "/notification-types/" + notificationTypeGUID + "/notification-subscribers/" + subscriberGUID;

        notificationManager.call(path + "/attach", requestBody);

        assertLinked(OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName, subscriberGUID, true);

        notificationManager.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertLinked(OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName, subscriberGUID, false);
    }


    @AfterAll
    static void cleanUp()
    {
        ClassificationExplorerFvtFixtures.deleteAsset(resourceGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(subscriberGUID);
        ClassificationExplorerFvtFixtures.deleteGovernanceDefinition(notificationTypeGUID);
    }


    /**
     * Check the relationship from both ends.
     */
    private static void assertLinked(String  relationshipTypeName,
                                     String  otherGUID,
                                     boolean expected)
    {
        List<String> fromNotificationType = ClassificationExplorerFvtFixtures.relatedGUIDs(notificationTypeGUID, relationshipTypeName);
        List<String> fromOther            = ClassificationExplorerFvtFixtures.relatedGUIDs(otherGUID, relationshipTypeName);

        if (expected)
        {
            assertEquals(List.of(otherGUID), fromNotificationType, relationshipTypeName + " not visible from the notification type");
            assertEquals(List.of(notificationTypeGUID), fromOther, relationshipTypeName + " not visible from " + otherGUID);
        }
        else
        {
            assertFalse(fromNotificationType.contains(otherGUID), relationshipTypeName + " still visible from the notification type");
            assertTrue(fromOther.isEmpty(), relationshipTypeName + " still visible from " + otherGUID);
        }
    }
}
