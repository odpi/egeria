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
 * Verify what happens when a caller asks for the elements related to one element and names the type of
 * element they want at the other end.
 * <br>
 * There are two separate properties here, and they are not equally serious.
 * <br>
 * The first is correctness, and the contract states it: paging returns every matching element exactly
 * once, and nothing of the wrong type appears.  A caller pages by asking again from a moved-on starting
 * point until it is handed null - empty and short pages part way through are ordinary and mean nothing.
 * That property holds however the type narrowing is implemented, so this test guards it as a regression
 * rather than as a demonstration.
 * <br>
 * The second is efficiency.  If the type is not part of the question the repository is asked, the
 * repository chooses a page of relationships of every type of member, and the ones reaching the wrong
 * type are discarded afterwards - after the page boundary has already been drawn.  The caller is then
 * made to walk the whole membership to collect the few it wanted.  Naming the type in the query lets the
 * repository skip them instead.
 * <br>
 * The data is arranged so the difference is visible: the unwanted members are created first, and the
 * results are ordered oldest first, so the earliest pages are entirely made up of members that the type
 * narrowing removes.  Pushed down, those pages come back full of wanted members; filtered afterwards,
 * they come back empty.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class RelatedElementTypeFilterFVT
{
    private static final int    UNWANTED_COUNT = 15;
    private static final int    WANTED_COUNT   = 12;
    private static final int    PAGE_SIZE      = 10;
    private static final String CATEGORY       = "RelatedTypeFilter";

    /**
     * Namespace is a subtype of Collection, so a member of this type is a member the plain-Collection
     * members are not - and asking for it also exercises the type matching following the hierarchy.
     */
    private static final String WANTED_TYPE_NAME = OpenMetadataType.NAMESPACE_COLLECTION.typeName;


    @Test
    void relatedElementsCanBeNarrowedToATypeAtTheFarEnd() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String collectionGUID = this.createCollection(collectionClient, createdGUIDs, null, "Parent");

            /*
             * The members of the wrong type are created first, so that ordering the results oldest first
             * puts them all in the earliest pages.
             */
            for (int i = 0; i < UNWANTED_COUNT; i++)
            {
                String unwantedGUID = this.createCollection(collectionClient, createdGUIDs, null, "Unwanted" + i);

                collectionClient.addToCollection(collectionGUID, unwantedGUID, null, null);
            }

            Set<String> wantedGUIDs = new HashSet<>();

            for (int i = 0; i < WANTED_COUNT; i++)
            {
                String wantedGUID = this.createCollection(collectionClient, createdGUIDs, WANTED_TYPE_NAME, "Wanted" + i);

                wantedGUIDs.add(wantedGUID);

                collectionClient.addToCollection(collectionGUID, wantedGUID, null, null);
            }

            Set<String> pagedGUIDs   = new HashSet<>();
            List<Integer> pageSizes  = new ArrayList<>();
            int           startFrom  = 0;

            while (true)
            {
                QueryOptions queryOptions = new QueryOptions();

                queryOptions.setStartFrom(startFrom);
                queryOptions.setPageSize(PAGE_SIZE);
                queryOptions.setMetadataElementTypeName(WANTED_TYPE_NAME);

                /*
                 * Oldest first, explicitly: the default order is most recently updated first, which would
                 * put the wanted members - created last - at the front and hide the effect being tested.
                 */
                queryOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);

                RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(collectionGUID,
                                                                                               1,
                                                                                               OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                               queryOptions);

                List<RelatedMetadataElement> pageElements = (page == null) ? null : page.getElementList();

                if (pageElements == null)
                {
                    /*
                     * Null ends the traversal.  An empty list does not.
                     */
                    break;
                }

                for (RelatedMetadataElement relatedElement : pageElements)
                {
                    String returnedGUID = relatedElement.getElement().getElementGUID();

                    assertFalse(pagedGUIDs.contains(returnedGUID),
                                "Member " + returnedGUID + " was returned on more than one page");

                    assertTrue(wantedGUIDs.contains(returnedGUID),
                               "Element " + returnedGUID + " is not a " + WANTED_TYPE_NAME + " member of this " +
                                       "collection - the type given for the far end was not honoured");

                    pagedGUIDs.add(returnedGUID);
                }

                pageSizes.add(pageElements.size());

                startFrom += PAGE_SIZE;
            }

            /*
             * Correctness first - this holds whether or not the type reaches the repository.
             */
            assertEquals(wantedGUIDs, pagedGUIDs,
                         "Paging should return every member of the requested type exactly once");

            /*
             * Then efficiency.  There are 12 wanted members and a page size of 10, so a repository that is
             * asked the whole question answers it in two populated pages and a terminating null.  One that
             * is asked a broader question spends its first pages returning members that are then discarded,
             * and reports them as empty.  Empty pages are legal, so this is not a contract failure - it is
             * the cost this test exists to show.
             */
            int emptyPages = 0;

            for (int pageSize : pageSizes)
            {
                if (pageSize == 0)
                {
                    emptyPages++;
                }
            }

            assertEquals(0, emptyPages,
                         "Narrowing the far end to " + WANTED_TYPE_NAME + " should be done by the repository, " +
                                 "not after paging: " + emptyPages + " of " + pageSizes.size() + " pages came " +
                                 "back empty because the members of the wrong type were fetched and then " +
                                 "discarded (page sizes were " + pageSizes + ")");
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
     * Create a collection, optionally of a subtype, and remember it for clean up.
     *
     * @param collectionClient client to create through
     * @param createdGUIDs list to record the new element in
     * @param typeName subtype to create, or null for a plain Collection
     * @param label distinguishes the elements of one run from each other
     * @return new element's unique identifier
     * @throws Exception the element could not be created
     */
    private String createCollection(CollectionClient collectionClient,
                                    List<String>     createdGUIDs,
                                    String           typeName,
                                    String           label) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties = new CollectionProperties();

        if (typeName != null)
        {
            properties.setTypeName(typeName);
        }

        properties.setQualifiedName(QueryFvtTestSupport.newQualifiedName(CATEGORY + ":" + label));
        properties.setDisplayName("query-fvt " + CATEGORY + " " + label);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, properties, null);

        createdGUIDs.add(elementGUID);

        return elementGUID;
    }
}
