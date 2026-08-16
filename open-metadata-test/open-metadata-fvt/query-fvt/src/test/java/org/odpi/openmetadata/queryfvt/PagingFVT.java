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
 * PagingFVT checks that paging genuinely works on {@code OpenMetadataStore.findMetadataElements}: that
 * paging through every page (with a page size much smaller than the total result set) visits every
 * matching element exactly once with no duplicates and no gaps, and that the total number of elements
 * visited that way matches what {@code countMetadataElements} reports for the same search criteria.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PagingFVT
{
    private static final int  ELEMENT_COUNT = 37;
    private static final int  PAGE_SIZE     = 10;
    private static final String CATEGORY    = "Paging";

    @Test
    void pagingThroughAllElementsMatchesCount() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String            runPrefix = QueryFvtTestSupport.newQualifiedName(CATEGORY);
        List<String>      createdGUIDs = new ArrayList<>();

        try
        {
            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();
                createProperties.setQualifiedName(runPrefix + ":" + i);
                createProperties.setDisplayName("query-fvt Paging Collection " + i);

                createdGUIDs.add(collectionClient.createCollection(newElementOptions, null, createProperties, null));
            }

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            long reportedCount = openMetadataStore.countMetadataElements(OpenMetadataType.COLLECTION.typeName,
                                                                          null,
                                                                          searchProperties,
                                                                          null);

            assertEquals(ELEMENT_COUNT, reportedCount,
                         "countMetadataElements should report exactly the number of collections this test created");

            Set<String> visitedGUIDs = new HashSet<>();
            int         startFrom    = 0;
            int         pagesFetched = 0;

            while (true)
            {
                QueryOptions queryOptions = new QueryOptions();
                queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
                queryOptions.setStartFrom(startFrom);
                queryOptions.setPageSize(PAGE_SIZE);
                queryOptions.setSequencingOrder(SequencingOrder.GUID);

                List<OpenMetadataElement> page = openMetadataStore.findMetadataElements(searchProperties,
                                                                                         null,
                                                                                         queryOptions);

                if ((page == null) || page.isEmpty())
                {
                    break;
                }

                assertTrue(page.size() <= PAGE_SIZE, "A page should never return more than the requested page size");

                for (OpenMetadataElement element : page)
                {
                    boolean firstSighting = visitedGUIDs.add(element.getElementGUID());

                    assertTrue(firstSighting, "Element " + element.getElementGUID() + " was returned on more than one page");
                }

                pagesFetched++;
                startFrom += PAGE_SIZE;

                // Safety valve in case paging never terminates.
                assertTrue(pagesFetched <= (ELEMENT_COUNT / PAGE_SIZE) + 5, "Paging did not terminate as expected");
            }

            assertEquals(ELEMENT_COUNT, visitedGUIDs.size(),
                         "Paging through every page should visit exactly the number of elements countMetadataElements reported");

            for (String createdGUID : createdGUIDs)
            {
                assertTrue(visitedGUIDs.contains(createdGUID),
                           "Every element this test created should have been visited while paging through the results");
            }
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    @Test
    void lastPageIsPartial() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        // A count that is not an exact multiple of the page size, so the last page is guaranteed partial.
        int           elementCount = 23;
        int           pageSize     = 10;
        String        runPrefix    = QueryFvtTestSupport.newQualifiedName(CATEGORY + "LastPage");
        List<String>  createdGUIDs = new ArrayList<>();

        try
        {
            for (int i = 0; i < elementCount; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();
                createProperties.setQualifiedName(runPrefix + ":" + i);
                createProperties.setDisplayName("query-fvt Last Page Collection " + i);

                createdGUIDs.add(collectionClient.createCollection(newElementOptions, null, createProperties, null));
            }

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            QueryOptions firstPageOptions = new QueryOptions();
            firstPageOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            firstPageOptions.setStartFrom(0);
            firstPageOptions.setPageSize(pageSize);
            firstPageOptions.setSequencingOrder(SequencingOrder.GUID);

            List<OpenMetadataElement> firstPage = openMetadataStore.findMetadataElements(searchProperties,
                                                                                          null,
                                                                                          firstPageOptions);

            assertNotNull(firstPage);
            assertEquals(pageSize, firstPage.size(), "The first page should be completely full");

            QueryOptions lastPageOptions = new QueryOptions();
            lastPageOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            lastPageOptions.setStartFrom(2 * pageSize);
            lastPageOptions.setPageSize(pageSize);
            lastPageOptions.setSequencingOrder(SequencingOrder.GUID);

            List<OpenMetadataElement> lastPage = openMetadataStore.findMetadataElements(searchProperties,
                                                                                         null,
                                                                                         lastPageOptions);

            assertNotNull(lastPage);
            assertEquals(elementCount - (2 * pageSize), lastPage.size(),
                         "The final, partial page should contain exactly the remaining elements");

            QueryOptions pastTheEndOptions = new QueryOptions();
            pastTheEndOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            pastTheEndOptions.setStartFrom(3 * pageSize);
            pastTheEndOptions.setPageSize(pageSize);

            List<OpenMetadataElement> pastTheEnd = openMetadataStore.findMetadataElements(searchProperties,
                                                                                           null,
                                                                                           pastTheEndOptions);

            assertTrue((pastTheEnd == null) || pastTheEnd.isEmpty(),
                       "Requesting a page entirely past the end of the results should return nothing");
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }
}
