/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Checks which change events for a monitored resource Baudot passes on to subscribers, and when a change has to
 * wait for the notification type's minimum interval.
 */
public class MonitoredResourceChangeFilterTest
{
    private static final Date DATA_START    = new Date(1_790_000_000_000L);
    private static final Date DATA_END      = new Date(1_794_000_000_000L);
    private static final Date NEW_DATA_END  = new Date(1_794_100_000_000L);

    private final MonitoredResourceChangeFilter filter         = new MonitoredResourceChangeFilter();
    private final PropertyHelper                propertyHelper = new PropertyHelper();


    /**
     * A change to the resource's own properties is passed on.
     */
    @Test
    public void testEntityUpdateIsAChange()
    {
        assertTrue(filter.isChangeToResource(new OpenMetadataOutTopicEvent()));
    }


    /**
     * Pepys recording that the resource was read is not a change - this is what made each delivery of a
     * subscription trigger the next one.
     */
    @Test
    public void testUsageActivityIsNotAChange()
    {
        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setClassificationName(OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION.typeName);

        assertFalse(filter.isChangeToResource(event));
    }


    /**
     * The open lineage cataloguer and Lovelace record reads and analysis results in the DataScope additional
     * properties without moving the data collection times - not a change.
     */
    @Test
    public void testDataScopeActivityIsNotAChange()
    {
        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setClassificationName(OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName);
        event.setPreviousClassificationProperties(getDataScope(DATA_START, DATA_END, Map.of("readCount", "1")));
        event.setClassificationProperties(getDataScope(DATA_START, DATA_END, Map.of("readCount", "2")));

        assertFalse(filter.isChangeToResource(event));
    }


    /**
     * Jacquard moving the data collection end time because new data has arrived is a change.
     */
    @Test
    public void testNewDataIsAChange()
    {
        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setClassificationName(OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName);
        event.setPreviousClassificationProperties(getDataScope(DATA_START, DATA_END, null));
        event.setClassificationProperties(getDataScope(DATA_START, NEW_DATA_END, null));

        assertTrue(filter.isChangeToResource(event));
    }


    /**
     * The first DataScope classification - the first time the data has been seen - is a change.
     */
    @Test
    public void testFirstDataScopeIsAChange()
    {
        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setClassificationName(OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName);
        event.setClassificationProperties(getDataScope(DATA_START, DATA_END, null));

        assertTrue(filter.isChangeToResource(event));
    }


    /**
     * Any other classification change - here a governance classification - is passed on.
     */
    @Test
    public void testOtherClassificationIsAChange()
    {
        OpenMetadataOutTopicEvent event = new OpenMetadataOutTopicEvent();

        event.setClassificationName(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName);

        assertTrue(filter.isChangeToResource(event));
    }


    /**
     * A change inside the minimum interval waits; one after it, or for a notification type that has never
     * notified, goes straight away.
     */
    @Test
    public void testMinimumInterval()
    {
        Date                       lastNotification = new Date(1_794_000_000_000L);
        NotificationTypeProperties properties       = new NotificationTypeProperties();

        properties.setMinimumNotificationInterval(60L);

        assertFalse(BaudotSubscriptionManagementConnector.isWithinMinimumInterval(properties, lastNotification),
                    "a notification type that has never notified should notify straight away");

        properties.setLastNotification(lastNotification);

        assertTrue(BaudotSubscriptionManagementConnector.isWithinMinimumInterval(properties, new Date(lastNotification.getTime() + 59 * 60 * 1000)));
        assertFalse(BaudotSubscriptionManagementConnector.isWithinMinimumInterval(properties, new Date(lastNotification.getTime() + 61 * 60 * 1000)));

        properties.setMinimumNotificationInterval(0L);

        assertFalse(BaudotSubscriptionManagementConnector.isWithinMinimumInterval(properties, lastNotification));
    }


    /**
     * A change that has to wait is sent just after the interval ends - not at the next refresh - and the wait
     * is never negative.
     */
    @Test
    public void testDeferredDeliveryDelay()
    {
        Date                       lastNotification = new Date(1_794_000_000_000L);
        NotificationTypeProperties properties       = new NotificationTypeProperties();

        properties.setMinimumNotificationInterval(30L);
        properties.setLastNotification(lastNotification);

        long delay = BaudotSubscriptionManagementConnector.getDeferredDeliveryDelay(properties, new Date(lastNotification.getTime() + 10 * 60 * 1000));

        assertTrue(delay > 20 * 60 * 1000, "the change should wait until the interval ends");
        assertTrue(delay <= 20 * 60 * 1000 + 5000, "the change should be sent as soon as the interval ends");

        long lateDelay = BaudotSubscriptionManagementConnector.getDeferredDeliveryDelay(properties, new Date(lastNotification.getTime() + 31 * 60 * 1000));

        assertTrue((lateDelay > 0) && (lateDelay <= 5000), "a change after the interval should be sent straight away");

        properties.setMinimumNotificationInterval(0L);

        assertTrue(BaudotSubscriptionManagementConnector.getDeferredDeliveryDelay(properties, lastNotification) > 0);
    }


    /**
     * Build DataScope classification properties as they appear in a change event.
     *
     * @param start data collection start time
     * @param end data collection end time
     * @param additionalProperties other values recorded in the classification
     * @return properties
     */
    private ElementProperties getDataScope(Date                start,
                                           Date                end,
                                           Map<String, String> additionalProperties)
    {
        ElementProperties properties = propertyHelper.addDateProperty(null, OpenMetadataProperty.DATA_COLLECTION_START_TIME.name, start);

        properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.DATA_COLLECTION_END_TIME.name, end);

        if (additionalProperties != null)
        {
            properties = propertyHelper.addStringMapProperty(properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties);
        }

        return properties;
    }
}
