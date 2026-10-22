/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.jdk;

import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.net.http.HttpClient;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * Verify that JDKRESTClientConnector instances share an HttpClient.  Each HttpClient owns a selector thread and
 * a connection pool, and the connector used to build one per instance - a busy platform accumulated hundreds of
 * "HttpClient-N-SelectorManager" threads.
 */
public class JDKRESTClientConnectorSharedClientTest
{
    private static final String SELECTOR_THREAD_PREFIX = "HttpClient-";
    private static final String SELECTOR_THREAD_SUFFIX = "-SelectorManager";


    /**
     * Two connectors created with the same strict SSL setting use the same HttpClient.
     */
    @Test
    public void testConnectorsShareHttpClient() throws Exception
    {
        HttpClient first  = getHttpClient(new JDKRESTClientConnector());
        HttpClient second = getHttpClient(new JDKRESTClientConnector());

        assertNotNull(first);
        assertSame(second, first);
    }


    /**
     * Creating many connectors does not create a selector thread for each one.
     */
    @Test
    public void testManyConnectorsDoNotLeakSelectorThreads() throws Exception
    {
        /*
         * Make sure the shared client exists before taking the baseline.
         */
        new JDKRESTClientConnector();

        long before = countSelectorThreads();

        for (int i = 0; i < 100; i++)
        {
            new JDKRESTClientConnector();
        }

        long after = countSelectorThreads();

        assertTrue(after <= before, "Selector threads grew from " + before + " to " + after + " after creating 100 connectors.");
    }


    /**
     * Return the HttpClient a connector uses.
     *
     * @param connector connector to inspect
     * @return its HttpClient
     * @throws Exception reflection problem
     */
    private HttpClient getHttpClient(JDKRESTClientConnector connector) throws Exception
    {
        Field field = JDKRESTClientConnector.class.getDeclaredField("httpClient");

        field.setAccessible(true);

        return (HttpClient) field.get(connector);
    }


    /**
     * Count the live HttpClient selector threads in this JVM.
     *
     * @return thread count
     */
    private long countSelectorThreads()
    {
        return Thread.getAllStackTraces().keySet().stream()
                     .map(Thread::getName)
                     .filter(name -> name.startsWith(SELECTOR_THREAD_PREFIX) && name.endsWith(SELECTOR_THREAD_SUFFIX))
                     .count();
    }
}
