/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewAttachmentRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CommentType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.InformalTagProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.RatingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FeedbackManagerFVT covers every request of the Feedback Manager OMVS through its own URL marker: comments
 * (including accepted answers), likes, ratings, informal tags and note logs, all attached to a collection
 * created for the purpose with the Collection Manager.  The notification that is read back as a note log's
 * entry is created with Asset Maker, since notes are assets.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedbackManagerFVT
{
    private static final ViewServiceClient feedbackManager   = new ViewServiceClient(ViewServiceDescription.FEEDBACK_MANAGER);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static String elementGUID = null;


    /**
     * Create the element that the feedback is attached to.
     */
    @Test
    @Order(1)
    void createFeedbackTarget()
    {
        CollectionProperties properties = new CollectionProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Collection", "feedback-target"));
        properties.setDisplayName("Feedback manager FVT target");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        elementGUID = collectionManager.create("/collections", requestBody);
    }


    /**
     * A comment attached to the element can be retrieved, updated, found by search and listed for the element;
     * an answer to a question comment can be accepted and the acceptance cleared; removed comments are gone.
     */
    @Test
    @Order(2)
    void comments()
    {
        String commentGUID = addComment(elementGUID, "comment", "This collection is useful", CommentType.STANDARD_COMMENT);

        CommentProperties retrieved = assertInstanceOf(CommentProperties.class, getComment(commentGUID).getProperties());

        assertEquals("This collection is useful", retrieved.getDescription());
        assertEquals(CommentType.STANDARD_COMMENT, retrieved.getCommentType());

        /*
         * updateComment validates the properties as a create does, so even a merge update must repeat the
         * qualified name.
         */
        CommentProperties update = new CommentProperties();

        update.setQualifiedName(retrieved.getQualifiedName());
        update.setDescription("This collection is very useful");
        update.setCommentType(CommentType.SUGGESTION);

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        assertTrue(feedbackManager.post("/comments/" + commentGUID + "/update", updateRequestBody, BooleanResponse.class).getFlag(),
                   "updateComment reported that nothing changed");

        retrieved = assertInstanceOf(CommentProperties.class, getComment(commentGUID).getProperties());

        assertEquals("This collection is very useful", retrieved.getDescription());
        assertEquals(CommentType.SUGGESTION, retrieved.getCommentType());

        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/elements/" + elementGUID + "/comments/retrieve", new ResultsRequestBody()), commentGUID),
                   "getAttachedComments does not include the comment");

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/comments/by-search-string", search), commentGUID),
                   "findComments did not return the comment");

        /*
         * Question and accepted answer.
         */
        String questionGUID = addComment(elementGUID, "question", "Who maintains this collection?", CommentType.QUESTION);
        String answerGUID   = addComment(questionGUID, "answer", "The FVT maintains it.", CommentType.ANSWER);

        feedbackManager.call("/comments/questions/" + questionGUID + "/answers/" + answerGUID, new NewRelationshipRequestBody());

        assertTrue(RelatedElementChecks.isLinked(getComment(questionGUID), OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName, answerGUID),
                   "the question does not show its accepted answer");

        feedbackManager.call("/comments/questions/" + questionGUID + "/answers/" + answerGUID + "/remove", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(getComment(questionGUID), OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName, answerGUID),
                    "the question still shows the cleared accepted answer");

        for (String guid : List.of(answerGUID, questionGUID, commentGUID))
        {
            feedbackManager.call("/comments/" + guid + "/remove", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(feedbackManager, "/comments/" + guid + "/retrieve");
        }

        assertTrue(feedbackManager.elements("/elements/" + elementGUID + "/comments/retrieve", new ResultsRequestBody()).isEmpty(),
                   "getAttachedComments still returns removed comments");
    }


    /**
     * A like is listed for the element until it is removed.
     */
    @Test
    @Order(3)
    void likes()
    {
        LikeProperties likeProperties = new LikeProperties();

        likeProperties.setEmoji(":thumbsup:");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setProperties(likeProperties);

        feedbackManager.call("/elements/" + elementGUID + "/likes", requestBody);

        List<OpenMetadataRootElement> likes = feedbackManager.elements("/elements/" + elementGUID + "/likes/retrieve", new ResultsRequestBody());

        assertEquals(1, likes.size(), "getAttachedLikes should return the one like");
        assertEquals(":thumbsup:", assertInstanceOf(LikeProperties.class, likes.get(0).getProperties()).getEmoji());

        feedbackManager.call("/elements/" + elementGUID + "/likes/remove", new DeleteElementRequestBody());

        assertTrue(feedbackManager.elements("/elements/" + elementGUID + "/likes/retrieve", new ResultsRequestBody()).isEmpty(),
                   "getAttachedLikes still returns the removed like");
    }


    /**
     * A rating is listed for the element until it is removed.
     */
    @Test
    @Order(4)
    void ratings()
    {
        RatingProperties ratingProperties = new RatingProperties();

        ratingProperties.setStarRating(StarRating.FOUR_STARS);
        ratingProperties.setReview("Well organized");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setProperties(ratingProperties);

        feedbackManager.call("/elements/" + elementGUID + "/ratings", requestBody);

        List<OpenMetadataRootElement> ratings = feedbackManager.elements("/elements/" + elementGUID + "/ratings/retrieve", new ResultsRequestBody());

        assertEquals(1, ratings.size(), "getAttachedRatings should return the one rating");

        RatingProperties retrieved = assertInstanceOf(RatingProperties.class, ratings.get(0).getProperties());

        assertEquals(StarRating.FOUR_STARS, retrieved.getStarRating());
        assertEquals("Well organized", retrieved.getReview());

        feedbackManager.call("/elements/" + elementGUID + "/ratings/remove", new DeleteElementRequestBody());

        assertTrue(feedbackManager.elements("/elements/" + elementGUID + "/ratings/retrieve", new ResultsRequestBody()).isEmpty(),
                   "getAttachedRatings still returns the removed rating");
    }


    /**
     * An informal tag can be created, retrieved, updated and found by name and search string (including among my
     * own tags); attached to the element it links the two in both directions until removed; then it is deleted.
     */
    @Test
    @Order(5)
    void informalTags()
    {
        String tagName = ViewServicesFvtTestSupport.qualifiedName("InformalTag", "fvt-tag");

        InformalTagProperties tagProperties = new InformalTagProperties();

        tagProperties.setQualifiedName(tagName);
        tagProperties.setDisplayName(tagName);
        tagProperties.setDescription("Tag created by the FVT");

        NewElementRequestBody createRequestBody = new NewElementRequestBody();

        createRequestBody.setIsOwnAnchor(true);
        createRequestBody.setProperties(tagProperties);

        String tagGUID = feedbackManager.create("/tags", createRequestBody);

        assertEquals("Tag created by the FVT", assertInstanceOf(InformalTagProperties.class, getTag(tagGUID).getProperties()).getDescription());

        InformalTagProperties update = new InformalTagProperties();

        update.setDescription("Tag updated by the FVT");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        assertTrue(feedbackManager.post("/tags/" + tagGUID + "/update", updateRequestBody, BooleanResponse.class).getFlag(),
                   "updateTagDescription reported that nothing changed");

        InformalTagProperties retrieved = assertInstanceOf(InformalTagProperties.class, getTag(tagGUID).getProperties());

        assertEquals("Tag updated by the FVT", retrieved.getDescription());
        assertEquals(tagName, retrieved.getDisplayName(), "a merge update should keep the tag's name");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(tagName);

        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/tags/by-name", byName), tagGUID), "getTagsByName did not return the tag");

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/tags/by-search-string", search), tagGUID), "findTags did not return the tag");

        List<OpenMetadataRootElement> myTags = feedbackManager.elements("/tags/private/by-search-string", search);

        assertTrue(RelatedElementChecks.contains(myTags, tagGUID), "findMyTags did not return a tag this user created");
        assertTrue(myTags.stream().allMatch(tag -> OMAGPlatformExtension.USER_ID.equals(tag.getElementHeader().getVersions().getCreatedBy())),
                   "findMyTags returned a tag created by another user");

        feedbackManager.call("/elements/" + elementGUID + "/tags/" + tagGUID, new MetadataSourceRequestBody());

        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/elements/by-tag/" + tagGUID + "/retrieve", new ResultsRequestBody()), elementGUID),
                   "getElementsByTag does not include the tagged element");
        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/elements/" + elementGUID + "/tags/retrieve", new ResultsRequestBody()), tagGUID),
                   "getAttachedTags does not include the tag");

        feedbackManager.call("/elements/" + elementGUID + "/tags/" + tagGUID + "/remove", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.contains(feedbackManager.elements("/elements/by-tag/" + tagGUID + "/retrieve", new ResultsRequestBody()), elementGUID),
                    "getElementsByTag still includes the untagged element");
        assertFalse(RelatedElementChecks.contains(feedbackManager.elements("/elements/" + elementGUID + "/tags/retrieve", new ResultsRequestBody()), tagGUID),
                    "getAttachedTags still includes the removed tag");

        feedbackManager.call("/tags/" + tagGUID + "/remove", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(feedbackManager, "/tags/" + tagGUID + "/retrieve");
    }


    /**
     * createInformalTag must refuse properties that are not for an informal tag, as the other create requests
     * do, rather than report success without creating anything.
     */
    @Test
    @Order(6)
    void createInformalTagRefusesWrongProperties()
    {
        CommentProperties wrongProperties = new CommentProperties();

        wrongProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Comment", "not-a-tag"));
        wrongProperties.setDescription("Not a tag");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setProperties(wrongProperties);

        JsonNode response = feedbackManager.postForFailure("/tags", requestBody);

        assertEquals(400, response.path("relatedHTTPCode").asInt());
    }


    /**
     * Note logs can be created attached to the element or standalone, updated, found by name and search string,
     * listed for the element, read back with their entries, and removed.
     */
    @Test
    @Order(7)
    void noteLogs()
    {
        NoteLogProperties attachedProperties = noteLogProperties("attached");

        NewAttachmentRequestBody attachedRequestBody = new NewAttachmentRequestBody();

        attachedRequestBody.setProperties(attachedProperties);

        String attachedNoteLogGUID = feedbackManager.create("/elements/" + elementGUID + "/note-logs", attachedRequestBody);

        NoteLogProperties standaloneProperties = noteLogProperties("standalone");

        NewElementRequestBody standaloneRequestBody = new NewElementRequestBody();

        standaloneRequestBody.setIsOwnAnchor(true);
        standaloneRequestBody.setProperties(standaloneProperties);

        String standaloneNoteLogGUID = feedbackManager.create("/note-logs", standaloneRequestBody);

        assertEquals(standaloneProperties.getQualifiedName(),
                     assertInstanceOf(NoteLogProperties.class, getNoteLog(standaloneNoteLogGUID).getProperties()).getQualifiedName());

        NoteLogProperties update = new NoteLogProperties();

        update.setDescription("Updated by the FVT");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        assertTrue(feedbackManager.post("/note-logs/" + standaloneNoteLogGUID, updateRequestBody, BooleanResponse.class).getFlag(),
                   "updateNoteLog reported that nothing changed");

        NoteLogProperties retrieved = assertInstanceOf(NoteLogProperties.class, getNoteLog(standaloneNoteLogGUID).getProperties());

        assertEquals("Updated by the FVT", retrieved.getDescription());
        assertEquals(standaloneProperties.getQualifiedName(), retrieved.getQualifiedName(), "a merge update should keep the qualified name");

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<OpenMetadataRootElement> found = feedbackManager.elements("/note-logs/by-search-string", search);

        assertTrue(RelatedElementChecks.contains(found, attachedNoteLogGUID), "findNoteLogs did not return the attached note log");
        assertTrue(RelatedElementChecks.contains(found, standaloneNoteLogGUID), "findNoteLogs did not return the standalone note log");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(attachedProperties.getQualifiedName());

        List<OpenMetadataRootElement> byNameResults = feedbackManager.elements("/note-logs/by-name", byName);

        assertTrue(RelatedElementChecks.contains(byNameResults, attachedNoteLogGUID), "getNoteLogsByName did not return the note log");
        assertFalse(RelatedElementChecks.contains(byNameResults, standaloneNoteLogGUID), "getNoteLogsByName returned a note log with another name");

        List<OpenMetadataRootElement> forElement = feedbackManager.elements("/elements/" + elementGUID + "/note-logs/retrieve", new ResultsRequestBody());

        assertTrue(RelatedElementChecks.contains(forElement, attachedNoteLogGUID), "getNoteLogsForElement does not include the attached note log");
        assertFalse(RelatedElementChecks.contains(forElement, standaloneNoteLogGUID), "getNoteLogsForElement includes a note log that is not attached");

        /*
         * A note log's entries are notifications linked to it by AttachedNoteLogEntry.
         */
        NotificationProperties notificationProperties = new NotificationProperties();

        notificationProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Notification", "note-log-entry"));
        notificationProperties.setDisplayName("FVT note log entry");

        NewElementRequestBody entryRequestBody = new NewElementRequestBody();

        entryRequestBody.setAnchorGUID(attachedNoteLogGUID);
        entryRequestBody.setIsOwnAnchor(false);
        entryRequestBody.setParentGUID(attachedNoteLogGUID);
        entryRequestBody.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName);
        entryRequestBody.setParentAtEnd1(true);
        entryRequestBody.setProperties(notificationProperties);

        String entryGUID = assetMaker.create("/assets", entryRequestBody);

        assertTrue(RelatedElementChecks.contains(feedbackManager.elements("/note-logs/" + attachedNoteLogGUID + "/notes/retrieve", new ResultsRequestBody()), entryGUID),
                   "getNotesForNoteLog does not include the note log's entry");

        assetMaker.call("/assets/" + entryGUID + "/delete", new DeleteElementRequestBody());

        assertFalse(RelatedElementChecks.contains(feedbackManager.elements("/note-logs/" + attachedNoteLogGUID + "/notes/retrieve", new ResultsRequestBody()), entryGUID),
                    "getNotesForNoteLog still includes the deleted entry");

        for (String guid : List.of(attachedNoteLogGUID, standaloneNoteLogGUID))
        {
            feedbackManager.call("/note-logs/" + guid + "/remove", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(feedbackManager, "/note-logs/" + guid + "/retrieve");
        }
    }


    @AfterAll
    static void deleteFeedbackTarget()
    {
        if (elementGUID != null)
        {
            collectionManager.call("/collections/" + elementGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + elementGUID + "/retrieve");
            elementGUID = null;
        }
    }


    private static String addComment(String      parentGUID,
                                     String      name,
                                     String      text,
                                     CommentType commentType)
    {
        CommentProperties properties = new CommentProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Comment", name));
        properties.setDescription(text);
        properties.setCommentType(commentType);

        NewAttachmentRequestBody requestBody = new NewAttachmentRequestBody();

        requestBody.setProperties(properties);

        return feedbackManager.create("/elements/" + parentGUID + "/comments", requestBody);
    }


    private static NoteLogProperties noteLogProperties(String name)
    {
        NoteLogProperties properties = new NoteLogProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("NoteLog", name));
        properties.setDisplayName("FVT note log " + name);
        properties.setDescription("Created by the feedback manager FVT");

        return properties;
    }


    private static OpenMetadataRootElement getComment(String guid)
    {
        OpenMetadataRootElement element = feedbackManager.element("/comments/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "comment " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement getTag(String guid)
    {
        OpenMetadataRootElement element = feedbackManager.element("/tags/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "tag " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement getNoteLog(String guid)
    {
        OpenMetadataRootElement element = feedbackManager.element("/note-logs/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "note log " + guid + " is missing");

        return element;
    }
}
