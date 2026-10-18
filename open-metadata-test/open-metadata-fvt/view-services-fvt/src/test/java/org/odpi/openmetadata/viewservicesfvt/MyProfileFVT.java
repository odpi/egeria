/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActionRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActivityStatusRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewAttachmentRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MyProfileFVT covers every request of the My Profile OMVS, the service a user works with their own profile
 * through: creating it, reading it and the actors, identities, roles and resources linked to it, the actions
 * assigned to, sponsored by and requested by them, and their activity log, blog and private journal.
 * <br>
 * The entries the three activity requests create are assets, so they are read back and deleted with the Asset
 * Maker's ordinary asset requests.  A journal entry deleted that way, with no external source, must go: it was
 * created locally, so the provenance check that refuses a local caller (OMAG-REPOSITORY-HANDLER-400-008) does
 * not apply to it.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyProfileFVT
{
    private static final ViewServiceClient myProfile  = new ViewServiceClient(ViewServiceDescription.MY_PROFILE);
    private static final ViewServiceClient assetMaker = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final String DISPLAY_NAME = "View services FVT user";

    private static String profileGUID = null;


    /**
     * The user has no profile until they create one, then both forms of getMyProfile return it, with the
     * user identity that addMyProfile links to it.
     */
    @Test
    @Order(1)
    void createAndReadMyProfile()
    {
        if (myProfile.get("", OpenMetadataRootElementResponse.class).getElement() == null)
        {
            PersonProperties personProperties = new PersonProperties();

            personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", OMAGPlatformExtension.USER_ID));
            personProperties.setDisplayName(DISPLAY_NAME);

            NewElementRequestBody requestBody = new NewElementRequestBody();

            requestBody.setIsOwnAnchor(true);
            requestBody.setProperties(personProperties);

            profileGUID = myProfile.create("/new", requestBody);
        }

        OpenMetadataRootElement byGet = myProfile.get("", OpenMetadataRootElementResponse.class).getElement();

        assertNotNull(byGet, "getMyProfile (GET) returned no profile");

        if (profileGUID == null)
        {
            profileGUID = byGet.getElementHeader().getGUID();
        }

        assertEquals(profileGUID, byGet.getElementHeader().getGUID());

        OpenMetadataRootElement byPost = myProfile.element("", new GetRequestBody());

        assertNotNull(byPost, "getMyProfile (POST) returned no profile");
        assertEquals(profileGUID, byPost.getElementHeader().getGUID());
    }


    /**
     * A second addMyProfile is refused: a user has one profile.
     */
    @Test
    @Order(2)
    void aSecondProfileIsRefused()
    {
        PersonProperties personProperties = new PersonProperties();

        personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "second"));
        personProperties.setDisplayName(DISPLAY_NAME);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(personProperties);

        myProfile.postForFailure("/new", requestBody);
    }


    /**
     * My actors are the user identities and roles linked to my profile: the user identity that addMyProfile
     * created for the suite's userId, and no roles because none have been appointed.
     */
    @Test
    @Order(3)
    void myActorsAndIdentities()
    {
        for (String request : List.of("/actors", "/actors/user-identities"))
        {
            List<OpenMetadataRootElement> actors = myProfile.elements(request, new ResultsRequestBody());

            assertTrue(actors.stream().anyMatch(identity -> (identity.getProperties() instanceof UserIdentityProperties userIdentityProperties) &&
                               OMAGPlatformExtension.USER_ID.equals(userIdentityProperties.getUserId())),
                       request + " does not include the user identity for " + OMAGPlatformExtension.USER_ID);
        }

        assertTrue(myProfile.elements("/actors/assigned-roles", new ResultsRequestBody()).isEmpty(),
                   "getMyRoles returned roles for a user who has been appointed to none");

        myProfile.elements("/assigned-resources", new ResultsRequestBody());
    }


    /**
     * An action that I originated, sponsor and am assigned to is returned by each of the three action requests.
     */
    @Test
    @Order(4)
    void myActions()
    {
        ActionProperties actionProperties = new ActionProperties();

        actionProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Action", "my-actions"));
        actionProperties.setDisplayName("Review the view services FVT results");

        ActionRequestBody requestBody = new ActionRequestBody();

        requestBody.setProperties(actionProperties);
        requestBody.setOriginatorGUID(profileGUID);
        requestBody.setActionSponsorGUID(profileGUID);
        requestBody.setAssignToActorGUID(profileGUID);

        String actionGUID = assetMaker.create("/actions", requestBody);

        for (String request : List.of("/assigned-actions", "/sponsored-actions", "/requested-actions"))
        {
            List<OpenMetadataRootElement> actions = myProfile.elements(request, new ActivityStatusRequestBody());

            assertTrue(actions.stream().anyMatch(action -> actionGUID.equals(action.getElementHeader().getGUID())),
                       request + " does not include the action " + actionGUID);
        }

        assetMaker.call("/assets/" + actionGUID + "/delete", new DeleteElementRequestBody());
        assertGone(actionGUID);
    }


    /**
     * Activity log and blog entries are created in their own note logs and can be deleted.
     */
    @Test
    @Order(5)
    void myActivityLogAndBlog()
    {
        for (String request : List.of("/log-my-activity", "/blog-my-activity"))
        {
            String entryGUID = myProfile.create(request, attachment(request));

            readable(entryGUID);

            assetMaker.call("/assets/" + entryGUID + "/delete", new DeleteElementRequestBody());
            assertGone(entryGUID);
        }
    }


    /**
     * Journal entries are JournalEntry assets in one MyJournal note log.  Deleting one through Asset Maker with
     * no external source takes only that entry, and the journal survives its last entry being deleted.
     */
    @Test
    @Order(6)
    void myJournal()
    {
        String firstEntryGUID  = myProfile.create("/journal-my-activity", attachment("first"));
        String secondEntryGUID = myProfile.create("/journal-my-activity", attachment("second"));

        assertNotEquals(firstEntryGUID, secondEntryGUID);

        for (String entryGUID : List.of(firstEntryGUID, secondEntryGUID))
        {
            assertEquals(OpenMetadataType.JOURNAL_ENTRY.typeName,
                         readable(entryGUID).getElementHeader().getType().getTypeName(),
                         "journal-my-activity created the wrong type of element");
        }

        assertEquals(1, journalCount(), "each journal entry should go into the one MyJournal note log");

        assetMaker.call("/assets/" + firstEntryGUID + "/delete", new DeleteElementRequestBody());

        assertGone(firstEntryGUID);
        readable(secondEntryGUID);

        assetMaker.call("/assets/" + secondEntryGUID + "/delete", new DeleteElementRequestBody());

        assertGone(secondEntryGUID);
        assertEquals(1, journalCount(), "deleting the journal's entries should not delete the journal");

        String nextEntryGUID = myProfile.create("/journal-my-activity", attachment("after"));

        readable(nextEntryGUID);
        assertEquals(1, journalCount(), "a new entry should go into the existing journal");

        assetMaker.call("/assets/" + nextEntryGUID + "/delete", new DeleteElementRequestBody());
        assertGone(nextEntryGUID);
    }


    private static NewAttachmentRequestBody attachment(String label)
    {
        NotificationProperties properties = new NotificationProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Notification", label));
        properties.setDisplayName("Entry " + label);
        properties.setDescription("Written by " + MyProfileFVT.class.getSimpleName());

        NewAttachmentRequestBody requestBody = new NewAttachmentRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    private static OpenMetadataRootElement readable(String assetGUID)
    {
        OpenMetadataRootElement element = assetMaker.element("/assets/" + assetGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "asset " + assetGUID + " is missing");

        return element;
    }


    private static void assertGone(String assetGUID)
    {
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + assetGUID + "/retrieve");
    }


    /**
     * Count the profile's note logs called MyJournal.
     */
    private static int journalCount()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setGraphQueryDepth(1);

        OpenMetadataRootElement profile = myProfile.element("", requestBody);

        assertNotNull(profile);

        int count = 0;

        if (profile.getNoteLogs() != null)
        {
            for (RelatedMetadataElementSummary noteLog : profile.getNoteLogs())
            {
                if ((noteLog.getRelatedElement().getProperties() instanceof NoteLogProperties noteLogProperties) &&
                        (noteLogProperties.getQualifiedName() != null) &&
                        (noteLogProperties.getQualifiedName().endsWith("::MyJournal")))
                {
                    count++;
                }
            }
        }

        return count;
    }
}
