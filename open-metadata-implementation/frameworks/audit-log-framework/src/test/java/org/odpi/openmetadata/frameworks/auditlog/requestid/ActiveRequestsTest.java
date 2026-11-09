/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.auditlog.requestid;

import org.testng.annotations.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify that ActiveRequests tells a nested call (made by one server on behalf of a request another server on the
 * platform is processing) from a new external request, and shares activity reports across the nested calls.
 */
public class ActiveRequestsTest
{
    /**
     * With no request tracked, nothing is nested and every report is the first.
     */
    @Test
    public void testNoRequestTracked()
    {
        assertFalse(ActiveRequests.isNestedRequest());
        assertTrue(ActiveRequests.isFirstReport("anchor-1"));
        assertTrue(ActiveRequests.isFirstReport("anchor-1"));
    }


    /**
     * A call on another thread carrying the identifier of a request in flight is nested and shares its reports.
     */
    @Test(timeOut = 10000)
    public void testNestedCallOnAnotherThread() throws Exception
    {
        String requestId = UUID.randomUUID().toString();
        int    before    = ActiveRequests.getActiveRequestCount();

        ActiveRequests.beginRequest(requestId);
        try
        {
            assertFalse(ActiveRequests.isNestedRequest());
            assertTrue(ActiveRequests.isFirstReport("anchor-1"));

            AtomicBoolean  nested           = new AtomicBoolean(false);
            AtomicBoolean  firstReportAgain = new AtomicBoolean(true);
            AtomicBoolean  firstReportOther = new AtomicBoolean(false);
            CountDownLatch done             = new CountDownLatch(1);

            Thread hop = new Thread(() ->
            {
                ActiveRequests.beginRequest(requestId);
                try
                {
                    nested.set(ActiveRequests.isNestedRequest());
                    firstReportAgain.set(ActiveRequests.isFirstReport("anchor-1"));
                    firstReportOther.set(ActiveRequests.isFirstReport("anchor-2"));
                }
                finally
                {
                    ActiveRequests.endRequest();
                    done.countDown();
                }
            });
            hop.start();
            done.await();

            assertTrue(nested.get());
            assertFalse(firstReportAgain.get());
            assertTrue(firstReportOther.get());
            assertFalse(ActiveRequests.isFirstReport("anchor-2"));
        }
        finally
        {
            ActiveRequests.endRequest();
        }

        assertEquals(ActiveRequests.getActiveRequestCount(), before);
        assertFalse(ActiveRequests.isNestedRequest());
    }


    /**
     * A client thread that reuses one request identifier for a sequence of calls makes separate requests.
     */
    @Test
    public void testSequentialCallsWithSameIdAreSeparate()
    {
        String requestId = UUID.randomUUID().toString();

        ActiveRequests.beginRequest(requestId);
        assertFalse(ActiveRequests.isNestedRequest());
        assertTrue(ActiveRequests.isFirstReport("anchor-1"));
        ActiveRequests.endRequest();

        ActiveRequests.beginRequest(requestId);
        assertFalse(ActiveRequests.isNestedRequest());
        assertTrue(ActiveRequests.isFirstReport("anchor-1"));
        ActiveRequests.endRequest();
    }


    /**
     * An in-process nested call on the same thread is nested, and ending it restores the outer request.
     */
    @Test
    public void testNestedCallOnSameThread()
    {
        String requestId = UUID.randomUUID().toString();
        String otherId   = UUID.randomUUID().toString();

        ActiveRequests.beginRequest(requestId);
        ActiveRequests.beginRequest(requestId);
        assertTrue(ActiveRequests.isNestedRequest());

        ActiveRequests.beginRequest(otherId);
        assertFalse(ActiveRequests.isNestedRequest());
        ActiveRequests.endRequest();

        assertTrue(ActiveRequests.isNestedRequest());
        ActiveRequests.endRequest();
        assertFalse(ActiveRequests.isNestedRequest());
        ActiveRequests.endRequest();
        assertFalse(ActiveRequests.isNestedRequest());
    }


    /**
     * Requests whose end is never recorded do not accumulate without limit.
     */
    @Test
    public void testLostEndsAreReleased()
    {
        int before = ActiveRequests.getActiveRequestCount();

        for (int i = 0; i < 100; i++)
        {
            ActiveRequests.beginRequest(UUID.randomUUID().toString());
        }

        assertTrue(ActiveRequests.getActiveRequestCount() - before <= 32);

        for (int i = 0; i < 100; i++)
        {
            ActiveRequests.endRequest();
        }

        assertEquals(ActiveRequests.getActiveRequestCount(), before);
    }


    /**
     * A null request identifier is ignored.
     */
    @Test
    public void testNullRequestId()
    {
        ActiveRequests.beginRequest(null);
        assertFalse(ActiveRequests.isNestedRequest());
        ActiveRequests.endRequest();
    }
}
