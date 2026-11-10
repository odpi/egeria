/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Verify that pages are retrieved from the repositories in bounded batches and reassembled in order.
 */
public class RepositoryBatchRetrievalTest
{
    /**
     * A repository holding a fixed list of instances that records the page size of each call.
     */
    private static class TestRepository implements RepositoryHandler.RepositoryBatchRetriever<Integer>
    {
        private final List<Integer> instances  = new ArrayList<>();
        final         List<Integer> pageSizes  = new ArrayList<>();
        final         List<Integer> startFroms = new ArrayList<>();

        TestRepository(int instanceCount)
        {
            for (int i = 0; i < instanceCount; i++)
            {
                instances.add(i);
            }
        }

        @Override
        public List<Integer> retrieve(int batchStartingFrom,
                                      int batchPageSize)
        {
            startFroms.add(batchStartingFrom);
            pageSizes.add(batchPageSize);

            if (batchStartingFrom >= instances.size())
            {
                return null;
            }

            return new ArrayList<>(instances.subList(batchStartingFrom, Math.min(instances.size(), batchStartingFrom + batchPageSize)));
        }
    }


    /**
     * Default constructor
     */
    public RepositoryBatchRetrievalTest()
    {
    }


    /**
     * A page that fits in one batch is one call, passed through unchanged.
     *
     * @throws Exception problem in the test repository
     */
    @Test public void testSmallPageIsOneCall() throws Exception
    {
        TestRepository repository = new TestRepository(50);

        List<Integer> page = RepositoryHandler.retrieveInBatches(10, 20, 100, repository);

        assertEquals(page.size(), 20);
        assertEquals(page.get(0).intValue(), 10);
        assertEquals(repository.pageSizes, List.of(20));
    }


    /**
     * An unlimited page (zero) is read in batches until the repository runs out, so no call asks for everything.
     *
     * @throws Exception problem in the test repository
     */
    @Test public void testUnlimitedPageIsBatched() throws Exception
    {
        TestRepository repository = new TestRepository(250);

        List<Integer> page = RepositoryHandler.retrieveInBatches(0, 0, 100, repository);

        assertEquals(page.size(), 250);

        for (int i = 0; i < 250; i++)
        {
            assertEquals(page.get(i).intValue(), i, "instances keep the repository's order");
        }

        assertEquals(repository.startFroms, List.of(0, 100, 200));
        assertTrue(repository.pageSizes.stream().allMatch(size -> size <= 100));
    }


    /**
     * An unlimited page that ends exactly on a batch boundary needs one more call to find the end.
     *
     * @throws Exception problem in the test repository
     */
    @Test public void testUnlimitedPageEndingOnBoundary() throws Exception
    {
        TestRepository repository = new TestRepository(200);

        assertEquals(RepositoryHandler.retrieveInBatches(0, 0, 100, repository).size(), 200);
        assertEquals(repository.startFroms, List.of(0, 100, 200));
    }


    /**
     * A large page stops once it is full, and the last batch only asks for what is still needed.
     *
     * @throws Exception problem in the test repository
     */
    @Test public void testLargePageStopsWhenFull() throws Exception
    {
        TestRepository repository = new TestRepository(1000);

        List<Integer> page = RepositoryHandler.retrieveInBatches(5, 250, 100, repository);

        assertEquals(page.size(), 250);
        assertEquals(page.get(0).intValue(), 5);
        assertEquals(page.get(249).intValue(), 254);
        assertEquals(repository.startFroms, List.of(5, 105, 205));
        assertEquals(repository.pageSizes, List.of(100, 100, 50));
    }


    /**
     * Nothing to return is null, as from a single repository call.
     *
     * @throws Exception problem in the test repository
     */
    @Test public void testNoInstances() throws Exception
    {
        assertNull(RepositoryHandler.retrieveInBatches(0, 0, 100, new TestRepository(0)));
        assertNull(RepositoryHandler.retrieveInBatches(0, 500, 100, new TestRepository(0)));
    }
}
