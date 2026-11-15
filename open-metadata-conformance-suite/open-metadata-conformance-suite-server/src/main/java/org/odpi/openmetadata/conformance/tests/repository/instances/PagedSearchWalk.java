/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pages through a search the way a caller following the paging contract does, and records what it saw.
 * <br>
 * The contract: a page is requested with a starting element and a page size, and the next page starts a page
 * size further on.  An empty list means the page was retrieved but nothing on it could be returned (for example,
 * it was filtered out), so the caller asks for the next page.  Null means there is nothing more, so the caller
 * stops.  A repository that returns null too soon hides instances from every caller that pages; one that returns
 * an empty list instead of null at the end means a caller never stops.
 * <br>
 * The walk is bounded so that a repository that never returns null cannot hang the conformance run.
 */
class PagedSearchWalk
{
    /**
     * A call to the repository for one page of a search.
     */
    interface PageRetriever
    {
        /**
         * Retrieve one page.
         *
         * @param startFrom position of the first instance on the page
         * @param pageSize maximum number of instances on the page
         * @return the page, an empty list if it was filtered empty, or null if there are no more instances
         * @throws Exception problem calling the repository
         */
        List<? extends InstanceHeader> retrieve(int startFrom, int pageSize) throws Exception;
    }


    private final Set<String> guidsSeen          = new HashSet<>();
    private boolean           pageTooLarge       = false;
    private boolean           duplicateReturned  = false;
    private boolean           endedWithNull      = false;
    private int               nextStartFrom      = 0;
    private int               pagesRequested     = 0;
    private long              elapsedTime        = 0;


    /**
     * Page through a search.
     *
     * @param retriever call to the repository
     * @param pageSize page size to request
     * @param expectedInstanceCount number of instances the search should return in total
     * @throws Exception problem calling the repository
     */
    PagedSearchWalk(PageRetriever retriever,
                    int           pageSize,
                    int           expectedInstanceCount) throws Exception
    {
        /*
         * Enough pages to reach every expected instance, one more to receive the null that ends the walk,
         * and one spare.
         */
        int maxPages = ((expectedInstanceCount + pageSize - 1) / pageSize) + 2;

        long start = System.currentTimeMillis();

        while (pagesRequested < maxPages)
        {
            List<? extends InstanceHeader> page = retriever.retrieve(nextStartFrom, pageSize);

            pagesRequested++;

            if (page == null)
            {
                endedWithNull = true;
                break;
            }

            if (page.size() > pageSize)
            {
                pageTooLarge = true;
            }

            for (InstanceHeader instance : page)
            {
                if ((instance != null) && (! guidsSeen.add(instance.getGUID())))
                {
                    duplicateReturned = true;
                }
            }

            nextStartFrom = nextStartFrom + pageSize;
        }

        elapsedTime = System.currentTimeMillis() - start;
    }


    /**
     * Return whether any page held more instances than the page size requested.
     *
     * @return true if a page was too large
     */
    boolean isPageTooLarge()
    {
        return pageTooLarge;
    }


    /**
     * Return whether any instance was returned on more than one page.
     *
     * @return true if an instance was returned twice
     */
    boolean isDuplicateReturned()
    {
        return duplicateReturned;
    }


    /**
     * Return whether the walk was ended by the repository returning null.  False means the repository kept
     * returning pages (empty or not) beyond the instances it should hold.
     *
     * @return true if the walk ended with null
     */
    boolean isEndedWithNull()
    {
        return endedWithNull;
    }


    /**
     * Return whether the walk reached the instance with the given unique identifier.
     *
     * @param guid unique identifier of the instance
     * @return true if it was returned on one of the pages
     */
    boolean reached(String guid)
    {
        return guidsSeen.contains(guid);
    }


    /**
     * Return the number of distinct instances returned across all pages.
     *
     * @return count of instances
     */
    int getInstanceCount()
    {
        return guidsSeen.size();
    }


    /**
     * Return the starting element of the page after the last one requested.
     *
     * @return position beyond the walk
     */
    int getNextStartFrom()
    {
        return nextStartFrom;
    }


    /**
     * Return the number of pages requested.
     *
     * @return page count
     */
    int getPagesRequested()
    {
        return pagesRequested;
    }


    /**
     * Return the time taken by the whole walk.
     *
     * @return elapsed time in milliseconds
     */
    long getElapsedTime()
    {
        return elapsedTime;
    }
}
