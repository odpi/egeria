/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CommentClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.LikeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.NoteLogClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.PropertyFacetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.RatingClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SearchKeywordClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.RatingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.PropertyFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FeedbackClientFVT covers the clients that attach something to an element that already exists, rather than
 * creating a standalone element of their own: comments, likes, ratings, search keywords, property facets and
 * note logs.
 * <br>
 * These do not fit {@link ClientLifecycleFVT}'s create/retrieve/update/delete shape - there is no
 * {@code createComment}, only {@code addCommentToElement} - so each is driven here against a host element the
 * test creates first.  The assertions are the ones that matter for an attachment: after attaching, the
 * element's list of that attachment contains it; after removing, it does not.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class FeedbackClientFVT
{
    /**
     * Attach a comment, find it on the element, then remove it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void commentClientAttachesAndDetaches() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        CommentClient        commentClient    = connectorContext.getCommentClient();

        String hostGUID    = createHostCollection(connectorContext, "Comment");
        String commentGUID = null;

        try
        {
            CommentProperties commentProperties = new CommentProperties();

            commentProperties.setQualifiedName(ClientFvtTestSupport.newQualifiedName("Comment"));
            // the client requires comment text - a comment with no description is rejected, correctly
            commentProperties.setDescription("client-fvt comment text");

            commentGUID = commentClient.addCommentToElement(hostGUID, new MetadataSourceOptions(), null, commentProperties);

            assertNotNull(commentGUID, "addCommentToElement returned no GUID");
            assertNotNull(commentClient.getCommentByGUID(commentGUID, null),
                          "The comment could not be read back after being added");

            List<OpenMetadataRootElement> attached = commentClient.getAttachedComments(hostGUID, new QueryOptions());

            assertTrue(containsGUID(attached, commentGUID),
                       "getAttachedComments did not return the comment just added to the element");
        }
        finally
        {
            if (commentGUID != null)
            {
                commentClient.deleteComment(commentGUID, new DeleteOptions());
            }

            cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Add a like, see it on the element, then remove it and see it gone.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void likeClientAddsAndRemoves() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        LikeClient           likeClient       = connectorContext.getLikeClient();

        String hostGUID = createHostCollection(connectorContext, "Like");

        try
        {
            likeClient.addLikeToElement(hostGUID, new UpdateOptions(), new LikeProperties());

            assertTrue(isNotEmpty(likeClient.getAttachedLikes(hostGUID, new QueryOptions())),
                       "getAttachedLikes found nothing after a like was added");

            likeClient.removeLikeFromElement(hostGUID, new MetadataSourceOptions());

            assertTrue(! isNotEmpty(likeClient.getAttachedLikes(hostGUID, new QueryOptions())),
                       "getAttachedLikes still returns a like after it was removed");
        }
        finally
        {
            cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Add a rating, see it on the element, then remove it and see it gone.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void ratingClientAddsAndRemoves() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        RatingClient         ratingClient     = connectorContext.getRatingClient();

        String hostGUID = createHostCollection(connectorContext, "Rating");

        try
        {
            RatingProperties ratingProperties = new RatingProperties();

            ratingProperties.setStarRating(StarRating.THREE_STARS);
            ratingProperties.setReview("client-fvt review");

            ratingClient.addRatingToElement(hostGUID, new UpdateOptions(), ratingProperties);

            assertTrue(isNotEmpty(ratingClient.getAttachedRatings(hostGUID, new QueryOptions())),
                       "getAttachedRatings found nothing after a rating was added");

            ratingClient.removeRatingFromElement(hostGUID, new MetadataSourceOptions());

            assertTrue(! isNotEmpty(ratingClient.getAttachedRatings(hostGUID, new QueryOptions())),
                       "getAttachedRatings still returns a rating after it was removed");
        }
        finally
        {
            cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Attach a search keyword to an element, read it back, then remove it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void searchKeywordClientAttachesAndDetaches() throws Exception
    {
        ConnectorContextBase connectorContext    = ConnectorContextFactory.newContext();
        SearchKeywordClient  searchKeywordClient = connectorContext.getSearchKeywordClient();

        String hostGUID    = createHostCollection(connectorContext, "SearchKeyword");
        String keywordGUID = null;

        try
        {
            SearchKeywordProperties keywordProperties = new SearchKeywordProperties();

            // SearchKeywordProperties descends from OpenMetadataRootProperties, not Referenceable, so it has
            // a display name rather than a qualified name to identify it by.
            keywordProperties.setDisplayName(ClientFvtTestSupport.newQualifiedName("SearchKeyword"));

            keywordGUID = searchKeywordClient.addSearchKeywordToElement(hostGUID, new MetadataSourceOptions(),
                                                                         null, keywordProperties);

            assertNotNull(keywordGUID, "addSearchKeywordToElement returned no GUID");
            assertNotNull(searchKeywordClient.getSearchKeywordByGUID(keywordGUID, null),
                          "The search keyword could not be read back after being added");
        }
        finally
        {
            if (keywordGUID != null)
            {
                searchKeywordClient.deleteSearchKeyword(keywordGUID, new DeleteOptions());
            }

            cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Attach a property facet to an element, read it back, then remove it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void propertyFacetClientAttachesAndDetaches() throws Exception
    {
        ConnectorContextBase connectorContext    = ConnectorContextFactory.newContext();
        PropertyFacetClient  propertyFacetClient = connectorContext.getPropertyFacetClient();

        String hostGUID  = createHostCollection(connectorContext, "PropertyFacet");
        String facetGUID = null;

        try
        {
            PropertyFacetProperties facetProperties = new PropertyFacetProperties();

            facetProperties.setQualifiedName(ClientFvtTestSupport.newQualifiedName("PropertyFacet"));

            facetGUID = propertyFacetClient.addPropertyFacetToElement(hostGUID, new MetadataSourceOptions(),
                                                                       null, facetProperties, null);

            assertNotNull(facetGUID, "addPropertyFacetToElement returned no GUID");
            assertNotNull(propertyFacetClient.getPropertyFacetByGUID(facetGUID, null),
                          "The property facet could not be read back after being added");
        }
        finally
        {
            if (facetGUID != null)
            {
                propertyFacetClient.deletePropertyFacet(facetGUID, new DeleteOptions());
            }

            cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Create a note log on an element, find it through the element, then remove it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void noteLogClientAttachesAndDetaches() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        NoteLogClient        noteLogClient    = connectorContext.getNoteLogClient();

        String hostGUID    = createHostCollection(connectorContext, "NoteLog");
        String noteLogGUID = null;

        try
        {
            NoteLogProperties noteLogProperties = new NoteLogProperties();

            noteLogProperties.setQualifiedName(ClientFvtTestSupport.newQualifiedName("NoteLog"));

            noteLogGUID = noteLogClient.createNoteLog(hostGUID, new MetadataSourceOptions(), null, noteLogProperties);

            assertNotNull(noteLogGUID, "createNoteLog returned no GUID");

            assertTrue(containsGUID(noteLogClient.getNoteLogsForElement(hostGUID, new QueryOptions()), noteLogGUID),
                       "getNoteLogsForElement did not return the note log just created on the element");
        }
        finally
        {
            if (noteLogGUID != null)
            {
                noteLogClient.deleteNoteLog(noteLogGUID, new DeleteOptions());
            }

            cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Create the element that the attachment under test hangs off.  A collection is used throughout: it is
     * the simplest Referenceable to create and every one of these clients accepts one.
     *
     * @param connectorContext live context
     * @param label short label naming what the host is for
     * @return the new collection's GUID
     * @throws Exception the host could not be created
     */
    static String createHostCollection(ConnectorContextBase connectorContext,
                                       String               label) throws Exception
    {
        CollectionClient collectionClient = connectorContext.getCollectionClient();

        CollectionProperties properties = new CollectionProperties();

        properties.setQualifiedName(ClientFvtTestSupport.newQualifiedName(label + "Host"));
        properties.setDisplayName("client-fvt " + label + " host");

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        return collectionClient.createCollection(newElementOptions, null, properties, null);
    }


    /**
     * Remove the host element, best-effort - the leftover sweep catches anything this misses.
     *
     * @param connectorContext live context
     * @param hostGUID element to remove
     */
    static void cleanUpHost(ConnectorContextBase connectorContext,
                            String               hostGUID)
    {
        try
        {
            ClientFvtTestSupport.purgeElement(connectorContext.getOpenMetadataStore(), hostGUID);
        }
        catch (Exception ignored)
        {
            // best effort
        }
    }


    /**
     * Does this result list contain the element with this GUID?
     *
     * @param elements results
     * @param elementGUID GUID to look for
     * @return true if present
     */
    private static boolean containsGUID(List<OpenMetadataRootElement> elements, String elementGUID)
    {
        if (elements == null) return false;

        for (OpenMetadataRootElement element : elements)
        {
            if ((element.getElementHeader() != null) && elementGUID.equals(element.getElementHeader().getGUID()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Is this result list non-empty?
     *
     * @param elements results
     * @return true if it holds at least one element
     */
    private static boolean isNotEmpty(List<OpenMetadataRootElement> elements)
    {
        return (elements != null) && (! elements.isEmpty());
    }
}
