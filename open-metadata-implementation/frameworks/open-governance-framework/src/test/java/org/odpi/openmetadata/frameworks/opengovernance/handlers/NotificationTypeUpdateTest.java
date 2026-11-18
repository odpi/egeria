/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * The notification type records each round of notifications (notificationCount, lastNotification).  It must
 * only be updated when a round actually notified someone - or when the notification type has run its course.
 * Updating it on every pass wrote a new version of every one-time notification type each time Baudot refreshed
 * (about 950 versions an hour on the Quickstart) and counted notifications that were never sent.
 */
public class NotificationTypeUpdateTest
{
    private static final long ONE_HOUR = 60L * 60L * 1000L;


    /**
     * A notification handler whose reads return a prepared notification type and whose updates are recorded.
     */
    private static class RecordingNotificationHandler extends NotificationHandler
    {
        private final OpenMetadataRootElement          notificationType;
        private final List<OpenMetadataRootProperties> updates             = new ArrayList<>();
        private final List<String>                     notifiedSubscribers = new ArrayList<>();

        RecordingNotificationHandler(OpenMetadataRootElement notificationType)
        {
            super("testServer",
                  new AuditLog(null, 1, ComponentDevelopmentStatus.IN_DEVELOPMENT, "test", null, null),
                  "testService",
                  null,
                  null);

            this.notificationType = notificationType;
        }

        @Override
        public OpenMetadataRootElement getRootElementByGUID(String     userId,
                                                            String     elementGUID,
                                                            GetOptions suppliedGetOptions,
                                                            String     methodName)
        {
            return notificationType;
        }

        @Override
        protected boolean updateElement(String                     userId,
                                        String                     elementGUID,
                                        String                     guidParameterName,
                                        UpdateOptions              updateOptions,
                                        OpenMetadataRootProperties properties,
                                        String                     methodName)
        {
            updates.add(properties);
            return true;
        }

        @Override
        boolean notifySubscriber(String                                userId,
                                 String                                subscriberGUID,
                                 ElementControlHeader                  subscriberHeader,
                                 String                                notificationSubscriberGUID,
                                 NotificationSubscriberProperties      notificationSubscriberProperties,
                                 long                                  notificationCount,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 NotificationProperties                outboundNotificationProperties,
                                 String                                notificationTypeGUID,
                                 Map<String, String>                   requestParameters,
                                 String                                actionRequesterGUID,
                                 List<NewActionTarget>                 actionTargets,
                                 long                                  minimumNotificationInterval,
                                 ActivityStatus                        newSubscriberStatus)
        {
            notifiedSubscribers.add(subscriberGUID);
            return true;
        }
    }


    private OpenMetadataRootElement notificationType(boolean                             multipleNotificationsPermitted,
                                                     Date                                plannedCompletionDate,
                                                     List<RelatedMetadataElementSummary> subscribers)
    {
        NotificationTypeProperties properties = new NotificationTypeProperties();

        properties.setDisplayName("Test notification type");
        properties.setContentStatus(ContentStatus.ACTIVE);
        properties.setMultipleNotificationsPermitted(multipleNotificationsPermitted);
        properties.setNotificationCount(7L);
        properties.setLastNotification(new Date(System.currentTimeMillis() - ONE_HOUR));
        properties.setPlannedCompletionDate(plannedCompletionDate);

        OpenMetadataRootElement element = new OpenMetadataRootElement();

        element.setElementHeader(new ElementHeader());
        element.setProperties(properties);
        element.setSubscribers(subscribers);

        return element;
    }


    /**
     * A subscriber that has already had its notification.
     *
     * @return subscriber
     */
    private RelatedMetadataElementSummary notifiedSubscriber()
    {
        return subscriber("subscriber-guid", new Date(System.currentTimeMillis() - ONE_HOUR));
    }


    /**
     * A subscriber that has never been notified.
     *
     * @return subscriber
     */
    private RelatedMetadataElementSummary newSubscriber()
    {
        return subscriber("new-subscriber-guid", null);
    }


    private RelatedMetadataElementSummary subscriber(String subscriberGUID,
                                                     Date   lastNotification)
    {
        NotificationSubscriberProperties subscriberProperties = new NotificationSubscriberProperties();

        subscriberProperties.setLastNotification(lastNotification);

        ElementHeader subscriberHeader = new ElementHeader();
        subscriberHeader.setGUID(subscriberGUID);

        MetadataElementSummary subscriberElement = new MetadataElementSummary();
        subscriberElement.setElementHeader(subscriberHeader);

        ElementHeader relationshipHeader = new ElementHeader();
        relationshipHeader.setGUID("subscriber-relationship-guid");

        RelatedMetadataElementSummary subscriber = new RelatedMetadataElementSummary();

        subscriber.setRelatedElement(subscriberElement);
        subscriber.setRelationshipHeader(relationshipHeader);
        subscriber.setRelationshipProperties(subscriberProperties);

        return subscriber;
    }


    private void notifySubscribers(NotificationHandler handler) throws Exception
    {
        handler.notifySubscribers(null,
                                  null,
                                  "testUser",
                                  null,
                                  new NotificationProperties(),
                                  new NotificationProperties(),
                                  new NotificationProperties(),
                                  "notification-type-guid",
                                  null,
                                  null,
                                  null,
                                  "test");
    }


    /**
     * A one-time notification type whose subscriber has had its notification sends nothing, so it is not updated.
     *
     * @throws Exception not expected
     */
    @Test
    public void testOneTimeTypeAlreadyNotifiedIsNotUpdated() throws Exception
    {
        RecordingNotificationHandler handler = new RecordingNotificationHandler(notificationType(false, null, List.of(notifiedSubscriber())));

        notifySubscribers(handler);

        assertTrue(handler.updates.isEmpty());
    }


    /**
     * A notification type with no subscribers yet sends nothing, so it is not updated.
     *
     * @throws Exception not expected
     */
    @Test
    public void testTypeWithoutSubscribersIsNotUpdated() throws Exception
    {
        RecordingNotificationHandler handler = new RecordingNotificationHandler(notificationType(true, null, null));

        notifySubscribers(handler);

        assertTrue(handler.updates.isEmpty());
    }


    /**
     * A notification type past its planned completion date is still marked obsolete, even with no one to notify.
     *
     * @throws Exception not expected
     */
    @Test
    public void testCompletedTypeIsMarkedObsolete() throws Exception
    {
        RecordingNotificationHandler handler = new RecordingNotificationHandler(notificationType(false, new Date(System.currentTimeMillis() - ONE_HOUR), null));

        notifySubscribers(handler);

        assertEquals(handler.updates.size(), 1);
        assertTrue(handler.updates.get(0) instanceof NotificationTypeProperties);
        assertEquals(((NotificationTypeProperties) handler.updates.get(0)).getContentStatus(), ContentStatus.OBSOLETE);
    }


    /**
     * A daily notification type that notified its subscribers an hour ago.
     *
     * @param subscribers its subscribers
     * @return notification type
     */
    private OpenMetadataRootElement dailyNotificationType(List<RelatedMetadataElementSummary> subscribers)
    {
        OpenMetadataRootElement element = notificationType(true, null, subscribers);

        ((NotificationTypeProperties) element.getProperties()).setMinimumNotificationInterval(1440L);

        return element;
    }


    /**
     * A new subscriber to a daily notification type is notified straight away, even though the last round of
     * notifications was only an hour ago; the existing subscriber waits for the next round.  The next round is
     * not postponed: only the notification count changes on the notification type.
     *
     * @throws Exception not expected
     */
    @Test
    public void testNewSubscriberIsNotifiedBetweenRounds() throws Exception
    {
        OpenMetadataRootElement    notificationType   = dailyNotificationType(List.of(notifiedSubscriber(), newSubscriber()));
        NotificationTypeProperties originalProperties = (NotificationTypeProperties) notificationType.getProperties();
        Date                       lastNotification   = originalProperties.getLastNotification();

        RecordingNotificationHandler handler = new RecordingNotificationHandler(notificationType);

        notifySubscribers(handler);

        assertEquals(handler.notifiedSubscribers, List.of("new-subscriber-guid"));
        assertEquals(handler.updates.size(), 1);

        NotificationTypeProperties update = (NotificationTypeProperties) handler.updates.get(0);

        assertEquals(update.getNotificationCount(), Long.valueOf(8L));
        assertEquals(update.getLastNotification(), lastNotification);
        assertEquals(update.getNextScheduledNotification(), originalProperties.getNextScheduledNotification());
    }


    /**
     * Between rounds, a daily notification type with only existing subscribers notifies no one and is not updated.
     *
     * @throws Exception not expected
     */
    @Test
    public void testExistingSubscriberWaitsForNextRound() throws Exception
    {
        RecordingNotificationHandler handler = new RecordingNotificationHandler(dailyNotificationType(List.of(notifiedSubscriber())));

        notifySubscribers(handler);

        assertTrue(handler.notifiedSubscribers.isEmpty());
        assertTrue(handler.updates.isEmpty());
    }


    /**
     * Once the interval has passed, a round notifies every subscriber and records the round on the notification type.
     *
     * @throws Exception not expected
     */
    @Test
    public void testRoundNotifiesEverySubscriber() throws Exception
    {
        OpenMetadataRootElement notificationType = dailyNotificationType(List.of(notifiedSubscriber(), newSubscriber()));

        ((NotificationTypeProperties) notificationType.getProperties()).setLastNotification(new Date(System.currentTimeMillis() - 25 * ONE_HOUR));

        RecordingNotificationHandler handler = new RecordingNotificationHandler(notificationType);

        notifySubscribers(handler);

        assertEquals(handler.notifiedSubscribers, List.of("subscriber-guid", "new-subscriber-guid"));
        assertEquals(handler.updates.size(), 1);

        NotificationTypeProperties update = (NotificationTypeProperties) handler.updates.get(0);

        assertEquals(update.getNotificationCount(), Long.valueOf(8L));
        assertTrue(update.getLastNotification().getTime() > System.currentTimeMillis() - ONE_HOUR);
    }
}
