/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify that paging still works when the query asks for elements to be excluded by classification.
 * <br>
 * skipClassifiedElements and includeOnlyClassifiedElements can be applied either by the repository, as
 * part of the query, or here afterwards on whatever the repository returned.  Either way the caller sees
 * the same elements: paging ends when a null is returned, and an empty list part way through simply means
 * that batch was entirely filtered out, so nothing is lost by filtering late.  What filtering late costs
 * is work - a larger read and more round trips to collect the same answers.
 * <br>
 * This asserts the part that must hold however the filtering is done: every wanted element is returned
 * exactly once across the traversal, and no excluded element appears at all.  The elements are arranged
 * so that the excluded ones lead - created first, and the query asking for oldest first - because that is
 * the arrangement where late filtering produces empty pages, and the traversal has to cope with them.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ClassificationFilterPagingFVT
{
    private static final int    EXCLUDED_COUNT = 15;
    private static final int    WANTED_COUNT   = 12;
    private static final int    PAGE_SIZE      = 10;
    private static final String CATEGORY       = "ClassificationFilterPaging";


    @Test
    void pagingHonoursPageSizeWhenElementsAreExcludedByClassification() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        /*
         * Shared by every element of this run so that one search returns exactly them.
         */
        String runName = QueryFvtTestSupport.newQualifiedName(CATEGORY + ":run");

        try
        {
            /*
             * The excluded elements are created first so that they occupy the first page.
             */
            for (int i = 0; i < EXCLUDED_COUNT; i++)
            {
                String guid = this.createCollection(collectionClient, createdGUIDs, runName);

                openMetadataStore.classifyMetadataElementInStore(guid,
                                                                 OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                                 new MetadataSourceOptions(),
                                                                 null);
            }

            Set<String> wantedGUIDs = new HashSet<>();

            for (int i = 0; i < WANTED_COUNT; i++)
            {
                wantedGUIDs.add(this.createCollection(collectionClient, createdGUIDs, runName));
            }

            /*
             * Ask for the elements of this run that are not templates, a page at a time.
             */
            Set<String> pagedGUIDs = new HashSet<>();
            int         startFrom  = 0;

            while (true)
            {
                QueryOptions queryOptions = new QueryOptions();

                queryOptions.setStartFrom(startFrom);
                queryOptions.setPageSize(PAGE_SIZE);
                queryOptions.setSkipClassifiedElements(List.of(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName));

                /*
                 * Oldest first, so that the excluded elements - which were created first - lead the
                 * results.  Without stating the order the default puts the most recently created first,
                 * which is the wanted elements, and the excluded ones never get the chance to fill a page.
                 */
                queryOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);

                List<OpenMetadataRootElement> page = collectionClient.getCollectionsByName(runName, queryOptions);

                if (page == null)
                {
                    /*
                     * Null ends the results.  An empty list does not - it means this batch was entirely
                     * filtered out, which is exactly what the excluded elements at the front produce.
                     */
                    break;
                }

                for (OpenMetadataRootElement element : page)
                {
                    String returnedGUID = element.getElementHeader().getGUID();

                    assertFalse(pagedGUIDs.contains(returnedGUID),
                                "Collection " + returnedGUID + " was returned on more than one page");

                    assertTrue(wantedGUIDs.contains(returnedGUID),
                               "Collection " + returnedGUID + " carries the excluded classification and should " +
                                       "not have been returned");

                    pagedGUIDs.add(returnedGUID);
                }

                startFrom += PAGE_SIZE;
            }

            assertEquals(wantedGUIDs, pagedGUIDs, "Paging should return every unclassified collection exactly once");
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
     * Create a collection sharing one qualified name across the run, and remember it for clean up.
     *
     * @param collectionClient client to create through
     * @param createdGUIDs list to record the new element in
     * @param runName qualified name shared by every element of this run
     * @return new element's unique identifier
     * @throws Exception the element could not be created
     */
    private String createCollection(CollectionClient collectionClient,
                                    List<String>     createdGUIDs,
                                    String           runName) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties = new CollectionProperties();

        /*
         * A unique qualified name per element - it is a unique property and the repository refuses a
         * duplicate - with the run's shared name carried on the display name, which is the other property
         * getCollectionsByName() matches on.
         */
        properties.setQualifiedName(QueryFvtTestSupport.newQualifiedName(CATEGORY));
        properties.setDisplayName(runName);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, properties, null);

        createdGUIDs.add(elementGUID);

        return elementGUID;
    }
}
