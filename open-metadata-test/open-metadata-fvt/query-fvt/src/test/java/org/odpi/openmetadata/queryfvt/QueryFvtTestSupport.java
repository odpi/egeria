/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;

/**
 * Shared conventions and helpers used across the query-fvt test suite.
 * <br>
 * Every element this suite creates uses a qualified name starting with {@link #QUALIFIED_NAME_PREFIX} -
 * this makes it possible to reliably find and purge test debris, both the debris a test leaves behind
 * itself (each test should clean up after itself in a {@code finally} block) and, more importantly,
 * anything left over from an earlier run that failed partway through: unlike open-metadata-bvt's
 * in-memory repository, this suite's PostgreSQL-backed repository persists across runs, so a crashed
 * previous run's half-finished elements would otherwise accumulate indefinitely and could confuse later
 * test assertions (for example, paging/counting tests that expect an exact count of "just what this run
 * created" for a given type).
 */
final class QueryFvtTestSupport
{
    /**
     * Every qualified name this suite creates starts with this prefix, so leftover test elements from a
     * previous (possibly failed) run can always be found and purged before the next run's tests start.
     */
    static final String QUALIFIED_NAME_PREFIX = "query-fvt:";

    static final int MAX_PAGE_SIZE = ConnectorContextFactory.MAX_PAGE_SIZE;

    private QueryFvtTestSupport()
    {
        // no instances
    }


    /**
     * Build a qualified name for a newly created test element that is unique to this test run and
     * recognisable as query-fvt debris if it is ever left behind.
     *
     * @param category short label for what kind of element this is (for example "Collection")
     * @return unique qualified name starting with {@link #QUALIFIED_NAME_PREFIX}
     */
    static String newQualifiedName(String category)
    {
        return QUALIFIED_NAME_PREFIX + category + ":" + java.util.UUID.randomUUID();
    }


    /**
     * Permanently remove an element, regardless of the delete method the supplied client was set up
     * with.  Used both for the leftover-element cleanup pass and for tests to tidy up after themselves
     * once they no longer need the element (including ones that were only ever soft-deleted, to check
     * status filtering, and now need to be fully removed).
     * <br><br>
     * A cleanup failure does not fail the test - but it is reported rather than discarded, and the caller
     * is told whether the element actually went.  Silently ignoring a failed purge hides a broken purge:
     * the element stays soft-deleted, a later query that includes every status finds it again, and the
     * test fails somewhere else entirely with no sign of where the trouble started.  That is exactly how a
     * genuine purge defect presented, and it cost a long time to trace back to here.
     * <br><br>
     * PURGE only succeeds on an element that is already soft-deleted (the classic OMRS lifecycle enforced
     * by the repository connector), so this soft-deletes the element first - best-effort, since it may
     * already be deleted (for example by an earlier failed run, or by a cascade delete from one of its own
     * anchor/owner elements) - before purging it.  Only the purge step's outcome is reported: the
     * soft-delete failing is ordinary, the purge failing is not.
     * <br><br>
     * {@code forLineage} is set on both steps: an element left ARCHIVEd by a test (or a previous test's
     * LOOK_FOR_LINEAGE call) carries the Memento classification, and without forLineage=true it would be
     * invisible to the lookup each delete step needs to do first - the delete would silently no-op rather
     * than throw, so the element would never actually be removed.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     * @return true if the element was purged
     */
    static boolean purgeElement(OpenMetadataStore openMetadataStore,
                                String            elementGUID)
    {
        try
        {
            purgeElementOrFail(openMetadataStore, elementGUID);

            return true;
        }
        catch (Exception error)
        {
            System.err.println("query-fvt: could not purge element " + elementGUID + " - "
                                       + error.getClass().getSimpleName() + ": " + error.getMessage());

            return false;
        }
    }


    /**
     * Permanently remove an element, failing if it does not go.  This is the one to use where a test's
     * assertions depend on the element having actually been removed, rather than where it is tidying up
     * after itself.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     * @throws Exception the element could not be purged
     */
    static void purgeElementOrFail(OpenMetadataStore openMetadataStore,
                                   String            elementGUID) throws Exception
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            softDeleteOptions.setCascadedDelete(true);
            softDeleteOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            /*
             * Ordinary: the element may already be deleted - see the note on purgeElement().  The purge
             * below is the step that has to work.
             */
        }

        DeleteOptions purgeOptions = new DeleteOptions();

        purgeOptions.setDeleteMethod(DeleteMethod.PURGE);
        purgeOptions.setCascadedDelete(true);
        purgeOptions.setForLineage(true);

        openMetadataStore.deleteMetadataElementInStore(elementGUID, purgeOptions);
    }


    /**
     * Permanently remove a relationship.  Used by tests that create their own relationships to tidy up after
     * themselves, including ones that were only ever soft-deleted to check status filtering.
     * <br><br>
     * A cleanup failure does not fail the test, but it is reported rather than discarded - see the note on
     * {@link #purgeElement(OpenMetadataStore, String)} for what a silently failed purge costs.
     * <br><br>
     * PURGE only succeeds on a relationship that is already soft-deleted (the classic OMRS lifecycle enforced
     * by the repository connector), so this soft-deletes the relationship first - best-effort, since it may
     * already be deleted - before purging it.
     *
     * @param openMetadataStore store to delete through
     * @param relationshipGUID relationship to remove
     * @return true if the relationship was purged
     */
    static boolean purgeRelationship(OpenMetadataStore openMetadataStore,
                                     String            relationshipGUID)
    {
        try
        {
            purgeRelationshipOrFail(openMetadataStore, relationshipGUID);

            return true;
        }
        catch (Exception error)
        {
            System.err.println("query-fvt: could not purge relationship " + relationshipGUID + " - "
                                       + error.getClass().getSimpleName() + ": " + error.getMessage());

            return false;
        }
    }


    /**
     * Permanently remove a relationship, failing if it does not go.  Use this where a test's assertions
     * depend on the relationship having actually been removed.
     *
     * @param openMetadataStore store to delete through
     * @param relationshipGUID relationship to remove
     * @throws Exception the relationship could not be purged
     */
    static void purgeRelationshipOrFail(OpenMetadataStore openMetadataStore,
                                        String            relationshipGUID) throws Exception
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            /*
             * Ordinary: the relationship may already be deleted.  The purge below is the step that has to work.
             */
        }

        DeleteOptions purgeOptions = new DeleteOptions();

        purgeOptions.setDeleteMethod(DeleteMethod.PURGE);

        openMetadataStore.deleteRelationshipInStore(relationshipGUID, purgeOptions);
    }


    /**
     * Find and permanently purge every element whose qualified name starts with
     * {@link #QUALIFIED_NAME_PREFIX}, in any status (including elements that were only soft-deleted, and
     * ones that were ARCHIVEd - forLineage=true on the search is required to find those, since a Memento-
     * classified element is otherwise invisible to a default query).  Called once, after the server is
     * activated and before any test runs, so that debris from an earlier, possibly failed, run does not
     * affect this run's assertions.
     *
     * @throws Exception problem communicating with the server - fatal, since a dirty repository would
     * invalidate the whole run
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore     openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper        propertyHelper    = new PropertyHelper();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         QUALIFIED_NAME_PREFIX,
                                                                         PropertyComparisonOperator.STARTS_WITH));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
        queryOptions.setPageSize(MAX_PAGE_SIZE);
        queryOptions.setForLineage(true);

        /*
         * Purging shrinks the result set, so it is simplest (and safest against paging off-by-ones while
         * elements are disappearing underneath the query) to keep re-querying from the start until
         * nothing more comes back, rather than trying to page through a moving target.
         */
        int  purgedCount    = 0;
        int  emptyPassLimit = 50;

        while (emptyPassLimit > 0)
        {
            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if ((found == null) || found.isEmpty())
            {
                break;
            }

            boolean anyPurgedThisPass = false;

            for (OpenMetadataElement element : found)
            {
                if (purgeElement(openMetadataStore, element.getElementGUID()))
                {
                    purgedCount++;
                    anyPurgedThisPass = true;
                }
            }

            if (! anyPurgedThisPass)
            {
                /*
                 * The same elements will come back on the next pass, so re-querying just repeats the same
                 * failures.  purgeElement() has already reported why.
                 */
                System.err.println("query-fvt: leftover cleanup gave up - " + found.size()
                                           + " element(s) could not be purged");
                break;
            }

            emptyPassLimit--;
        }

        if (purgedCount > 0)
        {
            System.out.println("query-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }
}
