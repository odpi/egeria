/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic;

import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.testng.Assert.assertTrue;

/**
 * Verify that OMFOutTopicClientConnector can be disconnected while it is delivering an event.  It used to hold its own lock while
 * calling its listeners, and disconnect() needs that lock - so a listener that waited on anything the
 * disconnecting thread held deadlocked the two threads.  An engine host shut down while an engine action event
 * was being delivered hung that way for good.
 */
public class OMFOutTopicClientConnectorTest
{
    @Test(timeOut = 60000)
    public void testDisconnectDuringEventDelivery() throws Exception
    {
        OMFOutTopicClientConnector connector = new OMFOutTopicClientConnector();

        CountDownLatch insideListener = new CountDownLatch(1);
        CountDownLatch disconnected   = new CountDownLatch(1);
        AtomicBoolean  sawDisconnect  = new AtomicBoolean(false);

        connector.registerListener("test", new OpenMetadataEventListener()
        {
            @Override
            public void processEvent(OpenMetadataOutTopicEvent event)
            {
                insideListener.countDown();

                try
                {
                    /*
                     * Stands in for a listener that needs something the disconnecting thread holds.  It only
                     * gives up after a while, so the test fails rather than hangs if disconnect() is blocked.
                     */
                    sawDisconnect.set(disconnected.await(10, TimeUnit.SECONDS));
                }
                catch (InterruptedException interrupted)
                {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread delivery = new Thread(() -> connector.processEvent("{}"), "event delivery");
        delivery.start();

        assertTrue(insideListener.await(10, TimeUnit.SECONDS), "The event never reached the listener.");

        connector.disconnect();
        disconnected.countDown();

        delivery.join(20000);

        assertTrue(sawDisconnect.get(),
                   "disconnect() waited for the event delivery to finish - it is blocked on the lock processEvent holds.");
    }
}
