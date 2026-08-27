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
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify what happens when a caller asks for the elements related to one element as at a particular moment,
 * and some of those relationships are outside their effectivity window.
 * <br>
 * As with the type filter, there are two properties here and they are not equally serious.
 * <br>
 * The first is correctness: a relationship whose effectivity has ended must not be returned for a moment
 * after it ended. That is what effectivity means, and it holds however the filtering is implemented.
 * <br>
 * The second is efficiency. If the effectivity window is not part of the question the repository is asked,
 * the repository chooses a page of relationships without regard to it and the expired ones are discarded
 * afterwards - after the page boundary has already been drawn. The caller is then made to walk the whole
 * membership to collect the few still in effect.
 * <br>
 * The data is arranged so the difference is visible: the expired memberships are created first and the
 * results are ordered oldest first, so the earliest pages consist entirely of relationships the effectivity
 * filter removes. Pushed down, those pages come back full of live memberships; filtered afterwards, they come
 * back empty.
 * <br>
 * The window is half open - in effect at the from date, out of effect at the to date - so the expired
 * memberships here are given a to date firmly in the past rather than one close to now, and the assertions
 * do not depend on where the boundary instant falls.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class RelatedElementEffectivityFVT
{
    private static final int    EXPIRED_COUNT = 15;
    private static final int    LIVE_COUNT    = 12;
    private static final int    PAGE_SIZE     = 10;
    private static final String CATEGORY      = "RelatedEffectivity";

    private static final long ONE_DAY = 24L * 60L * 60L * 1000L;


    @Test
    void relatedElementsOutsideTheirEffectivityWindowAreNotReturned() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String collectionGUID = this.createCollection(collectionClient, createdGUIDs, "Parent");

            Date now       = new Date();
            Date longAgo   = new Date(now.getTime() - (30L * ONE_DAY));
            Date yesterday = new Date(now.getTime() - ONE_DAY);
            Date tomorrow  = new Date(now.getTime() + ONE_DAY);

            /*
             * Memberships that ended yesterday, created first so that oldest-first ordering puts them in the
             * earliest pages.
             */
            Set<String> expiredGUIDs = new HashSet<>();

            for (int i = 0; i < EXPIRED_COUNT; i++)
            {
                String memberGUID = this.createCollection(collectionClient, createdGUIDs, "Expired" + i);

                expiredGUIDs.add(memberGUID);

                this.addMember(collectionClient, collectionGUID, memberGUID, longAgo, yesterday);
            }

            /*
             * Memberships in effect now - a window that has started and has not ended.
             */
            Set<String> liveGUIDs = new HashSet<>();

            for (int i = 0; i < LIVE_COUNT; i++)
            {
                String memberGUID = this.createCollection(collectionClient, createdGUIDs, "Live" + i);

                liveGUIDs.add(memberGUID);

                this.addMember(collectionClient, collectionGUID, memberGUID, longAgo, tomorrow);
            }

            Set<String>   pagedGUIDs = new HashSet<>();
            List<Integer> pageSizes  = new ArrayList<>();
            int           startFrom  = 0;

            while (true)
            {
                QueryOptions queryOptions = new QueryOptions();

                queryOptions.setStartFrom(startFrom);
                queryOptions.setPageSize(PAGE_SIZE);
                queryOptions.setEffectiveTime(now);
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

                    assertFalse(expiredGUIDs.contains(returnedGUID),
                                "Member " + returnedGUID + " was returned even though its membership ended "
                                        + "yesterday - a relationship outside its effectivity window is not in "
                                        + "effect and must not be returned for a moment after it ended");

                    assertTrue(liveGUIDs.contains(returnedGUID),
                               "Element " + returnedGUID + " is not a member of this collection");

                    pagedGUIDs.add(returnedGUID);
                }

                pageSizes.add(pageElements.size());

                startFrom += PAGE_SIZE;
            }

            /*
             * Correctness first - this holds whether or not the effectivity reaches the repository.
             */
            assertEquals(liveGUIDs, pagedGUIDs,
                         "Paging should return every membership that is in effect, exactly once");

            /*
             * Then efficiency.  Twelve live memberships and a page size of ten answer in two populated pages
             * and a terminating null.  A repository asked without the effectivity window spends its first
             * pages returning memberships that are then discarded, and reports them as empty.  Empty pages are
             * legal, so this is not a contract failure - it is the cost this test exists to show.
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
                         "Excluding memberships outside their effectivity window should be done by the "
                                 + "repository, not after paging: " + emptyPages + " of " + pageSizes.size()
                                 + " pages came back empty because expired memberships were fetched and then "
                                 + "discarded (page sizes were " + pageSizes + ")");
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
     * Add an element to a collection with an effectivity window on the membership relationship.
     *
     * @param collectionClient client to create through
     * @param collectionGUID the collection
     * @param memberGUID the element to add
     * @param effectiveFrom when the membership starts
     * @param effectiveTo when the membership stops
     * @throws Exception the membership could not be created
     */
    private void addMember(CollectionClient collectionClient,
                           String           collectionGUID,
                           String           memberGUID,
                           Date             effectiveFrom,
                           Date             effectiveTo) throws Exception
    {
        CollectionMembershipProperties membershipProperties = new CollectionMembershipProperties();

        membershipProperties.setEffectiveFrom(effectiveFrom);
        membershipProperties.setEffectiveTo(effectiveTo);

        collectionClient.addToCollection(collectionGUID, memberGUID, null, membershipProperties);
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
