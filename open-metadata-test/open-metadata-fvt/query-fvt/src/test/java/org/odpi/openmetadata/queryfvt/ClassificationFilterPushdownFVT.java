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
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ClassificationFilterPushdownFVT checks that a search-string query narrowed by
 * {@code includeOnlyClassifiedElements} returns the classified elements on its first page, rather than
 * whichever of them happened to survive the filtering of a page that was chosen without the filter.
 * <br><br>
 * {@link ClassificationFilterPagingFVT} covers the weaker guarantee that holds however the filtering is
 * done - a full traversal returns every wanted element exactly once, and an empty page part way through
 * does not end it.  That guarantee is not enough on its own.  The ordinary shape of a "find the elements
 * classified X" call is a single request for one page, and a caller that makes one is entitled to get
 * answers rather than candidates.  When the filter is applied only after the repository has already
 * chosen the page, a whole page can be filtered away and the caller cannot tell an empty result from
 * "nothing matches" - which is exactly how this presented: an application showed no results at all while
 * the classified elements were sitting in the repository the whole time.
 * <br><br>
 * The elements are arranged so that the unclassified ones lead - created first, with the query asking for
 * oldest first, and more of them than fit on a page - because that is the arrangement where filtering
 * after the fact empties the first page completely.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ClassificationFilterPushdownFVT
{
    private static final int    LEADING_UNCLASSIFIED_COUNT = 15;
    private static final int    CLASSIFIED_COUNT           = 6;
    private static final int    PAGE_SIZE                  = 10;
    private static final String CATEGORY                   = "ClassificationFilterPushdown";


    @Test
    void searchStringQueryAppliesClassificationFilterBeforePaging() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        /*
         * Shared by every element of this run so that one search string returns exactly them.
         */
        String       runName      = QueryFvtTestSupport.newQualifiedName(CATEGORY + ":run");
        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            /*
             * The unclassified elements are created first, and there are more of them than one page
             * holds, so that they fill the first page of an oldest-first query on their own.
             */
            for (int i = 0; i < LEADING_UNCLASSIFIED_COUNT; i++)
            {
                this.createCollection(collectionClient, createdGUIDs, runName);
            }

            Set<String> classifiedGUIDs = new HashSet<>();

            for (int i = 0; i < CLASSIFIED_COUNT; i++)
            {
                String elementGUID = this.createCollection(collectionClient, createdGUIDs, runName);

                openMetadataStore.classifyMetadataElementInStore(elementGUID,
                                                                 OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                                 new MetadataSourceOptions(),
                                                                 null);

                classifiedGUIDs.add(elementGUID);
            }

            /*
             * One request, for one page, exactly as a caller looking for "the templates of this run"
             * would make it.
             */
            List<OpenMetadataRootElement> firstPage = collectionClient.findCollections(runName,
                                                                                       this.getSearchOptions(0));

            assertNotNull(firstPage,
                          "A search narrowed by includeOnlyClassifiedElements should return the classified "
                                  + "elements, not null - there are " + CLASSIFIED_COUNT + " of them in the repository");

            assertEquals(classifiedGUIDs, this.getGUIDs(firstPage),
                         "The first page of a search narrowed by includeOnlyClassifiedElements should hold the "
                                 + "classified elements.  Getting fewer (commonly none) means the classification "
                                 + "filter was applied to a page the repository had already chosen without it, so "
                                 + "the " + LEADING_UNCLASSIFIED_COUNT + " unclassified elements ahead of them "
                                 + "consumed the page");

            /*
             * The traversal has to stay correct as well as start correctly: no element twice, none missed,
             * and nothing returned that lacks the classification.
             */
            Set<String> pagedGUIDs = new HashSet<>();
            int         startFrom  = 0;

            while (true)
            {
                List<OpenMetadataRootElement> page = collectionClient.findCollections(runName,
                                                                                       this.getSearchOptions(startFrom));

                if (page == null)
                {
                    /*
                     * Null ends the results.  An empty list does not - see ClassificationFilterPagingFVT.
                     */
                    break;
                }

                for (String elementGUID : this.getGUIDs(page))
                {
                    assertTrue(classifiedGUIDs.contains(elementGUID),
                               "Collection " + elementGUID + " does not carry the required classification and "
                                       + "should not have been returned");

                    assertTrue(pagedGUIDs.add(elementGUID),
                               "Collection " + elementGUID + " was returned on more than one page");
                }

                startFrom += PAGE_SIZE;
            }

            assertEquals(classifiedGUIDs, pagedGUIDs,
                         "Paging should return every classified collection exactly once");
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
     * Build the options for one page of the search: the classification filter under test, a page smaller
     * than the block of unclassified elements, and oldest-first so that those unclassified elements lead.
     * Without stating the order the default puts the most recently created first, which is the classified
     * elements, and the unclassified ones never get the chance to fill a page.
     *
     * @param startFrom paging start point
     * @return search options
     */
    private SearchOptions getSearchOptions(int startFrom)
    {
        SearchOptions searchOptions = new SearchOptions();

        searchOptions.setStartFrom(startFrom);
        searchOptions.setPageSize(PAGE_SIZE);
        searchOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);
        searchOptions.setIncludeOnlyClassifiedElements(List.of(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName));

        return searchOptions;
    }


    /**
     * Extract the GUIDs from a page of results.
     *
     * @param page results returned by the server
     * @return the GUIDs they carry
     */
    private Set<String> getGUIDs(List<OpenMetadataRootElement> page)
    {
        Set<String> elementGUIDs = new HashSet<>();

        for (OpenMetadataRootElement element : page)
        {
            elementGUIDs.add(element.getElementHeader().getGUID());
        }

        return elementGUIDs;
    }


    /**
     * Create a collection sharing one display name across the run, and remember it for clean up.
     *
     * @param collectionClient client to create through
     * @param createdGUIDs list to record the new element in
     * @param runName name shared by every element of this run, and the string the search matches on
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
         * duplicate - with the run's shared name carried on the display name, which is one of the
         * properties the search string is matched against.
         */
        properties.setQualifiedName(QueryFvtTestSupport.newQualifiedName(CATEGORY));
        properties.setDisplayName(runName);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, properties, null);

        createdGUIDs.add(elementGUID);

        return elementGUID;
    }
}
