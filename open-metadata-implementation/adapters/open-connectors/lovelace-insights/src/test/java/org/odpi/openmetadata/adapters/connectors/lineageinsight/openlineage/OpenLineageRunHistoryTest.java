/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

/**
 * Validate that a run history hands out one key for each job or dataset, and one copy of each repeated value, so
 * that the records of a large log store do not each hold their own copies.
 */
public class OpenLineageRunHistoryTest
{
    /**
     * The same namespace and name always give the same key, whichever strings they are passed in.
     */
    @Test
    public void testKeysAreShared()
    {
        OpenLineageRunHistory history = new OpenLineageRunHistory();

        OpenLineageRunHistory.Key first  = history.key(new String("airflow://prod"), new String("load_orders"));
        OpenLineageRunHistory.Key second = history.key(new String("airflow://prod"), new String("load_orders"));

        assertSame(second, first);
        assertEquals(first, new OpenLineageRunHistory.Key("airflow://prod", "load_orders"));
        assertEquals(first.display(), "airflow://prod/load_orders");

        assertNotSame(history.key("airflow://prod", "load_customers"), first);
        assertNotSame(history.key("airflow://test", "load_orders"), first);
    }


    /**
     * A job or dataset with no namespace has a key of its own.
     */
    @Test
    public void testKeyWithoutNamespace()
    {
        OpenLineageRunHistory history = new OpenLineageRunHistory();

        OpenLineageRunHistory.Key key = history.key(null, "load_orders");

        assertNull(key.namespace());
        assertSame(history.key(null, "load_orders"), key);
        assertNotSame(history.key("airflow://prod", "load_orders"), key);
    }


    /**
     * A key is only shared within a history.
     */
    @Test
    public void testKeysBelongToAHistory()
    {
        OpenLineageRunHistory.Key first  = new OpenLineageRunHistory().key("airflow://prod", "load_orders");
        OpenLineageRunHistory.Key second = new OpenLineageRunHistory().key("airflow://prod", "load_orders");

        assertEquals(second, first);
        assertNotSame(second, first);
    }


    /**
     * Equal values give the same copy; null stays null.
     */
    @Test
    public void testSharedValues()
    {
        OpenLineageRunHistory history = new OpenLineageRunHistory();

        String first = history.shared(new String("COMPLETE"));

        assertEquals(first, "COMPLETE");
        assertSame(history.shared(new String("COMPLETE")), first);
        assertNotSame(history.shared(new String("FAIL")), first);
        assertNull(history.shared(null));
    }


    /**
     * The number of jobs and datasets is the number held, unless a reader that holds only some of them recorded
     * how many there are in the window.
     */
    @Test
    public void testJobAndDataSetCounts()
    {
        OpenLineageRunHistory history = new OpenLineageRunHistory();

        assertEquals(history.getJobCount(), 0);
        assertEquals(history.getDataSetCount(), 0);

        history.getJob(history.key("airflow://prod", "load_orders"));
        history.getDataSet(history.key("postgres://prod", "sales.orders"));
        history.getDataSet(history.key("postgres://prod", "sales.customers"));

        assertEquals(history.getJobCount(), 1);
        assertEquals(history.getDataSetCount(), 2);

        history.recordJobCount(929);
        history.recordDataSetCount(1212);

        assertEquals(history.getJobCount(), 929);
        assertEquals(history.getDataSetCount(), 1212);
        assertEquals(history.getJobs().size(), 1);
        assertEquals(history.getDataSets().size(), 2);
    }
}
