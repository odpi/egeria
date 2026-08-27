/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify that paging through the elements related to one element returns every one of them, once.
 * <br>
 * Paging is defined by the termination signal, not by the size of a page: a caller keeps asking, moving
 * the starting point on by the page size each time, until it is handed null.  An empty list part way
 * through is ordinary - it means everything the repository returned for that batch was filtered out -
 * and the caller carries on.  A short page means nothing either way.
 * <br>
 * So what is asserted here is what the contract actually promises: every member is returned exactly once
 * across the whole traversal, and nothing that is not a member appears.  Asserting full pages would test
 * an implementation detail the contract deliberately leaves open.
 * <br>
 * Two things have to be true of the data before the property can be tested at all, and the obvious
 * arrangement satisfies neither:
 * <ul>
 *     <li>There must be more relationships than fit in a page, or every page is the last one.</li>
 *     <li>The starting element must also sit at the <em>other</em> end of relationships of the same type,
 *     so that narrowing to one end actually discards rows.  A collection whose CollectionMembership
 *     relationships all have it at end 1 is filtered by a test that throws nothing away, and pages come
 *     back full whether the narrowing happens in the repository or afterwards.</li>
 * </ul>
 * So the collection here is given members <em>and</em> made a member of several other collections.  Asking
 * for its members then has to leave the parent memberships out, which is what makes a page short if the
 * leaving out happens after the page was chosen.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class RelatedElementPagingFVT
{
    private static final int    MEMBER_COUNT = 25;
    private static final int    PARENT_COUNT = 15;
    private static final int    PAGE_SIZE    = 10;
    private static final String CATEGORY     = "RelatedPaging";


    @Test
    void pagingThroughRelatedElementsReturnsEachOnce() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String collectionGUID = this.createCollection(collectionClient, createdGUIDs, "Parent");

            Set<String> memberGUIDs = new HashSet<>();

            for (int i = 0; i < MEMBER_COUNT; i++)
            {
                String memberGUID = this.createCollection(collectionClient, createdGUIDs, "Member" + i);

                memberGUIDs.add(memberGUID);

                collectionClient.addToCollection(collectionGUID, memberGUID, null, null);
            }

            /*
             * The same collection is now made a member of several others, so it sits at end 2 of a further
             * set of CollectionMembership relationships.  Those must not appear when its own members are
             * asked for - and they are what the end constraint has to exclude.
             */
            for (int i = 0; i < PARENT_COUNT; i++)
            {
                String parentGUID = this.createCollection(collectionClient, createdGUIDs, "Parent" + i);

                collectionClient.addToCollection(parentGUID, collectionGUID, null, null);
            }

            /*
             * Page through the members.  Every page before the last has to be full: a short page is the
             * signature of results being discarded after the repository applied the page size, and a caller
             * that stops on a short page would never see the rest.
             */
            Set<String> pagedGUIDs = new HashSet<>();
            int         startFrom  = 0;
            int         pageCount  = 0;

            while (true)
            {
                QueryOptions queryOptions = new QueryOptions();

                queryOptions.setStartFrom(startFrom);
                queryOptions.setPageSize(PAGE_SIZE);

                RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(collectionGUID,
                                                                                                1,
                                                                                                OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                queryOptions);

                List<RelatedMetadataElement> pageElements = (page == null) ? null : page.getElementList();

                if (pageElements == null)
                {
                    /*
                     * Null is the end of the results.  An empty list is not - it means this batch was
                     * entirely filtered out - so the loop keeps going until it is handed null.
                     */
                    break;
                }

                for (RelatedMetadataElement relatedElement : pageElements)
                {
                    String returnedGUID = relatedElement.getElement().getElementGUID();

                    assertFalse(pagedGUIDs.contains(returnedGUID),
                                "Member " + returnedGUID + " was returned on more than one page");

                    assertTrue(memberGUIDs.contains(returnedGUID),
                               "Element " + returnedGUID + " is not a member of this collection - a parent " +
                                       "collection has been returned as though it were one");

                    pagedGUIDs.add(returnedGUID);
                }

                startFrom += PAGE_SIZE;
                pageCount++;
            }

            assertEquals(memberGUIDs, pagedGUIDs, "Paging should return every member exactly once");
            assertTrue(pageCount > 0, "Paging should have made at least one request");
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
     * Create a collection and remember it for clean up.
     *
     * @param collectionClient client to create through
     * @param createdGUIDs list to record the new element in
     * @param label distinguishes the elements of one run from each other
     * @return new element's unique identifier
     * @throws Exception the element could not be created
     */
    private String createCollection(CollectionClient collectionClient,
                                    List<String>     createdGUIDs,
                                    String           label) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties = new CollectionProperties();

        properties.setQualifiedName(QueryFvtTestSupport.newQualifiedName(CATEGORY + ":" + label));
        properties.setDisplayName("query-fvt " + CATEGORY + " " + label);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, properties, null);

        createdGUIDs.add(elementGUID);

        return elementGUID;
    }
}
