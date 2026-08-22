/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.templatesfvt;

import java.util.Map;
import java.util.ArrayList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
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
 * Shared conventions and helpers used across the templates-fvt test suite.
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
final class TemplatesFvtTestSupport
{
    /**
     * Every qualified name this suite creates starts with this prefix, so leftover test elements from a
     * previous (possibly failed) run can always be found and purged before the next run's tests start.
     */
    static final String QUALIFIED_NAME_PREFIX = "templates-fvt:";

    static final int MAX_PAGE_SIZE = ConnectorContextFactory.MAX_PAGE_SIZE;

    private TemplatesFvtTestSupport()
    {
        // no instances
    }


    /**
     * Search one set of properties for placeholder markers that were never substituted.
     * <br>
     * A placeholder appears in a template as <code>~{variableName}~</code>.  Finding one in an element created
     * from that template means the substitution did not happen: the catalogued element is carrying a variable
     * name where a real value belongs, which is worse than an empty property because it looks like data.
     * <br>
     * Every property is checked as its string form, which covers arrays and maps as well as plain strings - a
     * placeholder hidden inside one entry of an additionalProperties map matters just as much as one in a
     * display name.
     *
     * @param location where these properties were found, for the failure message
     * @param properties properties to search, may be null
     * @return one description per property still holding a marker, empty if there are none
     */
    static List<String> findPlaceholders(String            location,
                                         ElementProperties properties)
    {
        List<String> found = new ArrayList<>();

        if (properties == null)
        {
            return found;
        }

        Map<String, String> asStrings = properties.getPropertiesAsStrings();

        if (asStrings == null)
        {
            return found;
        }

        for (Map.Entry<String, String> property : asStrings.entrySet())
        {
            String value = property.getValue();

            if ((value != null)
                        && value.contains(TemplateCatalog.PLACEHOLDER_PREFIX)
                        && value.contains(TemplateCatalog.PLACEHOLDER_SUFFIX))
            {
                found.add(location + " -> " + property.getKey() + " = \"" + value + "\"");
            }
        }

        return found;
    }


    /**
     * Build a qualified name for a newly created test element that is unique to this test run and
     * recognisable as templates-fvt debris if it is ever left behind.
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
     * status filtering, and now need to be fully removed).  Failures are swallowed - this is a
     * best-effort cleanup operation, not something a test should fail on.
     * <br><br>
     * PURGE only succeeds on an element that is already soft-deleted (the classic OMRS lifecycle enforced
     * by the repository connector), so this soft-deletes the element first - best-effort, since it may
     * already be deleted (for example by an earlier failed run, or by a cascade delete from one of its own
     * anchor/owner elements) - before purging it.
     * <br><br>
     * {@code forLineage} is set on both steps: an element left ARCHIVEd by a test (or a previous test's
     * LOOK_FOR_LINEAGE call) carries the Memento classification, and without forLineage=true it would be
     * invisible to the lookup each delete step needs to do first - the delete would silently no-op rather
     * than throw, so the element would never actually be removed.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     */
    static void purgeElement(OpenMetadataStore openMetadataStore,
                             String            elementGUID)
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
            // Best-effort - see the method comment above for why this is expected to fail sometimes.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);
            purgeOptions.setCascadedDelete(true);
            purgeOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort cleanup - nothing further can be done if this fails.
        }
    }


    /**
     * Permanently remove a relationship.  Used by tests that create their own relationships to tidy up after
     * themselves, including ones that were only ever soft-deleted to check status filtering.  Failures are
     * swallowed - this is a best-effort cleanup operation, not something a test should fail on.
     * <br><br>
     * PURGE only succeeds on a relationship that is already soft-deleted (the classic OMRS lifecycle enforced
     * by the repository connector), so this soft-deletes the relationship first - best-effort, since it may
     * already be deleted - before purging it.
     *
     * @param openMetadataStore store to delete through
     * @param relationshipGUID relationship to remove
     */
    static void purgeRelationship(OpenMetadataStore openMetadataStore,
                                  String            relationshipGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort - see the method comment above for why this is expected to fail sometimes.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort cleanup - nothing further can be done if this fails.
        }
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

            for (OpenMetadataElement element : found)
            {
                purgeElement(openMetadataStore, element.getElementGUID());
                purgedCount++;
            }

            emptyPassLimit--;
        }

        if (purgedCount > 0)
        {
            System.out.println("templates-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }
}
