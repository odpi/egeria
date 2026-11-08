/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Checks the gates that decide whether a notification type may notify its subscribers.
 */
public class NotificationPermittedTest
{
    private static final long ONE_MINUTE = 60L * 1000L;

    private final NotificationHandler handler = new NotificationHandler("testServer",
                                                                        new AuditLog(null, 1, ComponentDevelopmentStatus.IN_DEVELOPMENT, "test", null, null),
                                                                        "testService",
                                                                        null,
                                                                        null);


    private NotificationTypeProperties activeNotificationType()
    {
        NotificationTypeProperties properties = new NotificationTypeProperties();

        properties.setDisplayName("Test notification type");
        properties.setContentStatus(ContentStatus.ACTIVE);

        return properties;
    }


    /**
     * A notification type is skipped until its planned start date has passed - the audit message
     * NOTIFICATION_TYPE_NOT_STARTED says so, but the check used to log it and carry on.
     */
    @Test
    public void testNotStartedIsSkipped()
    {
        NotificationTypeProperties properties = activeNotificationType();

        properties.setPlannedStartDate(new Date(System.currentTimeMillis() + 60 * ONE_MINUTE));

        assertFalse(handler.notificationPermitted("test", properties));
    }


    @Test
    public void testStartedIsPermitted()
    {
        NotificationTypeProperties properties = activeNotificationType();

        properties.setPlannedStartDate(new Date(System.currentTimeMillis() - 60 * ONE_MINUTE));

        assertTrue(handler.notificationPermitted("test", properties));
    }


    @Test
    public void testInactiveIsSkipped()
    {
        NotificationTypeProperties properties = activeNotificationType();

        properties.setContentStatus(ContentStatus.DRAFT);

        assertFalse(handler.notificationPermitted("test", properties));
    }


    @Test
    public void testMinimumIntervalIsInMinutes()
    {
        NotificationTypeProperties properties = activeNotificationType();

        properties.setMinimumNotificationInterval(10L);

        properties.setLastNotification(new Date(System.currentTimeMillis() - 5 * ONE_MINUTE));
        assertFalse(handler.notificationPermitted("test", properties));

        properties.setLastNotification(new Date(System.currentTimeMillis() - 11 * ONE_MINUTE));
        assertTrue(handler.notificationPermitted("test", properties));
    }


    @Test
    public void testNoMinimumIntervalIsAlwaysPermitted()
    {
        NotificationTypeProperties properties = activeNotificationType();

        properties.setLastNotification(new Date());

        assertTrue(handler.notificationPermitted("test", properties));
    }
}
