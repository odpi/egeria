/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PagingStabilityFVT checks that paging stays correct when every element in the result set ties on the
 * value being sorted by.
 * <br><br>
 * {@link PagingFVT} covers the same traversal but asks for {@code SequencingOrder.GUID}, which is a total
 * order on its own, so it cannot see this problem.  Every other sequencing a caller can ask for names a
 * value that is not unique - creation time, update time, or a property value - and each page is a separate
 * execution of an "order by ... limit ... offset ..." query rather than a server-side cursor.  If the
 * ordering is not a total order, rows that tie may be ordered differently by each execution, so an element
 * can move between offset windows from one page to the next and be returned twice, or skipped entirely,
 * while the traversal still terminates normally and reports no error.  That is what made the original
 * defect so hard to see: a broad search silently returned a fraction of the real population.
 * <br><br>
 * The elements below are arranged so that every one of these orderings is a complete tie: they are created
 * as fast as the server will take them (so creation and update times collide), they all share one
 * {@code category} value, and none of them is given a {@code versionIdentifier} - the last of these
 * reproduces the case where a caller sequences by a property that is null for an entire type, which ties
 * every element together at the null position.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PagingStabilityFVT
{
    private static final int    ELEMENT_COUNT  = 45;
    private static final int    PAGE_SIZE      = 10;
    private static final String CATEGORY       = "PagingStability";

    /**
     * Carried by every element of the run, so that sequencing by it is a complete tie.
     */
    private static final String SHARED_CATEGORY = "query-fvt paging stability - one value for every element";


    @Test
    void pagingIsCompleteWhenEveryElementTiesOnTheSortKey() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName(CATEGORY);
        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();

                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();

                createProperties.setQualifiedName(runPrefix + ":" + String.format("%02d", i));
                createProperties.setDisplayName("query-fvt Paging Stability Collection " + i);

                /*
                 * The same value for every element, so that sequencing by it leaves nothing to order on.
                 * versionIdentifier is deliberately left unset for the null-property case below.
                 */
                createProperties.setCategory(SHARED_CATEGORY);

                createdGUIDs.add(collectionClient.createCollection(newElementOptions, null, createProperties, null));
            }

            SearchProperties searchProperties = new SearchProperties();

            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            runPrefix,
                                                                            PropertyComparisonOperator.STARTS_WITH));

            Set<String> expectedGUIDs = new HashSet<>(createdGUIDs);

            long reportedCount = openMetadataStore.countMetadataElements(OpenMetadataType.COLLECTION.typeName,
                                                                         null,
                                                                         searchProperties,
                                                                         null);

            assertEquals(ELEMENT_COUNT, reportedCount,
                         "countMetadataElements should report exactly the number of collections this test created");

            /*
             * The sequencing left unset - whatever the default is, it is one of the orderings below and is
             * what the majority of callers actually get.
             */
            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               null, null, "the default sequencing");

            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               SequencingOrder.CREATION_DATE_RECENT, null,
                                               "CREATION_DATE_RECENT");

            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               SequencingOrder.CREATION_DATE_OLDEST, null,
                                               "CREATION_DATE_OLDEST");

            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               SequencingOrder.LAST_UPDATE_RECENT, null,
                                               "LAST_UPDATE_RECENT");

            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               SequencingOrder.PROPERTY_ASCENDING, OpenMetadataProperty.CATEGORY.name,
                                               "PROPERTY_ASCENDING on a property every element shares a value for");

            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               SequencingOrder.PROPERTY_DESCENDING, OpenMetadataProperty.CATEGORY.name,
                                               "PROPERTY_DESCENDING on a property every element shares a value for");

            /*
             * The case that turned a duplicated scan into an incomplete one: the caller's chosen
             * sequencingProperty is null for every element, so the property value orders nothing at all.
             */
            assertPagingVisitsEveryElementOnce(openMetadataStore, searchProperties, expectedGUIDs,
                                               SequencingOrder.PROPERTY_ASCENDING,
                                               OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                               "PROPERTY_ASCENDING on a property that is null for every element");
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    /**
     * Page through the whole result set with the supplied sequencing and check that it visited every
     * expected element exactly once - no element returned on two pages, and none missed.
     * <br><br>
     * The first page is also fetched twice and the two results compared.  A traversal can only line its
     * offsets up if repeating the same request gives the same answer, and checking it directly says
     * "the ordering is not stable" rather than leaving it to be inferred from a duplicate or a gap
     * further into the traversal.
     *
     * @param openMetadataStore store to search through
     * @param searchProperties conditions that select exactly this run's elements
     * @param expectedGUIDs the elements the traversal has to visit
     * @param sequencingOrder ordering to ask for, or null to leave the default in place
     * @param sequencingProperty property to order by, where the ordering names one
     * @param description how to describe this ordering in an assertion failure
     * @throws Exception problem talking to the server
     */
    private void assertPagingVisitsEveryElementOnce(OpenMetadataStore openMetadataStore,
                                                    SearchProperties  searchProperties,
                                                    Set<String>       expectedGUIDs,
                                                    SequencingOrder   sequencingOrder,
                                                    String            sequencingProperty,
                                                    String            description) throws Exception
    {
        List<String> firstPage       = pageGUIDs(openMetadataStore, searchProperties, sequencingOrder, sequencingProperty, 0);
        List<String> firstPageRepeat = pageGUIDs(openMetadataStore, searchProperties, sequencingOrder, sequencingProperty, 0);

        assertEquals(firstPage, firstPageRepeat,
                     "Repeating the same request with " + description + " returned a different page, so the "
                             + "ordering is not stable and the paging offsets cannot line up");

        Set<String> visitedGUIDs = new HashSet<>();
        int         startFrom    = 0;
        int         pagesFetched = 0;

        while (true)
        {
            List<String> page = pageGUIDs(openMetadataStore, searchProperties, sequencingOrder, sequencingProperty, startFrom);

            if (page == null)
            {
                /*
                 * Null ends the results.  An empty list does not - it means this batch was entirely
                 * filtered out, and the next one may still hold something.  See
                 * ClassificationFilterPagingFVT, which is built around exactly that case.
                 */
                break;
            }

            assertTrue(page.size() <= PAGE_SIZE, "A page should never return more than the requested page size");

            for (String elementGUID : page)
            {
                assertTrue(visitedGUIDs.add(elementGUID),
                           "Element " + elementGUID + " was returned on more than one page when paging with "
                                   + description);
            }

            pagesFetched++;
            startFrom += PAGE_SIZE;

            // Safety valve in case paging never terminates.
            assertTrue(pagesFetched <= (ELEMENT_COUNT / PAGE_SIZE) + 5,
                       "Paging with " + description + " did not terminate as expected");
        }

        assertEquals(expectedGUIDs, visitedGUIDs,
                     "Paging with " + description + " should visit every element of the result set exactly once");
    }


    /**
     * Fetch one page and return the GUIDs it contains, in the order the server returned them.
     *
     * @param openMetadataStore store to search through
     * @param searchProperties conditions that select exactly this run's elements
     * @param sequencingOrder ordering to ask for, or null to leave the default in place
     * @param sequencingProperty property to order by, where the ordering names one
     * @param startFrom paging start point
     * @return GUIDs on the page, or null once the traversal has run out of results - an empty list means
     * this batch held nothing, not that the traversal is over
     * @throws Exception problem talking to the server
     */
    private List<String> pageGUIDs(OpenMetadataStore openMetadataStore,
                                   SearchProperties  searchProperties,
                                   SequencingOrder   sequencingOrder,
                                   String            sequencingProperty,
                                   int               startFrom) throws Exception
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(PAGE_SIZE);

        if (sequencingOrder != null)
        {
            queryOptions.setSequencingOrder(sequencingOrder);
        }

        if (sequencingProperty != null)
        {
            queryOptions.setSequencingProperty(sequencingProperty);
        }

        List<OpenMetadataElement> page = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

        if (page == null)
        {
            return null;
        }

        List<String> elementGUIDs = new ArrayList<>();

        for (OpenMetadataElement element : page)
        {
            assertNotNull(element.getElementGUID(), "Every returned element should carry a GUID");

            elementGUIDs.add(element.getElementGUID());
        }

        return elementGUIDs;
    }
}
