/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.IncomingEvent;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Verify the hand-off of received events from the Kafka consumer thread to the listener thread: every event is
 * handed over once, in order, and the queue is left empty.
 */
public class IncomingEventHandOffTest
{
    @Test(timeOut = 10000)
    public void testEventsHandedOverOnceInOrder()
    {
        KafkaOpenMetadataTopicConnector connector = new KafkaOpenMetadataTopicConnector();

        assertNull(connector.checkForIncomingEvents());

        final int eventCount = 50000;
        for (int i = 0; i < eventCount; i++)
        {
            connector.distributeToListeners(new KafkaIncomingEvent("event-" + i, i));
        }

        assertEquals(connector.getNumberOfUnprocessedEvents(), eventCount);

        List<IncomingEvent> events = connector.checkForIncomingEvents();

        assertEquals(events.size(), eventCount);
        for (int i = 0; i < eventCount; i++)
        {
            assertEquals(events.get(i).getJson(), "event-" + i);
        }

        assertEquals(connector.getNumberOfUnprocessedEvents(), 0);
        assertNull(connector.checkForIncomingEvents());
    }
}
